'use strict';

var herodotusControllers = angular.module('herodotusControllers',[]);
//markersArray["denhaagMarker"]= denhaag
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


herodotusControllers.controller('HomeCtrl', [ '$scope',
    function HomeCtrl($scope) {
		// Initialize map
		initializeMap($scope, initMarkerArray);
	} 
]);


herodotusControllers.controller('ListCtrl', [ '$scope', '$rootScope',
    function ListCtrl($scope, $rootScope) {
		$scope.museums = $rootScope.data.hits.hits;
		var markers = createMarkerArray($scope);
		initializeMap($scope, markers);
		//console.log($scope.museums);
		//console.log($scope.museums[0]._source.title);
		//console.log($scope.museums[0]._source.geoLocation.latitude);
		//$scope.title_facets = response.facets.title.terms;
	} 
]);

//EXPLANATION WHY IS CRASHED: http://stackoverflow.com/questions/8081701/i-keep-getting-uncaught-syntaxerror-unexpected-token-o
herodotusControllers.controller('SearchCtrl', [ '$scope', '$location', '$rootScope', 'es',
    function SearchCtrl($scope, $location, $rootScope, es) {
		// define our search function that will be called when a user
		// submits a search
	    $scope.search = function() {
	       	es.search(getQuery($scope.queryTerm), function (error, response) {
	       		$rootScope.data = response;
	    		$location.path("/list");
	    	});
	        $scope.queryTerm = "";
	    };
	} 
]);


function getQuery(queryTerm) {
	var queryString = { 
		index: 'herodotus',
		type: 'page',
		body: {
			from : 0, size : 30,
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
			console.log(museum._source.geoLocation.latitude + " : " + museum._source.geoLocation.longitude);
			markerCnt++;
			markersArray[markerCnt] = marker;
		} else {
			console.log(museum._source.title + " has no geo-location info");
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
