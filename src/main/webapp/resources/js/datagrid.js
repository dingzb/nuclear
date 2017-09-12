/**
 * Created by Dzb on 2017/3/16.
 */
/**
 * 数据表
 */
angular.module('ws.datagrid', []).directive('wsDatagrid', ['$sce', function ($sce) {
    return {
        transclude: true,
        restrict: 'E',
        templateUrl: 'tpls/common/datagrid.html',
        scope: {
            /*
             * configuration:{
             * 	url:'',
             * 	param:{},
             *  queryOnLoad:true,
             *  toolbar: true,
             *  paging: true,
             * 	columns:[{
             * 		filed:'',
             * 		title:'',
             *		formatter:fn(row), // return a html code
             *      sortable:false,
             *      width:null, // number
             *      translator: fn(row) //return another value
             * 	}],
             *  //默认的排序字段
             *  sortName:'',
             *  sortOrder:'asc/desc',
             * ///checkbox可以是boolean，这时不使用指定字段判断选中状态
             * checkbox:{
             *     field:'yes'
             * },
             * extension: {
             * 	checkboxFn: xxx
             * }
             * }
             */
            configuration: '=',
            innerCtrl: '='
        },
        require: ['wsDatagrid'],
        link: function (scope, element, attrs, ctrl) {
            //创建页面码对象
            function makePage(number, text) {
                return {
                    number: number,
                    text: text
                };
            }

            /**
             * 获取页面列表
             * @param current 当前页码
             * @param size 页码数量
             * @total 页码总数
             */
            scope.getPages = function (current, size, total) {
                size = parseInt(size);
                var pages = [];
                var startPage = Math.max(((Math.ceil(current / size) - 1) * size) + 1, 0);
                var endPage = Math.min(startPage + size - 1, total);
                if (startPage == 0 && endPage == 0) {
                    return;
                }
                for (var number = startPage; number <= endPage; number++) {
                    var page = makePage(number, number);
                    pages.push(page);
                }

                if (startPage > 1) {
                    var previousPageSet = makePage(startPage - 1, '...', false);
                    pages.unshift(previousPageSet);
                }

                if (endPage < total) {
                    var nextPageSet = makePage(endPage + 1, '...', false);
                    pages.push(nextPageSet);
                }
                return pages;
            };

            //选择列表函数
            scope.selectPage = function (pageNum) {
//		    	scope.pageCurrent=pageNum;
//		    	scope.pages=scope.getPages(pageNum,parseInt(scope.pageSize),scope.pageTotal);
                ctrl[0].query(pageNum, scope.params);
            };

            //数据条目数选择函数
            scope.selectSize = function (size) {
                scope.size = size;
                ctrl[0].load(scope.params);
            };

            scope.checkall = {
                is: false
            };

            //全选
            scope.selectAll = function () {
                $(scope.rows).each(function (i) {
                    scope.rows[i][scope.fieldCheckField] = scope.checkall.is;
                });
            };
            //单选事件，需要增加回调函数，
            scope.selectOne = function (checkedIndex) {
                if (scope.extension && scope.extension.checkboxFn && (typeof scope.extension.checkboxFn === "function")) {
                    scope.extension.checkboxFn();
                }
                if (scope.singleSelect === true) {
                    $(scope.rows).each(function (i) {
                        if (i == checkedIndex) {
                            return;
                        }
                        scope.rows[i][scope.fieldCheckField] = false;
                    });
                } else {
                    //表示不是单选状态，当有一个取消选择了，则需要把全选取消掉
                    var checkss = [];
                    $(scope.rows).each(function (i) {
                        if (scope.rows[i][scope.fieldCheckField])
                            checkss.push(scope.rows[i]);
                    });
                    scope.checkall.is = checkss.length == scope.rows.length;
                }
            };

            //格式化返回值
            scope.htmlFormatter = function (fn, val) {
                return $sce.trustAsHtml(fn(val));
            };

            //排序
            scope.changeSort = function (column) {
                if (!column.sortable) {
                    return;
                }
                if (scope.params.sort === column.field) {
                    if (scope.params.order === 'asc') {
                        scope.params.order = 'desc';
                    } else {
                        scope.params.order = 'asc';
                    }
                } else {
                    scope.params.sort = column.field;
                    scope.params.order = 'asc';
                    console.info(scope.params.field);
                }

                ctrl[0].load(scope.params);
            };

            ////////////////////cfg///////////////////////////////////////////
            scope.url = scope.configuration.url;
            scope.method = scope.configuration.method;
            scope.params = scope.configuration.params;
            scope.columns = scope.configuration.columns;
            scope.extension = scope.configuration.extension;
            scope.toolbar = (scope.configuration.toolbar === undefined) ? true : scope.configuration.toolbar;
            scope.paging = (scope.configuration.paging === undefined) ? true : scope.configuration.paging;

            if (scope.configuration.sortName) {
                scope.params.sort = scope.configuration.sortName;
            }
            if (scope.configuration.sortOrder) {
                scope.params.order = scope.configuration.sortOrder;
            } else {
                scope.params.order = 'asc';
            }
            //数据条目列表
            scope.sizes = scope.configuration.sizes || [20, 30, 50, 80];
            //页码个数
            scope.pageSize = scope.configuration.pageSize;
            //数据条目数
            scope.size = scope.sizes[0];
            scope.innerCtrl = ctrl[0];
            scope.checkbox = scope.configuration.checkbox;
            if (scope.checkbox && (typeof scope.checkbox === 'boolean')) {
                scope.fieldCheckbox = false;
                scope.fieldCheckField = 'checkbox';
            } else if (scope.checkbox && (typeof scope.checkbox === 'object')) {
                scope.fieldCheckbox = true;
                scope.fieldCheckField = scope.checkbox.field;
            }
            //判断是否只允许单选
            scope.singleSelect = scope.configuration.singleSelect;
            //数据总数
            scope.total = 0;
            //当前页
            scope.pageCurrent = 0;
            //总页数
            scope.pageTotal = 0;
            //页面码列表
            scope.pages = scope.getPages(1, scope.pageSize, scope.pageTotal);
            //默认加载完成后查询数据
            if (scope.configuration.queryOnLoad || scope.configuration.queryOnLoad === undefined) {
                ctrl[0].load(scope.params);
            }

            ////////////////////////////////////////////////////////////////////
        },
        controller: ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
            var self = this;
            /**
             * 载入
             * @param params 查询参数
             * @param callback
             */
            this.load = function (params, callback) {
                self.query(1, params, callback);
            };

            /**
             * grid查询函数
             * @param page    页码
             * @param param 查询参数
             */
            this.query = function (page, params, callbak) {
                if (!$scope.url || $scope.url.length <= 0) {
                    //数据总数
                    $scope.total = 0;
                    //当前页
                    $scope.pageCurrent = 0;
                    //总页数
                    $scope.pageTotal = 0;
                    //页面码列表
                    $scope.pages = $scope.getPages(page, $scope.pageSize, $scope.pageTotal);
                    return;
                }
                params = params || {};
                if ($scope.paging) {
                    params.page = page;
                    params.size = $scope.size;
                }
                $scope.isLoading = true;
                $http({
                    method: $scope.method,
                    url: $scope.url,
                    data: $scope.method !== 'GET' ? params : undefined,
                    params: $scope.method === 'GET' ? params : undefined
                }).success(function (data, status, headers, config) {
                    $scope.isLoading = false;
                    if (data.success) {
                        $scope.rows = data.data.rows;
                        $scope.total = data.data.total;
                        $scope.pageCurrent = data.data.total == 0 ? 0 : page;
                        //总页数
                        $scope.pageTotal = Math.ceil(data.data.total / $scope.size);
                        //页面码列表
                        $scope.pages = $scope.getPages(page, $scope.pageSize, $scope.pageTotal);
                        /**
                         * 成功时回调的函数
                         */
                        if (typeof callbak === 'function') {
                            callbak();
                        }

                        if ($scope.checkbox) {
                            if ($scope.rows.length === 0) {
                                $scope.checkall.is = false;
                            } else {
                                $scope.checkall.is = self.getChecked().length === $scope.rows.length
                            }
                        }
                    } else {
                        if (status == 200) {
                            $rootScope.alert(data.message, 'error');
                        }
                    }
                }).error(function (data, status, headers, config) {
//					$scope.isLoading = false;
                });
            };
            /**
             * 重新加载数据，当前页码不变，查询条件不变
             */
            this.reload = function (callback) {
                self.query($scope.pageCurrent, $scope.params, callback);
            };
            /**
             * 获取选中的行
             */
            this.getChecked = function () {
                var checkeds = [];
                $($scope.rows).each(function (i) {
                    if ($scope.rows[i][$scope.fieldCheckField])
                        checkeds.push($scope.rows[i]);
                });
                return checkeds;
            }
        }]
    };
}]);