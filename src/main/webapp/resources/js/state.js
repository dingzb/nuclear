/**
 * Created by Dzb on 2017/3/30.
 * state 配置
 * 1、配置登陆页面
 * 2、主视图
 * 3、欢迎视图
 */
angular.module('ws.stateConfig', []).config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('login', {
        url: '/login',
        templateUrl: 'tpls/login.html'
    }).state('main', {
        url: '',
        views: {
            '': {
                templateUrl: 'tpls/main.html'
            },
            'top@main': {
                templateUrl: 'tpls/common/navbar.html'
            },
            'bottom@main': {
                templateUrl: 'tpls/welcome.html'
            }
        }
    }).state('main.welcome', {
        url: '/'
    });
}]);