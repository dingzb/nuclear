/**
 * 场所管理
 * @author Dzb
 * @since 2015-08-07
 */
angular.module('ws.app')
.controller('acPlaceManageController',['$rootScope','$scope','$http','Upload',function($rootScope,$scope,$http,Upload){
	
	$scope.saveIndex=1;
	$scope.stat="2,3";//审核状态，默认为已通过、未通过。
	
//	 $scope.initdate = function() {
	        $('#bussinessStart').datetimepicker({
	            /*默认格式化格式，与其他不同 YYYY-MM-DD hh:mm*/
	            format: 'HH:mm',
	            showClear: true,
	            showClose: true,
	            showTodayButton: true
	        });
	        $('#bussinessEnd').datetimepicker({
	            format: 'HH:mm',
	            showClear: true,
	            showClose: true,
	            showTodayButton: true
	        });
	        $('#bussinessStart').bind('dp.change',
	            function(date, oldDate) {
	              $scope.$apply(function(){
	                  $scope.info.businessHourStart = $("#bussinessStart").val();
	              });
	            });
	        $('#bussinessEnd').bind('dp.change',
	          function(date, oldDate) {
	              $scope.$apply(function(){
	                $scope.info.businessHourEnd = $("#bussinessEnd").val();
	              });
	          });
//	    }

	//初始化变量
	$scope.isSave=false;
	$scope.placeTypeDisabled=false;
	$scope.isApprovePress=false;
	$scope.isSubPress=false;
	$scope.approvePer = true;
	
	//初始化省份
	initProvinces();
	
	//初始化服务类型
	initSelect();
	
	
	//初始化列表
	$scope.datagrid={
			url:'app/analysis/system/place/getPlacesList',
			method:'post',
			params:{},
			columns:[{
				field:'name',
				title:'场所名称'
			},{
				field:'code',
				title:'场所编码'
			},{
				field:'address',
				title:'地址'
			},{
				field:'placeTypeName',
				title:'服务类型'
			},{
				field:'contactsName',
				title:'联系人'
			},{
                field: 'apCount',
                title: 'AP数量',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" style=\"color:blue\" title='AP数量' "
                    +" onClick=\"angular.custom.showApCount('" + row ['id'] + "')\"><span style='color:#5CB85C'>" + row['inlineApCount']+"</span>/"+row['apCount'] + "</button>";//row ['id']
                }
            },{
            	field:'approveCN',
            	title:'审核状态',
            },{
                field: 'placeDetail',
                title: '操作',
                formatter: function(row) {
                    var str = JSON.stringify(row);
                    str = str.replace(/"/g, "'");
                    return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详细信息' onClick=\"angular.custom.showDetail(" + str + "," + (row['regionCode']?row['regionCode']:"''")  +")\"><span class=\"glyphicon glyphicon-link\"></span></button>";
                }
			}],
			checkbox:{
				field:'yes'
			},
	    sizes:[10,20,50,80],
	    pageSize:3
	};
	
	//场所详细信息
	 angular.custom.showDetail = function(str,regionCode) {
			var cityCode =  Math.floor(regionCode/100)*100;
			var provinceCode =  Math.floor(cityCode/1000)*1000;
		 	initProvinces();
		 	
		 	getCitys(provinceCode,cityCode);
			//getRegions(cityCode);
			
			$scope.provinceId=provinceCode;
			$scope.cityId=cityCode;
		 	initSelect();
			initIspSelect();
			initAccessSelect();
			initCertificate();
	        $scope.$apply(function(){
	        	 $scope.modalTitle="详细信息";
	        	$scope.detail = str;
	        	$scope.detail.provinceCode = provinceCode;
	        	$scope.detail.cityCode = cityCode;
	        	$scope.detail.regionCode = parseInt((regionCode+"").substr(0,6),10);
	        });
	        $("#detailModal").modal('show');
	    }
	 
	//AP数量
	 angular.custom.showApCount = function(str){
		$scope.$apply(function(){
	          $scope.modalTitle="AP信息";
	          $scope.datagridShowCount.params={};
	          $scope.datagridShowCount.params.placeId=str;
	          $scope.innerCtrlQuery.load($scope.datagridShowCount.params);
	        });
		$("#apCountlModal").modal('show');
	 } 
	 
	//初始化AP信息
	$scope.initAPCount = function() {
	 $scope.datagridShowCount = {
	            url: 'app/ac/apmanage/getRecord',
	            method: 'post',
	            quaryOnload:true,
	            params: {},
	            columns: [{
	                field: 'name',
	                title: 'AP名称'
	            },{
	                field: 'terminalType',
	                title: '类型',
	                formatter: function(row) {
	                    var type=row['terminalType'];
	                    if(type==0){
	                        return "终端采集";
	                    }else{
	                        return "无线接入";
	                    }
	                }
	            }, {
	                field: 'code',
	                title: 'AP编码'
	            }, {
	                field: 'modelName',
	                title: '型号'
	            }, {
	                field: 'mac',
	                title: 'MAC'
	            },{
	                field: 'status',
	                title: '在线状态',
	                formatter: function(row) {
	                    var status=row['status'];
	                    if(status==0){
	                        return "<span style='color:#F0AD4E'>不在线</span>";
	                    }else if(status==1){
	                        return "<span style='color:#5CB85C'>在线</span>";
	                    }
	                }
	            }, {
	                field: 'placeName',
	                title: '场所名称'
	            }],
	            sizes: [10, 20, 50, 80],
	            pageSize: 3,
	        };
	}
	 
	//初始化联系人列表
	$scope.initAddPage = function() {
        $scope.innerCtrlContacts = {};
        $scope.datagridAdd = {
            url: 'app/analysis/system/place/getContactsList',
            method: 'post',
            params: {},
            columns: [{
                field: 'name',
                title: '联系人'
            },{
            	field: 'idCard',
                title: '证件号码'
            },{
            	field: 'phone',
                title: '联系电话'
            }],
            checkbox: {
                field: 'selected'
            },
            sizes: [5, 10, 15],
            pageSize: 3,
        };
    }
	
	//初始化场所详细列表
	$scope.initShowDetailPage = function() {
        $scope.datagridShow = {
            url: 'app/analysis/system/place/getPlacesList',
            method: 'post',
            params: {},
            columns: [{
                field: 'name',
                title: '名称'
            },{
				field:'code',
				title:'场所编码'
			},{
                field: 'address',
                title: '地址'
            },{
				field:'placeTypeName',
				title:'服务类型'
			},{
				field:'contactsName',
				title:'联系人'
			},{
				field:'latitude',
				title:'纬度'
			},{
				field:'longitude',
				title:'经度'
			},{
				field:'outip',
				title:'出口IP'
			},{
                field: 'description',
                title: '描述'
            },{
                field: 'createtime',
                title: '创建时间'
            }],
            sizes: [5, 10, 15],
            pageSize: 3,
        };
    }
	
	//查询场所
	$scope.search=function(stat){
		searchPlace(stat);
	};
	function searchPlace(stat){
		var tmp=undefined;
		if ($scope.datagrid.params.regionCode) {
			tmp=$scope.datagrid.params.regionCode;
		}else if ($scope.datagrid.params.cityCode) {
			var temp2 =$scope.datagrid.params.cityCode;
			tmp =Math.floor(temp2/100)
		}else if($scope.datagrid.params.provinceCode){
			var temp1 = $scope.datagrid.params.provinceCode;
			tmp =Math.floor(temp1/10000)
		}
		if($scope.datagrid.params.placeTypeId){
			var placeTypeId = $scope.datagrid.params.placeTypeId;
		}
		//提交者点击未审核
		if(stat==0){
			if($scope.isSubPress){
				$scope.isSubPress=false;
				stat="2,3";
			}else{
				$scope.isSubPress=true;
				$scope.isApprovePress=false;
				stat="0,1";
			}
		}
		//审核者点击待审核
		if(stat==1){
			if($scope.isApprovePress){
				$scope.isApprovePress=false;
				stat="2,3";
			}else{
				$scope.isApprovePress=true;
				$scope.isSubPress=false;
			}
		}
		//正常点击搜索,需要包含审核状态条件
		if(stat==undefined || stat==''){
			if($scope.isApprovePress){
				stat="1";
			}else if($scope.isSubPress){
				stat="0,1";
			}else{
				stat="2,3";
			}
		}
		$scope.stat = stat;
		$scope.innerCtrl.load(angular.extend($scope.datagrid.params,{
			regionCode:tmp,placeTypeId:placeTypeId,approveStatus:stat
		}));
	}
	
	//添加onclick
    $scope.onAdd=function(){
    	$scope.approvePer=true;
    	$scope.modalTitle="添加场所";
    	$("#addModal").modal('show');
		$scope.info={};
		$scope.provinceId="";
		$scope.cityId="";
		$scope.isSave=true;
		$scope.saveIndex=1;
		$scope.baseForm.$setPristine();
     $scope.positionForm.$setPristine();
     $scope.placeForm.$setPristine();
     $scope.netForm.$setPristine();
     $scope.legalPersonForm.$setPristine();
     $scope.placeSafeForm.$setPristine();

     $scope.innerCtrlContacts.load();
     $('#serialnumber').attr({"disabled":false});
		initSelect();
		initIspSelect();
		initAccessSelect();
		initCertificate();
		initProvinces();
	}
	
	//添加场所
    $scope.onSave=function(button){
//    	$('#placeSaveBtn1').attr("disabled",false);
//    	$('#placeSaveBtn2').attr("disabled",false);
//    	$('#placeSaveBtn3').attr("disabled",false);
    	if(!checkPage('save')){
   			return;
   		}
    	//若saveIndex=7表示为最后提交，否则为中间暂存
    	if($scope.saveIndex==7){
    		var checked=$scope.innerCtrlContacts.getChecked();
        	if(checked.length<=0){
    		}
        	if(checked.length>1){
        		$scope.showMsg("只能选择一个联系人");
        		return;
        	}
        	if(checked.length==1){
        		$scope.datagridAdd.params.contactsId=checked[0].id;
        	}
        	$scope.info.approve=1;
        	
    	}else{
    		$scope.info.approve=0;
    		
    	}
    	$scope.isSubPress=false;
		$scope.isApprovePress=true;
    	
   		var url="";
   		if($scope.isSave){
   			url="app/analysis/system/place/addPlace";
   			$scope.mask(true,0);
   		}else{
   			url="app/analysis/system/place/editPlace";
   			$scope.mask(true,2);
   		}
   		//置 disabled
   		$('.placeSaveBtn').addClass('disabled');
   		var codeTemp = ($scope.info.code1==undefined||$scope.info.code1==null?"":$scope.info.code1)+""
   						+($scope.info.code==undefined||$scope.info.code==null?"":$scope.info.code);
   		$http.post(url, {
   			'id':$scope.info.id,
   	        'name': $scope.info.name,//场所名称
   	        'address': $scope.info.address,//场所地址
   	        'inService': $scope.info.inService,//在服
   	        'description': $scope.info.description,//备注
   	        'latitude': $scope.info.latitude,//经度
   	        'longitude': $scope.info.longitude,//纬度
   	        'outip': $scope.info.outip,//出口IP
   	        'code': codeTemp,//场所编码
   	        'placeTypeId':$scope.info.placeTypeId,//场所类型
   	        'contactsId':$scope.datagridAdd.params.contactsId,//联系人
   	        'regionCode':$scope.info.regionCode,//所属区域
   	        'serialNumber':$scope.info.serialNumber,//流水号
   			'employess':$scope.info.employess,//从业人数
	   		'machine':$scope.info.machine,//报装机器数
	   		'server':$scope.info.server,//服务器数 
	   		'organization':$scope.info.organization,//组织机构 
	   		'postal':$scope.info.postal,//邮编 
	   		'ispCode':$scope.info.ispCode,//接入服务商 
	   		'accessModelCode':$scope.info.accessModelCode,//接入方式 
	   		'legalPersonName':$scope.info.legalPersonName,//法定代表人 
	   		'legalPersonIdType':$scope.info.legalPersonIdType,//法定代表人证件类型
	   		'legalPersonIdNo':$scope.info.legalPersonIdNo,//法人证件号码 
	   		'legalPersonPhone':$scope.info.legalPersonPhone,//法人电话 
	   		'duty':$scope.info.duty,//场所负责人 
	   		'dutyPhone':$scope.info.dutyPhone,//场所负责人电话 
	   		'safety':$scope.info.safety,//信息安全员 
	   		'safetyPhone':$scope.info.safetyPhone,//安全员电话 
	   		'safetyEmail':$scope.info.safetyEmail,//安全员email 
	   		'netControl':$scope.info.netControl,//所属网监 
	   		'netPeople':$scope.info.netPeople,//网监负责人 
	   		'netPhone':$scope.info.netPhone,//网监负责人电话
	   		'businessType':$scope.info.businessType,//经营性质
	   		'businessHourEnd':$scope.info.businessHourEnd,
	   		'businessHourStart':$scope.info.businessHourStart,
	   		'businessType':$scope.info.businessType,//经营性质
	   		'approve':$scope.info.approve//审核状态

   	    })
      	.success(function(data,status) {
      		$scope.mask(false);
      		if(status==403){
      		    $scope.showMsg("没有权限操作");
      		    return;
      		}
	        if (data.success){
	        	$scope.showMsg(data.message,true);
	        	$("#addModal").modal('hide');
	        	//保存成功后应保留查询结果
	        	searchPlace(0);
            } else
        	  	$scope.showMsg(data.message);
        }).error(function(){
        	$scope.mask(false);
        	$scope.showMsg("操作失败");
        });
   	}
   	
   	$scope.saveNext=function(index){
   			$('#placeSaveBtn1').attr("disabled",false);
	    	$('#placeSaveBtn2').attr("disabled",false);
	    	$('#placeSaveBtn3').attr("disabled",false);
   		if(!checkPage('next'))return false;//页面内容校验不通过
   		if(index!=undefined&& typeof index =='number'){
   			if(index>$scope.saveIndex){
   				$scope.saveIndex++;
   				$scope.saveNext(index);
   				return;
   			}
   			$scope.saveIndex=index;
   			return;
   		}
   		$scope.saveIndex=$scope.saveIndex+1;
   	}
   	function checkPage(isNext){
   		var nextFlag = isNext=='next';
   		var saveFlag = isNext=='save';
   		if(($scope.saveIndex==1 && nextFlag)||(saveFlag && $scope.saveIndex>=1)){
   			if ($scope.baseForm.$invalid) {
   				$scope.baseForm.addressname.$setDirty();
	   			$scope.baseForm.serialnumber.$setDirty();
	   			$scope.baseForm.organization.$setDirty();
	   			$scope.baseForm.inService.$setDirty();
	   			$scope.baseForm.description.$setDirty();
	   			return false;
	   			
   			}
   		}
   		if(($scope.saveIndex==2 && nextFlag)||(saveFlag && $scope.saveIndex>=2)){
   			if ($scope.positionForm.$invalid) {
   				$scope.positionForm.code.$setDirty();
   				$scope.positionForm.latitude.$setDirty();
	    		$scope.positionForm.longitude.$setDirty();
	    		$scope.positionForm.postal.$setDirty();
	    		$scope.positionForm.regionCode.$setDirty();
	    		$scope.positionForm.address.$setDirty();
	    		$scope.positionForm.businessType.$setDirty(); 
	    		$scope.positionForm.businessHourStart.$setDirty(); 
	    		$scope.positionForm.businessHourEnd.$setDirty(); 
	    		return false;
   			}
   			if($scope.info.businessType!=0&&
   					($scope.info.placeTypeId==''|| 
   						$scope.info.placeTypeId==undefined)){
   				$scope.positionForm.placeTypeId.$setDirty();
				return false;
   			}
   		}
   		if(($scope.saveIndex==3 && nextFlag)||(saveFlag && $scope.saveIndex>=3)){
   			if ($scope.placeForm.$invalid) {
	   			$scope.placeForm.employess.$setDirty();
	    		$scope.placeForm.machine.$setDirty();
	    		$scope.placeForm.server.$setDirty();
	    		return false;
   			}
   		}
   		if(($scope.saveIndex==4 && nextFlag)||(saveFlag && $scope.saveIndex>=4)){
   			if ($scope.netForm.$invalid) {
   				$scope.netForm.ispCode.$setDirty();
	   			$scope.netForm.accessModelCode.$setDirty();
	    		$scope.netForm.outip.$setDirty();
   				return false;
   			}
   		}
   		if(($scope.saveIndex==5 && nextFlag)||(saveFlag && $scope.saveIndex>=5)){
   			if ($scope.legalPersonForm.$invalid) {
   				$scope.legalPersonForm.legalpersonname.$setDirty();
	   			$scope.legalPersonForm.certificate.$setDirty();
	   			$scope.legalPersonForm.legalpersoncardno.$setDirty();
	   			$scope.legalPersonForm.legalpersonph.$setDirty();
	   			return false;
   			}
   		}
   		if(($scope.saveIndex==6 && nextFlag)||(saveFlag && $scope.saveIndex>=6)){
   			if ($scope.placeSafeForm.$invalid) {
   				$scope.placeSafeForm.duty.$setDirty();
	   			$scope.placeSafeForm.dutyph.$setDirty();
		    		$scope.placeSafeForm.safety.$setDirty();
		    		$scope.placeSafeForm.safetyph.$setDirty();
		    		$scope.placeSafeForm.safetyem.$setDirty();
		    		$scope.placeSafeForm.netcontrol.$setDirty();
		    		$scope.placeSafeForm.netph.$setDirty();
	    			return false;
   			}
   		}
   		return true;
   	}
   	
   	$scope.savePrevious = function(){
   		$('#placeSaveBtn1').attr("disabled",false);
	    	$('#placeSaveBtn2').attr("disabled",false);
	    	$('#placeSaveBtn3').attr("disabled",false);
			var index = $scope.saveIndex;
			if(index > 1){
				$scope.saveIndex = index - 1 ;
			}
			//如是下一页有错误提示时返回上一页，清空错误提示
			if(index==2){
				$scope.positionForm.code.$setPristine(); 
   				$scope.positionForm.latitude.$setPristine(); 
	    		$scope.positionForm.longitude.$setPristine(); 
	    		$scope.positionForm.postal.$setPristine(); 
	    		$scope.positionForm.regionCode.$setPristine(); 
	    		$scope.positionForm.address.$setPristine(); 
	    		$scope.positionForm.businessType.$setPristine(); 
	    		$scope.positionForm.businessHourStart.$setPristine(); 
	    		$scope.positionForm.businessHourEnd.$setPristine(); 
			}
			if(index==3){
				$scope.placeForm.employess.$setPristine(); 
	    		$scope.placeForm.machine.$setPristine(); 
	    		$scope.placeForm.server.$setPristine(); 
			}
			if(index==4){
				$scope.netForm.ispCode.$setPristine(); 
	   			$scope.netForm.accessModelCode.$setPristine(); 
	    		$scope.netForm.outip.$setPristine(); 
			}
			if(index==5){
				$scope.legalPersonForm.legalpersonname.$setPristine(); 
	   			$scope.legalPersonForm.certificate.$setPristine(); 
	   			$scope.legalPersonForm.legalpersoncardno.$setPristine(); 
	   			$scope.legalPersonForm.legalpersonph.$setPristine(); 
			}
			if(index==6){
					$scope.placeSafeForm.duty.$setPristine(); 
		   			$scope.placeSafeForm.dutyph.$setPristine(); 
		    		$scope.placeSafeForm.safety.$setPristine(); 
		    		$scope.placeSafeForm.safetyph.$setPristine(); 
		    		$scope.placeSafeForm.safetyem.$setPristine(); 
		    		$scope.placeSafeForm.netcontrol.$setPristine(); 
		    		$scope.placeSafeForm.netph.$setPristine(); 
			}
	}

	$scope.getPlaceCode = function(param){
		var tmp = $scope.info.regionCode;
		if($scope.info.businessType==0){
			tmp+="10";
		}else if($scope.info.businessType==1){
			tmp+="2"+$scope.info.placeTypeId;
		}else if($scope.info.businessType==2){
			tmp+="3"+$scope.info.placeTypeId;
		}
		tmp = tmp.replace(/undefined/g, "");
		$scope.info.code1=tmp;
		if(param==1){
			if($scope.info.businessType==0){
				$scope.info.placeTypeId="";
				$scope.placeTypeDisabled=true;
			}else{
				$scope.placeTypeDisabled=false;
			}
		}
	}
	//判断是否有审核权限
	function hasApprovePermission(){
		var perms = $rootScope.permCodes;
		return perms.indexOf('app.analysis.system.place.approvePlace')>-1;
	}
	
    //编辑场所
    $scope.onEdit=function(attrs){
    	var checkeds=$scope.innerCtrl.getChecked();
    	$('#serialnumber').attr({"disabled":true});
    	if(checkeds.length==1){
    		var checked = checkeds[0];
    		if(checked.approve==1){
    			
    			if(!hasApprovePermission()){
    				$scope.showMsg("待审核状态的记录不允许编辑！");
        			return;
    			}else{
    				//审核者编辑时只能点击提交，不可点击暂存
    				$scope.approvePer=false;
    			}
    		}
    		if(checked.approve==2){
    			$scope.showMsg("审核通过的记录不允许编辑！");
    			return;
    		}
    		$scope.info={
    				id:checked.id,
    				serialNumber:checked.serialNumber,
    				name:checked.name,
    				latitude:checked.latitude,
    				longitude:checked.longitude,
    				placeTypeId:checked.placeTypeId,
    				businessType:checked.businessType,
    				ispCode:checked.ispCode,
    				accessModelCode:checked.accessModelCode,
    				legalPersonIdType:checked.legalPersonIdType,
    				code1:checked.code.substring(0,8),
    				code:checked.code.substring(8),
    				employess:checked.employess,
    				machine:checked.machine,
    				server:checked.server,
    				organization:checked.organization,
    				postal:checked.postal,
    				address:checked.address,
    				outip:checked.outip,
    				legalPersonName:checked.legalPersonName,
    				legalPersonIdNo:checked.legalPersonIdNo,
    				legalPersonPhone:checked.legalPersonPhone,
    				duty:checked.duty,
    				dutyPhone:checked.dutyPhone,
    				safety:checked.safety,
    				safetyPhone:checked.safetyPhone,
    				safetyEmail:checked.safetyEmail,
    				netControl:checked.netControl,
    				netPeople:checked.netPeople,
    				netPhone:checked.netPhone,
    				description:checked.description,
    				inService:checked.inService?"true":"false",
    				businessHourStart:checked.businessHourStart,
    				businessHourEnd:checked.businessHourEnd,
    			};
	    		if($scope.info.businessType==0){
					$scope.info.placeTypeId="";
					$scope.placeTypeDisabled=true;
				}else{
					$scope.placeTypeDisabled=false;
				}
			$("#addModal").modal('show');
		}else if(checkeds.length>1){
			$scope.showMsg("只能选择一条记录进行编辑！");
		}else{
    		$scope.showMsg("必须选择一条记录进行编辑！");
			return;
		}
    	$scope.modalTitle="修改场所";
		$scope.isSave=false;
    	$scope.saveIndex=1;
		$scope.baseForm.$setPristine();
        $scope.positionForm.$setPristine();
        $scope.placeForm.$setPristine();
        $scope.netForm.$setPristine();
        $scope.legalPersonForm.$setPristine();
        $scope.placeSafeForm.$setPristine();

        $scope.innerCtrlContacts.load({contactsId:checked.contactsId});

    	initSelect();
		initIspSelect();
		initAccessSelect();
		initCertificate();
		initProvinces();
		var regionCode=checkeds[0].regionCode;
		var cityCode =  Math.floor(regionCode/100)*100;
		var provinceCode =  Math.floor(cityCode/1000)*1000;
		getCitys(provinceCode,cityCode);
		//getRegions(cityCode);

		$scope.provinceId=provinceCode;
		$scope.cityId=cityCode;
		// $scope.info.provinceCode=provinceCode;
		// $scope.info.cityCode=cityCode;
		$scope.info.regionCode=regionCode;
	}
    
    //删除场所
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
		            $http.post('app/analysis/system/place/removePlace', {
		                'param': selectRowIds
		            })
		            .success(function(data) {
		                if (data.success) {
		                	$scope.innerCtrl.load($scope.datagrid.params);
		                    $scope.showMsg(data.message,true);
		                } else{
		                	$scope.innerCtrl.load($scope.datagrid.params);
		                	$scope.showMsg(data.message);
		                }
		            });
		        },selectRowIds);
	}
	
	//清空查询条件
	$scope.resetSearch=function(){
		$scope.datagrid.params.name="";
		$scope.datagrid.params.code="";
//		$scope.datagrid.params.contactsName="";
		$scope.datagrid.params.placeTypeId="";
		$scope.citysList=[];
		$scope.regionsList=[];
		// $scope.datagrid.params.placeTypeName="";
		$scope.datagrid.params.provinceCode="";
		$scope.datagrid.params.cityCode="";
		$scope.datagrid.params.regionCode="";
	}
	//清空添加条件
	$scope.reset=function(){
		if($scope.saveIndex==1){
   			$scope.info.name="";
   			$scope.info.serialNumber="";
			$scope.info.organization="";//组织机构 
			$scope.info.description="";
   		}
   		if($scope.saveIndex==2){
   			$scope.info.latitude="";
			$scope.info.longitude="";
			$scope.info.postal="";//邮编 
			$scope.info.cityCode="";
			$scope.info.provinceCode="";
			$scope.info.regionCode="";
			$scope.info.address="";
			$scope.info.code1="";//场所编码固定
			$scope.info.code="";//场所编码
			$scope.info.businessType="";//经营性质
			$scope.info.placeTypeId="";//服务类型
			$scope.info.businessHourStart="";
			$scope.info.businessHourEnd="";
   		}
   		if($scope.saveIndex==3){
			$scope.info.employess="";//从业人数
			$scope.info.machine="";//报装机器数
	   		$scope.info.server="";//服务器数 
   		}
   		if($scope.saveIndex==4){
   			$scope.info.ispCode="";//接入服务商 
	   		$scope.info.accessModelCode="";//接入方式 
			$scope.info.outip="";
   		}
   		if($scope.saveIndex==5){
   			$scope.info.legalPersonName="";//法定代表人 
	   		$scope.info.legalPersonIdType="";//法定代表人证件类型
	   		$scope.info.legalPersonIdNo="";//法人证件号码 
	   		$scope.info.legalPersonPhone="";//法人电话 
   		}
   		if($scope.saveIndex==6){
   			$scope.info.duty="";//场所负责人 
	   		$scope.info.dutyPhone="";//场所负责人电话 
	   		$scope.info.safety="";//信息安全员 
	   		$scope.info.safetyPhone="";//安全员电话 
	   		$scope.info.safetyEmail="";//安全员email 
	   		$scope.info.netControl="";//所属网监 
	   		$scope.info.netPeople="";//网监负责人 
	   		$scope.info.netPhone="";//网监负责人电话
   		}
   		if($scope.saveIndex==7){
        	$scope.datagridAdd.params.contactsId="";//联系人
   		}
	}
	
	//初始化服务类型下拉框
	function initSelect(){
		$http.get("app/analysis/system/place/getPlaceTypeSelectItem")
			.success(function(data){
					if(data.success){
						$scope.placeTypeItems=data.data;
					}
			});
	}
	
	//初始化接入服务商下拉框
	function initIspSelect(){
		$http.get("app/analysis/system/place/getISP")
			.success(function(data){
					if(data.success){
						$scope.ispList=data.data;
					}
			});
	}
	
	//初始化接入方式下拉框
	function initAccessSelect(){
		$http.get("app/analysis/system/place/getAccess")
			.success(function(data){
					if(data.success){
						$scope.accessList=data.data;
					}
			});
	}
	//初始化证件类型下拉框
	function initCertificate(){
		$http.get("app/analysis/system/place/getCertificate")
			.success(function(data){
					if(data.success){
						$scope.certificateList=data.data;
					}
			});
	}
	
	//初始化省份下拉框
	function initProvinces(){
		$http.get("app/analysis/system/place/getProvinces")
			.success(function(data){
					if(data.success){
						$scope.provinceList=data.data;
					}
			});
	}
	
	//初始化城市下拉框
	$scope.getCitys=function(val){
		$http({
			url:'app/analysis/system/place/getCitys',
			method:'post',
			data:{
				type:1,
				provinceCode:val==''?'NULL':val
			}
		}).success(function(data){
			if(data.success){
				$scope.citysList=data.data;
				$scope.regionsList=[];
				$scope.datagrid.params.cityCode="";
				$scope.datagrid.params.regionCode="";
			}
		});
	}
	
	//初始化城市下拉框
	function getCitys(val,val2){
		$http({
			url:'app/analysis/system/place/getCitys',
			method:'post',
			data:{
				type:1,
				provinceCode:val==''?'NULL':val
			}
		}).success(function(data){
			if(data.success){
				$scope.citysList=data.data;
				$scope.regionsList=[];

				$http({
					url:'app/analysis/system/place/getRegions',
					method:'post',
					data:{
						type:1,
						cityCode:val==''?'NULL':val2
					}
				}).success(function(data){
					if(data.success){
						$scope.regionsList=data.data;
						$scope.datagrid.params.regionCode="";
					}
				});
			
			}
		});
	}
	
	//初始区域下拉框
	$scope.getRegions=function(val){
		$http({
			url:'app/analysis/system/place/getRegions',
			method:'post',
			data:{
				type:1,
				cityCode:val==''?'NULL':val
			}
		}).success(function(data){
			if(data.success){
				$scope.regionsList=data.data;
				$scope.datagrid.params.regionCode="";
			}
		});
	}
	
	//初始区域下拉框
	function getRegions(val) {
		$http({
			url:'app/analysis/system/place/getRegions',
			method:'post',
			data:{
				type:1,
				cityCode:val==''?'NULL':val
			}
		}).success(function(data){
			if(data.success){
				$scope.regionsList=data.data;
			}
		});
	}
	
	//日期控件初始化
	$scope.initdate = function() {
        $('#createtime').datetimepicker({
            /*默认格式化格式，与其他不同 YYYY-MM-DD hh:mm*/
            locale: 'zh_cn',
            format: 'YYYY-MM-DD',
            showClear: true,
            showClose: true,
            showTodayButton: true
        });
        $('#createtime').bind('dp.change',
            function(date,oldDate) {
                $scope.datagrid.params.createtimeStart = 
                  $("#createtime").val().replace(/-/g, "");
            });
    };
	
	//添加场所输入校验
