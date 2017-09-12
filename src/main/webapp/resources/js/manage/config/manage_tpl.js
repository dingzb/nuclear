/**
 * 模块：AC-AP管理-配置模版管理
 * author：Dzb
 * date：2015-7-13
 */
angular.module('ws.app')
.controller('apConfTplManageCtrl', function($scope, $http, $timeout) {
    
    $scope.config={};
    $scope.isSave=true;

    $scope.initPage = function() {
      //初始化分页列表
      $scope.innerScope = {};
      $scope.datagrid = {
          url: 'app/ac/apmanage/getApCfgs',
          method: 'post',
          params: {},
          columns: [{
              field: 'name',
              title: '模版名称'
          },{
              field: 'apType',
              title: '模版类型',
              formatter: function(row) {
                var type=row['apType'];
                  if(type=='TERMINAL'){
                    return "采集设备";
                  }else if(type=='ACCESS'){
                    return "接入设备";
                  }
              }
          },  {
              field: 'userName',
              title: '模版创建用户'
          }, {
              field: 'createtime',
              title: '创建时间'
          }, {
              field: 'modifytime',
              title: '修改时间'
          }, {
              field: 'id',
              title: '操作',
              formatter: function(row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.onView(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>" ;
                //+ "<button type=\"button\" class=\"btn btn-link btn-sm\" title='编辑' onClick=\"angular.custom.onEdit(" + str + ")\"><span class=\"glyphicon glyphicon-pencil\"> </span></button>" 
                // + "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete('" + row['id'] + "')\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
              }
          }],
          checkbox: {
              field: 'yes'
          },
          sizes: [10, 20, 50, 80],
          pageSize: 3,
      };
    }
    $scope.searchTpl = function(){
    	if($scope.datagrid.params.createtimeStart > $scope.datagrid.params.createtimeEnd){
    		$scope.showMsg('开始时间不能大于结束时间','提示');
    		return false;
    	}
    	$scope.innerCtrl.load($scope.datagrid.params);
    }
    $scope.initdate = function() {
        $('#createtimeStart').datetimepicker({
            /*默认格式化格式，与其他不同 YYYY-MM-DD hh:mm*/
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
    }

    $scope.changeEncry=function(){
      if($scope.config.encryption=='none'||$scope.config.encryption==''){
        $scope.config.passwd="";
      }
    }

    $scope.onAdd = function() {
      $scope.modalTitle="新建模板";
      $scope.config={
        checked:true,
        encryption:"none",
        channel:0//默认为自动
      };
      $scope.infoForm.$setPristine();
      $scope.isSave=true;
      $("#addModal").modal('show');
    }

    $scope.onEdit = function() {
        $scope.modalTitle="修改模板";
        $scope.isSave=false;
        var checkeds=$scope.innerCtrl.getChecked();
        if(checkeds.length==1){
          // $scope.config=checkeds[0];
          $scope.config = {};
          angular.extend($scope.config,checkeds[0]);
          if(checkeds[0].apType=='TERMINAL'){
            $scope.config.checked=false;
          }else if(checkeds[0].apType=='ACCESS'){
            $scope.config.checked=true;
          }

          $scope.infoForm.$setPristine();
          $("#addModal").modal('show');
        }else{
          $scope.showMsg("只能选择一个进行编辑");
        }
    }

    $scope.validateIp = function() {
        if (!$scope.config.hasSsid) {
            $scope.invalidateIp = false;
            return true;
        }
        //验证网段
        var tmpBrip = $scope.config.brip;
        var tmpBrip2 = $scope.config.tBrip;
        var IP_REGEXP = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-4])))$/;
        if(tmpBrip==""){
          return true;
        }
        if (IP_REGEXP.test(tmpBrip2) && IP_REGEXP.test(tmpBrip)) {
            var sourceIp = tmpBrip;
            var target = tmpBrip2;
            sourceIp = sourceIp.substring(0, sourceIp.lastIndexOf("."));
            target = target.substring(0, target.lastIndexOf("."));
            if (target != '' && target != sourceIp) {
                $scope.invalidateIp = false;
                return true;
            } else {
                $scope.invalidateIp = true;
                return false;
            }
        } else {
            return false;
        }
    }

    $scope.validateSsid = function() {
        if (!$scope.config.hasSsid) {
            $scope.invalidateSsid = false;
            return true;
        }
        // 验证ssid
        var tmpSsid = $scope.config.ssid;
        var tmpSsid2 = $scope.config.tSsid;
        if (tmpSsid2 != "" && tmpSsid == tmpSsid2) {
            $scope.invalidateSsid = true;
            return false;
        } else {
            $scope.invalidateSsid = false;
            return true;
        }
    }

    function validateForm(){
        if ($scope.infoForm.$invalid) {
            $scope.infoForm.name.$setDirty();
            $scope.infoForm.ssid.$setDirty();
            $scope.infoForm.encryption.$setDirty();
            if($scope.config.encryption!="none"){
                $scope.infoForm.passwd.$setDirty();
            }
            $scope.infoForm.channel.$setDirty();
            $scope.infoForm.maxassoc.$setDirty();
            $scope.infoForm.brip.$setDirty();
            return false;
        }

        if($scope.config.hasSsid && $scope.config.checked){
            if (!$scope.validateSsid())
                return false;
            if (!$scope.validateIp())
                return false;
            if ($scope.tConfigForm.$invalid) {
                $scope.tConfigForm.tSsid.$setDirty();
                $scope.tConfigForm.tBrip.$setDirty();
                $scope.tConfigForm.tEncryption.$setDirty();
                $scope.tConfigForm.tPasswd.$setDirty();
                return false;
            }
        }
        return true;
    }


    $scope.onSave=function(){
      if(!validateForm()){
        return;
      }

      var url="";
      if($scope.isSave){
        $scope.mask(true,0);
        url="app/ac/apmanage/addApCfgTpl";
      }else{
        $scope.mask(true,2);
        url="app/ac/apmanage/editApCfgTpl";
      }
      $http.post(url, {
        'id':$scope.config.id,
        'ssid':$scope.config.ssid,
        'encryption':$scope.config.encryption,
        'passwd':$scope.config.passwd,
        'channel':$scope.config.channel,
        'maxassoc':$scope.config.maxassoc,
        'name':$scope.config.name,
        'brip':$scope.config.brip,
        'hasSsid':$scope.config.hasSsid,
        'tSsid':$scope.config.tSsid,
        'tBrip':$scope.config.tBrip,
        'tEncryption':$scope.config.tEncryption,
        'tPasswd':$scope.config.tPasswd,
        'apType':$scope.config.checked ? "ACCESS" : "TERMINAL"
      })
      .success(function(data,status) {
    	  if(status==403){
    		  $scope.mask(false);
    		  $scope.showMsg("没有权限操作");
    		  return;
    	  }
          $scope.mask(false);
          if (data.success){
              $("#addModal").modal('hide');
              $scope.showMsg(data.message,true);              
              $scope.innerCtrl.load();
          } else
              $scope.showMsg(data.message);              
      })
      .error(function(){
          $scope.mask(false);
          $scope.showMsg("操作失败");
      });
    }

    $scope.onDelete = function() {
      var checkeds=$scope.innerCtrl.getChecked();
        if(checkeds.length<=0){
            $scope.showMsg("必须勾选一条记录才能删除！");
            return;
        }
        var selectRowIds = new Array();
        for(var i =0;i<checkeds.length;i++){
            selectRowIds[i] = checkeds[i].id;
        }

        $scope.showMsg("将要删除"+checkeds.length+"条记录",null,function(selectRowIds){
            $http.post('app/ac/apmanage/removeApCfgTpl', {
                'param': selectRowIds
            })
            .success(function(data,status) {
            	if(status==403){
        		  $scope.showMsg("没有权限操作");
        		  return;
        		}
                if (data.success){
                    $scope.innerCtrl.load($scope.datagrid.params);
                    $scope.showMsg(data.message,true);
                }else
                    $scope.showMsg(data.message);
            }).error(function(){
                $scope.showMsg("操作失败");
            });
        },selectRowIds);
    }

    $scope.onReset = function() {
        var tmp="";
        if($scope.config.id){
          tmp=$scope.config.id;
        }
        $scope.config={};
        $scope.config.id = tmp;
    }

    $scope.onResetSearch=function(){
      $scope.datagrid.params.name="";
      $scope.datagrid.params.createtimeStart="";
      $scope.datagrid.params.createtimeEnd="";
      $scope.datagrid.params.apType="";
    }

    angular.custom.onEdit = function(row) {
        $scope.modalTitle="修改配置";
        $scope.isSave = false;
        $scope.infoForm.$setPristine();
        $scope.$apply(function(){
          $scope.config = row;
        });
        $("#addModal").modal('show');
    }

    angular.custom.onDelete = function(id) {
        $scope.showMsg("确认要删除？",null,function(id){
            var selectRowIds = new Array();
            selectRowIds.push(id);
            $http.post('app/ac/apmanage/removeApCfgTpl', {
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
                $scope.mask(false);
                $scope.showMsg("操作失败");
            });
        },id);
    }

    angular.custom.onView= function(row) {
        $scope.$apply(function(){
          $scope.view = row;
        });
        $("#viewModal").modal('show');
    }
});
