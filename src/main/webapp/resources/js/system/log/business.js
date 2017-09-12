/**
 * 业务日志
 */
angular.module('ws.app').controller('businessLogCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.initDtp = function () {
        $scope.datetimepicker('#startTime').onChange(function (d) {
            $scope.searchParams.startTime = d;
        });
        $scope.datetimepicker('#endTime').onChange(function (d) {
            $scope.searchParams.endTime = d;
        });
    };
    $scope.searchParams = {};
    $scope.showSetTime = function () {
        getTime();
        $("#setTimeModal").modal("show");
    };

    $scope.resetSetTime = function () {
        $scope.setTimeForm.$setPristine();
        $scope.settime = {};
        $scope.settime.time = '';
    };

    $scope.setTime = function () {
        if ($scope.setTimeForm.$valid) {
            $scope.mask(true, 0);
            $http({
                url: 'app/system/log/business/time/set',
                method: 'post',
                data: {
                    time: $scope.settime.time
                }
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    //关闭
                    $('#setTimeModal').modal('hide');
                } else {
                    $scope.alert(data.message, 'error');
                }
            }).error(function () {
                $scope.mask(false);
            });
        } else {
            $scope.setTimeForm.time.$setDirty();
        }
    };

    getTime = function () {
        $http({
            url: 'app/system/log/business/time/get',
            method: 'post'
        }).success(function (data) {
            $scope.mask(false);
            if (data.success) {
                $scope.settime = {
                    time: data.data
                };
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function () {
            $scope.mask(false);
        });
    };

    /**
     * 查询
     */
    $scope.search = function () {
        if ($scope.searchParams.startTime && $scope.searchParams.endTime &&
            $scope.searchParams.startTime > $scope.searchParams.endTime) {
            $scope.alert('开始时间不能大于结束时间', 'error', '提示');
            return false;
        }
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };
    //清空
    $scope.reset = function () {
        $scope.searchParams.username = "";
        $scope.searchParams.isException = "";
        $scope.searchParams.operation = "";
        $scope.searchParams.startTime = undefined;
        $scope.searchParams.endTime = undefined;
    };
    //grid配置
    $scope.datagrid = {
        url: 'app/system/log/business/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'username',
            title: '用户名'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'ipAddr',
            title: 'IP地址'
        }, {
            field: 'content',
            title: '操作内容'
        }, {
            field: 'isException',
            title: '是否异常',
            formatter: function (row) {
                var isException = row['isException'];
                if (isException) {
                    return "是";
                } else {
                    return '否';
                }
            }
        }, {
            field: 'operation',
            title: '操作类别',
            formatter: function (row) {
                var operation = row['operation'];
                if (operation == 'LOGIN') {
                    return "登录";
                } else if (operation == 'ADD') {
                    return "添加";
                } else if (operation == 'DELETE') {
                    return "删除";
                } else if (operation == 'MODIFY') {
                    return "修改";
                } else if (operation == 'QUERY') {
                    return "查询";
                } else if (operation == 'IMPORT') {
                    return "导入";
                } else if (operation == 'EXPORT') {
                    return "导出";
                }
            }
        }, {
            field: '',
            title: '操作',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.showDetail(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>";
                // + "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete('" + row['id'] + "')\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
            }
        }],
        checkbox: {
            field: 'yes'
        },
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };


    $scope.delLog = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.alert("必须勾选一条记录才能删除！", 'error');
            return;
        }
        var selectRowIds = [];
        for (var i = 0; i < checkeds.length; i++) {
            selectRowIds[i] = checkeds[i].id;
        }

        $scope.confirm("将要删除" + checkeds.length + "条记录", function (y) {
            if (!y) {
                return;
            }
            $http.post('app/system/log/business/del', {
                'param': selectRowIds
            }).success(function (data) {
                    if (data.success) {
                        $scope.innerCtrl.load($scope.datagrid.params);
                        $scope.alert(data.message, 'error');
                    } else
                        $scope.alert(data.message);
                });
        });
    };

    angular.custom.showDetail = function (logMsg) {
        $scope.$apply(function () {
            $scope.detail = logMsg;
        });
        $("#detailModal").modal('show');
    };

}]);