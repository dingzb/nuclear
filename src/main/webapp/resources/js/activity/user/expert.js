angular.module('ws.app').controller('actExpertCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
    //初始化组列表
    $scope.datagrid = {
        url: 'app/activity/config/award/paging',
        method: 'post',
        params: {
            current: true
        },
        columns: [{
            field: 'name',
            title: '名称',
            width: 25
        }, {
            field: 'description',
            title: '描述',
            width: 30
        }, {
            field: 'typeName',
            title: '类型',
            width: 25
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 20
        }, {
            field: 'id',
            title: '操作',
            formatter: function (row) {
                var str = JSON.stringify(row);
                str = str.replace(/"/g, "'");
                return '<button type="button" class="btn btn-link btn-sm" title="等级" onClick="angular.custom.award.levelDetail(' + str + ')"><span class="fa fa-trophy"></span></button>'
                    +'<button type="button" class="btn btn-link btn-sm" title="指标" onClick="angular.custom.award.criterionDetail(' + str + ')"><span class="fa fa-navicon"></span></button>';
            }
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };
}]);

