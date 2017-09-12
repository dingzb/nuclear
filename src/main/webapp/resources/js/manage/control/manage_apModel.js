/**
 * ac管理
 * Dzb
 */
angular.module('ws.app')
.controller('apModelManaCtrl', function($scope, $http) {
    $scope.initPage = function() {
        //初始化分页列表
        $scope.innerCtrl = {};
        $scope.datagrid = {
            url: 'app/ac/apmanage/getApModelPaging',
            method: 'post',
            params: {},
            columns: [{
                field: 'name',
                title: '型号名称'
            },{
                field: 'ram',
                title: 'ram',
                formatter: function(row) {
                    if(row['ram'])
                        return row['ram']+"M";
                    else
                        return '0M';
                }
            },{
                field: 'rom',
                title: 'rom',
                formatter: function(row) {
                    if(row['rom'])
                        return row['rom']+"M";
                    else
                        return '0M';
                }
            },{
                field: 'id',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.onView(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>" ;
                }
            }],
            checkbox: {
                field: 'yes'
            },
            sizes: [10, 20, 50, 80],
            pageSize: 3
        };
    }

    $scope.onAdd = function() {
        $scope.modalTitle = "添加型号信息";
        $scope.info = {};
        $scope.reset();
        $scope.infoForm.$setPristine();
        $scope.isSave = true;
        $("#addModal").modal('show');
    }

    $scope.onEdit = function() {
        $scope.modalTitle = "修改型号信息";
        $scope.isSave = false;
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length == 1) {
            // $scope.info = checkeds[0];
            $scope.info = {};
            $scope.reset();
            angular.extend($scope.info,checkeds[0]);
            $("#addModal").modal('show');
        } else {
            $scope.showMsg("只能选择一个进行编辑");
        }
    }

    function validateForm() {
        if ($scope.infoForm.$invalid) {
            $scope.infoForm.name.$setDirty();
            $scope.infoForm.code.$setDirty();
            $scope.infoForm.cpu.$setDirty();
            $scope.infoForm.ram.$setDirty();
            $scope.infoForm.rom.$setDirty();
            $scope.infoForm.port.$setDirty();
            return false;
        }
        return true;
    }

    $scope.onSave = function() {
        if (!validateForm()) {
            return;
        }
        var url = "";
        if ($scope.isSave) {
            url = "app/ac/apmanage/addApModelModel";
        } else {
            url = "app/ac/apmanage/editApModelModel";
        }

        $http.post(url, {
            'id': $scope.info.id,
            'code': $scope.info.code,
            'name': $scope.info.name,
            'cpu': $scope.info.cpu,
            'ram': $scope.info.ram,
            'rom': $scope.info.rom,
            'port': $scope.info.port
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
            $http.post('app/ac/apmanage/removeApModelModel', {
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
        $scope.info.name="";
        $scope.info.code="";
        $scope.info.cpu="";
        $scope.info.ram="";
        $scope.info.rom="";
        $scope.info.port="";
    }

    $scope.onResetSearch = function() {
        $scope.datagrid.params = {};
    }

    angular.custom.onView = function(row) {
        $scope.$apply(function(){
          $scope.modalTitle = "ap型号详细信息";
          $scope.info = row;
        }); 
        $("#viewModal").modal('show');
    }
});
