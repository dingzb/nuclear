/**
 * Created by Dzb on 2017/3/13.
 */

/**
 * 导航条和目录激活状态变化服务
 */
angular.module('ws.menus', []).service('activeServ', ['$rootScope', function ($rootScope) {
    /**
     * @param stateCode 要设置激活的state code
     *
     */

    this.active = function (state, type) {
        var codes = state.split('.');
        var subSys = $('[ui-sref="' + codes[0] + '.' + codes[1] + '"]');

        if (subSys != null && (type == 'subSys' || !type)) {
            subSys.parents('nav').find('.active').removeClass('active');
            subSys.parent().addClass('active')
        }

        var menuGroup = $('#' + 'collapse-' + codes[0] + '-' + codes[1] + '-' + codes[2]);
        var menuState = codes[0] + '.' + codes[1] + '.' + codes[2] + '.' + codes[3];
        if (menuGroup != null && (type == 'menu' || !type)) {
            var menu = menuGroup.parents('.panel-group').find('.active').removeClass('active');
            if (menu != null) {
                menu.removeClass('active');
            }
            $('[ui-sref="' + menuState + '"]').addClass('active');
            //展开当前state目录//////////////
//			menuGroup.collapse('show');
            var menuCollapse = $('#' + 'collapse-' + codes[0] + '-' + codes[1] + '-' + codes[2]);
            var menuHead = $('#' + 'head-' + codes[0] + '-' + codes[1] + '-' + codes[2]);
            menuHead.removeClass('collapsed');
            menuHead.attr('aria-expanded', 'true');
            menuCollapse.addClass('in');
            menuCollapse.attr('aria-expanded', 'true');
            menuCollapse.removeAttr('style');
            //展开当前state目录//////////////

        }
    };
}]).controller('navbarCtrl', ['$scope', '$rootScope', '$http', 'authorizationServ', '$state', function ($scope, $rootScope, $http, authorizationServ, $state) {
    $scope.uname = $rootScope.user.name;

    var subSystems = $http({
        method: 'get',
        url: 'app/system/state/subsystem',
        dataType: 'json'
    }).success(function (data) {
        $scope.subSystems = data.data;
    });
    $scope.logout = function () {
        console.info('logout!');
        $http({
            method: 'post',
            url: 'app/logout'
        })
            .success(function (data) {
                $scope.mask(true, "正在退出...");
                authorizationServ.init();
                $scope.$emit('authorizationInterceptor', 'loginErr', 'need login or session timeout');
                // $scope.mask(false);
            });
    };
    $scope.info = function () {
        $state.go('main.system.permission.info');
    };
}]).directive('wsAccordion', [function () {
    // accordion with ui-router
    // 支持2层目录
    //
    // <pre>receive format
    // [{
    // 	name:'',
    // 	stateName:'',
    // 	chilrend:[{
    // 		name:'',
    // 		stateName:'',
    // 	}]
    // }]</pre>
    return {
        restrict: 'E',
        scope: {
            menus: '='
        },
        replace: true,
        templateUrl: 'tpls/common/accordion.html'
    };
}]);