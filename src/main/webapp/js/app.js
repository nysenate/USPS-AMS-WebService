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
    $scope.$resultContainer = $('.col-2');

    $scope.lookup = function() {
        var url = this.baseUrl + 'addr1=' + encodeURIComponent(this.addr1) + '&addr2=' + encodeURIComponent(this.addr2)
                               + '&city=' + encodeURIComponent(this.city) + '&state=' + this.state
                               + '&zip5=' + encodeURIComponent(this.zip5) + '&zip4=' + encodeURIComponent(this.zip4);
        $http.get(url)
            .success(function(data){
                $scope.result = data;
                if ($scope.result != null) {
                    $scope.statusClass = $filter('statusClassFilter')($scope.result.status.code);
                }
                window.scrollTo(0, 0);
                $scope.$resultContainer.addClass('bounce-in-anim');
            })
            .error(function(data){

            });
    };
});

$(document).ready(function(){

    setUpAnimations();

    /**
     * Used to setup event handlers to reset animations so that they
     * can replay when needed.
     */
    function setUpAnimations() {

        $animated = $('.animated');
        function removeAnimations() {
            $animated.removeClass('bounce-in-anim');
        }

        $animated.bind('webkitAnimationEnd', function() {
            removeAnimations();
        }).bind('oanimationend', function() {
            removeAnimations();
        }).bind('msAnimationEnd', function() {
            removeAnimations();
        }).bind('animationend', function() {
            removeAnimations();
        });
    }
});