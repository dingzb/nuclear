/**
 * Created by Dzb on 2017/3/17.
 * 一些基础功能
 */
angular.module('ws.basic', []).run(['$rootScope', function ($rootScope) {
    /**
     * 时间控件
     * @param target jquery selector or jquery object.
     * @param option
     */
    $rootScope.datetimepicker = function (target, option) {
        return new (function (target, option) {
            var that = this;
            var defaultOption = {
                locale: 'zh_cn',
                format: 'YYYY-MM-DD HH:mm',
                showClear: true,
                showClose: true,
                showTodayButton: true
            };
            var dtp = null;

            option = $.extend({}, option, defaultOption);

            if (typeof target === 'string') {
                dtp = $(target);
            } else if (typeof target === 'object') {
                dtp = target;
            } else {
                throw 'Error type of target.';
            }
            dtp.datetimepicker(option);

            /**
             * fn(date)
             * @param fn
             */
            this.onChange = function (fn) {
                if (!dtp) {
                    throw "Datetimepicker hasn't been initialized";
                }
                dtp.bind('dp.change', function (e) {
                    $rootScope.$apply(fn(dtp.val()));
                });
            };
            return this;
        })(target, option);
    };

    /**
     * alert
     * @param msg
     * @param type 'error', 'warning', 'info'
     * @param title
     * @param fn
     */
    $rootScope.alert = function (msg, type, title, fn) {
        var alert = $('#alert');

        alert.unbind('hidden.bs.modal');

        var defType = 'info';
        type = type ? type : defType;

        if (!title) {
            switch (type) {
                case 'error':
                    title = '错误';
                    break;
                case 'warning':
                    title = '警告';
                    break;
                case  'info':
                    title = '消息';
                    break;
                default:
                    title = '提示';
            }
        }
        $rootScope.alertOption = {
            msg: msg,
            type: type,
            title: title,
            fn: function (yn) {
                if (typeof fn === 'function') {
                    alert.on('hidden.bs.modal', function () {
                        fn(yn);
                    });
                }
            }
        };
        alert.modal('show');
    };

    /**
     * confirm
     * @param msg
     * @param title
     * @param type 'danger', 'warning', 'info'
     * @param fn fn(boolean)
     */
    $rootScope.confirm = function (msg, fn, title, type) {
        var confirm = $('#confirm');

        confirm.unbind('hidden.bs.modal');

        var defType = 'danger';
        var defTitle = '警告';
        type = type ? type : defType;
        title = title ? title : defTitle;

        $rootScope.confirmOption = {
            msg: msg,
            type: type,
            title: title,
            fn: function (b) {
                if (typeof fn == 'function') {
                    confirm.on('hidden.bs.modal', function () {
                        fn(b);
                    });
                }
            }
        };
        confirm.modal('show');
    };


    /**
     * 全局范围遮罩
     * @param show
     * @param msg
     */
    $rootScope.mask = function (show, msg) {

        var defMsg = '正在执行';

        msg = msg ? msg : defMsg;

        if (typeof msg == 'number') {
            switch (msg) {
                case 0:
                    msg = "正在保存...";
                    break;
                case 1:
                    msg = "正在删除...";
                    break;
                case 2:
                    msg = "正在编辑...";
                    break;
                case 3:
                    msg = "正在查询...";
                    break;
                default:
                    msg = defMsg;
            }
        }

        $rootScope.maskOption = {
            show: show,
            msg: msg
        };
    };
}]);


