var ams = angular.module('ams', []);

var baseApi = contextPath + '/api';
var validateApi = '/validate';
var cityStateApi = '/citystate';
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

ams.controller('ApiController', function($scope, $http) {
    $scope.validateUrl = baseApi + validateApi + "?detail=true&";
    $scope.cityStateUrl = baseApi + cityStateApi + "?detail=true&";
    $scope.inquiryUrl = baseApi + inquiryApi + "?detail=true&";
    $scope.$responseContainer = $('#api-response-container');

    $scope.validateInput = {
        addr1 : '',
        addr2 : '',
        city  : '',
        state : 'NY',
        zip5  : '',
        zip4  : ''
    };

    $scope.cityStateInput = {
        zip5  : ''
    };

    $scope.inquiryInput = {
        zip5  : '',
        zip4  : ''
    };

    $scope.activeRequestView = 'validate';
    $scope.activeResponseView = '';

    $scope.sendValidateRequest = function() {
        var url = this.validateUrl + $.param($scope.validateInput);
        $http.get(url)
            .success(function(data) {
                $scope.activeResponseView = 'validate';
                $scope.$broadcast('validateResponse', data);
            })
            .error(function() {
                $scope.alertDefaultError();
            });
    };

    $scope.sendCityStateRequest = function() {
        var url = this.cityStateUrl + $.param($scope.cityStateInput);
        $http.get(url)
            .success(function(data) {
                $scope.activeResponseView = 'cityState';
                $scope.$broadcast('cityStateResponse', data);
            })
            .error(function() {
                $scope.alertDefaultError();
            });
    };

    $scope.sendInquiryRequest = function() {
        var url = this.inquiryUrl + $.param($scope.inquiryInput);
        $http.get(url)
            .success(function(data) {
                $scope.activeResponseView = 'inquiry';
                $scope.$broadcast('validateResponse', data);
            })
            .error(function() {
                $scope.alertDefaultError();
            });
    };

    $scope.responseVisible = function() {
        return this.activeResponseView != null && this.activeResponseView != '';
    };

    $scope.alertDefaultError = function() {
        alert('Unable to get response. The server may be temporarily offline.');
    };

    $scope.replayAnimation = function() {
        window.scrollTo(0, 0);
        $scope.$responseContainer.addClass('bounce-in-anim');
    }
});

ams.controller('ValidateResponseController', function($scope, $http, $filter) {
    $scope.statusClass = '';

    $scope.$on('validateResponse', function(event, data) {
        $scope.result = data;
        if ($scope.result != null) {
            $scope.statusClass = $filter('statusClassFilter')($scope.result.status.code);
        }
        $scope.replayAnimation();
    });
});

ams.controller('CityStateResponseController', function($scope, $http, $filter) {
    $scope.statusClass = '';

    $scope.$on('cityStateResponse', function(event, data) {
        $scope.result = data;
        if ($scope.result != null) {
            $scope.statusClass = $filter('statusClassFilter')($scope.result.status.code);
        }
        $scope.replayAnimation();
    });
});

ams.controller('InquiryResponseController', function($scope, $http, $filter) {
    $scope.statusClass = '';

    $scope.$on('validateResponse', function(event, data) {
        $scope.result = data;
        if ($scope.result != null) {
            $scope.statusClass = $filter('statusClassFilter')($scope.result.status.code);
        }
        $scope.replayAnimation();
    });
});

$(document).ready(function(){

    setUpAnimations();
    setUpjQueryUi();

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

    /**
     * All elements that use jQuery UI are initialized here.
     */
    function setUpjQueryUi() {
        $('#method-selection').buttonset();
    }
});