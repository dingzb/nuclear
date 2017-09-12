/**
 * Created by Dzb on 2017/3/10.
 * 用户信息
 */

angular.module('ws.app').controller('systemUserInfoCtrl', ['$scope', '$http', function ($scope, $http) {

    $scope.fun = undefined;
    /*
     * 获取用户信息
     */
    function getInfo() {
        $http.get('app/system/user/info').success(function (data) {
            if (data.success) {
                $scope.info = data.data;
                $scope.display = angular.copy(data.data);
                console.info($scope.display)
            }
        });
    }

    getInfo();

    $scope.mod = function (str) {
        $scope.fun = str;
    };

    $scope.cancel = function () {
        $scope.modify = false;
    };

    $scope.edit = function () {
        if ($scope.infoForm.$valid) {
            $scope.disModInfo = true;
            $scope.mask(true, 2);
            $http.post('app/system/user/edit', {
                'id': $scope.info.id,
                'name': $scope.info.name,
                'email': $scope.info.email,
                'idCard': $scope.info.idCard,
                'phone': $scope.info.phone
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    $scope.alert(data.message);
                    getInfo();
                    $scope.fun = undefined;
                } else {
                    $scope.alert(data.message);
                }
                $scope.disModInfo = false;
            }).error(function () {
                $scope.mask(false);
                $scope.disModInfo = false;
            });
        } else {
            // 标记为已操作状态
            $scope.infoForm.name.$setDirty();
            $scope.infoForm.email.$setDirty();
            $scope.infoForm.idCard.$setDirty();
            $scope.infoForm.phone.$setDirty();
        }
    };
    //重置应显示之前保存的数据
    $scope.reset = function () {

        $http.get('app/system/user/info').success(function (data) {
            if (data.success) {
                $scope.info = data.data;
                $scope.infoForm.name.$setPristine();
                $scope.infoForm.email.$setPristine();
                $scope.infoForm.idCard.$setPristine();
                $scope.infoForm.phone.$setPristine();
            }

        });
    };

    ///修改密码
    $scope.pwd = {};
    $scope.showInfo = false;
    $scope.disabledSubmit = false;
    $scope.submit = function () {
        if ($scope.modPasswd.$valid) {
            $scope.mask(true, 2);
            $http.post('app/system/user/password/modify', {
                "password": $scope.oldPasswd,
                "newPasswd": $scope.newPasswd,
                "confirmPasswd": $scope.confirmPasswd
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    $scope.modErr = false;
                    $scope.showInfo = false;
                    $scope.alert("修改成功");
                    $scope.fun = undefined;
                } else {
                    var data = data.message;
                    if (data == "PASSWORD_ERROR") {
                        $scope.modErr = true;
                        $scope.showInfo = true;
                        $scope.errorInfo = "原始密码错误";
                    } else {
                        $scope.modErr = true;
                        $scope.showInfo = true;
                        $scope.errorInfo = "修改失败";
                    }
                }
                $scope.disabledSubmit = true;
            }).error(function () {
                $scope.mask(false);
                $scope.disabledSubmit = true;
            });
        } else {
            $scope.modPasswd.oldPasswd.$setDirty();
            $scope.modPasswd.newPasswd.$setDirty();
            $scope.modPasswd.confirmPasswd.$setDirty();
        }
    };
    $scope.oldPasswdChange = function () {
        $scope.showInfo = false;
        $scope.disabledSubmit = false;
    };
    $scope.clearConfirmPasswd = function () {
        $scope.confirmPasswd = '';
        $scope.disabledSubmit = false;
    };
    $scope.onreset = function () {
        $scope.oldPasswd = "";
        $scope.newPasswd = "";
        $scope.confirmPasswd = "";
    }

}]);