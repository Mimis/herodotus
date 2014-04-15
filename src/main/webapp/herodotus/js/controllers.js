'use strict';

var herodotusControllers = angular.module('herodotusControllers',[]);

var athens =  {
        lat: 37.96,
        lng: 23.71,
        message: "Athens",
        focus: true,
        draggable: false	
}


var initMarkerArray = {
    athensMarker: athens
}

var scrollStep = 20;
var currentQueryTerm = "";
var museums = [];


herodotusControllers.controller('HomeCtrl', [ '$scope',
    function HomeCtrl($scope) {
		console.log("HomeCtrl started... ");
		initializeMap($scope, initMarkerArray);
	} 
]);


herodotusControllers.controller('SearchCtrl', [ '$scope', '$location', '$route', '$rootScope', 'es',
    function SearchCtrl($scope, $location, $route, $rootScope, es) {
		console.log("SearchCtrl started... ");
		$scope.currentPage = 1;
		$scope.busy = false;
		$scope.searchByTerm = function() {
			currentQueryTerm = $scope.queryTerm;
	    	searchByTerm(es, $scope, $route, $rootScope, $location);
	    };
	    $scope.searchById = function(id) {
	    	searchById(id, es, $scope, $rootScope, $location);
	    };
	    $scope.nextPage = function() {
	    	console.log("nextPage() started... ");
	    	$scope.busy = true;
	    	$scope.currentPage = $scope.currentPage + 1;
	    	$scope.queryTerm = currentQueryTerm;
	    	
	    	console.log("$scope.currentPage " + $scope.currentPage);
	    	console.log("$scope.queryTerm " + $scope.queryTerm);
	    	searchByTerm(es, $scope, $route, $rootScope, $location);
	    	$scope.busy = false;
	    }
	} 
]);


herodotusControllers.controller('ListCtrl', [ '$scope', '$rootScope',
    function ListCtrl($scope, $rootScope) {
		console.log("ListCtrl started... ");
		$scope.searchByTerm = function() {
	    	searchByTerm(es, $scope, $rootScope, $location);
	    };
		$scope.museums = $rootScope.data.hits.hits;
		var markers = createMarkerArray($scope);
		initializeMap($scope, markers);
  	} 
]);


herodotusControllers.controller('DetailCtrl', [ '$scope', '$rootScope', 'es',
    function DetailCtrl($scope, $rootScope, es) {
		console.log("DetailCtrl started... ");
		// We need an array to pass in the createMarkerArray
   		$scope.museums = $rootScope.data.hits.hits;
   		// Only one museum
   		$scope.museum = $scope.museums[0];
   		var markers = createMarkerArray($scope);
		initializeMap($scope, markers);
        $scope.queryTerm = "";
	}
]);


function searchByTerm(es, $scope, $route, $rootScope, $location) {
	console.log("searchByTerm() started... ");
	console.log("$scope.currentPage " + $scope.currentPage);
	es.search(getQuery($scope.queryTerm, $scope.currentPage), function (error, response) {
   		//var responseArray = JSON.parse(response);
		//$rootScope.data = responseArray;
		$rootScope.data = response;
		$scope.museums = $rootScope.data.hits.hits;
		//museums.push($rootScope.data.hits.hits);
		//$scope.museums = museums;
		var currentPath = $location.path();
		if(currentPath == "/list") {
			$route.reload();
		} else {
			$location.path("/list");
		}
		
	});
    $scope.queryTerm = "";
}


function searchById(id, es, $scope, $rootScope, $location) {
	es.search(getByIdQuery(id), function (error, response) {
   		//var responseArray = JSON.parse(response);
   		//$rootScope.data = responseArray;
   		$rootScope.data = response;
		$location.path("/" + id);
	});
    $scope.queryTerm = "";
}


function getByIdQuery(id) {
	var queryString = { 
		index: 'herodotus',
		type: 'page',
		body: {
			query: {
		      match: {
		        id: id
		      }
		    }
		}
	};
	return queryString;
}

function getQuery(queryTerm, currentPage) {
	var queryString = { 
		index: 'herodotus',
		type: 'page',
		body: {
			from : ((currentPage - 1) * scrollStep), size : (currentPage * scrollStep),
		    query: {
		      match: {
		        title: queryTerm
		      }
		    }//,
   			//fields: {'id', 'url', 'title', 'summary', 'geoLocation.longitude', 'geoLocation.latitude'}
		}
	};
	return queryString;
}


function initializeMap($scope, markerArray) {
	
	angular.extend($scope, {
		athensCenter: {
			lat: 37.96,
	        lng: 23.71,
            zoom: 6
        },
        markers: markerArray,
        layers: {
            baselayers: {
                osm: {
                    name: 'OpenStreetMap',
                    url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                    type: 'xyz'
                },
                cloudmade2: {
                    name: 'Cloudmade Tourist',
                    type: 'xyz',
                    url: 'http://{s}.tile.cloudmade.com/{key}/{styleId}/256/{z}/{x}/{y}.png',
                    layerParams: {
                        key: '007b9471b4c74da4a6ec7ff43552b16f',
                        styleId: 7
                    }
                }
            }
        },
        defaults: {
            scrollWheelZoom: false
        }
    });
}

function createMarkerArray($scope) {
	var museums = $scope.museums;
	var markersArray = [];
	
	var markerCnt = 0;
	
	angular.forEach(museums, function(museum, index){
		if(museum._source.geoLocation != null) {
			var marker = markerFactory(museum._source.geoLocation.latitude, museum._source.geoLocation.longitude, museum._source.title);
			//console.log(museum._source.geoLocation.latitude + " : " + museum._source.geoLocation.longitude);
			markerCnt++;
			markersArray[markerCnt] = marker;
		} else {
			//console.log(museum._source.title + " has no geo-location info");
		}
		
		//var marker = markerFactory(museum._source.geoLocation.latitute, museum._source.geoLocation.longitude, museum._source.title);
		//markerCnt++;
		//marker = {markerCnt : marker};
		//console.log(marker);
		//markersArray.push(marker);
	});
	
	return markersArray;
	
}

function markerFactory(latitude, longitude, msg) {
	var marker = {
		lat: latitude,
		lng: longitude,
		message: msg,
	    focus: true,
	    draggable: false
	};
	
	return marker;
}
