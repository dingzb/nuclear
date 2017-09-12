/**
 * ac管理
 * Dzb
 */
angular.module('ws.app')
.controller('acClusterManaCtrl', function($scope, $http) {
    $scope.initPage = function() {
        //初始化分页列表
        $scope.innerCtrl = {};
        $scope.datagrid = {
            url: 'app/ac/apmanage/getAcClusterPaging',
            method: 'post',
            params: {},
            columns: [{
                field: 'name',
                title: '名称'
            },{
                field: 'code',
                title: '地址'
            },{
                field: 'id',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.onView(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>" ;
                    //+ "<button type=\"button\" class=\"btn btn-link btn-sm\" title='编辑' onClick=\"angular.custom.onEdit(" + str + ")\"><span class=\"glyphicon glyphicon-pencil\"> </span></button>" 
                    //+ "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete('" + row['id'] + "')\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
                }
            }],
            checkbox: {
                field: 'yes'
            },
            sizes: [10, 20, 50, 80],
            pageSize: 3
        };
    }

    function validateForm() {
        if ($scope.infoForm.$invalid) {
            $scope.infoForm.name.$setDirty();
            $scope.infoForm.code.$setDirty();
            return false;
        }
        return true;
    }

    $scope.onAdd = function() {
        $scope.modalTitle = "添加管控中心";
        $scope.info = {};
        $scope.infoForm.$setPristine();
        $scope.isSave = true;
        $("#addModal").modal('show');
    }

    $scope.onEdit = function() {
        $scope.modalTitle = "修改管控中心";
        $scope.isSave = false;
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length == 1) {
            // $scope.info = checkeds[0];
            $scope.info = {};
            angular.extend($scope.info,checkeds[0]);
            $("#addModal").modal('show');
        } else {
            $scope.showMsg("只能选择一个进行编辑");
        }
    }

    $scope.onSave = function() {
        if (!validateForm()) {
            return;
        }
        var url = "";
        if ($scope.isSave) {
            url = "app/ac/apmanage/addAcCluster";
        } else {
            url = "app/ac/apmanage/editAcCluster";
        }

        $http.post(url, {
            'id': $scope.info.id,
            'code': $scope.info.code,
            'name': $scope.info.name,
        }).success(function(data,status) {
        	if(status==403){
    		  $scope.showMsg("没有权限操作");
    		  return;
    		}
            if (data.success) {
                $("#addModal").modal('hide');
                $scope.showMsg(data.message, true);
                $scope.innerCtrl.load($scope.datagrid.params);
            } else {
                $scope.showMsg(data.message);
            }
        }).error(function(){
            $scope.mask(false);
            $scope.showMsg("操作失败");
        });
    }
    $scope.onDelete = function() {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.showMsg("必须勾选一条记录才能删除！");
            return;
        }

        $scope.showMsg("将要删除" + checkeds.length + "条记录", null, function(checkeds) {
            var selectRowIds = new Array();
            for (var i = 0; i < checkeds.length; i++) {
                selectRowIds[i] = checkeds[i].id;
            }
            $http.post('app/ac/apmanage/removeAcCluster', {
                    'param': selectRowIds
                })
                .success(function(data,status) {
                	if(status==403){
            		  $scope.showMsg("没有权限操作");
            		  return;
            		}
                    if (data.success) {
                        $scope.showMsg(data.message, true);
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else
                        $scope.showMsg(data.message);
                }).error(function(){
                    $scope.showMsg("操作失败");
                });
        }, checkeds);
    }

    $scope.reset = function() {
        var tmp="";
        if($scope.info.id){
          tmp=$scope.info.id;
        }
        $scope.info = {};
        $scope.info.code = "";
        $scope.info.id = tmp;
    }

    $scope.onResetSearch = function() {
        $scope.datagrid.params.name="";
        $scope.datagrid.params.code="";
    }

    angular.custom.onEdit = function(row) {
        $scope.$apply(function(){
          $scope.modalTitle = "修改管控中心";
          $scope.isSave = false;
          $scope.info = row;
        }); 
        $("#addModal").modal('show');
    }

    angular.custom.onView = function(row) {
        $scope.$apply(function(){
          $scope.modalTitle = "管控中心详细信息";
          $scope.info = row;
        }); 
        $("#viewModal").modal('show');
    }


    angular.custom.onDelete = function(id) {
        $scope.showMsg("确认要删除？", null, function(id) {
            var selectRowIds = new Array();
            selectRowIds.push(id);

            $http.post('app/ac/apmanage/removeAcCluster', {
                    'param': selectRowIds
                })
                .success(function(data,status) {
                	if(status==403){
            		  $scope.showMsg("没有权限操作");
            		  return;
            		}
                    if (data.success) {
                        $scope.showMsg(data.message, true);
                        $scope.innerCtrl.load($scope.datagrid.params)
                    } else {
                        $scope.showMsg(data.message);
                    }
                }).error(function(){
                    $scope.showMsg("操作失败");
                });
        }, id);
    }
});
