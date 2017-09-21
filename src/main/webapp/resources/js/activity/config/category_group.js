angular.module('ws.app').controller('actCategoryGroupCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    $scope.searchParams = {};

    $scope.search = function () {
        $scope.innerCtrl.load($.extend({}, $scope.datagrid.params, $scope.searchParams));
    };

    $scope.searchReset = function () {
        $scope.searchParams = {};
    };

    $http.post('app/activity/config/category/list',{}).success(function (data) {
        if (data.success){
            $scope.categories = data.data;
        } else {
            $scope.alert(data.message, 'error');
        }
    }).error(function (data) {
        $scope.alert(data, 'error');
    });

    //初始化组列表
    $scope.datagrid = {
        url: 'app/activity/config/category/group/paging',
        method: 'post',
        params: {
            current: true
        },
        columns: [{
            field: 'name',
            title: '名称',
            width: 15
        }, {
            field: 'description',
            title: '描述',
            width: 15
        }, {
            field: 'limitFirst',
            title: '一等奖限制',
            width: 8
        }, {
            field: 'limitSecond',
            title: '二等奖限制',
            width: 8
        }, {
            field: 'limitThird',
            title: '三等奖限制',
            width: 8
        }, {
            field: 'expertCount',
            title: '专家数',
            width: 8
        }, {
            field: 'categoryCodes',
            title: '专业',
            width: 38,
            // formatter: function(row) {
            //     var str = '';
            //     for (cc in row['categoryCodes']) {
            //         str += cc;
            //         str += ','
            //     }
            //     return str.split(0, str.length - 1);
            // }
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };

    $scope.reset = function () {
        $scope.categoryGroup = {
            name: '',
            description: '',
            limitFirst: undefined,
            limitSecond: undefined,
            limitThird: undefined
        };
        $scope.addForm.$setPristine();
    };

    $scope.categoryGroup = {
        name: '',
        description: ''
    };

    $scope.showAdd = function () {
        $scope.reset();
        $("#addModal").modal('show');
    };

    $scope.add = function () {
        if ($scope.addForm.$invalid) {
            $scope.addForm.name.$setDirty();
            return false;
        }

        $http.post("app/activity/config/category/group/add", $.extend({
            activityId: $scope.curAct.id ? $scope.curAct.id : ''
        }, $scope.categoryGroup)).success(function (data) {
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

    $scope.showEdit = function () {
        var checked = $scope.innerCtrl.getChecked();
        if (checked.length === 1) {
            $scope.categoryGroup = {
                id: checked[0].id,
                name: checked[0].name,
                description: checked[0].description,
                limitFirst: checked[0].limitFirst,
                limitSecond: checked[0].limitSecond,
                limitThird: checked[0].limitThird
            };
            $("#editModal").modal('show');
        } else {
            $scope.alert("只能选择一个进行编辑", 'error');
        }
    };

    $scope.edit = function () {
        if ($scope.editForm.$invalid) {
            $scope.editForm.name.$setDirty();
            return false;
        }

        $http.post("app/activity/config/category/group/edit", $scope.categoryGroup).success(function (data) {
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
            $http.post('app/activity/config/category/group/del', {
                'ids': ids
            }).success(function (data) {
                if (data.success) {
                    $scope.search();
                    $scope.alert(data.message);
                } else{
                    $scope.alert(data.message, 'error');
                }
            }).error(function (data) {
                $scop.alert(data, 'error');
            });
        });
    };

    $scope.refreshGroupCategories = function (fn){
        $http.post('app/activity/config/category/list/categorygroup', {
            categoryGroupId: $scope.groupCategoriesId
        }).success(function (data) {
            if (data.success) {
                $scope.groupCategories = data.data;
                if (typeof fn === 'function') {
                    fn(data.data);
                }
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    };

    $scope.showGroupCategories = function () {

        var checked = $scope.innerCtrl.getChecked();
        $scope.groupCategoriesId = checked[0].id;
        if (checked.length === 1) {
            $scope.refreshGroupCategories(function(){
                $("#categoriesModal").modal('show');
            });
        } else {
            $scope.alert("只能选择一个进行编辑", 'error');
        }
    };

    $scope.keyWord = '';

    $scope.searchCategory = function () {
        $scope.keyWord = $scope.keyWord.replace(/\s+/g, '');
        var reg = new RegExp($scope.keyWord, 'g');
        $scope.sCategories = [];
        if ($scope.keyWord) {
            for (var i = 0; i < $scope.categories.length; i++){
                if ($scope.categories[i].code &&$scope. categories[i].code.match(reg)) {
                    if ($scope.sCategories.length < 10) {
                        $scope.sCategories.push($scope.categories[i]);
                    }
                }
            }
        }
    };

    $scope.addCategoryToGroup = function (c) {
        $http.post('app/activity/config/category/group/add/category',{
            categoryGroupId: $scope.groupCategoriesId,
            categoryId: c
        }).success(function (data) {
            if (data.success) {
                $scope.refreshGroupCategories();
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        })
    };

    $scope.removeCategoryFromGroup = function (c) {
        $http.post('app/activity/config/category/group/remove/category',{
            categoryGroupId: $scope.groupCategoriesId,
            categoryId: c
        }).success(function (data) {
            if (data.success) {
                $scope.refreshGroupCategories();
            } else {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        })
    }

}]);


