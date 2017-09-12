/**
 * Created by Dzb on 2017/3/30.
 *
 */

angular.module('ws.login', []).controller('loginInCtrl', ['$scope', '$http', '$state', '$translate', 'authorizationServ', function ($scope, $http, $state, $translate, authorizationServ) {
    
    var validate = function () {
        if (!$scope.username){
            $scope.errMsg = '用户名不能为空';
            return false;
        }
        if (!$scope.password) {
            $scope.errMsg = '密码不能为空';
            return false;
        }
        if (!$scope.verifyCode) {
            $scope.errMsg = '验证码不能为空';
            return false;
        }
        return true;
    };
    /**
     * login
     */
    $scope.login = function () {
        if (!validate()){
            return;
        }
        $http.post('app/login', {
            'username': $scope.username,
            'password': $scope.password,
            'verify': $scope.verifyCode
        }, postCfg).success(function (data) {
            if (data.success) {
                $scope.errMsg = undefined;
                //保存权限认证信息
                authorizationServ.set(data.data);
                $state.go('main.welcome');
            } else {
                if (data.message) {
                    $scope.errMsg = data.message;
                    $scope.password = '';
                    $scope.verifyCode = '';
                    $scope.updateVerify();
                } else {
                    $scope.errMsg = '未知错误';
                }
            }
        });
    };

    $scope.updateVerify = function () {
        $('#verifyCode').attr('src', 'app/verifyCode/image?t='+new Date());
    };

    /* 设置语言 */
    $scope.changeLanguage = function(r){
        $.cookie('clientlanguage', $scope.lang, {expires: 7});
        $translate.use($scope.lang);
        // if (r)
        //     window.location.reload();
    };

    $scope.lang = ($.cookie('clientlanguage') || navigator.language || 'zh_CN').replace('-', '_');
    $scope.changeLanguage();

}]);