/**
 * 创建评审活动
 * Created by Neo on 2017/5/9.
 */


angular.module('ws.app').controller('actCreateCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {

    $scope.activity = {
        name: new Date().getFullYear() + '年度中国核能行业协会科学技术奖专业评审会'
    };

    $scope.create = function () {
        $http.post('app/activity/config/create', {
            name: $scope.activity.name
        }).success(function (data) {
            if (data.success) {
                $scope.alert(data.message, 'info', '创建成功', function () {
                    $scope.refreshCurAct();
                });
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    };

}]);