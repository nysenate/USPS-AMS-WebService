var ams = angular.module('ams', []);
var baseApi = contextPath + '/api/';

ams.filter('statusClassFilter', function(){
    return function(shortDesc) {
        switch (shortDesc) {
            case "No Match" : return 'empty-indication';
            case "Multiple Matches" :
            case "Default Match" : return 'warning-indication';
            case "Exact Match" : return 'success-indication';
            default : return 'error-indication';
        }
    };
});

ams.filter('foundFilter', function(){
    return function(success) {
        return (success) ? 'success-indication' : 'error-indication';
    }
});

ams.controller('ApiController', function($scope, $http) {
    $scope.validateUrl = baseApi + 'validate?';
    $scope.cityStateUrl = baseApi + 'citystate?';
    $scope.inquiryUrl = baseApi + 'inquiry?';
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
            $scope.statusClass = $filter('statusClassFilter')($scope.result.status.shortDesc);
        }
        $scope.replayAnimation();
    });
});

ams.controller('CityStateResponseController', function($scope, $http, $filter) {
    $scope.statusClass = '';
    $scope.messageResponse= '';
    $scope.message= '';

    $scope.$on('cityStateResponse', function(event, data) {
        $scope.result = data;
        if ($scope.result != null) {
            $scope.statusClass = $filter('foundFilter')($scope.result.success);
            $scope.messageResponse = ($scope.result.success) ? 'Success' : 'Failure';
            $scope.message = ($scope.result.success) ?
                'The zip code was matched to a city and state.' : 'The zip code was not matched to a city and state.';
        }
        $scope.replayAnimation();
    });
});

ams.controller('InquiryResponseController', function($scope, $http, $filter) {
    $scope.statusClass = '';

    $scope.$on('validateResponse', function(event, data) {
        $scope.result = data;
        if ($scope.result != null) {
            $scope.statusClass = $filter('statusClassFilter')($scope.result.status.shortDesc);
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
