

angular.module('ws.app')
.controller('securityVendorManageController',['$scope','$http',function($scope,$http){
	//初始化变量
	$scope.isSave=false;
	
	//初始化列表
	$scope.datagrid={
			url:'app/ac/securityVendor/getPaging',
			method:'post',
			params:{},
			columns:[{
				field:'name',
				title:'厂商名称'
			},{
				field:'code',
				title:'编码'
			},{
				field:'address',
				title:'厂商地址'
			},{
				field:'contactor',
				title:'厂商联系人'
			},{
                field: 'svDetail',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.onView(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>"
                }
			}],
			checkbox:{
				field:'yes'
			},
	    sizes:[10,20,50,80],
	    pageSize:3
	};
	
	//详细信息
	angular.custom.onView=function(str){
		$scope.$apply(function(){
        	$scope.detail = str;
        });
	 	$scope.modalTitle="详细信息";
        $("#detailModal").modal('show');
	}
	
	 //删除
	$scope.onDelete=function(){
			var checkeds=$scope.innerCtrl.getChecked();
			if(checkeds.length<=0){
				$scope.showMsg("必须勾选一条记录才能删除！");
				return;
			}
			var selectRowIds = new Array();
			for(var i =0;i<checkeds.length;i++){
				selectRowIds[i] = checkeds[i].id;
			}
			 $scope.showMsg("将要删除" + checkeds.length + "条记录",null,function(selectRowIds){
		            $http.post('app/ac/securityVendor/deleteVendor', {
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
	
	 $scope.onAdd=function(){
	      $scope.modalTitle = "添加安全厂商信息";
	      $scope.info = {};
	      $scope.vendorForm.$setPristine();
	      $scope.isSave = true;
	      $("#addModal").modal('show');
	    }
	 
	//添加
	    $scope.onSave=function(button){
	    	if(!validateForm()){
	   			return;
	   		}
	   		var url="";
	   		if($scope.isSave){
	   			url="app/ac/securityVendor/addVendor";
	   			$scope.mask(true,0);
	   		}else{
	   			url="app/ac/securityVendor/editVendor";
	   			$scope.mask(true,2);
	   		}
	   		$('#' + button).addClass('disabled');
	   		$http.post(url, {
	   			'id':$scope.info.id,
	   	        'name': $scope.info.name,
	   	        'code': $scope.info.code,
	   	        'address':$scope.info.address,	   	        
	   	        'contactor':$scope.info.contactor,	   	        
	   	        'contactorMail':$scope.info.contactorMail,	   	        
	   	        'contactorPhone':$scope.info.contactorPhone	   	        
	   	    })
	      	.success(function(data) {
	      		$('#' + button).removeClass('disabled');
	      		$scope.mask(false);
		        if (data.success){
	          		$scope.innerCtrl.load();
	          		$scope.showMsg(data.message,true);
	                $("#addModal").modal('hide');
	            } else
	        	  	$scope.showMsg(data.message);
	        }).error(function(){
	        	$scope.mask(false);
	        });
	   	}
	    //编辑
	    $scope.onEdit = function() {
	        var checkeds=$scope.innerCtrl.getChecked();
	    	if(checkeds.length==1){
				$scope.info={
					name:checkeds[0].name,
					code:checkeds[0].code,
					address:checkeds[0].address,
					contactor:checkeds[0].contactor,
					contactorPhone:checkeds[0].contactorPhone,
					contactorMail:checkeds[0].contactorMail,
					id:checkeds[0].id,
				};
				 $scope.modalTitle = "修改安全厂商信息";
				$("#addModal").modal('show');
			}else if(checkeds.length>1){
				$scope.showMsg("只能选择一条记录进行编辑！");
			}else{
	    		$scope.showMsg("必须选择一条记录进行编辑！");
				return;
			}
		        $scope.isSave = false;
		        $scope.vendorForm.$setPristine();
	    }
	    
	    $scope.search=function(){
	    	$scope.innerCtrl.load($scope.datagrid.params);
	    }
	    //清空查询
	    $scope.resetSearch=function(){
			$scope.datagrid.params.name="";
			$scope.datagrid.params.code="";
			$scope.datagrid.params.contactor="";
		}
		//清空添加
		$scope.reset=function(){
			$scope.info.name="";
			$scope.info.code="";
			$scope.info.address="";			
			$scope.info.contactor="";			
			$scope.info.contactorPhone="";			
			$scope.info.contactorMail="";
		}

		function validateForm(){
	    	if ($scope.vendorForm.$invalid) {
	    		$scope.vendorForm.name.$setDirty();
	    		$scope.vendorForm.code.$setDirty();
	    		$scope.vendorForm.address.$setDirty();
	    		$scope.vendorForm.contactor.$setDirty();
	    		$scope.vendorForm.contactorPhone.$setDirty();
	    		$scope.vendorForm.contactorMail.$setDirty();
	    		return false;
	    	}
	    	 return true;
	    }
}]);