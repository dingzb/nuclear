/**
 * 税务业务管理
 * Created by Neo on 2017/5/9.
 */


angular.module('ws.app').controller('taxThirdCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
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

    function getCategory(typeId, target) {
        console.info(typeId, target)
        $http.post('app/tax/business/category/list', {
            typeId: typeId
        }).success(function (data) {
            if (data.success) {
                $.extend(target, data.data);
            } else if (data.message) {
                $scope.alert(data.message, 'error');
            }
            console.info(target);
        }).error(function (data) {
            $scope.alert(data, 'error');
        });
    }

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
        url: 'app/tax/examine/paging/third',
        method: 'post',
        params: {},
        columns: [{
            field: 'taxpayerCode',
            title: '纳税人识别号'
        }, {
            field: 'taxpayerName',
            title: '纳税人名称'
        }, {
            field: 'categoryTypeName',
            title: '业务类型'
        }, {
            field: 'categoryName',
            title: '业务项目'
        }, {
            field: 'agencyName',
            title: '主管税务机关'
        }, {
            field: 'createName',
            title: '税收管理员'
        }, {
            field: 'busTime',
            title: '业务时间'
        }, {
            field: 'firstExamine',
            title: '自控意见',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                if(row.firstExamine){
                    return row.firstExamine.hasIssue ?
                        '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 1)">有问题</button>' :
                        '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
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
                    return row.secondExamine.hasIssue ?
                        '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 2)">有问题</button>' :
                        '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
                } else {
                    return '';
                }
            }
        }, {
        //     field: 'thirdExamine',
        //     title: '监控意见',
        //     formatter: function (row) {
        //         var str = JSON.stringify(row);
        //         str = str.replace(/"/g, "'");
        //         if(row.thirdExamine){
        //             return row.thirdExamine.hasIssue ?
        //                 '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 3)">有问题</button>' :
        //                 '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
        //         } else {
        //             return '';
        //         }
        //     }
        // }, {
            field: 'id',
            title: '操作',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return '<button type="button" class="btn btn-link btn-sm" title="监控" onClick="angular.custom.taxBusinessThird(' + str + ')">监控</button>'
                    + "<button type=\"button\" class=\"btn btn-link btn-sm\" title='查看附件' onClick=\"angular.custom.showAttachment(" + str + ")\"><span class=\"glyphicon glyphicon-paperclip\" ></span></button>";
            }
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 10,
        sortName:'busTime',
        sortOrder:'desc'
    };

    //查询
    $scope.search = function () {
        $scope.innerCtrl.load($.extend($scope.datagrid.params, $scope.searchParams));
    };

    //清空
    $scope.resetSearch = function () {
        var clearSearch = {
            taxpayerName: ''
        };
        $.extend($scope.datagrid.params, clearSearch);
        $.extend($scope.searchParams, clearSearch);
    };

    //========= 监控 =================
    angular.custom.taxBusinessThird = function (row) {
        $scope.$apply(function () {
            $scope.detailObj = row;
            $scope.isThird = row.thirdExamine !== null; //是否进行过监控
            $scope.hasIssue = false;
            $scope.issueDesc = '';
            initIssueTypes();
        });
        $("#thirdModal").modal('show');
    };

    function initIssueTypes() {
        var issues = $('#issues').find('input[type=checkbox]');
        $.each(issues, function(i){
            $(issues[i]).prop('checked', false);
        });
    }

    angular.custom.taxBusinessIssueDetail = function (row, step) {
        var issues = null;

        $scope.$apply(function () {
            $scope.detailObj = row;
            switch (step) {
                case 1:
                    issues = row.firstExamine.issues;
                    $scope.issueDetail = row.firstExamine;
                    $scope.issueDetail.title = '自控';
                    break;
                case 2:
                    issues = row.secondExamine.issues;
                    $scope.issueDetail = row.secondExamine;
                    $scope.issueDetail.title = '防控';
                    break;
                case 3:
                    issues = row.thirdExamine.issues;
                    $scope.issueDetail = row.thirdExamine;
                    $scope.issueDetail.title = '监控';
            }
        });

        $('#issue_issues').find('input').prop('chec ked', false);

        if (issues){
            issues.forEach(function (issue) {
                $('#issue_issues').find('input[value=' + issue.id + ']').prop('checked', true);
            });
        }


        $('#issueDetailModal').modal('show');
    };

    //=============== 提交监控 =====================

    $scope.commitThird = function (row) {
        console.info($scope.detailObj);
        var thirdData = {
            busId: $scope.detailObj.id,
            hasIssue: $scope.hasIssue
        };

        if ($scope.hasIssue) {
            var issues = $('#issues').find('input:checked');
            if (issues.length === 0){
                $scope.alert('问题类型必选', 'error');
                return;
            }
            var issuesStr = '';
            $.each(issues, function (i) {
                issuesStr = issuesStr + $(issues[i]).val() + ',';
            });
            issuesStr = issuesStr.substr(0, issuesStr.length - 1);

            thirdData = $.extend(thirdData, {
                issueIdStrs: issuesStr,
                description: $scope.issueDesc
            });
        }

        $http.post('app/tax/examine/third/commit', thirdData).success(function (data) {
            if (data.success) {
                // $scope.innerCtrl.load($scope.datagrid.params);
                $scope.innerCtrl.reload();
                $("#thirdModal").modal('hide');
                $scope.alert(data.message);
            } else
                $scope.alert(data.message, 'error');
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
            return size.toFixed(2) + ' Byte';
        }else if ((size = size / 1024) < 1024) {
            return size.toFixed(2) + ' KB';
        } else if ((size = size / 1024) < 1024) {
            return size.toFixed(2) + ' MB';
        } else if ((size = size / 1024) < 1024) {
            return size.toFixed(2) + ' GB';
        }
    }

}]);