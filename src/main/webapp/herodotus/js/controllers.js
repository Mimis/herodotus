'use strict';


var herodotusControllers = angular.module('herodotusControllers',[]);

var athens =  {
        lat: 37.96,
        lng: 23.71,
        message: "Athens",
        focus: true,
        draggable: false	
}

    var regions = {
        london: {
            southWest: {
                lat: 51.50211782162702,
                lng: -0.14428138732910156
            },
            northEast: {
                lat: 51.51280224425956,
                lng: -0.11681556701660155
            },

        },
        lisbon: {
            southWest: {
                lat: 38.700247900602726,
                lng: -9.165430068969727
            },
            northEast: {
                lat: 38.72703673982525,
                lng: -9.110498428344725
            }
        },
        warszawa: {
            southWest: {
                lat: 52.14823737817847,
                lng: 20.793685913085934
            },
            northEast: {
                lat: 52.31645452105213,
                lng: 21.233139038085938
            }
        }
    };

var initMarkerArray = {
    athensMarker: athens
}

var currentQueryTerm = "";
var museums = [];


herodotusControllers.controller('SearchCtrl', [ '$scope', 'es',
    function SearchCtrl($scope, es) {		
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
    var maxLat=-100000;
    var minLat=100000;
    var maxLon=-100000;
    var minLon=100000;
	angular.forEach(markersArray, function(mark, index){
	    maxLat = maxLat > mark.lat ? maxLat : mark.lat;
	    minLat = minLat < mark.lat ? minLat : mark.lat;	    
  	    maxLon = maxLon > mark.lng ? maxLon : mark.lng;
	    minLon = minLon < mark.lng ? minLon : mark.lng;
	    
	    console.log("mark.lat:"+mark.lat+"\tmark.lng:"+mark.lng);
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
    console.log("maxbounds.southWest.lat:"+maxbounds.southWest.lat+"maxbounds.southWest.lng:"+maxbounds.southWest.lng);
    console.log("maxbounds.northEast.lat:"+maxbounds.northEast.lat+"maxbounds.northEast.lng:"+maxbounds.northEast.lng);

    return maxbounds;
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
