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

var museums = [];


herodotusControllers.controller('SearchCtrl', [ '$scope', 'es',
    function SearchCtrl($scope, es) {	
    	
		initializeMap($scope, initMarkerArray, computeMaxBounds(initMarkerArray));
		
		$scope.searchByTerm = function() {
	    	searchByTerm(es, $scope);
	    };
	    $scope.searchById = function(id) {
	    	searchById(id, es, $scope);
	    };
	} 
]);


function searchByTerm(es, $scope) {
	es.search(getQuery($scope.queryTerm, $scope.currentPage), function (error, response) {
		$scope.museums = response.hits.hits;
		var markers = createMarkerArray($scope);
	    var maxbounds = computeMaxBounds(markers);
		initializeMap($scope, markers,maxbounds);
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
			size : 100,
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


function initializeMap($scope, markerArray, maxbounds) {
	
	angular.extend($scope, {
        maxbounds: maxbounds,
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


function computeMaxBounds(markersArray) {
    var maxLat=-100000;   var minLat=100000;
    var maxLon=-100000;   var minLon=100000;
	angular.forEach(markersArray, function(mark, index){
	    maxLat = maxLat > mark.lat ? maxLat : mark.lat;
	    minLat = minLat < mark.lat ? minLat : mark.lat;	    
  	    maxLon = maxLon > mark.lng ? maxLon : mark.lng;
	    minLon = minLon < mark.lng ? minLon : mark.lng;
	});
	
    var maxbounds = {
            southWest: {
                lat: minLat,
                lng: minLon
            },
            northEast: {
                lat: maxLat,
                lng: maxLon
            },
        };
    return maxbounds;
}


function createMarkerArray($scope) {
	var museums = $scope.museums;
	var markersArray = [];	
	var markerCnt = 0;	
	angular.forEach(museums, function(museum, index){
		if(museum._source.geoLocation != null) {
			//var marker = markerFactory(museum._source.geoLocation.latitude, museum._source.geoLocation.longitude, museum._source.title);
			var msg = messageTheme(museum._source);
    		var marker = markerFactory(museum._source.geoLocation.latitude, museum._source.geoLocation.longitude, msg);			
			markerCnt++;
			markersArray[markerCnt] = marker;
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
