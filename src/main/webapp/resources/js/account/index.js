/**
 * 系统配置菜单及系统配置首页
 */
angular.module('ws.app').controller('accountMenuCtrl', ['$scope', '$http', function ($scope, $http) {
    $http({
        url: 'app/system/state/menus',
        method: 'post',
        params: {
            code: 'account'
        }
    }).success(function (data) {
        if (data.success) {
            $scope.menus = data.data;
        } else {
            if (data.message) {
                alert(data.message);
            }
        }
    });
}]);
