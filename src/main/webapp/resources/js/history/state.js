/**
 * Created by Dzb on 2017/3/6.
 */
angular.module('ws.stateConfig').config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('main.history', {
        url: '/history',
        views: {
            'bottom@main': {
                templateUrl: 'tpls/history/layout.html'
            },
            'left@main.history': {
                templateUrl: 'tpls/history/menu.html'
            },
            'right@main.history': {
                templateUrl: 'tpls/history/welcome.html'
            }
        }
    });
}]);