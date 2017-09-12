/**
 * ac管理欢迎页
 */
angular.module('ws.app').controller('acMenuCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    //防止重复获取目录，需要将列表存储到rootScope一份
    if (!$rootScope.acMenus) {
        $http({
            url: 'app/system/state/menus',
            method: 'get',
            params: {
                code: 'manage'
            },
            dataType: 'json'
        }).success(function (data) {
            if (data.success)
                $scope.menus = $rootScope.acMenus = data.data;
        });
    } else {
        $scope.menus = $rootScope.acMenus;
    }
}]).config(['$stateProvider', function ($stateProvider) {
    $stateProvider.state('main.manage', {
        url: '/manage',
        views: {
            'bottom@main': {
                templateUrl: 'tpls/manage/layout.html'
            },
            'left@main.manage': {
                templateUrl: 'tpls/manage/menu.html'
            },
            'right@main.manage': {
                templateUrl: 'tpls/manage/welcome.html'
            }
        }
    }).state('main.manage.control', {
            url: '/control'
        })
        .state('main.manage.control.place', {
            url: '/place',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/place.html'
                }
            }
        })
        .state('main.manage.control.ap', {
            url: '/ap',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/ap.html'
                }
            }
        })
        .state('main.manage.control.apTerminal', {
            url: '/apTerminal',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/terminal.html'
                }
            }
        })
        .state('main.manage.control.acCluster', {
            url: '/acCluster',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/acClusterMana.html'
                }
            }
        })
        .state('main.manage.control.securityVendor', {
            url: '/securityVendor',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/securityVendorMana.html'
                }
            }
        })
        .state('main.manage.control.contacts', {
            url: '/contacts',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/contacts.html'
                }
            }
        })
        .state('main.manage.control.dataCenter', {
            url: '/dataCenter',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/dataCenter.html'
                }
            }
        })
        .state('main.manage.control.apModel', {
            url: '/apModel',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/apmodel.html'
                }
            }
        })
        .state('main.manage.control.ac', {
            url: '/ac',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/control/acMana.html'
                }
            }
        })
        .state('main.manage.config', {
            url: '/config'
        })
        .state('main.manage.config.tpl', {
            url: '/tpl',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/config/tpl.html'
                }
            }
        })
        .state('main.manage.config.schedule', {
            url: '/schedule',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/config/schedule.html'
                }
            }
        })
        .state('main.manage.config.terminalSchedule', {
            url: '/terminalSchedule',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/config/terminalSchedule.html'
                }
            }
        })
        //升级管理
        .state('main.manage.update', {
            url: '/update'
        })
        .state('main.manage.update.schedule', {
            url: '/schedule',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/update/schedule.html'
                }
            }
        })
        .state('main.manage.update.terminalSchedule', {
            url: '/terminalSchedule',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/update/terminalSchedule.html'
                }
            }
        })
        .state('main.manage.update.firmware', {
            url: '/firmware',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/manage/update/firmware.html'
                }
            }
        })
        .state('main.manage.update.apalarm', {
            url: '/apalarm',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/analysis/monitor/apalarm.html'
                }
            }
        })
        //状态管理
        .state('main.manage.status', {
            url: '/status'
        })
        .state('main.manage.status.apalarm', {
            url: '/apalarm',
            views: {
                'right@main.manage': {
                    templateUrl: 'tpls/analysis/monitor/apalarm.html'
                }
            }
        });
}])
/**
 * ac欢迎页控制器
 */
    .controller('acWelcomeCtrl', ['$scope', '$http', function ($scope, $http) {

        var map = null;
        var markerClusterer = null;
        var lastMarkers = null;
        //地图
        $scope.dataParam = null;
        $scope.initMap = function () {
            map = new BMap.Map("allmap");
            map.centerAndZoom(new BMap.Point(116.519533, 39.593000), 6);
            map.enableScrollWheelZoom();
            map.addControl(new BMap.NavigationControl());
//        map.addControl(new BMap.MapTypeControl());
            map.addControl(new BMap.ScaleControl());
            /*************创建地图内自定义控件s***********/
                // 创建控件
            var myrhCtrl = new RefreshControl();
            var mytypeCtrl = new TypeControl();
            // 添加到地图当中
            map.addControl(myrhCtrl);
            map.addControl(mytypeCtrl);
            /*************创建地图内自定义控件e***********/

            $http.get("app/ac/apmanage/getApMap")
                .success(function (data) {
                    $scope.dataParam = data.data;
                    var longitude = data.data[0].longitude;
                    var latitude = data.data[0].latitude;
                    if (longitude && latitude) {
                        map.centerAndZoom(new BMap.Point(longitude, latitude), 6);
                    }
                    $scope.show($scope.dataParam);
                });
        }

        // 定义一个控件类,即function
        function RefreshControl() {
            // 默认停靠位置和偏移量
            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
            //参数说明：从右偏移，从上偏移（px）
            this.defaultOffset = new BMap.Size(134, 10);
        }

        // 通过JavaScript的prototype属性继承于BMap.Control
        RefreshControl.prototype = new BMap.Control();

        // 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
        // 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
        RefreshControl.prototype.initialize = function (map) {
            // 创建一个DOM元素
            var objE = document.createElement("div");
            objE.style.cursor = "pointer";
            objE.style.border = "1px solid gray";
            objE.style.backgroundColor = "white";
            objE.onclick = function (e) {
                $scope.initMap();
            }

            var content = $('<button type="button" class="btn btn-link pull-right" >'
                + '<span class="glyphicon glyphicon-refresh" style="color:#8EA8E0" aria-hidden="true">刷新</span>'
                + '</button>').html();
            objE.innerHTML = content;
            // 添加DOM元素到地图中
            map.getContainer().appendChild(objE);
            // 将DOM元素返回
            return objE;
        }

        // 定义一个控件类,即function
        function TypeControl() {
            // 默认停靠位置和偏移量
            this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
            //参数说明：从右偏移，从上偏移（px）
            this.defaultOffset = new BMap.Size(185, 10);
        }

        TypeControl.prototype = new BMap.Control();

        TypeControl.prototype.initialize = function (map) {
            // 创建一个DOM元素
            var objE = document.createElement("div");

            // class="select_search "
            var content = $('<div><select style="width: 85px;height: 23px;" onchange="angular.mapFun.typeChange(this.value)" >'
                + '<option value="-1">全部</option><option value="1">无线接入</option>'
                + '<option value="0">终端采集</option></select></div>').html();
            objE.innerHTML = content;
            // 添加DOM元素到地图中
            map.getContainer().appendChild(objE);
            // 将DOM元素返回
            return objE;
        }
        angular.mapFun = function () {
        }
        angular.mapFun.typeChange = function (value) {
            $scope.changeType(value);
        }

        $scope.show = function (dataParam) {
            var markers = [];
            var points = [];
            var params = dataParam;

            for (var i = 0; i < params.length; i++) {
                var con = params[i].name;
                var lng = params[i].lng;
                var lat = params[i].lat;
                var status = params[i].status;
                var type = params[i].terminalType;
                var statusStr = params[i].status == 1 ? "在线" : "不在线";
                var picUrl = params[i].picturePath;
                if (picUrl == "" || picUrl == null) {
                    picUrl = 'http://app.baidu.com/map/images/tiananmen.jpg';
                }
                var text = "<div><b Style='font-size:14px;'>" + con + "</b></div>"
                    + "<div><img alt='' class='pano_thumnail_img' width='323' height='100' border='0' src='" + picUrl + "'>"
                    + "<table class='table_addr_tel table-hover' cellspacing='0'><tbody>"
                    + "<tr nowrap='nowrap'><th>场所名称：</th> <td>" + params[i].placeName + "</td> </tr>"
                    + "<tr ><th nowrap='nowrap'>场所类型：</th> <td  Style='font-size:12px;'>" + params[i].placeTypeName + "</td></tr>"
                    + "<tr > <th nowrap='nowrap'>地址：</th> <td>" + params[i].address + "</td></tr>"
                    + "<tr ><th nowrap='nowrap'>在线状态：</th><td>" + statusStr + "</td></tr>"
                    + "</tbody></table>";//<a href=\"#/manage/control/ap\" target='_blank'>转到AP中查看</a></div>
                var zpoint = new BMap.Point(lng, lat);
                points.push(zpoint);
                //0采集 1终端
                if (type == 0) {
                    myIcon = new BMap.Icon("resources/image/inInLine.png", new BMap.Size(23, 25));
                    text = text + "<a href=\"#/manage/control/apTerminal\" target='_blank'>转到终端采集中查看</a></div>";
                    if (params[i].status != 1) {
                        myIcon = new BMap.Icon("resources/image/inNotLine.png", new BMap.Size(23, 25));
                    }
                } else if (type == 1) {
                    myIcon = new BMap.Icon("resources/image/outInlinev.png", new BMap.Size(23, 25));
                    text = text + "<a href=\"#/manage/control/ap\" target='_blank'>转到AP中查看</a></div>";
                    if (params[i].status != 1) {
                        myIcon = new BMap.Icon("resources/image/outNotLine.png", new BMap.Size(23, 25));
                    }
                }

                var marker = new BMap.Marker(zpoint, {icon: myIcon});
                markers.push(marker);
                // map.addOverlay(marker);//会造成重复添加点
                addClickHandler(text, marker);
            }
            //设置点自适应
            // map.setViewport(points);

            function addClickHandler(content, marker) {
                marker.addEventListener("click", function (e) {
                    openInfo(content, e)
                });
            }

            function addClickLabel(content, label) {
                label.addEventListener("click", function (e) {
                    openInfo(content, e)
                });
            }

            function openInfo(content, e) {
                var p = e.target;
                var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
                var infoWindow = new BMap.InfoWindow(content); // 创建信息窗口对象
                map.openInfoWindow(infoWindow, point); //开启信息窗口
            }

            //点聚合写法
            //先执行清空上次的聚合点，否则过滤不起作用
            if (markerClusterer != null && lastMarkers != null) {
                markerClusterer.removeMarkers(lastMarkers);
            }
            lastMarkers = markers;
            markerClusterer = new BMapLib.MarkerClusterer(map, {markers: markers});
        }

        $scope.changeType = function (value) {
            var params = [];
            if (value == 0) { //采集
                for (var i = 0; i < $scope.dataParam.length; i++) {
                    if ($scope.dataParam[i].terminalType == 0) {
                        params.push($scope.dataParam[i]);
                    }
                }
            } else if (value == 1) { //接入
                for (var i = 0; i < $scope.dataParam.length; i++) {
                    if ($scope.dataParam[i].terminalType == 1) {
                        params.push($scope.dataParam[i]);
                    }
                }
            } else {
                params = $scope.dataParam;
            }
            map.clearOverlays();
            $scope.show(params);
        }
    }]);
