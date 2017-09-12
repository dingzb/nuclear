/**
 * 税务业务管理
 * Created by Neo on 2017/5/9.
 */


angular.module('ws.app').controller('taxFirstCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
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
        url: 'app/tax/examine/paging/first',
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
        // }, {
        //     field: 'firstExamine',
        //     title: '自控意见',
        //     formatter: function (row) {
        //         var str = JSON.stringify(row);
        //         str = str.replace(/"/g, "'");
        //         if (row.firstExamine) {
        //             return row.firstExamine.hasIssue ?
        //                 '<button type="button" class="btn btn-link btn-sm" title="有问题" onClick="angular.custom.taxBusinessIssueDetail(' + str + ', 1)">有问题</button>' :
        //                 '<button type="button" class="btn btn-link btn-sm" title="没问题" disabled>没问题</button>';
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
                return '<button type="button" class="btn btn-link btn-sm" title="自控" onClick="angular.custom.taxBusinessFirst(' + str + ')">自控</button>'
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

    //========= 自控 =================
    angular.custom.taxBusinessFirst = function (row) {
        $scope.$apply(function () {
            $scope.detailObj = row;
            $scope.isFirsted = row.firstExamine !== null;
            $scope.hasIssue = false;
            $scope.issueDesc = '';
            initIssueTypes();
        });
        $("#firstModal").modal('show');
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

            $('#issue_issues').find('input').prop('checked', false);

            if (issues){
                issues.forEach(function (issue) {
                    $('#issue_issues').find('input[value=' + issue.id + ']').prop('checked', true);
                });
            }
        });

        $('#issueDetailModal').modal('show');
    };

    //=============== 提交自控 =====================

    $scope.commitFirst = function (row) {
        console.info($scope.detailObj);
        var firstData = {
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

            firstData = $.extend(firstData, {
                issueIdStrs: issuesStr,
                description: $scope.issueDesc
            });
        }

        $http.post('app/tax/examine/first/commit', firstData).success(function (data) {
            if (data.success) {
                // $scope.innerCtrl.reload($scope.datagrid.params);
                $scope.innerCtrl.reload();
                $("#firstModal").modal('hide');
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