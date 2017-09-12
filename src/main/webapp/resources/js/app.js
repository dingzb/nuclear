/**
 * main app
 */

angular.module('ws.app', [
    'ws.basic',
    'ws.permission',
    'ws.menus',
    'ws.login',
    'ws.datagrid',
    'ws.stateConfig',
    'ui.router',
    'ngGrid',
    'ngMessages',
    'ui.bootstrap',
    'ui.tree',
    'ngFileUpload',
    'pascalprecht.translate']).config(['$httpProvider', '$translateProvider', function ($httpProvider, $translateProvider) { //全局$http.post配置
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
    $httpProvider.defaults.transformRequest = function (data) {
        if (data === undefined)
            return data;
        return $.param(data);
    };

    var lang = ($.cookie('clientlanguage') || navigator.language || 'zh_CN').replace('-', '_');
    $translateProvider.preferredLanguage(lang);
    $translateProvider.useStaticFilesLoader({
        prefix: '/locale/',
        suffix: '.json'
    });

}])

/**
 * state变化时激活变化的state菜单
 */
    .run(['$rootScope', '$state', 'activeServ', function ($rootScope, $state, activeServ) {
        $rootScope.$on('$stateChangeStart', function (evt, toState, toParams, fromState, fromParams) {
            angular.state = {
                current: toState.name
            };
            activeServ.active(toState.name);
        });
        $rootScope.$on('ngRepeatFinished', function (evt, type) {
            activeServ.active(angular.state.current, type);
        });
    }])
    /**
     * 渲染完后执行指令
     */
    .directive('wsOnFinishRenderFilters', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                if (scope.$last === true) {
                    $timeout(function () {
                        scope.$emit('ngRepeatFinished', attr.activeType);
                    });
                }
            }
        };
    })
    /**
     * 替换<pre>.</pre>为<pre>-</pre>
     * 用在state名称指定ID时
     */
    .filter('period2minus', [function () {
        return function (text) {
            return text.replace(/\./g, '-');
        };
    }])

    /**
     * 截取字符长度
     * max:要保留的最大长度
     * tail：后缀
     *
     * */
    .filter('cut', function () {
        return function (value, max, tail) {
            if (!value) return '';

            max = parseInt(max, 10);
            if (!max) return value;
            if (value.length <= max) return value;

            value = value.substr(0, max);
            return value + (tail || ' …');
        };
    })

    /**
     * 唯一性 表单验证指令
     * usage:<input ws-unique="url?arg"/>
     * arg为后台接收参数名称
     * [input].uniqueAajax展示ajax进度
     */
    .directive('wsUnique', ['$q', '$http', function ($q, $http) {
        return {
            require: 'ngModel',
            scope: {
                wsUnique: '@'
            },
            link: function (scope, elm, attrs, ctrl) {
                var urls = scope.wsUnique.split('?');
                var def = null;
                var outterModel = null;
                var outterView = null;
                ctrl.$asyncValidators.unique = function (modelValue, viewValue) {
                    outterModel = modelValue;
                    outterView = viewValue;
                    if (ctrl.$isEmpty(modelValue)) {
                        return $q.when();
                    }
                    def = $q.defer();
                    return def.promise;
                };

                elm.on('blur', function () {
                    var data = {};
                    data[urls[1]] = outterModel;
                    if (!ctrl.$invalid) {
                        ctrl.uniqueAajax = true;
                        $http({
                            method: 'post',
                            url: urls[0],
                            data: data
                        }).success(function (data, status, headers, config) {
                            ctrl.uniqueAajax = false;
                            if (data.success) {
                                return data.data ? def.reject() : def.resolve();
                            } else {
                                return def.reject();
                            }
                        }).error(function () {
                            ctrl.uniqueAajax = false;
                            return def.reject();
                        });
                    } else {
                        return def.reject();
                    }
                });
            }
        };
    }])
    /**
     * form 比较指令
     * 参考http://docs.ngnice.com/guide/forms
     * <any ws-comparewith="the obj to compared"></any>
     */
    .directive('wsComparewith', [function () {
        return {
            require: 'ngModel',
            scope: {
                wsComparewith: '='
            },
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    if (viewValue == '' || viewValue == scope.wsComparewith) {
                        ctrl.$setValidity('wsComparewith', true);
                    } else {
                        ctrl.$setValidity('wsComparewith', false);
                    }
                    return viewValue;
                });
            }
        };
    }])
    .directive('wsDiffrentwith', [function () {
        return {
            require: 'ngModel',
            scope: {
                wsDiffrentwith: '='
            },
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    if (viewValue != '' && viewValue != scope.wsDiffrentwith) {
                        ctrl.$setValidity('wsDiffrentwith', true);
                    } else {
                        ctrl.$setValidity('wsDiffrentwith', false);
                    }
                    return viewValue;
                });
            }
        };
    }])
    .directive('wsDiffrentip', [function () {
        return {
            require: 'ngModel',
            scope: {
                wsDiffrentip: '='
            },
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    var IP_REGEXP = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
                    if (IP_REGEXP.test(viewValue) && IP_REGEXP.test(scope.wsDiffrentip)) {
                        var sourceIp = scope.wsDiffrentip;
                        var target = viewValue;
                        // var spoint=sourceIp.substring(sourceIp.lastIndexOf("."));
                        // var tpoint=target.substring(target.lastIndexOf("."));
                        // if(spoint!='.1'||tpoint!='.1'){
                        // 		ctrl.$setValidity('wsDiffrentip', false);
                        // 		return viewValue;
                        // }
                        sourceIp = sourceIp.substring(0, sourceIp.lastIndexOf("."));
                        target = target.substring(0, target.lastIndexOf("."));
                        if (target != '' && target != sourceIp) {
                            ctrl.$setValidity('wsDiffrentip', true);
                        } else {
                            ctrl.$setValidity('wsDiffrentip', false);
                            return undefined;
                        }
                    } else {
                        // 验证不通过 表示格式不正确
                        ctrl.$setValidity('wsDiffrentip', false);
                        return undefined;
                    }
                    return viewValue;
                });
            }
        };
    }])
    .directive('wsBiggerthan', [function () {
        return {
            require: 'ngModel',
            scope: {
                wsBiggerthan: '='
            },
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    var source = parseInt(viewValue, 10);
                    var target = scope.wsBiggerthan;
                    if (source != '' && source > target) {
                        ctrl.$setValidity('wsBiggerthan', true);
                    } else {
                        ctrl.$setValidity('wsBiggerthan', false);
                    }
                    return viewValue;
                });
            }
        };
    }])
    .directive('wsLessthan', [function () {
        return {
            require: 'ngModel',
            scope: {
                wsLessthan: '='
            },
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    var source = parseInt(viewValue, 10);
                    var target = scope.wsLessthan;
                    if (source != '' && source < target) {
                        ctrl.$setValidity('wsLessthan', true);
                    } else {
                        ctrl.$setValidity('wsLessthan', false);
                    }
                    return viewValue;
                });
            }
        };
    }]);

/**
 * 初始启动配置
 *
 * 获取权限路径服务，并手动启动主网站主模块主App
 * @require angularjs 1.2.16
 * @require jquery 1.9.1
 */
(function ($, angular, document) {
    $(document).ready(function () {
        $.ajax({
            url: 'app/authorization',
            type: "POST",
            dataType: 'json',
            success: function (data) {
                angular.module('ws.app').run(['$rootScope', 'authorizationServ', function ($rootScope, authorizationServ) {
                    if (data.success) {
                        authorizationServ.set(data.data);
                    } else {
                        authorizationServ.init();
                    }
                }]);
                /////自定义对象
                angular.custom = {};
                angular.bootstrap(document, ['ws.app']);
            },
            statusCode: {
                401: function () {
                    location.href = '401';
                },
                404: function () {
                    location.href = '404';
                }
            }
        });
    });
})(jQuery, angular, document);