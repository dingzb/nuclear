/**
 * 角色管理
 */
angular.module('ws.app').controller('systemRoleCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {

    $scope.searchParams = {};
    $scope.zNodes = [{id: "-1", name: "正在加载...", isParent: false, pId: "-1"}];
    $scope._tree = "";

    $scope.datagrid = {
        url: 'app/system/role/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'name',
            title: '角色名称',
            width: 20
        }, {
            field: 'description',
            title: '描述',
            width: 50
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 15
        }, {
            field: 'modifyTime',
            title: '修改时间',
            width: 15
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    //添加onclick
    $scope.onAdd = function () {
        $scope.role = {};
        $scope.modalTitle = "添加角色";
        $scope.addRoleForm.$setPristine();
        $("#addModal").modal('show');
    };

    /**
     * 添加角色
     */
    $scope.addRole = function () {
        if ($scope.addRoleForm.$valid) {
            $scope.mask(true, 0);
            $scope.role.type = 'NORMAL';
            $http({
                url: 'app/system/role/add',
                method: 'post',
                data: $scope.role
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    //关闭
                    $('#addModal').modal('hide');
                    $scope.innerCtrl.load($scope.datagrid.params);
                } else {
                    $scope.alert(data.message, 'error');
                }
            }).error(function () {
                $scope.mask(false);
            });
        } else {
            $scope.addRoleForm.name.$setDirty();
        }
    };

    //查询
    $scope.searchRole = function () {
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };
    $scope.resetSearch = function () {
        var clearSearch = {
            name: '',
            userCheck: false
        };
        $.extend($scope.searchParams, clearSearch);
        $.extend($scope.datagrid.params, clearSearch);
    };

    $scope.resetEdit = function () {
        $scope.role.description = "";
//        $scope.role.type="NORMAL";
    };

    $scope.reset = function (str) {
        $scope.role.name = "";
        $scope.role.description = "";
        //$scope.role.type="NORMAL";
    };

    $scope.showEdit = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length != 1) {
            $scope.alert('只能选择一项', 'error');
            return;
        }
        $scope.role = {
            id: checkeds[0].id,
            name: checkeds[0].name,
            type: checkeds[0].type,
            description: checkeds[0].description
        };
        $('#editModal').modal('show');
    };

    /**
     * 编辑
     */
    $scope.editRole = function () {
        if ($scope.editRoleForm.$valid) {
            $scope.mask(true, 2);
            $http({
                url: 'app/system/role/edit',
                method: 'post',
                data: $scope.role
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    //关闭
                    $('#editModal').modal('hide');
                    $scope.innerCtrl.load($scope.datagrid.params);
                } else {
                    $scope.alert(data.message, 'error');
                }
            }).error(function () {
                $scope.mask(false);
            });
        } else {
            $scope.editRoleForm.name.$setDirty();
        }
    };

    /**
     * 删除
     */
    $scope.del = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        var selectRowIds = [];
        if (checkeds.length <= 0) {
            $scope.alert('只能选一项', 'error');
            return;
        }
        for (var i = 0; i < checkeds.length; i++) {
            selectRowIds[i] = checkeds[i].id;
        }
        $scope.confirm("将要删除" + checkeds.length + "条记录", function (y) {
            if (!y) {
                return;
            }
            $http.post('app/system/role/del', {
                'ids': selectRowIds
            }).success(function (data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params);
                    $scope.alert(data.message);
                } else
                    $scope.alert(data.message, 'error');
            });
        });
    };

    /**********配置权限方法**************************/

    //初始化树形 目录
    $scope.initTree = function () {
        var leftTreeSetting = {
            treeId: 'roleTree',
            callback: {
                // onCheck: onCheck,
                onClick: function (event, treeId, treeNode, clickFlag) {
                    $scope.currentStateId = treeNode.id;
                    var params = angular.extend($scope.actionDatagrid.params, {
                        roleIds: $scope.checkedRole.id
                    });

                    if (treeNode.checked) {
                        params.stateId = $scope.currentStateId;

                    } else {
                        params.stateId = 'undefined';
                    }
                    $scope.actionInnerCtrl.load(params);
                }
            },
            check: {
                enable: true
            },
            data: {
                keep: {
                    parent: false //节点为空是否保持节点锁
                },
                simpleData: {
                    enable: true
                }
            },
            setting: {
                check: {
                    enable: true,
                    chkStyle: "checkbox",
                    chkboxType: {
                        "Y": "ps",
                        "N": "ps"
                    }
                }
            }
        };
        $scope._tree = $.fn.zTree.init($("#roleTree"), leftTreeSetting, $scope.zNodes);
    };

    // function addHoverDom(treeId, treeNode) {
    //     if (!treeNode.checked) return;
    //     var aObj = $("#" + treeNode.tId + "_a");
    //     var btn = $("#diyBtn_" + treeNode.id);
    //     if (btn.length > 0) return;
    //     var editStr = '<img id="diyBtn_' + treeNode.id + '" title="配置【' + treeNode.name + '】的权限" onfocus="this.blur();"' + ' src="resources/image/wrench.png" style="border-width: 0; position: absolute;"/>';
    //     aObj.append(editStr);
    //     btn = $("#diyBtn_" + treeNode.id);
    //     if (btn) btn.bind("click", function () {
    //         $scope.currentStateId = treeNode.id;
    //         $scope.actionInnerCtrl.load(angular.extend($scope.actionDatagrid.params, {
    //             stateId: $scope.currentStateId,
    //             roleIds: $scope.checkedRole.id
    //         }));
    //         // removeHoverDom(treeId, treeNode);
    //     });
    // }
    //
    // function removeHoverDom(treeId, treeNode) {
    //     $("#diyBtn_" + treeNode.id).unbind().remove();
    // }
    //
    // function onCheck(event, treeId, treeNode) {
    //     if (!treeNode.checked) {
    //         removeHoverDom(treeId, treeNode);
    //     }
    // }

    /**
     * 打开配置角色权限窗口
     */
    $scope.showAuth = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length != 1) {
            $scope.alert('只能选一项', 'error');
            return;
        }
        $scope.checkedRole = checkeds[0];
        //获取state树
        $http({
            url: 'app/system/state/tree',
            method: 'post',
            data: {
                roleId: $scope.checkedRole.id
            }
        }).success(function (data, status) {
            if (status == 200 && data.success) {
                $('#authModal').modal('show');

                var nodes = $scope._tree.getNodes();
                var array = nodes.concat();
                for (var i = 0; i < array.length; i++) {
                    $scope._tree.removeNode(array[i]);
                }
                $scope._tree.addNodes(null, data.data, false);
                // console.info(data.data);
            } else {
                $scope.alert(data.message ? data.message : data, 'error');
            }
        });

    };

    //获取角色类型列表
    $http.post('app/system/role/types', {}).success(function (data) {
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

    $scope.actionDatagrid = {
        url: 'app/system/action/paging',
        method: 'post',
        params: {},
        queryOnLoad: false,
        toolbar: false,
        paging: false,
        columns: [{
            field: 'name',
            title: '访问名称',
            width: 30
        }, {
            field: 'code',
            title: '编码',
            width: 35
        }, {
            field: 'url',
            title: '地址',
            width: 35
        }],
        checkbox: {
            field: 'selected'
        }
    };

    /**
     * 获取选中的actionaid
     */
    function getCheckedAction() {
        var roleActionIds = [];
        var checkedActions = $scope.actionInnerCtrl.getChecked();
        angular.forEach(checkedActions, function (data) {
            roleActionIds.push(data.id);
        });
        return roleActionIds;
    }


    /**
     * 保存action并返回
     */
    $scope.saveAction = function () {
        var checkedActionIds = getCheckedAction();
        // if (checkedActionIds && checkedActionIds.length > 0) {
        $scope.mask(true, 0);
        $http({
            url: 'app/system/role/action/save',
            method: 'post',
            data: {
                roleId: $scope.checkedRole.id,
                actionIds: checkedActionIds,
                stateId: $scope.currentStateId
            }
        }).success(function (data) {
            $scope.mask(false);
            if (data.success) {
                $scope.alert("保存访问成功");
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function () {
            $scope.mask(false);
        });
        // } else {
        //     $scope.alert("至少选择一个访问在保存！");
        // }
    };

    /**
     * 保存访问连接state
     */
    $scope.saveState = function () {
        var selectedIds = [];
        var selecteds = $scope._tree.getCheckedNodes();
        console.info(selecteds);
        angular.forEach(selecteds, function (data) {
            selectedIds.push(data.id);
        });

        $scope.mask(true, 0);
        $http({
            url: 'app/system/role/state/save',
            method: 'post',
            data: {
                roleId: $scope.checkedRole.id,
                stateIds: selectedIds
            }
        }).success(function (data) {
            $scope.mask(false);
            if (data.success) {
                $scope.alert("保存访问成功");
            } else {
                $scope.alert(data.message, 'error');
            }
        })
            .error(function () {
                $scope.mask(false);
            });
    };

}]);
