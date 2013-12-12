/**
* angular.module defines a module for our applicatoin, 'searchApp', with
* an array of the modules searchApp depends on.
* elasticjs.service is a javascript client for elasticsearch
* ngSanitize angularjs module to sanitize HTML
*/
var searchApp = angular.module('searchApp', [
    'elasticjs.service',
    'ngRoute'
])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {
                controller: 'Herodotus',
                templateUrl: 'home.html'
            })
    }]);

