/**
 * 模块：管理-终端采集
 * author：Dzb
 * date：2016-3-22
 */
angular.module('ws.app')
	.controller('apTerminalCtrl', ['$rootScope', '$scope', '$http', 'Upload', function($rootScope,$scope, $http, Upload) {
		$scope.info = {};
		$scope.view = {};
		$scope.isSave = true;

		$scope.initPage = function() {
			//初始化分页列表
			$scope.innerCtrl = {};
			$scope.datagrid = {
				url: 'app/ac/apmanage/getTerminalPaging',
				method: 'post',
				params: {
					'approveStatus':"2,3"
				},
				columns: [{
					field: 'name',
					title: '设备名称'
				}, {
					field: 'placeName',
					title: '场所名称'
				}, {
					field: 'code',
					title: '设备编码'
				}, {
					field: 'modelName',
					title: '型号'
				}, {
					field: 'mac',
					title: 'MAC'
				}, {
					field: 'status',
					title: '设备状态',
					formatter: function(row) {
						var status = row['status'];
						if (status == 0) {
							return "<span style='color:#F0AD4E'>不在线</span>";
						} else if (status == 1) {
							return "<span style='color:#5CB85C'>在线</span>";
						}
					}
				}, {
					field: 'approveStr',
					title: '审核状态',
				}, {
					field: 'id',
					title: '操作',
					formatter: function(row) {
						var str = JSON.stringify(row);
						str = str.replace(/"/g, "'");
						return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.onView(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>";
					}
				}],
				checkbox: {
					field: 'yes'
				},
				sizes: [10, 20, 50, 80],
				pageSize: 3,
			};
		}

		$scope.search=function(stat){
			//待审核
			if(stat==1){
				$scope.isSubPress=false;
				if($scope.isApprovePress){
					$scope.isApprovePress=false;
					$scope.datagrid.params.approveStatus="2,3";
				}else{
					$scope.isApprovePress=true;
					$scope.datagrid.params.approveStatus="1";
				}
			}
			//未审核
			if(stat==0){
				$scope.isApprovePress=false;
				if($scope.isSubPress){
					$scope.isSubPress=false;
					$scope.datagrid.params.approveStatus="2,3";
				}else{
					$scope.isSubPress=true;
					$scope.datagrid.params.approveStatus="0,1";
				}
			}
			$scope.innerCtrl.load($scope.datagrid.params);
		}

		function initSelect() {
			$http.get("app/ac/getPlaceSelectItem")
				.success(function(data) {
					if (data.success) {
						$scope.placeItems = data.data;
					}
				});
			$http.get("app/ac/getApModelSelectItem")
				.success(function(data) {
					if (data.success) {
						$scope.apModelItems = data.data;
					}
				});
			$http.get("app/ac/apmanage/getAcClusterPaging")
				.success(function(data) {
					if (data.success) {
						$scope.acClustersItems = data.data.rows;
					}
				});
			$http.get("app/ac/securityVendor/getPaging")
				.success(function(data) {
					if (data.success) {
						$scope.organizations = data.data.rows;
					}
				});
			$http.get("app/ac/dataCenter/getDataCenterPaging")
				.success(function(data) {
					if (data.success) {
						$scope.dataCenterItems = data.data.rows;
					}
				});
		}

		$scope.initValidity = function() {
			$('#validity').datetimepicker({
				locale: 'zh_cn',
				format: 'YYYY-MM-DD',
				showClear: false,
				showClose: true,
				showTodayButton: true,
				minDate: new Date
			});
			$('#validity').bind('dp.change',
				function(date, oldDate) {
					$scope.$apply(function() {
						$scope.info.validity = $("#validity").val();
					});
				});
		};

		$scope.onAdd = function() {
			$scope.approvePer=true;
			$scope.modalTitle = "添加终端采集设备";
			$scope.info = {};
			$scope.infoForm.$setPristine();
			$scope.isSave = true;
			var myDate = new Date();
			var y = myDate.getFullYear() + 10; //获取完整的年份(4位,1970-????)
			var m = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
			var d = myDate.getDate(); //获取当前日(1-31)
			$scope.info.validity = y + "-" + m + "-" + d;
			$("#addModal").modal('show');
			initSelect();
		}

		$scope.getCode = function() {
			var tmp = $scope.info.securityOrganizationId + $scope.info.mac;
			if (typeof tmp != "string") return;
			tmp = tmp.replace(/undefined/g, "");
			tmp = tmp.replace(/-/g, "");
			$scope.info.code = tmp;
		}

		function validateForm() {
			// 先判断基本信息表单状态
			if ($scope.infoForm.$invalid) {
				$scope.infoForm.name.$setDirty();
				$scope.infoForm.placeId.$setDirty();
				$scope.infoForm.modelId.$setDirty();
				$scope.infoForm.mac.$setDirty();
				$scope.infoForm.secOrga.$setDirty();
				$scope.infoForm.code.$setDirty();
				$scope.infoForm.acClusterId.$setDirty();
				$scope.infoForm.position.$setDirty();
				$scope.infoForm.validity.$setDirty();
				$scope.infoForm.type.$setDirty();
				$scope.infoForm.plateNumber.$setDirty();
				$scope.infoForm.subwayLine.$setDirty();
				$scope.infoForm.subwayVehicle.$setDirty();
				$scope.infoForm.subwayCarriage.$setDirty();
				$scope.infoForm.longitude.$setDirty();
				$scope.infoForm.latitude.$setDirty();
				$scope.infoForm.uploadTimeInterval.$setDirty();
				$scope.infoForm.collectionRadius.$setDirty();
				$scope.infoForm.dataCenterId.$setDirty();
				$scope.infoForm.baseInfoUploadInterval.$setDirty();
				return false;
			}

			return true;
		}

		$scope.onSave = function(isTmp) {
			if (!validateForm()) {
				return;
			}
			//保存跳到未审核页面
			if (isTmp) {
				$scope.info.approve = 0;
			} else {
				$scope.info.approve = 1;
			}
			$scope.isApprovePress=false;
			$scope.isSubPress=true;
			$scope.datagrid.params.approveStatus="0,1";

			var url = "";
			if ($scope.isSave) {
				url = "app/ac/apmanage/addTerminal";
				$scope.mask(true, 0);
			} else {
				url = "app/ac/apmanage/editTerminal";
				$scope.mask(true, 2);
			}

			$http.post(url, {
					'id': $scope.info.id,
					'name': $scope.info.name,
					'placeId': $scope.info.placeId,
					'modelId': $scope.info.modelId,
					'mac': $scope.info.mac,
					'securityOrganizationId': $scope.info.securityOrganizationId,
					'code': $scope.info.code,
					'acClusterId': $scope.info.acClusterId,
					'position': $scope.info.position,
					'validity': $scope.info.validity,
					'type': $scope.info.type,
					'plateNumber': $scope.info.plateNumber,
					'subwayLine': $scope.info.subwayLine,
					'subwayVehicle': $scope.info.subwayVehicle,
					'subwayCarriage': $scope.info.subwayCarriage,
					'longitude': $scope.info.longitude,
					'latitude': $scope.info.latitude,
					'uploadTimeInterval': $scope.info.uploadTimeInterval,
					'dataCenterId': $scope.info.dataCenterId,
					'collectionRadius': $scope.info.collectionRadius,
					'baseInfoUploadInterval': $scope.info.baseInfoUploadInterval,
					'approve': $scope.info.approve //审核状态
				})
				.success(function(data) {
					$scope.mask(false);
					if (data.success) {
						$scope.innerCtrl.load($scope.datagrid.params);
						$scope.showMsg(data.message, true);
						$("#addModal").modal('hide');
					} else {
						if (data.message != "") {
							$scope.showMsg(data.message);
						}
					}
				})
				.error(function() {
					$scope.mask(false);
				});
		}

		$scope.onDelete = function() {
			var checkeds = $scope.innerCtrl.getChecked();
			if (checkeds.length <= 0) {
				$scope.showMsg("必须勾选一条记录才能删除！");
				return;
			}
			var selectRowIds = new Array();
			for (var i = 0; i < checkeds.length; i++) {
				selectRowIds[i] = checkeds[i].id;
			}

			$scope.showMsg("将要删除" + checkeds.length + "条记录", null, function(selectRowIds) {
				$http.post('app/ac/apmanage/removeApModel', {
						'param': selectRowIds
					})
					.success(function(data) {
						if (data.success) {
							$scope.innerCtrl.load($scope.datagrid.params)
							$scope.showMsg(data.message, true);
						} else
							$scope.showMsg(data.message);
					});
			}, selectRowIds);
		}

		$scope.reset = function() {
			var tmp = "",
				mac = "";
			if ($scope.info.id) {
				tmp = $scope.info.id;
				mac = $scope.info.mac;
			}
			$scope.info = {};
			$scope.info.id = tmp;
			$scope.info.mac = mac;
		}

		$scope.onResetSearch = function() {
			$scope.datagrid.params.placeName = "";
			$scope.datagrid.params.name = "";
			$scope.datagrid.params.status = "";
			$scope.datagrid.params.code="";
		      $scope.datagrid.params.modelName="";
		      $scope.datagrid.params.mac="";
		}

		$scope.addFilter=function (type) {
	        $scope.datagrid.params.isNew=false;
            $scope.datagrid.params.status="";
	        if(type =="isNew"){
	            $scope.datagrid.params.isNew=true;
	        }
	        if(type =="notOnline"){
	            $scope.datagrid.params.status=0;
	        }
	        if(type =="online"){
	            $scope.datagrid.params.status=1;
	        }
	        if(type =="clear"){
	            // $scope.datagrid.params.isNew=false;
	            // $scope.datagrid.params.status="";
	            $scope.datagrid.params={};
	        }
	        $scope.innerCtrl.load($scope.datagrid.params);
	    }

		//判断是否有审核权限
		function hasApprovePermission(){
			var perms = $rootScope.permCodes;
			return perms.indexOf('app.ac.apmanage.approveTerminal')>-1;
		}

		$scope.onEdit = function() {
			var checkeds = $scope.innerCtrl.getChecked();
			if (checkeds.length <= 0 || checkeds.length > 1) {
				$scope.showMsg("必须勾选一条记录才能编辑！");
				return;
			}
			var checked=checkeds[0];

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
			
			$scope.modalTitle = "修改终端采集设备";
			$scope.isSave = false;
			$scope.info = {};
			angular.extend($scope.info, checked);
			$("#addModal").modal('show');
			initSelect();
		}

		angular.custom.onView = function(row) {
			initSelect();
			$scope.view = row;
			$("#viewModal").modal('show');
		}

		$scope.onApprove = function() {
			var checkeds = $scope.innerCtrl.getChecked();
			if (checkeds.length <= 0) {
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
			$scope.appro = {};
			$scope.approveForm.$setPristine();
			$scope.appro.status = "2";
			$('#approveModel').modal("show");
		}

		$scope.saveApprove = function() {
			if (!validApprove()) {
				return false;
			}
			var checkeds = $scope.innerCtrl.getChecked();
			var ids = '';
			for (var i = 0; i < checkeds.length; i++) {
				ids += checkeds[i].id + ',';
			}
			$http.post('app/ac/apmanage/approveTerminal', {
					'foreignIds': ids,
					'status': $scope.appro.status,
					'descr': $scope.appro.descr
				})
				.success(function(data) {
					if (data.success) {
						$('#approveModel').modal("hide");
						$scope.showMsg(data.message, true);
						$scope.innerCtrl.load($scope.datagrid.params);
					} else {
						$scope.showMsg(data.message);
					}
				});
		}

		function validApprove() {
			if ($scope.approveForm.$invalid) {
				$scope.approveForm.desc.$setDirty();
				return false;
			}
			return true;
		}


		/******终端配置-s****/
		$scope.onViewApConfig = function() {
            var aps= $scope.innerCtrl.getChecked();
            var length =aps.length;
            if(length==0){
                $scope.showMsg("查看配置需要勾选一个设备！");
            }else if(length==1){
                var row=aps[0];
                $http.post('app/ac/apmanage/getApCfgByApId', {
                        'apId': row['id']
                    })
                    .success(function(data) {
                        if (data.success) {
                            var info = data.data;
                            info.name = "";
                            info.istemplate = false;
                            $scope.config = info;
                            if ($scope.config.brip == "") {
                                $scope.config.brip = "172.31.200.1";
                            }
                        }else{
                        	$scope.config={};
                        }
                    });
                    $("#addConfigModal").modal('show');
            }else{
                $scope.showMsg("查看配置只能勾选一个设备！");
            }
      }
		
		/******终端配置-e****/

		//ap配置状态
		$scope.apStatus = function() {
			var checkeds = $scope.innerCtrl.getChecked();
			if (checkeds.length <= 0) {
				$scope.showMsg("必须勾选一条记录才能查看！");
				return;
			}
			if (checkeds.length == 1) {
				$("#apStatus").modal('show');
				highchart();
			}
		}

		//刷新ap配置状态
		$scope.restartapstatus = function() {
			highchart();
		}

		function highchart() {
			var checkeds = $scope.innerCtrl.getChecked();
			$http.post('app/ac/apmanage/getApCfgStatus', {
				apmac: checkeds[0].mac
			}).success(function(data) {
				if (data.success) {
					var newVal = data.data.cpu;
					$('#cpu').highcharts({

							chart: {
								type: 'gauge',
								plotBackgroundColor: null,
								plotBackgroundImage: null,
								plotBorderWidth: 0,
								plotShadow: false
							},
							credits: { //去除版权信息
								enabled: false
							},

							title: {
								text: 'cpu使用率'
							},

							pane: {
								startAngle: -150,
								endAngle: 150,
								background: [{
									backgroundColor: {
										linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
										stops: [
											[0, '#FFF'],
											[1, '#333']
										]
									},
									borderWidth: 0,
									outerRadius: '109%'
								}, {
									backgroundColor: {
										linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
										stops: [
											[0, '#333'],
											[1, '#FFF']
										]
									},
									borderWidth: 1,
									outerRadius: '107%'
								}, {
									// default background
								}, {
									backgroundColor: '#DDD',
									borderWidth: 0,
									outerRadius: '105%',
									innerRadius: '103%'
								}]
							},

							// the value axis
							yAxis: {
								min: 0,
								max: 100,

								minorTickInterval: 'auto',
								minorTickWidth: 1,
								minorTickLength: 10,
								minorTickPosition: 'inside',
								minorTickColor: '#666',

								tickPixelInterval: 30,
								tickWidth: 2,
								tickPosition: 'inside',
								tickLength: 10,
								tickColor: '#666',
								labels: {
									step: 2,
									rotation: 'auto'
								},
								title: {
									text: '%'
								},
								plotBands: [{
									from: 0,
									to: 60,
									color: '#55BF3B' // green
								}, {
									from: 60,
									to: 85,
									color: '#DDDF0D' // yellow
								}, {
									from: 85,
									to: 100,
									color: '#DF5353' // red
								}]
							},
							series: [{
								name: 'cpu使用率',
								data: [newVal],
								tooltip: {
									valueSuffix: ' %'
								}
							}]

						}
						// Add some life
					);
					//内存使用率
					$('#memory').highcharts({

							chart: {
								type: 'gauge',
								plotBackgroundColor: null,
								plotBackgroundImage: null,
								plotBorderWidth: 0,
								plotShadow: false
							},
							credits: { //去除版权信息
								enabled: false
							},
							title: {
								text: '内存使用率'
							},

							pane: {
								startAngle: -150,
								endAngle: 150,
								background: [{
									backgroundColor: {
										linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
										stops: [
											[0, '#FFF'],
											[1, '#333']
										]
									},
									borderWidth: 0,
									outerRadius: '109%'
								}, {
									backgroundColor: {
										linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
										stops: [
											[0, '#333'],
											[1, '#FFF']
										]
									},
									borderWidth: 1,
									outerRadius: '107%'
								}, {
									// default background
								}, {
									backgroundColor: '#DDD',
									borderWidth: 0,
									outerRadius: '105%',
									innerRadius: '103%'
								}]
							},

							// the value axis
							yAxis: {
								min: 0,
								max: 100,

								minorTickInterval: 'auto',
								minorTickWidth: 1,
								minorTickLength: 10,
								minorTickPosition: 'inside',
								minorTickColor: '#666',

								tickPixelInterval: 30,
								tickWidth: 2,
								tickPosition: 'inside',
								tickLength: 10,
								tickColor: '#666',
								labels: {
									step: 2,
									rotation: 'auto'
								},
								title: {
									text: '%'
								},
								plotBands: [{
									from: 0,
									to: 60,
									color: '#55BF3B' // green
								}, {
									from: 60,
									to: 85,
									color: '#DDDF0D' // yellow
								}, {
									from: 85,
									to: 100,
									color: '#DF5353' // red
								}]
							},
							series: [{
								name: '内存使用率',
								data: [data.data.memory],
								tooltip: {
									valueSuffix: ' %'
								}
							}]

						}
						// Add some life
					);
					//上行下行速率
					var gaugeOptions = {

						chart: {
							type: 'solidgauge'
						},
						credits: { //去除版权信息
							enabled: false
						},

						title: null,

						pane: {
							center: ['50%', '85%'],
							size: '140%',
							startAngle: -90,
							endAngle: 90,
							background: {
								backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
								innerRadius: '60%',
								outerRadius: '100%',
								shape: 'arc'
							}
						},

						tooltip: {
							enabled: false
						},

						// the value axis
						yAxis: {
							stops: [
								[0.1, '#55BF3B'], // green
								[0.5, '#DDDF0D'], // yellow
								[0.9, '#DF5353'] // red
							],
							lineWidth: 0,
							minorTickInterval: null,
							tickPixelInterval: 400,
							tickWidth: 0,
							title: {
								y: -70
							},
							labels: {
								y: 16
							}
						},

						plotOptions: {
							solidgauge: {
								dataLabels: {
									y: 5,
									borderWidth: 0,
									useHTML: true
								}
							}
						}
					};

					// The speed gauge
					$('#container-speed').highcharts(Highcharts.merge(gaugeOptions, {
						yAxis: {
							min: 0,
							max: 2000,
							title: {
								text: '上行速率'
							}
						},

						credits: {
							enabled: false
						},

						series: [{
							name: '上行速率',
							data: [data.data.upFlow],
							dataLabels: {
								format: '<div style="text-align:center"><span style="font-size:25px;color:' +
									((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
									'<span style="font-size:12px;color:silver">kb/s</span></div>'
							},
							tooltip: {
								valueSuffix: ' km/h'
							}
						}]

					}));

					// The RPM gauge
					$('#container-rpm').highcharts(Highcharts.merge(gaugeOptions, {
						yAxis: {
							min: 0,
							max: 5000,
							title: {
								text: '下行速率'
							}
						},

						series: [{
							name: '下行速率',
							data: [data.data.downFlow],
							dataLabels: {
								format: '<div style="text-align:center"><span style="font-size:25px;color:' +
									((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
									'<span style="font-size:12px;color:silver">kb/s</span></div>'
							},
							tooltip: {
								valueSuffix: ' revolutions/min'
							}
						}]

					}));
				}else{
					$scope.showMsg(data.message);
				}
			})
		}

		$scope.initTips = function() {
			$http.post('app/ac/apmanage/getApTips',{
	            isTerminal:true
	        }).success(function(data) {
				if (data.success) {
					$scope.tip = data.data;
				} else {
					$scope.tip = {
						newAdd: 0,
						inLine: 0,
						outLine: 0,
						apSum: 0
					}
				}
			});
		}

	}]);
