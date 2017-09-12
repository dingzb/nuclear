/**
 * Created by Dzb on 2017/3/6.
 */
angular.module('ws.stateConfig').config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('main.system', {
        url: '/system',
        views: {
            'bottom@main': {
                templateUrl: 'tpls/system/layout.html'
            },
            'left@main.system': {
                templateUrl: 'tpls/system/menu.html'
            },
            'right@main.system': {
                templateUrl: 'tpls/system/welcome.html'
            }
        }
    }).state('main.system.resources', {
        url: '/state'
    }).state('main.system.resources.subsystem', {
        url: '/subsystem',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/resources/subsystem.html'
            }
        }
    }).state('main.system.resources.menugroup', {
        url: '/menugroup',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/resources/menugroup.html'
            }
        }
    }).state('main.system.resources.menu', {
        url: '/menu',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/resources/menu.html'
            }
        }
    }).state('main.system.resources.notmenu', {
        url: '/notmenu',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/resources/notmenu.html'
            }
        }
    }).state('main.system.resources.access', {
        url: '/access',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/resources/action.html'
            }
        }
    }).state('main.system.permission', {
        url: '/permission'
    }).state('main.system.permission.user', {
        url: '/user',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/permission/user.html'
            }
        }
    }).state('main.system.permission.role', {
        url: '/role',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/permission/role.html'
            }
        }
    }).state('main.system.permission.group', {
        url: '/group',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/permission/group.html'
            }
        }
    }).state('main.system.permission.info', {
        url: '/info',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/permission/info.html'
            }
        }
    }).state('main.system.log', {
        url: '/log'
    }).state('main.system.log.business', {
        url: '/business',
        views: {
            'right@main.system': {
                templateUrl: 'tpls/system/log/business.html'
            }
        }
    });
}]);