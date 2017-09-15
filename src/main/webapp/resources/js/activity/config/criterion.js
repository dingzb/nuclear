angular.module('ws.app').controller('actCriterionCtrl', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
//初始化组列表
    $scope.datagrid = {
        url: 'app/system/group/paging',
        method: 'post',
        params: {},
        columns: [{
            field: 'name',
            title: '名称'
        }, {
            field: 'type',
            title: '类型',
            formatter: function (val) {
                switch (val.type) {
                    case 'ADMIN' :
                        return '管理员';
                    case 'NORMAL' :
                        return '普通';
                    case 'USER' :
                        return '用户本身组';
                }
            }
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'modifyTime',
            title: '修改时间'
        }, {
            field: 'description',
            title: '描述'
        }],
        checkbox: true,
        sizes: [10, 20, 50, 80],
        pageSize: 3
    };
}]);
