angular.module('ws.app')
.controller('dataCenterController',['$scope','$http',function($scope,$http){
	//初始化变量
	$scope.isSave=false;
	
	//初始化列表
	$scope.datagrid={
			url:'app/ac/dataCenter/getDataCenterPaging',
			method:'post',
			params:{},
			columns:[{
				field:'dataCenterName',
				title:'名称'
			},{
				field:'dataCenterIp',
				title:'地址'
			},{
                field: 'conDetail',
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
		            $http.post('app/ac/dataCenter/deletesDataCenter', {
		                'param': selectRowIds
		            })
		            .success(function(data) {
		                if (data.success) {
		                	$scope.innerCtrl.load($scope.datagrid.params);
		                    $scope.showMsg(data.message,true);
		                } else{
		                	$scope.showMsg(data.message);
		                	$scope.innerCtrl.load($scope.datagrid.params);
		                }
		            });
		        },selectRowIds);
	}
	
	 $scope.onAdd=function(){
	      $scope.modalTitle = "添加数据中心信息";
	      $scope.info = {};
	      $scope.reset();
	      $scope.dataCenterForm.$setPristine();
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
	   			url="app/ac/dataCenter/addDataCenter";
	   			$scope.mask(true,0);
	   		}else{
	   			url="app/ac/dataCenter/editDataCenter";
	   			$scope.mask(true,2);
	   		}
	   		$('#' + button).addClass('disabled');
	   		$http.post(url, {
	   			'id':$scope.info.id,
	   	        'dataCenterName': $scope.info.dataCenterName,
	   	        'dataCenterIp': $scope.info.dataCenterIp
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
					dataCenterName:checkeds[0].dataCenterName,
					dataCenterIp:checkeds[0].dataCenterIp,
					id:checkeds[0].id,
				};
				$scope.modalTitle = "修改数据中心信息";
				$("#addModal").modal('show');
			}else if(checkeds.length>1){
				$scope.showMsg("只能选择一条记录进行编辑！");
			}else{
	    		$scope.showMsg("必须选择一条记录进行编辑！");
				return;
			}
		     $scope.isSave = false;
		     $scope.dataCenterForm.$setPristine();
	    }
	    
	    $scope.search=function(){
	    	$scope.innerCtrl.load($scope.datagrid.params);
	    }
	    //清空查询
	    $scope.resetSearch=function(){
			$scope.datagrid.params.dataCenterName="";
			$scope.datagrid.params.dataCenterIp="";
		}
		//清空添加
		$scope.reset=function(){
			$scope.info.dataCenterName="";
			$scope.info.dataCenterIp="";
		}

		function validateForm(){
	    	if ($scope.dataCenterForm.$invalid) {
	    		$scope.dataCenterForm.dataCenterName.$setDirty();
	    		$scope.dataCenterForm.dataCenterIp.$setDirty();
	    		return false;
	    	}
	    	 return true;
	    }
}]);
