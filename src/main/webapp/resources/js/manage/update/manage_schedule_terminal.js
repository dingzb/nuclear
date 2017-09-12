/**
 * 模块：AC-AP管理-系统升级
 * author：Dzb
 * date：2015-7-14
 */
angular.module('ws.app')
.controller('apUpdateTerminalApCtrl', function($scope, $http, $timeout) {
    $scope.config={};
    $scope.view={};
    $scope.initPage = function() {
        //初始化分页列表
        $scope.innerCtrl = {};
        $scope.datagrid = {
            url: 'app/ac/apmanage/getUpdateSchedules',
            method: 'post',
            params: {terminalType:0},
            columns: [{
                field: 'name',
                title: '名称'
            }, /*{
                field:'type',
                title:"类型",
                formatter: function(row) {
                    var status=row['type'];
                    if(status==0){
                        return "类型1";
                    }else{
                        return "其他";
                    }
                }
            },*/{
                field: 'createtime',
                title: '创建时间'
            }, {
                field: 'status',
                title: '任务状态',
                formatter: function(row) {
                    var status = row['status'];
                    if (status == null 
                        ||typeof status == null 
                        || typeof status == undefined) {
                        return '任务初始';
                    } else if (status == 0) {
                        return '任务启动';
                    } else if (status == 1) {
                        return '任务运行';
                    } else if (status == 2) {
                        return '任务成功';
                    } else if (status == 3) {
                        return '任务失败';
                    }
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='任务详情' onClick=\"angular.custom.onShowView(" + str + ")\"><span class=\"glyphicon glyphicon-link\"> </span></button>" ;
                    //+"<button type=\"button\" class=\"btn btn-link btn-sm\" title='编辑' onClick=\"angular.custom.onEdit(" + str + ")\"><span class=\"glyphicon glyphicon-pencil\"> </span></button>" 
                    //+ "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete("+ str +")\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
                }
            }],
            checkbox: {
                field: 'yes'
            },
            sizes: [10, 20, 50, 80],
            pageSize: 3,
        };

        $scope.countTask = {};
        $http.post('app/ac/apmanage/getConfigUpdateCount', {
          'type': 0
        }).success(function(data){
          if(data.success){
              $scope.countTask.taskRun=data.data.taskRun==null?0:data.data.taskRun;
              $scope.countTask.taskFailure=data.data.taskFailure==null?0:data.data.taskFailure;
              $scope.countTask.taskSuccess=data.data.taskSuccess==null?0:data.data.taskSuccess;
              $scope.countTask.totalTask=data.data.totalTask==null?0:data.data.totalTask;
          }
        });
    }
    
    $scope.searchTerminalSchedule = function(){
    	if($scope.datagrid.params.createtimeStart > $scope.datagrid.params.createtimeEnd){
    		$scope.showMsg('开始时间不能大于结束时间','提示');
    		return false;
    	}
    	$scope.innerCtrl.load($scope.datagrid.params);
    }
    $scope.initdate = function() {
        $('#createtimeStart').datetimepicker({
            locale: 'zh_cn',
            format: 'YYYY-MM-DD HH:mm',
            showClear: true,
            showClose: true,
            showTodayButton: true
        });
        $('#createtimeEnd').datetimepicker({
            locale: 'zh_cn',
            format: 'YYYY-MM-DD HH:mm',
            showClear: true,
            showClose: true,
            showTodayButton: true
        });
        $('#createtimeStart').bind('dp.change',
            function(date, oldDate) {
              $scope.$apply(function(){
                  $scope.datagrid.params.createtimeStart = $("#createtimeStart").val();
              });
            });
        $('#createtimeEnd').bind('dp.change',
            function(date, oldDate) {
                $scope.$apply(function(){
                  $scope.datagrid.params.createtimeEnd = $("#createtimeEnd").val();
                });
            });
    };
    $scope.initAddPage = function() {
        //初始化分页列表
        $scope.innerCtrlAdd = {};
        $scope.datagridAdd = {
            url: 'app/ac/apmanage/getApWithFirmware',
            method: 'post',
            quaryOnload:true,
            params: {terminalType:0},
            columns: [{
                field: 'name',
                title: '设备名称'
            },{
                field: 'code',
                title: '设备编码'
            }
            /*{
                field: 'updateStatus',
                title: '任务状态',
                formatter: function(row) {
                    var status=row['updateStatus'];
                    if (status == null ||
                        typeof status == null || 
                        typeof status == undefined) {
                        return "无任务";
                    }else if(status==0){
                        return "未启动";
                    }else if(status==1){
                        return "任务中";
                    }else if(status==2){
                        return "任务成功";
                    }else if(status==3){
                        return "任务失败";
                    }
                }
            }*/],
            checkbox: {
                field: 'selected'
            },
            sizes: [5, 10, 15],
            pageSize: 3,
        };
        $http.post("app/ac/apmanage/getFirmwares", {
            'type':'collect'
        }).success(function(data) {
            if (data.success) {
                $scope.firmwares = data.data.rows;
            }
        });
        
        
    }

    $scope.initViewPage = function() {
        //初始化分页列表
        $scope.innerCtrlView = {};
        $scope.datagridView = {
            url: 'app/ac/apmanage/getApUpdateView',
            method: 'post',
            quaryOnload:true,
            params: {},
            columns: [{
                field: 'apName',
                title: '名称'
            },{
                field: 'attempts',
                title: '尝试次数'
            },{
                field: 'status',
                title: '状态',
                formatter:function(row){
                    var val=row['status'];
                    if(val == null
                        ||typeof val ==  null 
                        || typeof val == undefined){
                      return "任务初始";
                    }
                    if(val==1){
                      return '升级成功';
                    }else if(val==2){
                      return '安装包获取失败';
                    }else if(val==3){
                      return 'MD5值校验失败';
                    }else if(val==4){
                      return '安装包中无固件包或ipk包';
                    }else if(val==5){
                      return 'ftp地址错误';
                    }else {
                        return "未知状态("+val+")";
                    }
                }
            },{
                field: 'message',
                title: '信息'
            },{
                field: 'version',
                title: '固件版本'
            },{
                field: 'path',
                title: '固件路径'
            },{
                field: 'md5',
                title: 'md5'
            },{
                field: 'userName',
                title: 'ftp用户名'
            }],
            sizes: [5, 10, 15],
            pageSize: 3,
        };
        $http.post("app/ac/apmanage/getFirmwares", {
            'type':'collect'
        }).success(function(data) {
            if (data.success) {
                $scope.firmwares = data.data.rows;
            }
        });
    }

    $scope.onAdd = function() {
        $scope.modalTitle = "新建升级任务(终端采集)";
        $scope.config = {};
        $scope.infoForm.$setPristine();
        $scope.innerCtrlAdd.load($scope.datagridAdd.params);
        $("#addModal").modal('show');
    }

    $scope.onSave = function() {
        var checkeds = $scope.innerCtrlAdd.getChecked();
        //验证所选ap数量>1
        if (checkeds.length !=1) {
            $scope.showMsg("只能选择1个设备");
            return;
        }

        //验证 所选ap全是相同类型
        var checkApType = "";
        var selectRowIds = new Array();
        for (var i = 0; i < checkeds.length; i++) {
            if (i == 0) {
                checkApType = checkeds[i].modelId;
            } else {
                if (checkApType != checkeds[i].modelId) {
                    $scope.showMsg("禁止选择不同型号的ap进行同一任务");
                    return;
                }
            }
            // if(checkeds[i].updateStatus<=1 && 
            //     checkeds[i].updateStatus!=null){
            //     $scope.showMsg("禁止选择任务中的ap");
            //     return;
            // }
            selectRowIds[i] = checkeds[i].id;
        }

        if ($scope.infoForm.$invalid) {
             $scope.infoForm.name.$setDirty();
             $scope.infoForm.firmwareId.$setDirty();
             return;
        }

        var url="";
        $scope.mask(true,0);
        url="app/ac/apmanage/addUpdateSchedule";
        $http.post(url, {
            'id':$scope.config.id,
            'param': selectRowIds,
            'name': $scope.config.name,
            'firmwareId': $scope.config.firmwareId,
            'terminalType':0
        })
        .success(function(data,status) {
            $scope.mask(false);
            if(status==403){
            	  $scope.showMsg("没有权限操作");
            	  return;
            	}
            if (data.success) {
                $("#addModal").modal('hide');
                $scope.innerCtrl.load($scope.datagrid.params);
                $scope.showMsg(data.message,true);
            } else
                $scope.showMsg(data.message);
        })
        .error(function(){
            $scope.mask(false);
        });
    }

    $scope.onStart=function(){
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.showMsg("必须勾选一条任务才能启动！");
            return;
        }
        var selectRowIds = new Array();
        for (var i = 0; i < checkeds.length; i++) {
            selectRowIds[i] = checkeds[i].id;
            if(checkeds[i].status==0||checkeds[i].status==1){
                $scope.showMsg("【"+checkeds[i].name+"】当前任务已经启动，禁止重复启动任务！");
                return;
            }
        }
        //判断版本号是否相同并且提示用户
        $http.post('app/ac/apmanage/judgeUpdateSchedule', {
            'param': selectRowIds
        })
        .success(function(data,status) {
        	if(status==403){
        		$scope.showMsg("没有权限操作");
        		return;
        	}
            if (data.success) {
                $scope.showMsg("将要启动" + checkeds.length + "个任务",null,function(selectRowIds){
                    $http.post('app/ac/apmanage/startUpdateSchedule', {
                        'param': selectRowIds
                    })
                    .success(function(data,status) {
                    	if(status==403){
                		  $scope.showMsg("没有权限操作");
                		  return;
                		}
                        if (data.success) {
                            $scope.innerCtrl.load($scope.datagrid.params)
                            $scope.showMsg(data.message,true);
                        } else
                            $scope.showMsg(data.message);
                    }).error(function(){
                        $scope.showMsg("操作失败");
                    });
                },selectRowIds);
            } else{
                $scope.showMsg(data.message,null,$scope.startschedule,selectRowIds);
            }
        }).error(function(){
            $scope.showMsg("操作失败");
        });
    }

    $scope.startschedule = function(selectRowIds) {
        $http.post('app/ac/apmanage/startUpdateSchedule', {
            'param': selectRowIds
        })
        .success(function(data) {
            if (data.success) {
                $scope.innerCtrl.load($scope.datagrid.params)
                $scope.showMsg(data.message, true);
            } else
                $scope.showMsg(data.message);
        });
    }


    $scope.onStop = function() {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.showMsg("必须勾选一条记录才能停止！");
            return;
        }
        var selectRowIds = new Array();
        for (var i = 0; i < checkeds.length; i++) {
            console.info(checkeds[i].status)
            if (checkeds[i].status != 0&&checkeds[i].status !=null) {
                $scope.showMsg("不允许停止已经运行的任务:"+checkeds[i].name);
                return;
            }
            if (checkeds[i].status ==null) {
                $scope.showMsg("初始状态的任务不需要停止:"+checkeds[i].name);
                return;
            }
            selectRowIds[i] = checkeds[i].id;
        }

        $scope.showMsg("将要停止" + checkeds.length + "条任务", null, function(selectRowIds) {
            $http.post('app/ac/apmanage/stopUpdateSchedule', {
                    'param': selectRowIds
                })
                .success(function(data) {
                    if (data.success) {
                        $scope.innerCtrl.load($scope.datagrid.params);
                        $scope.showMsg(data.message, true);
                    } else
                        $scope.showMsg(data.message);
                });
        }, selectRowIds);
    }


    $scope.onDelete = function() {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.showMsg("必须勾选一条记录才能删除！");
            return;
        }
        //只能删除状态为非启动状态的条目
        var selectRowIds = new Array();
        for (var i = 0; i < checkeds.length; i++) {
            selectRowIds[i] = checkeds[i].id;
            if(checkeds[i].status==1){
              $scope.showMsg("只能删除状态为非启动状态的条目！");
              return;
            }
        }
        $scope.showMsg("将要删除" + checkeds.length + "条记录",null,function(selectRowIds){
            $http.post('app/ac/apmanage/removeUpdateSchedule', {
                'param': selectRowIds
            })
            .success(function(data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params)
                    $scope.showMsg(data.message,true);
                } else
                    $scope.showMsg(data.message);
            });
        },selectRowIds);
    }
    $scope.onResetSearch=function(){
      $scope.datagrid.params.name="";
      $scope.datagrid.params.createtimeStart="";
      $scope.datagrid.params.createtimeEnd="";
      $scope.datagrid.params.status="";
    }
    
    angular.custom.onDelete=function(row){
        //只能删除状态为非启动状态的条目
        var selectRowIds = new Array();
        selectRowIds.push(row['id']);
        if(row['status']==1){
          $scope.showMsg("只能删除状态为非启动状态的条目！");
          return;
        }
        $scope.showMsg("确定要删除该条记录？",null,function(selectRowIds){
            $http.post('app/ac/apmanage/removeUpdateSchedule', {
                'param': selectRowIds
            })
            .success(function(data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params)
                    $scope.showMsg(data.message,true);
                } else
                    $scope.showMsg(data.message);
            });
        },selectRowIds);
    }

    angular.custom.onShowView = function(row) {
       $("#viewModal").modal('show');
        // console.info(row);
        $scope.view.name=row['name'];
        $scope.view.firmwareId=row['firmwareId'];
        $scope.datagridView.params.updateScheduleId=row["id"];
        $scope.innerCtrlView.load($scope.datagridView.params);
    }
    
    $scope.addFilter=function (type) {
        if(type =="success"){
            $scope.datagrid.params.status=2;
        }
        if(type =="fail"){
            $scope.datagrid.params.status=3;
        }
        if(type =="run"){
            $scope.datagrid.params.status=1;
        }
        if(type =="clear"){
            $scope.datagrid.params={};
        }
        $scope.innerCtrl.load($scope.datagrid.params);
    }
});
