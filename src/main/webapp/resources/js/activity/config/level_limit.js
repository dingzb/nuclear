angular.module('ws.app').controller('actLevelLimitCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {

    $scope.refreshLevelLimit = function () {
        $scope.cancelEditLevelLimit();
        $http.post('app/activity/config/level/limit/get',{
            activityId: $scope.curAct.id
        }).success(function (data) {
            if (data.success) {
                $scope.levelLimit = data.data;
                if ($scope.levelLimit.limitFirst === null || $scope.levelLimit.limitSecond === null) {
                    $scope.isEditing = true;
                }
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    };

    $scope.showEditLevelLimit = function(){
        if ($scope.levelLimit) {
            $scope.eLevelLimit = {
                id: $scope.levelLimit.id,
                limitFirst: $scope.levelLimit.limitFirst,
                limitSecond: $scope.levelLimit.limitSecond
            };
            $scope.isEditing = true;
        }
    };

    $scope.editLevelLimit = function(){
        if ($scope.eLevelLimit && $scope.eLevelLimit.id === undefined) {
            $scope.eLevelLimit.id = $scope.curAct.id;
        }
        $http.post('app/activity/config/level/limit/edit',$scope.eLevelLimit).success(function (data) {
            if (data.success) {
                $scope.refreshLevelLimit();
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    };

    $scope.cancelEditLevelLimit = function(){
        $scope.isEditing = false;
        $scope.eLevelLimit = {};
    };

    $scope.refreshLevelLimit();
}]);