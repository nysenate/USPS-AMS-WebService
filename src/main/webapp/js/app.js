var ams = angular.module('ams', ['ams-common']);

var baseApi = contextPath + '/api';
var validateApi = '/validate';
var citystateApi = '/citystate';
var inquiryApi = '/inquiry';

ams.directive('myTable', function() {
    return function(scope, element, attrs) {

        // apply DataTable options, use defaults if none specified by user
        var options = {};
        if (attrs.myTable.length > 0) {
            options = scope.$eval(attrs.myTable);
        } else {
            options = {
                "bStateSave": true,
                "iCookieDuration": 2419200, /* 1 month */
                "bJQueryUI": true,
                "bPaginate": true,
                "bLengthChange": true,
                "bFilter": false,
                "bInfo": false,
                "bDestroy": true
            };
        }

        // Tell the dataTables plugin what columns to use
        // We can either derive them from the dom, or use setup from the controller
        var explicitColumns = [];
        element.find('th').each(function(index, elem) {
            explicitColumns.push($(elem).text());
        });
        if (explicitColumns.length > 0) {
            options["aoColumns"] = explicitColumns;
        } else if (attrs.aoColumns) {
            options["aoColumns"] = scope.$eval(attrs.aoColumns);
        }

        // aoColumnDefs is dataTables way of providing fine control over column config
        if (attrs.aoColumnDefs) {
            options["aoColumnDefs"] = scope.$eval(attrs.aoColumnDefs);
        }

        // aaSorting defines which column to sort on by default
        if (attrs.aaSorting) {
            options["aaSorting"] = scope.$eval(attrs.aaSorting);
        }

        if (attrs.fnRowCallback) {
            options["fnRowCallback"] = scope.$eval(attrs.fnRowCallback);
        }

        // apply the plugin
        var dataTable = element.dataTable(options);

        //$("#street-search").keyup( function () {
        //    /* Filter on the street column */
        //    dataTable.fnFilter( this.value, 2 );
        //});

        // watch for any changes to our data, rebuild the DataTable
        scope.$watch(attrs.aaData, function(value) {
            var val = value || null;
            if (val) {
                dataTable.fnClearTable();
                dataTable.fnAddData(scope.$eval(attrs.aaData));
            }
        });
    };
});

ams.filter('statusNameFilter', function(){
    return function(statusCode) {
        switch (statusCode) {
            case 2  : return 'Missing Address';
            case 3  : return 'Missing Zip 9';
            case 10 : return 'Dual Address';
            case 11 : return 'Invalid Address';
            case 12 : return 'Invalid State';
            case 13 : return 'Invalid City';
            case 21 : return 'No Match';
            case 22 : return 'Multiple Matches';
            case 31 : return 'Exact Match';
            case 32 : return 'Default Match';
            default : return 'Error';
        }
    };
});

ams.filter('statusClassFilter', function(){
    return function(statusCode) {
        switch (statusCode) {
            case 21 : return 'empty-indication';
            case 22 :
            case 32 : return 'warning-indication';
            case 31 : return 'success-indication';
            default : return 'error-indication';
        }
    };
});

ams.filter('parityFilter', function(){
    return function(parityCode) {
        switch(parityCode) {
            case 'O' : return 'ODD';
            case 'E' : return 'EVEN';
            case 'B' : return 'BOTH';
            default : return '';
        }
    }
});

ams.controller('ValidateController', function($scope, $http, $filter, dataBus) {
    $scope.baseUrl = baseApi + validateApi + "?detail=true&";
    $scope.addr1 = '';
    $scope.addr2 = '';
    $scope.city = '';
    $scope.state = 'NY';
    $scope.zip5 = '';
    $scope.zip4 = '';

    $scope.statusClass = '';

    (function(){
        var $element = $('.col-2, .col-3').bind('webkitAnimationEnd', function(){
            $(this).removeClass('wobble');
        });
    })();

    $scope.lookup = function() {
        var url = this.baseUrl + 'addr1=' + this.addr1 + '&addr2=' + this.addr2 + '&city=' + this.city
                               + '&state=' + this.state + '&zip5=' + this.zip5 + '&zip4=' + this.zip4;
        $http.get(url)
            .success(function(data){
                $scope.result = angular.extend($scope, data);
                if ($scope.result != null) {
                    $scope.statusClass = $filter('statusClassFilter')($scope.result.status.code);
                }
                $('.col-2, .col-3').addClass('wobble');
            })
            .error(function(data){

            });
    };
});

ams.controller('ValidateView', function($scope, dataBus){

});