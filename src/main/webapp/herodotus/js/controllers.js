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


herodotusControllers.controller('SearchCtrl', [ '$scope', 'es',
    function SearchCtrl($scope, es) {
		console.log("SearchCtrl started... ");
		
		initializeMap($scope, initMarkerArray);
		
		$scope.searchByTerm = function() {
			currentQueryTerm = $scope.queryTerm;
	    	searchByTerm(es, $scope);
	    };
	    $scope.searchById = function(id) {
	    	searchById(id, es, $scope);
	    };
	} 
]);


function searchByTerm(es, $scope) {
	console.log("searchByTerm() started... ");
	es.search(getQuery($scope.queryTerm, $scope.currentPage), function (error, response) {
		$scope.museums = response.hits.hits;
		var markers = createMarkerArray($scope);
		initializeMap($scope, markers);
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
			markerCnt++;
			markersArray[markerCnt] = marker;
		} else {
			//console.log(museum._source.title + " has no geo-location info");
		}
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
