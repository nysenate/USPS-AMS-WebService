var ams = angular.module('ams', ['ams-common']);

var baseApi = contextPath + '/api';
var validateApi = '/validate';
var citystateApi = '/citystate';
var inquiryApi = '/inquiry';

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

ams.controller('ValidateController', function($scope, $http, dataBus) {
    $scope.baseUrl = baseApi + validateApi + "?detail=true&";
    $scope.addr1 = '';
    $scope.addr2 = '';
    $scope.city = '';
    $scope.state = 'NY';
    $scope.zip5 = '';
    $scope.zip4 = '';

    $scope.lookup = function() {
        var url = this.baseUrl + 'addr1=' + this.addr1 + '&addr2=' + this.addr2 + '&city=' + this.city
                               + '&state=' + this.state + '&zip5=' + this.zip5 + '&zip4=' + this.zip4;
        $http.get(url)
            .success(function(data){
                $scope.result = angular.extend($scope, data);
            })
            .error(function(data){

            });

    };
});

ams.controller('ValidateView', function($scope, dataBus){

});