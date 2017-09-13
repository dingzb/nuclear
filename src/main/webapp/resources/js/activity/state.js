/**
 * Created by Dzb on 2017/3/6.
 */
angular.module('ws.stateConfig').config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('main.activity', {
        url: '/activity',
        views: {
            'bottom@main': {
                templateUrl: 'tpls/activity/layout.html'
            },
            'left@main.activity': {
                templateUrl: 'tpls/activity/menu.html'
            },
            'right@main.activity': {
                templateUrl: 'tpls/activity/welcome.html'
            },
            'msg@main.activity': {
                templateUrl: 'tpls/activity/msg.html'
            }
        }
    });
}]);