//    function validateForm(){
//    	checkPage('save');
//		if ($scope.baseForm.$invalid) {
////			$scope.baseForm.addressname.$setDirty();
////			$scope.baseForm.serialnumber.$setDirty();
////			$scope.baseForm.placeTypeId.$setDirty();
////			$scope.baseForm.code.$setDirty();
////			$scope.baseForm.organization.$setDirty();
////			$scope.baseForm.inService.$setDirty();
////			$scope.baseForm.description.$setDirty();
//			
//			$scope.baseForm.addressname.$setDirty();
//   			$scope.baseForm.serialnumber.$setDirty();
//   			$scope.baseForm.organization.$setDirty();
//   			$scope.baseForm.inService.$setDirty();
//   			$scope.baseForm.description.$setDirty();
//			
//			return false;
//		}
//		//若为点击第一页暂存，则不校验其他页面内容
//		if($scope.saveIndex==1){
//			return true;
//		}
//		if ($scope.positionForm.$invalid) {
////			$scope.positionForm.latitude.$setDirty();
////			$scope.positionForm.longitude.$setDirty();
////			$scope.positionForm.postal.$setDirty();
////			$scope.positionForm.regionCode.$setDirty();
////			$scope.positionForm.address.$setDirty();
//			
//			$scope.positionForm.code.$setDirty();
//			$scope.positionForm.longitude.$setDirty();
//			$scope.positionForm.latitude.$setDirty();
//    		$scope.positionForm.postal.$setDirty();
//    		$scope.positionForm.regionCode.$setDirty();
//    		$scope.positionForm.address.$setDirty();
//    		$scope.positionForm.businessType.$setDirty();
//			return false;
//		}
//		if($scope.saveIndex==2){
//			return true;
//		}
//		if ($scope.placeForm.$invalid) {
//			$scope.placeForm.employess.$setDirty();
//			$scope.placeForm.machine.$setDirty();
//			$scope.placeForm.server.$setDirty();
//			$scope.placeForm.businessType.$setDirty();
//			return false;
//		}
//		if($scope.saveIndex==3){
//			return true;
//		}
//		if ($scope.netForm.$invalid) {
//			$scope.netForm.ispCode.$setDirty();
//			$scope.netForm.accessModelCode.$setDirty();
//			$scope.netForm.outip.$setDirty();
//			return false;
//		}
//		if($scope.saveIndex==4){
//			return true;
//		}
//		if ($scope.legalPersonForm.$invalid) {
//			$scope.legalPersonForm.legalpersonname.$setDirty();
//			$scope.legalPersonForm.certificate.$setDirty();
//			$scope.legalPersonForm.legalpersoncardno.$setDirty();
//			$scope.legalPersonForm.legalpersonph.$setDirty();
//			return false;
//		}
//		if($scope.saveIndex==5){
//			return true;
//		}
//		if ($scope.placeSafeForm.$invalid) {
//			$scope.placeSafeForm.duty.$setDirty();
//			$scope.placeSafeForm.dutyph.$setDirty();
//			$scope.placeSafeForm.safety.$setDirty();
//			$scope.placeSafeForm.safetyph.$setDirty();
//			$scope.placeSafeForm.safetyem.$setDirty();
//			$scope.placeSafeForm.netcontrol.$setDirty();
//			$scope.placeSafeForm.netpeople.$setDirty();
//			$scope.placeSafeForm.netph.$setDirty();
//			return false;
//		}
//    	return true;
//    }
	
    //导出选择行
	$scope.exportSelected=function(){
		var checkeds=$scope.innerCtrl.getChecked();
		if(checkeds.length<=0){
			$scope.showMsg("必须勾选一条记录才能导出！");
			return;
		}
		var selectRowIds = new Array();
		var paramId="";
		for(var i =0;i<checkeds.length;i++){
			selectRowIds[i] = checkeds[i].id;
			paramId=paramId+checkeds[i].id+'|';
		}
	    location.href="app/place/exportSelected?paramId="+paramId;
	};
    
	//导出当前页
	$scope.exportPage=function(){
			var href = 'app/place/exportPage?size=' +$scope.datagrid.params.size
			+'&&page='+$scope.datagrid.params.page;
			if($scope.datagrid.params.name){
				href = href +'&&name='+$scope.datagrid.params.name;
			}
			if($scope.datagrid.params.contactsName){
				href = href +'&&contactsName='+$scope.datagrid.params.contactsName;
			}
			if($scope.datagrid.params.regionCode){
				href = href +'&&regionCode='+$scope.datagrid.params.regionCode;
			}
			if($scope.datagrid.params.placeTypeId){
				href = href +'&&placeTypeId='+$scope.datagrid.params.placeTypeId;
			}
			href = href +'&&approveStatus='+$scope.stat;
			location.href=encodeURI(encodeURI(href));
	}
   
	//清空导出数量
	$scope.resetExcel=function(){
		$scope.counts="";
	}
	
	//数据导出框
	$scope.exportExcel=function(){
		$("#exportExcel").modal('show');
		$scope.counts="";
		$scope.excelForm.$setPristine();
	}
	
	//导出指定条数
	$scope.excelSave=function(button){
		if(!validateExcelForm()){
   			return;
   		}
		$("#exportExcel").modal('hide');
		var href = "app/place/exportCounts?counts="+$scope.counts;
			if($scope.datagrid.params.name){
				href = href +'&&name='+$scope.datagrid.params.name;
			}
			if($scope.datagrid.params.contactsName){
				href = href +'&&contactsName='+$scope.datagrid.params.contactsName;
			}
			if($scope.datagrid.params.regionCode){
				href = href +'&&regionCode='+$scope.datagrid.params.regionCode;
			}
			if($scope.datagrid.params.placeTypeId){
				href = href +'&&placeTypeId='+$scope.datagrid.params.placeTypeId;
			}
			href = href +'&&approveStatus='+$scope.stat;
			location.href=encodeURI(encodeURI(href));
	}
	
	//导出表单验证
	function validateExcelForm(){
    	if ($scope.excelForm.$invalid) {
    		$scope.excelForm.size.$setDirty();
    		return false;
    	}
    	 return true;
    }
	
	//导入数据
	$scope.importExcel=function(){
		$scope.advertisement="";
		$scope.advPreview="";
		$("#readExcel").modal('show');
		$scope.readExcelForm.$setPristine();
	}
	
	//上传文件
	$scope.uploadExcel=function(file,prefix){
		var excelPath = document.getElementById("excelPath").value;
	    if(excelPath == null || excelPath == ''){   
	    	$scope.showMsg("请选择要上传的Excel文件！");  
	        return;   
	    }else{   
	        var fileExtend = excelPath.substring(excelPath.lastIndexOf('.')).toLowerCase();    
	        if(fileExtend == '.xlsx'||fileExtend == '.xlsm'){   
	        }else{   
	        	$scope.showMsg("文件格式需为.xlsx格式！");     
	            return;   
	        }   
	    }
	    $scope[prefix+'Completed'] = 0;
	    $scope[prefix+'Preview'] = 'progress';
	    file.upload = Upload.upload({
		      url: 'app/place/importExcel',
		      file: file,
		      data: {
			    	 
		      }
		    })
		    .progress(function (evt) {
		    	var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
		    	$scope[prefix+'Completed'] = progressPercentage;
		    })
		    .success(function (data) {
		    	if (data.success){
		    		setTimeout(function(){
		    			$("#readExcel").modal('hide');
		    			$scope.showMsg(data.message,true);
		    			$scope.isSubPress=true;
						$scope.isApprovePress=false;
						$scope.datagrid.params.approveStatus="0,1";
		    			$scope.innerCtrl.load($scope.datagrid.params);
		    		},1000);
		    	}else{
		    		$scope.showMsg(data.message);
		    	}
		    });
	}
	
	//导出模板
	$scope.excelTemplate=function(button){
		location.href="template/PlaceTemplate.xlsm";
	}


	$scope.initTips=function(){
		$http({
			url:'app/analysis/system/place/getPlaceTips',
			method:'get',
			data:{}
		}).success(function(data){
			if(data.success){
				$scope.tip=data.data;
			}else{
				$scope.tip={
					newAdd:0,
					inService:0,
					outService:0,
					placeSum:0
				}
			}
		});
	}

	$scope.addFilter=function (type) {
        if(type =="isNew"){
            $scope.datagrid.params.isNew=true;
        }
        if(type =="inService"){
            $scope.datagrid.params.inService=true;
        }
        if(type =="outService"){
            $scope.datagrid.params.inService=false;
        }
        if(type =="clear"){
            $scope.datagrid.params={};
        }
        $scope.innerCtrl.load($scope.datagrid.params);
    }
	
	 $scope.excelExport=function(){  
   	  var div=document.getElementById("excel_export");  
   	  if(div.style.display=='block') div.style.display='none';  
   	  else div.style.display='block'; 
   	}
	 $scope.approve = function(){
		 var checkeds=$scope.innerCtrl.getChecked();
		if(checkeds.length<=0){
			$scope.showMsg("至少勾选一条记录才能审核！");
			return;
		}
		//是否有已经审核过的数据
		for(var i=0;i<checkeds.length;i++){
			var status = checkeds[i].approve; 
			if(status==2||status==3){
				$scope.showMsg("已经审核的数据不可进行二次审核！");
				return;
			}
			if(status==0){
				$scope.showMsg("初始化数据不可进行审核！");
				return;
			}
		 }
		$scope.info={};
		$scope.approveForm.$setPristine();
		 $scope.info.status="2";
		 $('#approveModel').modal("show");
	 }
	 $scope.saveApprove = function(){
		 if(!validApprove()){
			 return false;
		 }
		 var checkeds=$scope.innerCtrl.getChecked();
		 var ids = '';
		 for(var i=0;i<checkeds.length;i++){
			 ids += checkeds[i].id+',';
		 }
		 $http.post('app/analysis/system/place/approvePlace', {
              'foreignIds': ids,
              'status':$scope.info.status,
              'descr':$scope.info.descr
         })
         .success(function(data) {
             if (data.success) {
             	$('#approveModel').modal("hide");
                 $scope.showMsg(data.message,true);
                 $scope.innerCtrl.load($scope.datagrid.params);
             } else{
             	$scope.showMsg(data.message);
             }
         });
		 
		 
	 }
	 function validApprove(){
		 if ($scope.approveForm.$invalid) {
				$scope.approveForm.desc.$setDirty();
				return false;
			}
		 return true;
	 }

	$scope.menus=[{
		name:'系统管理',
		code:'main.analysis',
		children:[{
			name:'用户管理',
			code:'main.analysis.system.anaUser'
		},{
			name:'场所管理',
			code:'main.analysis.system.anaPlace'
		},{
			name:'场所类型管理',
			code:'main.analysis.system.anaPlaceType'
		}]
	}]
	
}])

//配置公安分析平台下目录state
.config(['$stateProvider',function($stateProvider){
	$stateProvider
    //场所管理
    .state('main.analysis.system.anaPlace',{
    	url:'/anaPlace',
    	views:{
    		'right@main.analysis':{
    			templateUrl:'tpls/analysis/system/anaPlace.html'
    		}
    	}
    })
}])
var transFun = function(data) {
	return $.param(data);
},
postCfg = {
	headers: {
		'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
	},
	transformRequest: transFun
};
