/**
 * URL权限控制模块
 * 在模块启动前需要指定$rootScope.permObj:{permCodes:[],permStates:[]}
 */
angular.module('ws.permission', ['ws.basic', 'ui.router']).constant('wsPermissionCfg', {
    // 默认包含的state，无论是否登录和是否有权限
    defaultState: [
        'error.404',
        'error.403',
        'error.401',
        'error.0',
        'main',
        'main.welcome',
        'main.system.permission.modinfo',
        'main.system.permission.modpassword'
    ]
}).directive('wsPermcode', ['$rootScope', function ($rootScope) {
    // UI权限指令
    return {
        link: function (scope, element, attrs) {
            var permcodes = attrs['wsPermcode'].split(',');
            var permed = false;
            permcodes.forEach(function (val) {
                permed = ($rootScope.permCodes.indexOf(val) != -1) || permed;
            });

            if (!permed) {
                element.remove();
            }
        }
    };
}]).run(['$rootScope', '$state', '$http', 'authorizationServ', function ($rootScope, $state, $http, authorizationServ) {
    // 监听state变化检测权限
    $rootScope.$on('$stateChangeStart', function (evt, toState, toParams, fromState, fromParams) {
        //取消权限监测//////////////
        //return;
        ///////////////////////////
        console.info('get state change from ', fromState, ' to ', toState);
        var state = toState.name;
        //登录检测
        if (!$rootScope.user && state !== 'login') {
            evt.preventDefault();
            $state.go('login');
            return;
        }
        if ($rootScope.user && state === 'login') {
            evt.preventDefault();
            $state.go('main.welcome'); //跳转到业务模块
            // $state.go('main.welcome');
            return;
        }
        //主视图或欢迎视图直接跳转到业务模块
        if ($rootScope.user && state === 'main'){
            evt.preventDefault();
            $state.go('main.welcome');
            return;
        }
        console.info('get normal state');
        //权限判断
        var permStates = $rootScope.permStates;
        if ($rootScope.user && permStates && permStates.indexOf(state) == -1) {
            evt.preventDefault();
            // console.info('get nopromisse error :'+state);
            $rootScope.alert('禁止访问', 'error', '权限错误');
            $state.go('error.403');
            // $state.go('error.404');
        }
    });
    //捕获自定义授权错误事件
    $rootScope.$on('authorizationInterceptor', function (evt, code, msg) {
        console.info('get event permission');
        if (code == 'loginErr') {
            evt.preventDefault();
            //$state.go('login');
            //解决当弹出模态窗时后台返回401错误跳转到首页时的遮罩层不消失问题
            window.location.href = window.location.pathname + "";
            return;
        }
        if (code == 'forbidden') {
            evt.preventDefault();
            //$state.go('error.403');
            $rootScope.alert('禁止访问:[' + msg + '],请联系管理员.', 'error', '权限错误');
            return;
        }
        if (code == 'notFound') {
            evt.preventDefault();
            //$state.go('error.404');
            // $rootScope.alert('页面找不到','系统错误',function(){});
            return;
        }
        if (code == 'timeout') {
            evt.preventDefault();
            //$state.go('error.0');
            $rootScope.alert('请求超时', 'error', '网络错误');
        }
    });
}]).config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function ($httpProvider, $stateProvider, $urlRouterProvider) {
    // 注册response拦截器
    $httpProvider.interceptors.push('PermissionInterceptor');
    $urlRouterProvider.otherwise('/');
    $stateProvider
        .state('error', {
            abstract: true,
            url: '/error',
            template: '<div ui-view></div>'
        })
        .state('error.404', {
            url: '/404',
            templateUrl: 'tpls/error/404.html'
        })
        .state('error.403', {
            url: '/403',
            templateUrl: 'tpls/error/403.html'
        })
        .state('error.0', {
            url: '/0',
            templateUrl: 'tpls/error/0.html'
        })
}]).service('authorizationServ', ['$rootScope', 'wsPermissionCfg', function ($rootScope, wsPermissionCfg) {
    // 初始化权限配置
    this.init = function () {
        console.info('init authorization configuartion');
        $rootScope.user = null;
        $rootScope.permCodes = null;
        $rootScope.permStates = wsPermissionCfg.defaultState;
    };
    // 设置权限
    this.set = function (authObj) {
        $rootScope.user = authObj.user;
        $rootScope.permCodes = [].concat(authObj.permCodes);
        $rootScope.permStates = authObj.permStates.concat(wsPermissionCfg.defaultState);
    };
}]).factory('PermissionInterceptor', ['$q', '$location', '$rootScope', '$window', 'authorizationServ', function ($q, $location, $rootScope, $window, authorizationServ) {
    /*
     * 对错误的response进行处理
     * 参考：
     * 1、http://my.oschina.net/blogshi/blog/300595
     * 2、http://docs.ngnice.com/api/ng/service/$http
     * 3、http://www.brafox.com/post/2015/javascript/angularjs/angularjs-router-interceptor.html
     * 错误指令
     * 1、401 未登录或登录超时
     * 2、。。。
     */
    function errorPage(status) {
        return '<h1>' + status + '</h1>';
    }

    return {
        //只能捕获4XX错误？
        'responseError': function (rejection) {
            //调用 authorizationServ#init 广播事件
            if (rejection.status == 401) {
                console.info('PermissionInterceptor get 401');
                authorizationServ.init();
                //广播自定义授权错误事件
                //事件function(evt,code,msg)
                $rootScope.$emit('authorizationInterceptor', 'loginErr', 'need login or session timeout');
                return rejection;
            } else if (rejection.status == 403) {
                console.info('PermissionInterceptor get 403');
                //广播自定义授权错误事件
                //事件function(evt,code,msg)
                $rootScope.$emit('authorizationInterceptor', 'forbidden', rejection.config.url);
            } else if (rejection.status == 404) {
                console.info('PermissionInterceptor get 404');
                //广播自定义授权错误事件
                //事件function(evt,code,msg)
                $rootScope.$emit('authorizationInterceptor', 'notFound', rejection.config.url);
            } else if (rejection.status == 0) {
                console.info('PermissionInterceptor get 0');
                //广播自定义授权错误事件
                //事件function(evt,code,msg)
                $rootScope.$emit('authorizationInterceptor', 'timeout', 'request timeout');
            } else {
                // console.info('PermissionInterceptor get other error :' + rejection.status);
                $rootScope.alert('Function error.', 'error');
                rejection.data = errorPage(rejection.status);
            }
            return rejection;
        }
    };
}]);