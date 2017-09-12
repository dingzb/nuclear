/**
 * Created by Dzb on 2017/3/30.
 */

angular.module('ws.login', []).controller('loginInCtrl', ['$scope', '$http', '$state', '$translate', 'authorizationServ', function ($scope, $http, $state, $translate, authorizationServ) {
    
    /**
     * verify code
     */
    $scope.validateVerifyCode = function (fn) {
        if (!$scope.verifyCode) {
            // $scope.errmsg = "验证码不能为空！";
            $translate('login.error.empty.password').then(function(t){$scope.errmsg = t;});
            $scope.error = true;
            return;
        }
        if (!(/^[a-z,A-Z0-9]{4}$/.test($scope.verifyCode))) {
            $scope.errmsg = "验证码格式不正确！";
            $scope.error = true;
            return;
        }
        $http.post('app/verifyCode/verify', {'code': $('#imgCode').val()}).success(function (data) {
            if (!data.success) {
                $scope.errmsg = "验证码不对！";
                $scope.error = true;
            } else {
                $scope.errmsg = "";
                $scope.error = false;
                if (typeof fn === 'function') {
                    fn();
                }
            }
        });
    };
    /**
     * login
     */
    $scope.login = function () {

        if ($scope.error) {
            return;
        }

        $http.post('app/login', {
            'username': $scope.username,
            'password': $scope.password,
            'verify': $scope.verifyCode
        }, postCfg).success(function (data) {
            if (data.success) {
                //保存权限认证信息
                authorizationServ.set(data.data);
                $state.go('main.tax');
                // $state.go('main.welcome');
            } else {
                if (data.message) {
                    $scope.errmsg = data.message;
                } else {
                    $scope.errmsg = '未知错误';
                }
                $scope.error = true;
            }
        });
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