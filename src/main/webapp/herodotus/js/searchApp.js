'use strict';

/**
* angular.module defines a module for our applicatoin, 'searchApp', with
* an array of the modules searchApp depends on.
* elasticjs.service is a javascript client for elasticsearch
* ngSanitize angularjs module to sanitize HTML
*/

var searchApp = angular.module('searchApp', [
  'ngRoute',
  'elasticsearch',
  'leaflet-directive',
  'herodotusControllers'
]);

searchApp.config(['$routeProvider', function($routeProvider){
    $routeProvider
    .when('/', {
        controller: 'HomeCtrl',
        templateUrl: 'home.html'
    })
    .when('/list', {
        controller: 'ListCtrl',
        templateUrl: 'list.html'
    })
}]);

searchApp.service('es', function (esFactory) {
	return esFactory({
		host: 'localhost:9200'
	});
});



