angular.module('ws.app').controller('systemUserCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    $scope.searchParams = {};
    //日期控件初始化
    $scope.initDtp = function (e) {
        console.info(e);
        $scope.datetimepicker('#createTimeStart').onChange(function (d) {
            $scope.searchParams.createTimeStart = d;
        });
        $scope.datetimepicker('#createTimeEnd').onChange(function (d) {
            $scope.searchParams.createTimeEnd = d;
        });
    };

    //获取二级机构（分局）
    $http.post('app/tax/agency/list', {}).success(function (data) {
        if (data.success) {
            $scope.agencies = data.data;
        } else if (data.message) {
            $scope.alert(data.message, 'error');
        }
    }).error(function (data) {
        $scope.alert(data, 'error');
    });

    //获取一级机构（县局）
    $http.post('app/tax/agency/list', {level: 0}).success(function (data) {
        if (data.success) {
            $scope.topAgencies = data.data;
            $scope.userTopAgencyId = data.data[0].id;
        } else if (data.message) {
            $scope.alert(data.message, 'error');
        }
    }).error(function (data) {
        $scope.alert(data, 'error');
    });


    //初始化列表
    $scope.datagrid = {
        url: 'app/system/user/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'username',
            title: '用户名',
            width: 20
        }, {
            field: 'name',
            title: '姓名',
            width: 20
        }, {
            field: 'agencyBoss',
            title: '负责人',
            translator: function (row) {
                return row.agencyBoss ? '是': '否';
            },
            width: 10
        }, {
            field: 'agency',
            title: '机关',
            translator: function (row) {
                return row.agency === null ? '' : row.agency.name;
            },
            width: 30
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 30
        }],
        checkbox: {
            field: 'yes'
        },
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    //清空用户查询条件
    $scope.resetSearch = function () {

        var clearSearch = {
            username: "",
            name: "",
            createTimeStart: "",
            createTimeEnd: ""
        };
        $.extend($scope.searchParams, clearSearch);
        $.extend($scope.datagrid.params, clearSearch);
    };

    //清空场所查询条件
    $scope.resetPlace = function () {
        $scope.datagridShow.params.name = "";
        $scope.datagridShow.params.address = "";
    };

    //清空角色查询条件
    $scope.resetRole = function () {
        $scope.datagridShows.params.name = "";
        $scope.datagridShows.params.description = "";
    };

    //清空添加条件
    $scope.reset = function () {
        $scope.info.username = "";
        $scope.info.name = "";
        $scope.info.idcard = "";
        $scope.info.email = "";
        $scope.info.phone = "";
//		$scope.info.type="";
    };

    //清空编辑条件
    $scope.resetEdit = function () {
        $scope.edit.name = "";
        $scope.edit.idcard = "";
        $scope.edit.email = "";
        $scope.edit.phone = "";
        // $scope.edit.type="";
    };
    $scope.search = function () {
        if ($scope.searchParams.createTimeStart && $scope.searchParams.createTimeEnd &&
            $scope.searchParams.createTimeStart > $scope.searchParams.createTimeEnd) {
            $scope.alert('开始时间不能大于结束时间', 'error');
            return false;
        }
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };

    //获取用户类型列表
    $http.post('app/system/user/types', {}).success(function (data) {
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
    });
    //添加onclick
    $scope.onAdd = function () {
        $scope.modalTitle = "添加用户";
        $scope.info = {
            agencyBoss: false
        };
        $scope.addUserForm.$setPristine();
        $("#addModal").modal('show');
    };

    //添加用户
    $scope.onSave = function () {
        if (!validateForm()) {
            return;
        }

        $scope.mask(true, 0);
        $http.post("app/system/user/add", {
            'username': $scope.info.username,
            'name': $scope.info.name,
            'agencyBoss': $scope.info.agencyBoss,
            'agencyId': $scope.userAgencyId ? $scope.userAgencyId : $scope.userTopAgencyId,
            'type': 'NORMAL'
        }).success(function (data, status, headers, config) {
            $scope.mask(false);
            if (data.success) {
                $scope.innerCtrl.load();
                $("#addModal").modal('hide');
                $scope.alert(data.message);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            } else {
                $scope.alert(data, 'error');
            }
        }).error(function (data, status, headers, config) {
            $scope.mask(false);
        });
    };

    //编辑用户
    $scope.onEdit = function () {
        $scope.modalTitle = "修改用户";
        var checkeds = $scope.innerCtrl.getChecked();
        var level = checkeds[0].agency.level;
        $scope.editUserAgencyId = level === 0 ? '' : checkeds[0].agency.id;
        $scope.editUserTopAgencyId = level !== 0 ? $scope.userTopAgencyId : checkeds[0].agency.id;

        if (checkeds.length === 1) {
            $scope.edit = checkeds[0];
            console.info(checkeds[0].agencyBoss);
            console.info(typeof checkeds[0].agencyBoss);
            console.info(checkeds[0].agencyBoss === null);
            $scope.edit = {
                'username': checkeds[0].username,
                'name': checkeds[0].name,
                'agencyBoss': checkeds[0].agencyBoss,
                'id': checkeds[0].id
            }
            ;
            $("#editModal").modal('show');
        } else {
            $scope.alert("必须选择一条记录进行编辑", 'error');
        }
    };

    //编辑用户
    $scope.editSave = function () {
        if (!validateEditForm()) {
            return;
        }
        $scope.mask(true, 2);
        $http.post("app/system/user/edit", {
            'id': $scope.edit.id,
            'username': $scope.edit.username,
            'name': $scope.edit.name,
            'agencyBoss': $scope.edit.agencyBoss,
            'agencyId': $scope.editUserAgencyId ? $scope.editUserAgencyId : $scope.editUserTopAgencyId
        }).success(function (data) {
            $scope.mask(false);
            if (data.success) {
                $scope.innerCtrl.load();
                $("#editModal").modal('hide');
                $scope.alert(data.message);
            } else
                $scope.alert(data.message, 'error');
        }).error(function () {
            $scope.mask(false);
        });
    };

    //编辑用户校验
    function validateEditForm() {
        if ($scope.ediUserForm.$invalid) {
            $scope.ediUserForm.username.$setDirty();
            $scope.ediUserForm.name.$setDirty();
            $scope.ediUserForm.idcard.$setDirty();
            $scope.ediUserForm.email.$setDirty();
            $scope.ediUserForm.phone.$setDirty();
            $scope.ediUserForm.type.$setDirty();
            return false;
        }
        return true;
    }

    //组vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    //设置组
    $scope.showGroups = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length !== 1) {
            $scope.alert("只能设置一条记录！", 'error');
            return;
        }
        $scope.groupInclDatagrid.params.name = undefined;
        $scope.groupExclDatagrid.params.name = undefined;

        $("#groupModal").modal('show');
        $scope.groupInclDatagrid.params.inclUserId = checkeds[0].id;
        $scope.groupInclCtrl.load($scope.groupInclDatagrid.params);
        $scope.groupExclDatagrid.params.exclUserId = checkeds[0].id;
        $scope.groupExclCtrl.load($scope.groupExclDatagrid.params);
    };

    //清空添加表查询表单
    $scope.clearGroupExcl = function () {
        $scope.groupExclDatagrid.params.name = undefined;
    };

    //添加组
    $scope.addGroupExcl = function () {
        var user = $scope.innerCtrl.getChecked()[0];
        var groups = $scope.groupExclCtrl.getChecked();
        if (groups.length <= 0) {
            $scope.alert("请至少选择一个", 'error');
        }
        var groupIds = [];
        groups.forEach(function (val) {
            groupIds.push(val.id);
        });

        $http.post('app/system/user/group/add', {
            id: user.id,
            ids: groupIds
        }).success(function (data) {
            if (data.success) {
                $scope.groupInclCtrl.load($scope.groupInclDatagrid.params);
                $scope.groupExclCtrl.load($scope.groupExclDatagrid.params);
            } else if (data.message) {
                $scope.alert(data.message, error);
            }
        });
    };

    //清空移除表格查询表单
    $scope.clearGroupIncl = function () {
        $scope.groupInclDatagrid.params.name = undefined;
    };

    //移除组
    $scope.removeGroupIncl = function () {
        var user = $scope.innerCtrl.getChecked()[0];
        var groups = $scope.groupInclCtrl.getChecked();
        if (groups.length <= 0) {
            $scope.alert("请至少选择一个", 'error');
        }
        var groupIds = [];
        groups.forEach(function (val) {
            groupIds.push(val.id);
        });

        $http.post('app/system/user/group/remove', {
            id: user.id,
            ids: groupIds
        }).success(function (data) {
            if (data.success) {
                $scope.groupInclCtrl.load($scope.groupInclDatagrid.params);
                $scope.groupExclCtrl.load($scope.groupExclDatagrid.params);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        });
    };

    $scope.searchGroupIncl = function () {
        $scope.groupInclCtrl.load($scope.groupInclDatagrid.params);
    };
    $scope.searchGroupExcl = function () {
        $scope.groupExclCtrl.load($scope.groupExclDatagrid.params);
    };

    var groupDataGrid = {
        url: 'app/system/group/paging',
        method: 'post',
        queryOnLoad: false,
        params: {
            inclUserType: false
        },
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
                    case 'AGENT' :
                        return '代理商';
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
            title: '描述',
            maxWidth: 100
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    $scope.groupInclDatagrid = $.extend(true, {}, groupDataGrid);

    $scope.groupExclDatagrid = $.extend(true, {}, groupDataGrid);


    //组^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //设置角色
    $scope.setRoles = function () {
        var selectUsers = $scope.innerCtrl.getChecked();
        if (selectUsers.length != 1) {
            $scope.alert("必须勾选一条记录才能设置角色！", 'error');
            return;
        }
        $scope.modalTitle = "设置角色";
        $("#roleModal").modal('show');
        $scope.roleInclDatagrid.params.name = "";
        $scope.roleExclDatagrid.params.name = "";
        $scope.roleInclDatagrid.params.inclUserId = selectUsers[0].id;
        $scope.roleInclCtrl.load($scope.roleInclDatagrid.params);
        $scope.roleExclDatagrid.params.exclUserId = selectUsers[0].id;
        $scope.roleExclCtrl.load($scope.roleExclDatagrid.params);
    };

    var roleDataGrid = {
        url: 'app/system/role/paging',
        method: 'post',
        queryOnLoad: false,
        params: {},
        columns: [{
            field: 'name',
            title: '名称'
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

    $scope.roleInclDatagrid = $.extend(true, {}, roleDataGrid);

    $scope.roleExclDatagrid = $.extend(true, {}, roleDataGrid);

    //清空移除角色查询表单
    $scope.clearRoleExcl = function () {
        $scope.roleExclDatagrid.params.name = undefined;
    };

    //清空添加角色查询表单
    $scope.clearRoleIncl = function () {
        $scope.roleInclDatagrid.params.name = undefined;
    };

    //添加角色
    $scope.addRoleExcl = function () {
        var user = $scope.innerCtrl.getChecked()[0];
        var roles = $scope.roleExclCtrl.getChecked();
        if (roles.length <= 0) {
            $scope.alert("请至少选择一个", 'alert');
        }
        var roleIds = [];
        roles.forEach(function (val) {
            roleIds.push(val.id);
        });

        $http.post('app/system/user/role/add', {
            id: user.id,
            ids: roleIds
        }).success(function (data) {
            if (data.success) {
                $scope.roleInclCtrl.load($scope.roleInclDatagrid.params);
                $scope.roleExclCtrl.load($scope.roleExclDatagrid.params);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        });
    };
    //移除角色
    $scope.removeRoleIncl = function () {
        var user = $scope.innerCtrl.getChecked()[0];
        var roles = $scope.roleInclCtrl.getChecked();
        if (roles.length <= 0) {
            $scope.alert("请至少选择一个", 'error');
        }
        var roleIds = [];
        roles.forEach(function (val) {
            roleIds.push(val.id);
        });

        $http.post('app/system/user/role/remove', {
            id: user.id,
            ids: roleIds
        }).success(function (data) {
            if (data.success) {
                $scope.roleInclCtrl.load($scope.roleInclDatagrid.params);
                $scope.roleExclCtrl.load($scope.roleExclDatagrid.params);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        });
    };
    //查询清空
    $scope.searchRoleIncl = function () {
        $scope.roleInclCtrl.load($scope.roleInclDatagrid.params);
    };
    $scope.searchRoleExcl = function () {
        $scope.roleExclCtrl.load($scope.roleExclDatagrid.params);
    };

    //添加用户输入校验
    function validateForm() {
        if ($scope.addUserForm.$invalid) {
            $scope.addUserForm.username.$setDirty();
            $scope.addUserForm.name.$setDirty();
            $scope.addUserForm.idCard.$setDirty();
            $scope.addUserForm.email.$setDirty();
            $scope.addUserForm.phone.$setDirty();
            $scope.addUserForm.type.$setDirty();
            return false;
        }
        return true;
    }

    //删除用户
    $scope.onDelete = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length !== 1) {
            $scope.alert("只能选择一条记录！", 'error');
            return;
        }

        $scope.confirm("将要删除" + checkeds.length + "条记录", function (y) {
            if (y) {
                $http.post('app/system/user/del', {
                    'userId': checkeds[0].id
                }).success(function (data) {
                    if (data.success) {
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else
                        $scope.alert(data.message);
                });

            }
        });
    }
}]);
var transFun = function (data) {
        return $.param(data);
    },
    postCfg = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        transformRequest: transFun
    };
