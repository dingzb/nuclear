/**
 * Created by Dzb on 2017/3/8.
 */
/**
 * 访问控制
 */
angular.module('ws.app').controller('systemActionCtrl', ['$scope', '$http', 'stateSer', function ($scope, $http, stateSer) {

    stateSer.setScope($scope);

    $scope.search = function () {
        var stateId = undefined;
        if ($scope.datagrid.params.notMenustateId) {
            stateId = $scope.datagrid.params.notMenustateId;
        } else if ($scope.datagrid.params.stateId) {
            stateId = $scope.datagrid.params.stateId;
        } else if ($scope.datagrid.params.menuGroupId) {
            stateId = $scope.datagrid.params.menuGroupId;
        } else if ($scope.datagrid.params.subsystemId) {
            stateId = $scope.datagrid.params.subsystemId;
        }

        $scope.innerCtrl.load($.extend(true, {}, $scope.datagrid.params, {
            stateId: stateId
        }));
    };

    /**
     * 获取子系统列表
     */
    $http({
        url: 'app/system/state/paging',
        method: 'post',
        data: {
            type: 2
        }
    }).success(function (data) {
        if (data.success) {
            $scope.subsystem = data.data.rows;
        }
    });

    $http({
        url: 'app/system/state/paging',
        method: 'post',
        data: {
            type: 3
        }
    }).success(function (data) {
        if (data.success) {
            $scope.notMenustates = data.data.rows;
        }
    });

    /**
     * 根据指定子系统获取目录组
     */
    $scope.getMenugroups = function (val, s, fn, gSelected) {
        $http({
            url: 'app/system/state/paging',
            method: 'post',
            data: {
                type: 1,
                parentId: val == '' ? 'NULL' : val
            }
        }).success(function (data) {
            if (data.success) {
                $scope.menugroup = data.data.rows;
                $scope.menus = [];
                if (!s) {
                    if ($scope.editaction) {
                        $scope.editaction.stateId = '';
                        $scope.editaction.menuGroupId = gSelected ? gSelected : '';
                    }
                    if ($scope.addaction) {
                        $scope.addaction.stateId = '';
                        $scope.addaction.menuGroupId = gSelected ? gSelected : '';
                    }
                } else {
                    $scope.datagrid.params.stateId = '';
                    $scope.datagrid.params.menuGroupId = gSelected ? gSelected : '';
                }
                if (typeof fn == 'function') {
                    fn();
                }
            }
        });
    };

    /**
     * 根据指定目录组获取目录
     */
    $scope.getMenus = function (parnetId, s, fn, mSelected) {
        $http({
            url: 'app/system/state/paging',
            method: 'post',
            data: {
                type: 0,
                parentId: parnetId == '' ? 'NULL' : parnetId
            }
        }).success(function (data) {
            if (data.success) {
                $scope.menus = data.data.rows;
                if (!s) {
                    if ($scope.editaction) {
                        $scope.editaction.stateId = mSelected ? mSelected : '';
                    }
                    if ($scope.addaction) {
                        $scope.addaction.stateId = mSelected ? mSelected : '';
                    }
                } else {
                    $scope.datagrid.params.stateId = mSelected ? mSelected : '';
                }
                if (typeof fn == 'function') {
                    fn();
                }
            }
        });
    };

    /**
     * 获取最高至子系统的父节点列表
     * @param val
     */
    $scope.getParentSubsystem = function (val) {
        $http({
            url: 'app/system/state/hierarchysubsystem',
            method: 'post',
            data: {
                id: val
            }
        }).success(function (data) {
            if (data.success) {
                $scope.getMenugroups(data.data[data.data.length - 1].id, false, function () {
                    $.extend($scope.editaction, {
                        subsystemId: data.data[data.data.length - 1].id
                    });
                    $scope.getMenus(data.data[data.data.length - 2].id, false, function () {
                        $.extend($scope.editaction, {stateId: val});
                    }, val);
                }, data.data[data.data.length - 2].id);
            } else {
                $scope.alert(data.message, 'error');
            }
        });
    };

    $scope.delAccess = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.alert('至少选一项', 'error');
            return;
        }
        $scope.confirm('确定要删除这[' + checkeds.length + ']项吗？', function (y) {
            if (!y) {
                return;
            }
            var ids = [];
            $(checkeds).each(function (i) {
                ids.push(checkeds[i].id);
            });

            $http({
                url: 'app/system/action/del',
                method: 'post',
                data: {
                    ids: ids
                }
            }).success(function (data) {
                if (data.success) {
                    $scope.search();
                }
            });
        });
    };

    /**
     * 显示模态窗
     * @param id 显示窗口id
     * @param formId 需要清空的form Id
     */
    $scope.showAdd = function (id, formId) {

        $scope.addaction = {};

        fields = $('#' + formId).find('input,select');

        fields.each(function (i) {
            $scope[formId][fields[i].name].$dirty = false;
        });
        $scope.addaction.stateId = $scope.datagrid.params.stateId;
        $scope.addaction.subsystemId = $scope.datagrid.params.subsystemId;
        $scope.addaction.menuGroupId = $scope.datagrid.params.menuGroupId;
        $scope.addaction.notMenustateId = $scope.datagrid.params.notMenustateId;
        $('#' + id).modal('show');
    };

    /**
     * 增加
     */
    $scope.addAccess = function () {
        var stateId = undefined;
        if ($scope.addaction.notMenustateId) {
            stateId = $scope.addaction.notMenustateId;
        } else if ($scope.addaction.stateId) {
            stateId = $scope.addaction.stateId;
        } else if ($scope.addaction.menuGroupId) {
            stateId = $scope.addaction.menuGroupId;
        } else if ($scope.addaction.subsystemId) {
            stateId = $scope.addaction.subsystemId;
        }
        if ($scope.addAction.$valid) {
            $scope.mask(true, 0);
            $http({
                url: 'app/system/action/add',
                method: 'post',
                data: angular.extend($scope.addaction, {
                    stateId: stateId
                })
            }).success(function (data) {
                $scope.mask(false);
                if (data.success) {
                    //关闭
                    $('#addModal').modal('hide');
                    //重新加载数据
                    $scope.search();
                } else {
                    alert(data.message);
                }
            })
                .error(function () {
                    $scope.mask(false);
                });
        } else {
            $scope.addAction.actionName.$setDirty();
            $scope.addAction.actionCode.$setDirty();
            $scope.addAction.actionUrl.$setDirty();
            $scope.addAction.searcheMenugroupSubsystemId.$setDirty();
            $scope.addAction.searcheMenugroupId.$setDirty();
            $scope.addAction.searcheMenuId.$setDirty();
        }
    };
    $scope.reset = function (formId) {
        $('#' + formId)[0].reset();
        $scope.action = {};
        $scope.addaction = {};
    };
    $scope.showEdit = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length != 1) {
            $scope.alert('只能选择一项', 'error');
            return;
        }
        console.info(checkeds[0]);
        //获取其父节点列表
        $scope.getParentSubsystem(checkeds[0].stateId);


        $scope.editaction = {
            id: checkeds[0].id,
            name: checkeds[0].name,
            code: checkeds[0].code,
            url: checkeds[0].url,
            method: checkeds[0].method
        };
        $('#editModal').modal('show');
    };

    $scope.editAccess = function () {

        var stateId = undefined;
        if ($scope.editaction.notMenustateId) {
            stateId = $scope.editaction.notMenustateId;
        } else if ($scope.editaction.stateId) {
            stateId = $scope.editaction.stateId;
        } else if ($scope.editaction.menuGroupId) {
            stateId = $scope.editaction.menuGroupId;
        } else if ($scope.editaction.subsystemId) {
            stateId = $scope.editaction.subsystemId;
        }

        if ($scope.editAction.$valid) {
            $http({
                url: 'app/system/action/edit',
                method: 'post',
                data: angular.extend($scope.editaction, {
                    stateId: stateId
                })
            }).success(function (data) {
                if (data.success) {
                    //关闭
                    $('#editModal').modal('hide');
                    //重新加载数据
                    $scope.search();
                } else {
                    $scope.alert(data.message, 'error');
                }
            });
        } else {
            $scope.editAction.actionName.$setDirty();
            $scope.editAction.actionCode.$setDirty();
        }
    };

    $scope.datagrid = {
        url: 'app/system/action/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'name',
            title: '访问名称'
        }, {
            field: 'code',
            title: '编码'
        }, {
            field: 'url',
            title: '地址'
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

}]);