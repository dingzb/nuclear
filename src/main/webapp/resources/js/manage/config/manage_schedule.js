/**
 * 模块：AC-AP管理-配置任务管理
 * author：Dzb
 * date：2015-7-14
 */
angular.module('ws.app')
    .controller('apConfTaskCtrl', function($scope, $http) {
        $scope.config = {};

        $scope.initPage = function() {
            //初始化分页列表
            $scope.innerCtrl = {};
            $scope.datagrid = {
                url: 'app/ac/apmanage/getConfigSchedules',
                method: 'post',
                params: { terminalType: 1 },
                columns: [{
                    field: 'name',
                    title: '名称'
                }, {
                    field: 'createtime',
                    title: "创建时间"
                }, {
                    field: 'status',
                    title: "任务状态",
                    formatter: function(row) {
                        var status = row["status"];
                        if (status == null || typeof status == null || typeof status == undefined) {
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
                        return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='任务详情' onClick=\"angular.custom.onShowview(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>";
                        // + "<button type=\"button\" class=\"btn btn-link btn-sm\" title='删除' onClick=\"angular.custom.onDelete("+ str +")\"><span class=\"glyphicon glyphicon-remove \" > </span></button>";
                    }
                }],
                checkbox: {
                    field: 'yes'
                },
                sizes: [10, 20, 50, 80],
                pageSize: 3,
            };
        }
        $scope.searchSche = function(){
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
                    $scope.$apply(function() {
                        $scope.datagrid.params.createtimeStart = $("#createtimeStart").val();
                    });
                });
            $('#createtimeEnd').bind('dp.change',
                function(date, oldDate) {
                    $scope.$apply(function() {
                        $scope.datagrid.params.createtimeEnd = $("#createtimeEnd").val();
                    });
                });
        };

        //初始化分页列表
        $scope.initAddPage = function() {
            $scope.innerCtrlAdd = {};

            $scope.datagrAddid = {
                url: 'app/ac/apmanage/config/getApsByConfigSchedule',
                method: 'post',
                quaryOnload: true,
                params: { terminalType: 1 },
                columns: [{
                    field: 'name',
                    title: '设备名称'
                }, {
                    field: 'code',
                    title: '设备编码'
                }],
                checkbox: {
                    field: 'yes'
                },
                sizes: [10, 20, 50, 80],
                pageSize: 3
            };
        }

        $scope.changeEncry=function(){
          if($scope.config.encryption=='none'||$scope.config.encryption==''){
            $scope.config.passwd="";
          }
        }

        $scope.onAdd = function() {
            $("#addModal").modal('show');
            $scope.innerCtrlAdd.load($scope.datagrAddid.params);
            initConfigSelect();
            $scope.showTab1 = true;
            $scope.showTab2 = false;
            $scope.configStep="INIT";

            $scope.useTemplate=false;
            $scope.tplId="";
            $scope.config.istemplate=false;
            $scope.config.name="";;
            $scope.config.isrestart="";
            $scope.config.ssid_status="";
        }

        $scope.onConfigNext = function() {
            var checkeds = $scope.innerCtrlAdd.getChecked();
            var length = checkeds.length;
            if (length <= 0) {
                $scope.showMsg("必须勾选一条记录才能进行下一步！");
                return;
            } else if (length == 1) {
                var node = checkeds[0];
                $scope.backFillConfig(node['id']);
            } else {
                //如果勾选多个，自动另存为模板
                $scope.config.istemplate = true;
                $scope.setDefaultConfig();
            }
            $scope.configStep="NEXT";
            $scope.showTab1 = false;
            $scope.showTab2 = true;
        }


        $scope.setDefaultConfig = function() {
            if($scope.configStep=="NEXT"){
                return;
            }
            //设置初始
            $scope.config = {};
            $scope.configForm.$setPristine();
            $scope.tConfigForm.$setPristine();
            $scope.titleForm.$setPristine();
            //默认配置
            $scope.config.encryption = "none";
            $scope.config.channel = 0;
            $scope.config.brip = "172.31.200.1";
            $scope.useTemplate = false;
        }

        //根据配置id判断 回显配置信息
        $scope.backFillConfig = function(apId) {
            if($scope.configStep=="NEXT"){
                return;
            }
            // 根据apid 和 当前状态码 判断从后台取出信息
            $http.post('app/ac/apmanage/getApCfgByApId', {
                    'apId': apId
                })
                .success(function(data) {
                    if (data.success) {
                        var info = data.data;
                        //清空另存名称，使用模板
                        info.name = "";
                        info.istemplate = false;
                        $scope.config = info;
                        if ($scope.config.brip == "") {
                            $scope.config.brip = "172.31.200.1";
                        }
                    } else {
                        $scope.setDefaultConfig();
                    }
                });
        }


        $scope.onConfigPre = function() {
            $scope.showTab1 = true;
            $scope.showTab2 = false;
        }

        // 选择模板后 -s
        $scope.onSelectTpl = function() {
            var tplId = $scope.tplId;
            if (tplId == "" || tplId == null) {
                $scope.config = {};
                return;
            }

            $http.post('app/ac/apmanage/getApCfgs', {
                    'id': tplId
                })
                .success(function(data) {
                    if (data.success) {
                        var info = data.data.rows[0];
                        info.name = "";
                        info.istemplate = false;
                        var tmpName = $scope.config.scheduleName;
                        $scope.config = info;
                        $scope.config.scheduleName = tmpName;
                    }
                });
        }

        // 验证 配置表单是否通过 -s
        function validateConfigForm() {
            if ($scope.configForm.$invalid) {
                $scope.configForm.ssid.$setDirty();
                $scope.configForm.encryption.$setDirty();
                if ($scope.config.encryption != "none") {
                    $scope.configForm.passwd.$setDirty();
                }
                $scope.configForm.channel.$setDirty();
                $scope.configForm.maxassoc.$setDirty();
                $scope.configForm.scheduleName.$setDirty();
                $scope.configForm.brip.$setDirty();
                return false;
            }
            if ($scope.config.istemplate) {
                if ($scope.titleForm.$invalid) {
                    $scope.titleForm.name.$setDirty();
                    return false;
                }
            }
            if ($scope.config.hasSsid) {
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

        $scope.onSaveConfig = function() {
            if (!validateConfigForm()) {
                return;
            }
            //组装apid
            var selectRowIds = new Array();
            var checkeds = $scope.innerCtrlAdd.getChecked();
            for (var i = 0; i < checkeds.length; i++) {
                selectRowIds[i] = checkeds[i].id;
            }
            $scope.mask(true, 0);

            // console.info({'param': selectRowIds, 'ssid': $scope.config.ssid, 'encryption': $scope.config.encryption, 'passwd': $scope.config.passwd, 'channel': $scope.config.channel, 'maxassoc': $scope.config.maxassoc, 'istemplate': $scope.config.istemplate, 'name': $scope.config.name, 'scheduleName': $scope.config.scheduleName, 'brip': $scope.config.brip, 'hasSsid': $scope.config.hasSsid, 'tSsid': $scope.config.tSsid, 'tBrip': $scope.config.tBrip, 'tEncryption': $scope.config.tEncryption, 'tPasswd': $scope.config.tPasswd, 'isrestart': $scope.config.isrestart, 'ssid_status': $scope.config.ssid_status, 'terminalType': 1 });
            // return;
            $http.post("app/ac/apmanage/addApCfg", {
                    'param': selectRowIds,
                    'ssid': $scope.config.ssid,
                    'encryption': $scope.config.encryption,
                    'passwd': $scope.config.passwd,
                    'channel': $scope.config.channel,
                    'maxassoc': $scope.config.maxassoc,
                    'istemplate': $scope.config.istemplate,
                    'name': $scope.config.name,
                    'scheduleName': $scope.config.scheduleName,
                    'brip': $scope.config.brip,
                    'hasSsid': $scope.config.hasSsid,
                    'tSsid': $scope.config.tSsid,
                    'tBrip': $scope.config.tBrip,
                    'tEncryption': $scope.config.tEncryption,
                    'tPasswd': $scope.config.tPasswd,
                    'isrestart': $scope.config.isrestart,
                    'ssid_status': $scope.config.ssid_status,
                    'terminalType': 1
                })
                .success(function(data) {
                    $scope.mask(false);
                    if (data.success) {
                        $("#addModal").modal('hide');
                        $scope.showMsg(data.message, true);
                        $scope.innerCtrl.load($scope.datagrid.params);
                    } else
                        $scope.showMsg(data.message);
                });
        }

        // 初始化下拉框
        function initConfigSelect() {
            //使用模板 另存模板名称
            $http.post("app/ac/apmanage/getApCfgs",{
                apType:"ACCESS"
            })
            .success(function(data) {
                if (data.success) {
                    $scope.apCfgItems = data.data.rows;
                }
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
                if (checkeds[i].status == 1) {
                    $scope.showMsg("不允许删除正在运行的任务");
                    return;
                }
                selectRowIds[i] = checkeds[i].id;
            }

            $scope.showMsg("将要删除" + checkeds.length + "条记录", null, function(selectRowIds) {
                $http.post('app/ac/apmanage/removeConfigSchedules', {
                        'param': selectRowIds
                    })
                    .success(function(data, status) {
                        if (status == 403) {
                            $scope.showMsg("没有权限操作");
                            return;
                        }
                        if (data.success) {
                            $scope.innerCtrl.load($scope.datagrid.params)
                            $scope.showMsg(data.message, true);
                        } else
                            $scope.showMsg(data.message);
                        $scope.initCount();
                    }).error(function() {
                        $scope.showMsg("删除错误");
                    });
            }, selectRowIds);
        }


        $scope.onStart = function() {
            var checkeds = $scope.innerCtrl.getChecked();
            if (checkeds.length <= 0) {
                $scope.showMsg("必须勾选一条记录才能启动！");
                return;
            }
            var selectRowIds = new Array();
            for (var i = 0; i < checkeds.length; i++) {
                if (checkeds[i].status == 1) {
                    $scope.showMsg("不允许重复启动正在运行的任务");
                    return;
                }
                selectRowIds[i] = checkeds[i].id;
            }

            $scope.showMsg("将要启动" + checkeds.length + "条任务", null, function(selectRowIds) {
                $http.post('app/ac/apmanage/startConfigSchedules', {
                        'param': selectRowIds
                    })
                    .success(function(data, status) {
                        if (status == 403) {
                            $scope.showMsg("没有权限操作");
                            return;
                        }
                        if (data.success) {
                            $scope.innerCtrl.load($scope.datagrid.params);
                            $scope.showMsg(data.message, true);
                        } else
                            $scope.showMsg(data.message);
                        $scope.initCount();
                    }).error(function() {
                        $scope.showMsg("操作失败");
                    });
            }, selectRowIds);
        }

        $scope.onStop = function() {
            var checkeds = $scope.innerCtrl.getChecked();
            if (checkeds.length <= 0) {
                $scope.showMsg("必须勾选一条记录才能停止！");
                return;
            }
            var selectRowIds = new Array();
            for (var i = 0; i < checkeds.length; i++) {
                if (checkeds[i].status != 0&&checkeds[i].status !=null) {
                    $scope.showMsg("不允许停止已经运行的任务:" + checkeds[i].name);
                    return;
                }
                if (checkeds[i].status ==null) {
                    $scope.showMsg("初始状态的任务不需要停止:"+checkeds[i].name);
                    return;
                }
                selectRowIds[i] = checkeds[i].id;
            }

            $scope.showMsg("将要停止" + checkeds.length + "条任务", null, function(selectRowIds) {
                $http.post('app/ac/apmanage/stopConfigSchedules', {
                        'param': selectRowIds
                    })
                    .success(function(data, status) {
                        if (status == 403) {
                            $scope.showMsg("没有权限操作");
                            return;
                        }
                        if (data.success) {
                            $scope.innerCtrl.load($scope.datagrid.params);
                            $scope.showMsg(data.message, true);
                        } else
                            $scope.showMsg(data.message);
                    }).error(function() {
                        $scope.showMsg("操作失败");
                    });
            }, selectRowIds);
        }

        $scope.onResetSearch = function() {
            $scope.datagrid.params = { "terminalType": 1 };
        }

        $scope.initViewPage = function() {
            //初始化分页列表
            $scope.innerCtrlView = {};
            $scope.datagridView = {
                url: 'app/ac/apmanage/getConfigApRelations',
                method: 'post',
                quaryOnload: true,
                params: {},
                columns: [{
                    field: 'apName',
                    title: '设备名称'
                }, {
                    field: 'attempts',
                    title: '尝试次数'
                }, {
                    field: 'status',
                    title: '状态',
                    formatter: function(row) {
                        var val = row['status'];
                        if (val == null || typeof val == null || typeof val == undefined) {
                            return "任务初始";
                        }
                        if (val <= 1) {
                            return '配置无线网络成功';
                        } else if (val > 1) {
                            return '配置失败';
                        }
                    }
                }, {
                    field: 'message',
                    title: '信息'
                }, {
                    field: 'maxassoc',
                    title: '最大连接数'
                }, {
                    field: 'channel',
                    title: '信道'
                }, {
                    field: 'ssid',
                    title: '主ssid'
                }, {
                    field: 'encryption',
                    title: '加密方式'
                }, {
                    field: 'passwd',
                    title: '密码'
                }, {
                    field: 'brip',
                    title: '管理地址'
                }],
                sizes: [5, 10, 15],
                pageSize: 3,
            };
        }

        angular.custom.onShowview = function(row) {
            $("#viewModal").modal('show');
            // console.info(row);
            $scope.datagridView.params.ConfigScheduleId = row["id"];
            $scope.innerCtrlView.load($scope.datagridView.params);
        }

        $scope.addFilter = function(type) {
            if (type == "success") {
                $scope.datagrid.params.status = 2;
            }
            if (type == "fail") {
                $scope.datagrid.params.status = 3;
            }
            if (type == "run") {
                $scope.datagrid.params.status = 1;
            }
            if (type == "clear") {
                $scope.datagrid.params = {};
            }
            $scope.innerCtrl.load($scope.datagrid.params);
        }

        $scope.countTask = {};
        $scope.initCount=function(){
            $http.post('app/ac/apmanage/getConfigCount', {
                'type': 1
            }).success(function(data) {
                if (data.success) {
                    $scope.countTask.taskRun = data.data.taskRun == null ? 0 : data.data.taskRun;
                    $scope.countTask.taskFailure = data.data.taskFailure == null ? 0 : data.data.taskFailure;
                    $scope.countTask.taskSuccess = data.data.taskSuccess == null ? 0 : data.data.taskSuccess;
                    $scope.countTask.totalTask = data.data.totalTask == null ? 0 : data.data.totalTask;
                }
            });
        }
        $scope.initCount();

    });
