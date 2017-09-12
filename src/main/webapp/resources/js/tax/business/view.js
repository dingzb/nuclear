/**
 * 税务业务管理
 * Created by Neo on 2017/5/9.
 */


angular.module('ws.app').controller('taxViewCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    $scope.searchParams = {};
    $scope.categories = [];
    $scope.addObj = {};
    $scope.editObj = {};

    $scope.hasIssue = false;
    $scope.issues = [];
    getIssue();

    $scope.initDtp = function (e) {
        $scope.datetimepicker('#add_time').onChange(function (d) {
            $scope.addObj.busTime = d;
        });
        $scope.datetimepicker('#edit_time').onChange(function (d) {
            $scope.editObj.busTime = d;
        });
    };

    $http.post('app/tax/business/category/type/list', {}).success(function (data) {
        if (data.success) {
            $scope.categoryTypes = data.data;
        } else if (data.message) {
            $scope.alert(data.message, 'error');
        }
    }).error(function (data) {
        $scope.alert(data, 'error');
    });


    $scope.getCategory = function () {
        $scope.categories = [];
        $scope.searchParams.categoryId = '';
        $http.post('app/tax/business/category/list', {
            typeId: $scope.searchParams.categoryTypeId
        }).success(function (data) {
            if (data.success) {
                $.extend($scope.categories, data.data);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    };

    function getIssue() {
        $http.post('app/tax/business/issue/list', {}).success(function (data) {
            if (data.success) {
                $.extend($scope.issues, data.data);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    }

    //初始化组列表
    $scope.datagrid = {
        url: 'app/tax/business/paging/all',
        method: 'post',
        params: {},
        columns: [{
            field: 'taxpayerCode',
            title: '纳税人识别号',
            sortable: true
        }, {
            field: 'taxpayerName',
            title: '纳税人名称',
            sortable: true
        }, {
            field: 'categoryName',
            title: '业务项目',
            sortable: true
        }, {
            field: 'agencyName',
            title: '主管税务机关',
            sortable: true
        }, {
            field: 'createName',
            title: '税收管理员',
            sortable: true
        }, {
            field: 'busTime',
            title: '业务时间',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            translator: function (row) {
                switch (row.status) {
                    case 0:
                        return '待提交';
                    case 1:
                        return '待自控';
                    case 2:
                        return '待防控';
                    case 3:
                        return '待监控';
                    case 4:
                        return '完成';
                    default:
                        return '待整改';
                }
            }
        },{
            field: 'firstExamine',
            title: '自控意见',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                if(row.firstExamine){
                    var rt = '';
                    if (row.firstExamine.hasIssue) {
                        rt += '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 1)">有问题</button>';
                        rt += '<span>/</span>';
                        if ((row.amendmentCode & 4) === 4) {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="已整改" style="color: #95ff73;" disabled>已整改</button>';
                        } else {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="未整改" style="color: #ff6e49;" disabled>未整改</button>';
                        }
                    } else {
                        rt = '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
                    }
                    return rt;
                } else {
                    return '';
                }
            }
        }, {
            field: 'secondExamine',
            title: '防控意见',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                if(row.secondExamine){
                    var rt = '';
                    if (row.secondExamine.hasIssue) {
                        rt += '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 2)">有问题</button>';
                        rt += '<span>/</span>';
                        if ((row.amendmentCode & 2) === 2) {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="已整改" style="color: #95ff73;" disabled>已整改</button>';
                        } else {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="未整改" style="color: #ff6e49;" disabled>未整改</button>';
                        }
                    } else {
                        rt = '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
                    }
                    return rt;
                } else {
                    return '';
                }
            }
        }, {
            field: 'thirdExamine',
            title: '监控意见',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                if(row.thirdExamine){
                    var rt = '';
                    if (row.thirdExamine.hasIssue) {
                        rt += '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 3)">有问题</button>';
                        rt += '<span>/</span>';
                        if ((row.amendmentCode & 1) === 1) {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="已整改" style="color: #95ff73;" disabled>已整改</button>';
                        } else {
                            rt += '<button type="button" class="btn btn-link btn-sm" title="未整改" style="color: #ff6e49;" disabled>未整改</button>';
                        }
                    } else {
                        rt = '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
                    }
                    return rt;
                } else {
                    return '';
                }
            }
        // }, {
        //     field: 'status',
        //     title: '整改状态',
        //     translator: function (row) {
        //         if (row['firstHasIssue'] || row['secondHasIssue'] || row['thirdHasIssue']) {
        //             return row['status'] === 4 ? '已整改' : '未整改';
        //         } else {
        //             return '';
        //         }
        //     }
        }, {
            field: 'id',
            title: '操作',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return "<button type=\"button\" class=\"btn btn-link btn-sm\" title='详情' onClick=\"angular.custom.taxBusinessDetail(" + str + ")\"><span class=\"glyphicon glyphicon-link\" > </span></button>" +
                       "<button type=\"button\" class=\"btn btn-link btn-sm\" title='查看附件' onClick=\"angular.custom.showAttachment(" + str + ")\"><span class=\"glyphicon glyphicon-paperclip\" ></span></button>";
            }
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 10,
        sortName:'busTime',
        sortOrder: 'desc'
    };

    //查询
    $scope.search = function () {
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };

    //清空
    $scope.resetSearch = function () {
        var clearSearch = {
            taxpayerName: '',
            taxpayerCode: '',
            createUserName: '',
            categoryTypeId: '',
            categoryId: ''
        };
        $.extend($scope.datagrid.params, clearSearch);
        $.extend($scope.searchParams, clearSearch);
    };

    //=========详情===================
    angular.custom.taxBusinessDetail = function (row) {
        $scope.$apply(function () {
            $scope.detailObj = row;
        });
        $("#detailModal").modal('show');
    };

    angular.custom.taxBusinessIssueDetail = function (row, step) {
        var issues = null;

        $scope.$apply(function () {
            $scope.detailObj = row;
            switch (step) {
                case 1:
                    issues = row.firstExamine.issues;
                    $scope.issueDetail = row.firstExamine;
                    $scope.issueDetail.title = '自查';
                    break;
                case 2:
                    issues = row.secondExamine.issues;
                    $scope.issueDetail = row.secondExamine;
                    $scope.issueDetail.title = '审查';
                    break;
                case 3:
                    issues = row.thirdExamine.issues;
                    $scope.issueDetail = row.thirdExamine;
                    $scope.issueDetail.title = '核查';
            }
        });

        $('#issue_issues').find('input').prop('checked', false);

        if (issues){
            issues.forEach(function (issue) {
                $('#issue_issues').find('input[value=' + issue.id + ']').prop('checked', true);
            });
        }


        $('#issueDetailModal').modal('show');
    };


    $scope.del = function () {
        var checkeds = $scope.innerCtrl.getChecked();
        if (checkeds.length <= 0) {
            $scope.alert("至少选择一条记录！", 'error');
            return;
        }
        var ids = [];
        for (var i = 0; i < checkeds.length; i++) {
            ids.push(checkeds[i].id);
        }
        $scope.confirm("将要删除" + ids.length + "条记录", function (y) {
            if (!y) {
                return;
            }
            $http.post('app/tax/business/del', {
                'ids': ids
            }).success(function (data) {
                if (data.success) {
                    $scope.innerCtrl.load($scope.datagrid.params);
                    $scope.alert(data.message);
                } else
                    $scope.alert(data.message, 'error');
            });
        });
    };

    /////// 附件 ///////////////
    angular.custom.showAttachment = function (row) {
        $scope.attachments = [];
        $http.post('app/tax/business/attachment/list', {
            busId: row.id
        }).success(function (data) {
            if (data.success && data.data.initialPreviewConfig) {
                for(i = 0; i < data.data.initialPreviewConfig.length; i++){
                    $scope.attachments.push($.extend(data.data.initialPreviewConfig[i], {
                        url: data.data.initialPreview[i]
                    }));
                }
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
        $('#attachmentModal').modal('show');
    };

    $scope.getSize = function (size) {

        if (size < 1024) {
            return parseInt(Math.round(size * 100)) / 100 + ' Byte';
        }else if ((size = size / 1024) < 1024) {
            return parseInt(Math.round(size * 100)) / 100 + ' KB';
        } else if ((size = size / 1024) < 1024) {
            return parseInt(Math.round(size * 100)) / 100 + ' MB';
        } else if ((size = size / 1024) < 1024) {
            return parseInt(Math.round(size * 100)) / 100 + ' GB';
        }
    }
}]);