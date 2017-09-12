
/**
 * 模块：AC-AP管理-固件升级
 * author：Dzb
 * date：2015-7-14
 */
angular.module('ws.app')
.controller('apFirmwareManaCtrl', function($scope, $http,$timeout) {
    $scope.isSave=true;

    $scope.saveDisable=false;

    $scope.initPage = function() {
        //初始化分页列表
        $scope.innerCtrl = {};
        $scope.datagrid = {
            url: 'app/ac/apmanage/getFirmwares',
            method: 'post',
            params: {},
            sortName:'modifytime',
            sortOrder:'desc',
            columns: [{
                field: 'name',
                title: '名称'
            }, {
                field: 'type',
                title: '类型',
                formatter:function(value){
					if(value.type=='wireless'){
						return "无线接入设备";
					}else if(value.type=='collect'){
						return "终端采集设备";
					} 
					return "未知";
				}
            }, 
            {
                field: 'version',
                title: '版本'
            }, {
                field: 'path',
                title: '路径',
                maxWidth:250
            }, {
                field: 'description',
                title: '描述',
                maxWidth:220
            },{
                field: 'id',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='任务详情' onClick=\"angular.custom.onShowView(" + str + ")\"><span class=\"glyphicon glyphicon-link\"> </span></button>";
                     //+"<button type=\"button\" class=\"btn btn-link btn-sm\" title='编辑' onClick=\"angular.custom.onEdit(" + str + ")\"><span class=\"glyphicon glyphicon-pencil\"> </span></button>" 
                     //+ "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete('" + row['id'] + "')\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
                }
            }],
            checkbox: {
                field: 'yes'
            },
            sizes: [10, 20, 50, 80],
            pageSize: 3,
        };
    }

    $scope.onAdd=function(){
      $scope.modalTitle = "添加固件";
      $scope.info = {};
      $scope.infoForm.$setPristine();
      $scope.isSave = true;
      $("#addModal").modal('show');
    }

    $scope.onEdit = function() {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length == 1) {
            $scope.mask(true,2);
            //先判断当前固件是否在任务中
            $http.post("app/ac/apmanage/validateEditFirm", {
                'id': checkeds[0].id
            })
            .success(function(data, status) {
                $scope.mask(false);
                if(data.success){
                    $scope.modalTitle = "编辑固件";
                    $scope.infoForm.$setPristine();
                    $scope.isSave = false;
                    $scope.info = {};
                    angular.extend($scope.info,checkeds[0]);
                    $("#addModal").modal('show');
                }else{
                    $scope.showMsg(data.message);
                }
            })
            .error(function(data, status, headers, config) {
                $scope.mask(false);
            });

        } else {
            $scope.showMsg("只能选择一个进行编辑");
        }
    }

    function validateForm() {
        if ($scope.infoForm.$invalid) {
            $scope.infoForm.name.$setDirty();
            $scope.infoForm.path.$setDirty();
            $scope.infoForm.md5.$setDirty();
            $scope.infoForm.userName.$setDirty();
            $scope.infoForm.passWord.$setDirty();
            $scope.infoForm.version.$setDirty();
            $scope.infoForm.description.$setDirty();
            $scope.infoForm.type.$setDirty();
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
            url = "app/ac/apmanage/addFirmwares";
        } else {
            url = "app/ac/apmanage/editFirmwares";
        }
        $scope.mask(true,0);
        $http.post(url, {
            'id': $scope.info.id,
            'name': $scope.info.name,
            'type': $scope.info.type,
            'path': $scope.info.path,
            'userName': $scope.info.userName,
            'passWord': $scope.info.passWord,
            'md5': $scope.info.md5,
            'version': $scope.info.version,
            'description': $scope.info.description
        })
        .success(function(data, status, headers, config) {
        	if (status == 200){
        		$scope.mask(false);
        		if (data.success) {
        			$("#addModal").modal('hide');
        			$scope.showMsg(data.message,true);
        			$scope.innerCtrl.load($scope.datagrid.params);
        		} else
        			$scope.showMsg(data.message);
        	} else {
        		$scope.mask(false);
        		$scope.showMsg("没有权限操作");
        	}
        })
        .error(function(data, status, headers, config) {
            $scope.mask(false);
        });
    }

    $scope.onDelete=function(){
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.showMsg("必须勾选一条记录才能删除！");
            return;
        }

        var selectRowIds = new Array();
        for (var i = 0; i < checkeds.length; i++) {
            selectRowIds[i] = checkeds[i].id;
        }
        $scope.showMsg("将要删除" + checkeds.length + "条记录",null,function(selectRowIds){
            $http.post('app/ac/apmanage/removeFirmwares', {
                'param': selectRowIds
            })
            .success(function(data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params);
                    $scope.showMsg(data.message,true);
                } else
                $scope.showMsg(data.message);
            });
        },selectRowIds);
    }

    $scope.onResetSearch=function(){
      $scope.datagrid.params.name="";
      $scope.datagrid.params.type="";
      $scope.datagrid.params.version="";
    }
    
    $scope.reset=function(){
        var tmp="";
        if($scope.info.id){
          tmp=$scope.info.id;
        }
        $scope.info ={};
        $scope.info.id = tmp;
    }


    angular.custom.onShowView = function(row) {
        $scope.$apply(function(){
            $scope.modalTitle = "固件详情";
            $scope.info = row;
            $scope.infoForm.$setPristine();
            $("#viewModal").modal('show');
        });
    }

    angular.custom.onEdit = function(row) {
        $scope.modalTitle="编辑固件";
        $scope.isSave = false;
        $scope.isView = false;
        $scope.infoForm.$setPristine();
        $scope.$apply(function(){
          $scope.info = row;
        });
        $("#addModal").modal('show');
    }

    angular.custom.onDelete = function(id) {
        $scope.showMsg("确认要删除？",null,function(id){
            var selectRowIds = new Array();
            selectRowIds.push(id);

            $http.post('app/ac/apmanage/removeFirmwares', {
                'param': selectRowIds
            })
            .success(function(data) {
                if (data.success) {
                  $scope.innerCtrl.load($scope.datagrid.params)
                  $scope.showMsg(data.message,true);
                } else
                  $scope.showMsg(data.message);
            });
        },id);
    }

});
