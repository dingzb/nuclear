/**
 * Created by Dzb on 2017/3/8.
 *
 * 编辑state通用service
 */
angular.module('ws.app').service('stateSer', ['$http', function ($http) {

    this.setScope = function ($scope) {
        //添加或编辑时的对象
        $scope.state = {};

        /**
         * 清空编辑、添加
         */
        $scope.reset = function (formId) {
            // $('#' + formId)[0].reset();
            $scope.state = {};
        };
        /**
         * 清空查询表单
         */
        $scope.resetSearch = function () {
            $scope.datagrid.params.name = undefined;
            $scope.datagrid.params.code = undefined;
        };
        /**
         * 显示模态窗
         * @param id 显示窗口id
         * @param formId 需要清空的form Id
         */
        $scope.showAdd = function (id, formId) {

            $scope.state = {};
            fields = $('#' + formId).find('input,select');

            fields.each(function (i) {
                $scope[formId][fields[i].name].$dirty = false;
            });
            $scope.state.parentId = $scope.datagrid.params.parentId;
            $scope.state.subsysId = $scope.datagrid.params.subsystemId;
            $('#' + id).modal('show');
        };

        /**
         * 添加state
         * 0:state,1:group,2subsystem,null:virtual
         * @param type state类型
         * @param form 表单名称
         * @param elseFn 验证没有通过后的回调函数
         */
        $scope.addState = function (type, form, elseFn) {
            if ($scope[form].$valid) {
                $scope.state.type = type;
                $scope.mask(true, 0);
                $http({
                    url: 'app/system/state/add',
                    method: 'post',
                    data: $scope.state
                }).success(function (data) {
                    $scope.mask(false);
                    if (data.success) {
                        //关闭
                        $('#addModal').modal('hide');
//						//清空
//						$scope.reset(form);
                        //重新加载数据
//						$scope.innerCtrl.load({
//							type:type
//						});
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else {
                        alert(data.message);
                    }
                });
            } else {
                elseFn();
            }
        };

        /**
         * 显示编辑窗口
         * @param id 要显示的modal id
         * @param fn 需要在验证通过后执行的函数
         */
        $scope.showEdit = function (id, fn) {
            console.info('[[[[[', $scope.datagrid.params);
            var checkeds = $scope.innerCtrl.getChecked();
            if (checkeds.length != 1) {
                $scope.alert('只能选择一项');
                return;
            }
            if (typeof fn == 'function') {
                fn(checkeds[0].parentId);
            } else {
                $scope.state.parentId = checkeds[0].parentId;
            }
            $scope.state.id = checkeds[0].id;
            $scope.state.name = checkeds[0].name;
            $scope.state.code = checkeds[0].code;
            $scope.state.sequence = checkeds[0].sequence;
            $scope.state.description = checkeds[0].description;


            $('#' + id).modal('show')
        };
        /**
         * 编辑state
         * 0:state,1:group,2subsystem,null:virtual
         * @param modal 模态窗id
         * @param form 表单id
         * @param elseFn 验证失败时执行的函数
         */
        $scope.editState = function (type, modal, form, elseFn) {
            console.info('[[[[[', $scope.datagrid.params);
            if ($scope[form].$valid) {
                $scope.state.type = type;
                $scope.mask(true, 2);
                $http({
                    url: 'app/system/state/edit',
                    method: 'post',
                    data: $scope.state
                }).success(function (data) {
                    $scope.mask(false);
                    if (data.success) {
                        //关闭
                        $('#' + modal).modal('hide');
                        //重新加载数据
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else {
                        $scope.alert(data.message, 'error');
                    }
                });
            } else {
                elseFn();
            }
        };

        /**
         * 删除
         */
        $scope.delState = function (type) {
            var checkeds = $scope.innerCtrl.getChecked();
            if (checkeds.length <= 0) {
                $scope.alert('至少选一项', 'error');
                return;
            }

            $scope.confirm('确定要删除吗', function (y) {
                if (!y) {
                    return;
                }
                var ids = [];
                $(checkeds).each(function (i) {
                    ids.push(checkeds[i].id);
                });
                $http({
                    url: 'app/system/state/del',
                    method: 'post',
                    data: {
                        ids: ids
                    }
                }).success(function (data) {
                    if (data.success) {
                        //关闭
                        $('#editModal').modal('hide');
                        //清空
                        $scope.state = {};
                        //重新加载数据
                        $scope.datagrid.params.type = type;
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else {
                        $scope.alert(data.message, 'error');
                    }
                });
            });
        };
    };
}]).controller('systemSubsystemCtrl', ['$scope', 'stateSer', function ($scope, stateSer) {

    stateSer.setScope($scope);

    /**
     * 设置表单验证时的回调函数
     */
    $scope.setDirty = function () {
        $scope.addSubsystem.subsystemName.$setDirty();
        $scope.addSubsystem.subsystemCode.$setDirty();
        $scope.addSubsystem.subsystemSequence.$setDirty();
    };

    $scope.datagrid = {
        url: 'app/system/state/paging',
        method: 'post',
        params: {
            type: 2
        },
        columns: [{
            field: 'name',
            title: '子系统名称'
        }, {
            field: 'code',
            title: '编码(State)'
        }, {
            field: 'sequence',
            title: '排序'
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
}]).controller('systemMenuGroupCtrl', ['$scope', '$http', 'stateSer', function ($scope, $http, stateSer) {

    stateSer.setScope($scope);

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

    /**
     * 设置表单验证时的回调函数
     */
    $scope.setAddDirty = function () {
        $scope.addMenugroup.menugroupName.$setDirty();
        $scope.addMenugroup.menugroupCode.$setDirty();
        $scope.addMenugroup.menugroupSubsystemId.$setDirty();
        $scope.addMenugroup.menugroupSequence.$setDirty();
    };
    /**
     * 设置表单验证时的回调函数
     */
    $scope.setEidtDirty = function () {
        $scope.editMenugroup.menugroupName.$setDirty();
        $scope.editMenugroup.menugroupCode.$setDirty();
        $scope.editMenugroup.menugroupSubsystemId.$setDirty();
        $scope.editMenugroup.menugroupSequence.$setDirty();
    };

    $scope.datagrid = {
        url: 'app/system/state/paging',
        method: 'post',
        params: {
            type: 1
        },
        columns: [{
            field: 'name',
            title: '目录组名称'
        }, {
            field: 'code',
            title: '编码(State)'
        }, {
            field: 'sequence',
            title: '排序'
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
}])
    .controller('systemMenuCtrl', ['$scope', '$http', 'stateSer', function ($scope, $http, stateSer) {

        stateSer.setScope($scope);

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
        /**
         * 根据指定子系统获取目录组
         */
        $scope.getMenugroups = function (val, s, callback, selected) {
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
                    if (s) {
                        $scope.datagrid.params.parentId = selected ? selected : '';
                    } else {
                        $scope.state.parentId = selected ? selected : '';
                    }
                    if (typeof callback == 'function') {
                        callback();
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
                        $.extend($scope.state, {
                            subsysId: data.data[data.data.length - 1].id
                        });
                    }, val);
                } else {
                    $scope.alert(data.message, 'error');
                }
            });
        };
        /**
         * 设置表单验证时的回调函数
         */
        $scope.setAddDirty = function () {
            $scope.addMenu.menuName.$setDirty();
            $scope.addMenu.menuCode.$setDirty();
            $scope.addMenu.menuSubsystemId.$setDirty();
            $scope.addMenu.menuGroupId.$setDirty();
            $scope.addMenu.menuSequence.$setDirty();
        };
        /**
         * 设置表单验证时的回调函数
         */
        $scope.setEidtDirty = function () {
            $scope.editMenu.menuName.$setDirty();
            $scope.editMenu.menuCode.$setDirty();
            $scope.editMenu.menuSubsystemId.$setDirty();
            $scope.editMenu.menuGroupId.$setDirty();
            $scope.editMenu.menuSequence.$setDirty();
        };

        $scope.datagrid = {
            url: 'app/system/state/paging',
            method: 'post',
            params: {
                type: 0
            },
            columns: [{
                field: 'name',
                title: '目录名称'
            }, {
                field: 'code',
                title: '编码(State)'
            }, {
                field: 'sequence',
                title: '排序'
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
    }]).controller('systemNotMenuCtrl', ['$scope', '$http', 'stateSer', function ($scope, $http, stateSer) {

    stateSer.setScope($scope);

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

    /**
     * 根据指定子系统获取目录组
     */
    $scope.getMenugroups = function (parnetId) {
        $http({
            url: 'app/system/state/paging',
            method: 'post',
            data: {
                type: 1,
                parentId: parnetId == '' ? 'NULL' : parnetId
            }
        }).success(function (data) {
            if (data.success) {
                $scope.datagrid.params.menugroupIds = undefined;
                $scope.datagrid.params.stateId = undefined;
                $scope.menus = undefined;
                $scope.menugroup = data.data.rows;
            }
        });
    };

    /**
     * 根据指定目录组获取目录
     */
    $scope.getMenus = function (parnetId, fn) {
        $http({
            url: 'app/system/state/paging',
            method: 'post',
            data: {
                type: 0,
                parentId: parnetId == '' ? 'NULL' : parnetId
            }
        }).success(function (data) {
            if (data.success) {
                $scope.datagrid.params.stateId = undefined;
                $scope.menus = data.data.rows;
                if (typeof fn == 'function') {
                    fn();
                }
            }
        });
    };

    $scope.addNotMenu = function () {
        var parentId = undefined;
        if ($scope.datagrid.params.stateId) {
            parentId = $scope.datagrid.params.stateId;
        } else if ($scope.datagrid.params.menugroupIds) {
            parentId = $scope.datagrid.params.menugroupIds;
        } else if ($scope.datagrid.params.subsystemId) {
            parentId = $scope.datagrid.params.subsystemId;
        }
        if ($scope.addAction.$valid) {
            $http({
                url: 'app/system/state/add',
                method: 'post',
                data: angular.extend($scope.notMenu, {
                    parentId: parentId,
                    type: 3
                })
            }).success(function (data) {
                if (data.success) {
                    //关闭
                    $('#addModal').modal('hide');
                    //重新加载数据
                    $scope.search();
                } else {
                    $scope.alert(data.message, 'error');
                }
            });
        } else {
            $scope.addAction.actionName.$setDirty();
            $scope.addAction.actionCode.$setDirty();
            $scope.addAction.searcheMenugroupSubsystemId.$setDirty();
            $scope.addAction.searcheMenugroupId.$setDirty();
            $scope.addAction.searcheMenuId.$setDirty();
        }
    };

    $scope.delNotMenu = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.alert('至少选一项', 'error');
            return;
        }
        if (confirm('确定要删除这[' + checkeds.length + ']项吗？')) {
            var ids = [];
            $(checkeds).each(function (i) {
                ids.push(checkeds[i].id);
            });

            $http({
                url: 'app/system/state/del',
                method: 'post',
                data: {
                    ids: ids
                }
            }).success(function (data) {
                if (data.success) {
                    //关闭
                    $('#editModal').modal('hide');
                    $scope.search();
                }
            });
        }
    };

    $scope.showEdit = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length != 1) {
            $scope.alert('只能选择一项', 'error');
            return;
        }
        //获取其父节点列表
        $scope.getParentSubsystem(checkeds[0].parentId);

        $scope.notMenu = {
            id: checkeds[0].id,
            name: checkeds[0].name,
            code: checkeds[0].code
        };
        $('#editModal').modal('show');
    };

    $scope.editNotMenu = function () {
        var parentId = undefined;
        if ($scope.datagrid.params.stateId) {
            parentId = $scope.datagrid.params.stateId;
        } else if ($scope.datagrid.params.menugroupIds) {
            parentId = $scope.datagrid.params.menugroupIds;
        } else if ($scope.datagrid.params.subsystemId) {
            parentId = $scope.datagrid.params.subsystemId;
        }
        if ($scope.addAction.$valid) {
            $http({
                url: 'app/system/state/edit',
                method: 'post',
                data: angular.extend($scope.notMenu, {
                    parentId: parentId,
                    type: 3
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
            $scope.addAction.actionName.$setDirty();
            $scope.addAction.actionCode.$setDirty();
            $scope.addAction.searcheMenugroupSubsystemId.$setDirty();
            $scope.addAction.searcheMenugroupId.$setDirty();
            $scope.addAction.searcheMenuId.$setDirty();
        }
    };

    $scope.search = function () {
        var parentId = undefined;
        if ($scope.datagrid.params.stateId) {
            parentId = $scope.datagrid.params.stateId;
        } else if ($scope.datagrid.params.menugroupIds) {
            parentId = $scope.datagrid.params.menugroupIds;
        } else if ($scope.datagrid.params.subsystemId) {
            parentId = $scope.datagrid.params.subsystemId;
        }
        $scope.innerCtrl.load(angular.extend({
            parentId: parentId
        }, $scope.datagrid.params));
    };

    /**
     * 获取其至子系统的父节点列表并赋值到相应的对象
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
                if (data.data[data.data.length - 1]) {
                    $scope.datagrid.params.subsystemId = data.data[data.data.length - 1].id;
                }
                if (data.data[data.data.length - 1] && data.data[data.data.length - 2]) {
                    $scope.getMenugroups(data.data[data.data.length - 1].id, function () {
                        $scope.datagrid.params.menugroupIds = data.data[data.data.length - 2].id;
                    });
                }
                if (data.data[data.data.length - 2]) {
                    $scope.getMenus(data.data[data.data.length - 2].id, function () {
                        $scope.datagrid.params.stateId = val;
                    })
                }
            } else {
                $scope.alert(data.message, 'error');
            }
        });
    };

    $scope.datagrid = {
        url: 'app/system/state/paging',
        method: 'post',
        params: {
            type: 3
        },
        columns: [{
            field: 'name',
            title: '目录名称'
        }, {
            field: 'code',
            title: '编码(State)'
        }, {
            field: 'sequence',
            title: '排序'
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