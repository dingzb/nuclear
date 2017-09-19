angular.module('ws.app').controller('actAwardsCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {

    $scope.searchParams = {};

    $scope.search = function () {
        $scope.innerCtrl.load($.extend({}, $scope.datagrid.params, $scope.searchParams));
    };

    $scope.searchRest = function () {
        $scope.searchParams = {};
    };

    //获取奖项类型列表
    $http.post('app/activity/config/award/type/list', {}).success(function (data) {
        if (data.success){
            $scope.types = data.data;
        }
    });

    //初始化组列表
    $scope.datagrid = {
        url: 'app/activity/config/award/paging',
        method: 'post',
        params: {
            current: true
        },
        columns: [{
            field: 'name',
            title: '名称',
            width: 25
        }, {
            field: 'typeName',
            title: '类型',
            width: 25
        }, {
            field: 'description',
            title: '描述',
            width: 30
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 20
        }, {
            field: 'id',
            title: '操作',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return '<button type="button" class="btn btn-link btn-sm" title="等级" onClick="angular.custom.award.levelDetail(' + str + ')"><span class="fa fa-trophy"></span></button>'
                    +'<button type="button" class="btn btn-link btn-sm" title="指标" onClick="angular.custom.award.levelDetail(' + str + ')"><span class="fa fa-navicon"></span></button>';
            }
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    $scope.award = {
        name: '',
        description: '',
        typeId: ''
    };

    $scope.rest = function () {
        $scope.award = {
            name: '',
            description: '',
            typeId: ''
        };
        $scope.addForm.$setPristine();
    };

    $scope.showAdd = function () {
        $scope.rest();
        $("#addModal").modal('show');
    };

    $scope.showEdit = function () {
        var checked = $scope.innerCtrl.getChecked();
        if (checked.length === 1) {
            $scope.award = {
                id: checked[0].id,
                name: checked[0].name,
                description: checked[0].description,
                typeId: checked[0].typeId
            };
            $("#editModal").modal('show');
        } else {
            $scope.alert("只能选择一个进行编辑", 'error');
        }
    };

    $scope.add = function () {
        if ($scope.addForm.$invalid) {
            $scope.addForm.name.$setDirty();
            $scope.addForm.type.$setDirty();
            return false;
        }

        $http.post("app/activity/config/award/add", $.extend({
            activityId: $scope.curAct.id ? $scope.curAct.id : ''
        }, $scope.award)).success(function (data) {
            if (data.success) {
                $scope.innerCtrl.load($scope.datagrid.params);
                $scope.alert(data.message);
                $("#addModal").modal('hide');
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.edit = function () {
        if ($scope.editForm.$invalid) {
            $scope.editForm.name.$setDirty();
            $scope.editForm.type.$setDirty();
            return false;
        }

        $http.post("app/activity/config/award/edit", $scope.award).success(function (data) {
            if (data.success) {
                $scope.innerCtrl.load($scope.datagrid.params);
                $scope.alert(data.message);
                $("#editModal").modal('hide');
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.del = function () {
        var checked = $scope.innerCtrl.getChecked();
        if (checked.length < 1) {
            $scope.alert('至少选择一项', 'error');
            return;
        }

        var ids = [];
        for (var i = 0; i < checked.length; i++) {
            ids.push(checked[i].id);
        }
        $scope.confirm("将要删除" + checked.length + "条记录", function (y) {
            if (!y) {
                return;
            }
            $http.post('app/activity/config/award/del', {
                'ids': ids
            }).success(function (data) {
                if (data.success) {
                    $scope.search();
                    $scope.alert(data.message);
                } else
                    $scope.alert(data.message, 'error');
            });
        });
    };

    //奖项等级配置======================
    $scope.showLevel = function () {
        var checked = $scope.innerCtrl.getChecked();
        if (checked.length === 1) {
            $scope.award = {
                id: checked[0].id,
                first: checked[0].first,
                second: checked[0].second,
                third: checked[0].third,
                deny: checked[0].deny
            };
            $("#levelModal").modal('show');
        } else {
            $scope.alert("只能选择一个进行编辑", 'error');
        }
    };

    $scope.updateLevel = function(){
        $http.post("app/activity/config/award/level/edit", $scope.award).success(function (data) {
            if (data.success) {
                $scope.innerCtrl.load($scope.datagrid.params);
                $scope.alert(data.message);
                $("#levelModal").modal('hide');
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    var refreshAwardCriterion = function (awardId) {
        $http.post('app/activity/config/award/criterion/list', {
            awardId: awardId
        }).success(function (data) {
            if (data.success) {
                $scope.awardCriteria = data.data;
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.showCriterion = function () {
        $scope.cancelAddCriterion();
        var checked = $scope.innerCtrl.getChecked();
        if (checked.length > 1) {
            $scope.alert("只能选择一个进行编辑", 'error');
            return;
        }
        if (checked.length === 0) {
            $scope.alert("请选择一个进行编辑", 'error');
            return;
        }

        $scope.criterion = {
            awardId: checked[0].id
        };

        refreshAwardCriterion(checked[0].id);

        $('#criterionModal').modal('show')
    };

    $scope.showAddCriterion = function () {
        $scope.criterion = {
            awardId: $scope.criterion.awardId
        };
        $scope.isAddCriterion = true;
    };

    $scope.addCriterion = function () {
        if ($scope.criterion.percent.match(/^\d+$/) === null){
            $scope.alert('占比必须为数字','error');
            return;
        }
        $http.post('app/activity/config/award/criterion/add', $scope.criterion).success(function (data) {
            if (data.success) {
                $scope.isAddCriterion = false;
                refreshAwardCriterion($scope.criterion.awardId);
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.cancelAddCriterion = function () {
        $scope.isAddCriterion = false;
    };

    $scope.showEditCriterion = function (criterion) {
        $scope.editCriterionId = criterion.id;
        $scope.eCriterion = $.extend({}, criterion)
    };

    $scope.cancelEditCriterion = function () {
        $scope.editCriterionId = undefined;
    };

    $scope.editCriterion = function () {
        if ($scope.eCriterion.percent.match(/^\d+$/) === null){
            $scope.alert('占比必须为数字','error');
            return;
        }
        $http.post('app/activity/config/award/criterion/edit', $scope.eCriterion).success(function (data) {
            if (data.success) {
                $scope.cancelEditCriterion();
                refreshAwardCriterion($scope.criterion.awardId);
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data.message, 'error');
        });
    };

    $scope.delCriterion = function () {

    };

    angular.custom.award = {
        levelDetail: function (row) {
            $scope.$apply(function(){
                $scope.award = {
                    id: row.id,
                    first: row.first,
                    second: row.second,
                    third: row.third,
                    deny: row.deny
                };
            });
            $("#levelDetailModal").modal('show');
        }
    };

}]);
