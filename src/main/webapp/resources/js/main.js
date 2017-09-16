/**
 * Created by Dzb on 2017/3/30.
 * 登陆后的所有的scope的父
 */
angular.module('ws.app').controller('mainCtrl', ['$scope', '$http', function ($scope, $http) {

    //获取当前正在进行的活动
    $scope.refreshCurAct = function (fn) {
        $http.post('app/activity/current',{}).success(function (data) {
            if (data.success){
                $scope.curAct = data.data;
                if (typeof fn === 'function') {
                    fn();
                }
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.refreshCurAct();

}]);