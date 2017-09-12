angular.module('ws.app')
.controller('contactsManageController',['$scope','$http',function($scope,$http){
	//初始化变量
	$scope.isSave=false;
	
	//初始化列表
	$scope.datagrid={
			url:'app/ac/contacts/getPaging',
			method:'post',
			params:{},
			columns:[{
				field:'name',
				title:'姓名'
			},{
				field:'idCard',
				title:'身份证号'
			},{
				field:'phone',
				title:'手机'
			},{
				field:'email',
				title:'邮箱'
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
		            $http.post('app/ac/contacts/deleteContacts', {
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
	      $scope.modalTitle = "添加联系人信息";
	      $scope.info = {};
	      $scope.contactsForm.$setPristine();
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
	   			url="app/ac/contacts/addContacts";
	   			$scope.mask(true,0);
	   		}else{
	   			url="app/ac/contacts/editContacts";
	   			$scope.mask(true,2);
	   		}
	   		$('#' + button).addClass('disabled');
	   		$http.post(url, {
	   			'id':$scope.info.id,
	   	        'name': $scope.info.name,
	   	        'idCard': $scope.info.idCard,
	   	        'phone':$scope.info.phone,	   	        
	   	        'email':$scope.info.email	   	        
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
					idCard:checkeds[0].idCard,
					phone:checkeds[0].phone,
					email:checkeds[0].email,
					id:checkeds[0].id,
				};
				$scope.modalTitle = "修改联系人信息";
				$("#addModal").modal('show');
			}else if(checkeds.length>1){
				$scope.showMsg("只能选择一条记录进行编辑！");
			}else{
	    		$scope.showMsg("必须选择一条记录进行编辑！");
				return;
			}
		     $scope.isSave = false;
		     $scope.contactsForm.$setPristine();
	    }
	    
	    $scope.search=function(){
	    	$scope.innerCtrl.load($scope.datagrid.params);
	    }
	    //清空查询
	    $scope.resetSearch=function(){
			$scope.datagrid.params.name="";
			$scope.datagrid.params.idCard="";
			$scope.datagrid.params.phone="";
			$scope.datagrid.params.email="";
		}
		//清空添加
		$scope.reset=function(){
			$scope.info.name="";
			$scope.info.idCard="";
			$scope.info.phone="";			
			$scope.info.email="";			
		}

		function validateForm(){
	    	if ($scope.contactsForm.$invalid) {
	    		$scope.contactsForm.name.$setDirty();
	    		$scope.contactsForm.idCard.$setDirty();
	    		$scope.contactsForm.phone.$setDirty();
	    		$scope.contactsForm.email.$setDirty();
	    		return false;
	    	}
	    	 return true;
	    }
}]);