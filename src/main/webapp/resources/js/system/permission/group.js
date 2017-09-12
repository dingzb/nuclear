/**
 * 组管理
 */
angular.module('ws.app').controller('systemGroupCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    $scope.searchParams = {};

    $http.post('app/system/group/types', {}).success(function (data) {
        if (data.success) {
            $scope.types = [];
            data.data.forEach(function (val) {
                switch (val) {
                    case 'ADMIN':
                        $scope.types.push({
                            id: val,
                            name: '管理员'
                        });
                        break;
                    case 'NORMAL':
                        $scope.types.push({
                            id: val,
                            name: '普通'
                        });
                        break;
                    default :
                        $scope.types.push({
                            id: 'unknown',
                            name: '未知'
                        });
                }
            });
        } else if (data.message) {
            $scope.alert(data.message, 'error');
        }
    }).error(function (data) {
        $scope.alert(data, 'error');
    });


    //初始化组列表
    $scope.datagrid = {
        url: 'app/system/group/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'name',
            title: '名称'
        }, {
            field: 'type',
            title: '类型',
            formatter: function (val) {
                switch (val.type) {
                    case 'ADMIN' :
                        return '管理员';
                    case 'NORMAL' :
                        return '普通';
                    case 'USER' :
                        return '用户本身组';
                }
            }
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'modifyTime',
            title: '修改时间'
        }, {
            field: 'description',
            title: '描述'
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    //查询
    $scope.searchGroup = function () {
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };

    //清空
    $scope.resetSearch = function () {
        var clearSearch = {
            name: '',
            inclUserType: ''
        };
        $.extend($scope.datagrid.params, clearSearch);
        $.extend($scope.searchParams, clearSearch);
    };

    //添加
    $scope.addGroup = function () {
        $scope.info = {};
        $scope.addForm.$setPristine();
        $("#addModal").modal('show');
    };

    //添加保存
    $scope.addSave = function () {
        if (!validateAddForm()) {
            return;
        }
        $scope.mask(true, 1);
        $http.post('app/system/group/add', {
            'name': $scope.info.name,
            'type': 'NORMAL',
            'description': $scope.info.description
        }).success(function (data) {
            if (data.success) {
                $scope.innerCtrl.load($scope.datagrid.params);
                $scope.alert(data.message);
                $("#addModal").modal('hide');
            } else {
                $scope.alert(data.message, 'error');
            }
            $scope.mask(false);
        }).error(function () {
            $scope.mask(false);
        });
    };

    //添加清空
    $scope.addReset = function () {
        $('#add-name').val("");
        $scope.info = {};
    };
    //添加校验
    function validateAddForm() {
        if ($scope.addForm.$invalid) {
            $scope.addForm.name.$setDirty();
            $scope.addForm.type.$setDirty();
            $scope.addForm.description.$setDirty();
            return false;
        }
        return true;
    }

    //编辑
    $scope.editGroup = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length == 1) {
            $scope.edit = {
                'id': checkeds[0].id,
                'name': checkeds[0].name,
                'type': checkeds[0].type,
                'description': checkeds[0].description
            };
            $("#editModal").modal('show');
        } else {
            $scope.alert("只能选择一个进行编辑", 'error');
        }
    };

    //编辑清空
    $scope.editReset = function () {
        $scope.edit.description = "";
    };

    //编辑保存
    $scope.editSave = function () {
        if (!validateEditForm()) {
            return;
        }
        $scope.mask(true, 2);
        $http.post('app/system/group/edit', {
            'id': $scope.edit.id,
            'name': $scope.edit.name,
            'type': $scope.edit.type,
            'description': $scope.edit.description
        }).success(function (data) {
            if (data.success) {
                $scope.innerCtrl.load();
                $scope.alert(data.message);
                $("#editModal").modal('hide');
            } else {
                $scope.alert(data.message, 'error');
            }
            $scope.mask(false);
        }).error(function () {
            $scope.mask(false);
        });
    };

    //编辑校验
    function validateEditForm() {
        if ($scope.editForm.$invalid) {
            $scope.editForm.name.$setDirty();
            $scope.editForm.description.$setDirty();
            return false;
        }
        return true;
    }

    //删除组
    $scope.delGroup = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.alert("必须勾选一条记录才能删除！", 'error');
            return;
        }
        var groupIds = [];
        for (var i = 0; i < checkeds.length; i++) {
            groupIds.push(checkeds[i].id);
        }
        $scope.confirm("将要删除" + checkeds.length + "条记录", function (y) {
            if (!y) {
                return;
            }
            $http.post('app/system/group/del', {
                'ids': groupIds
            }).success(function (data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params);
                    $scope.alert(data.message);
                } else
                    $scope.alert(data.message, 'error');
            });
        });
    };
}]);