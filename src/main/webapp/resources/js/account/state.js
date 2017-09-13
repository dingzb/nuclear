/**
 * Created by Dzb on 2017/3/6.
 */
angular.module('ws.stateConfig').config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('main.account', {
        url: '/account',
        views: {
            'bottom@main': {
                templateUrl: 'tpls/account/layout.html'
            },
            'left@main.account': {
                templateUrl: 'tpls/account/menu.html'
            },
            'right@main.account': {
                templateUrl: 'tpls/account/welcome.html'
            }
        }
    });
}]);