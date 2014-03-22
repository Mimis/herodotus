
searchApp.controller('Herodotus',  function ($scope, es) {
	
	angular.extend($scope, {
		amsterdamCenter: {
            lat: 52.37,
            lng: 4.89,
            zoom: 12
        },
        markers: {
        	amsterdamMarker: {
                lat: 52.37,
                lng: 4.89,
                message: "Where are the museums?",
                focus: true,
                draggable: false
            }
        },
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
    
    // define our search function that will be called when a user
    // submits a search
    $scope.search = function() {
    	
       	es.search({
    		index: 'herodotus',
    		type: 'page',
    		body: {
    		    query: {
    		      match: {
    		        title: $scope.queryTerm
    		      }
    		    },
       			fields: ['id', 'url', 'title', 'content']
    		}
    	}, function (error, response) {
    		var responseArray = JSON.parse(response)
    		console.log(responseArray);
    		$scope.museums = responseArray.hits.hits;
    		$scope.hits = responseArray.hits.total;
    		console.log($scope.hits);
    		//$scope.title_facets = response.facets.title.terms;
    	});
    	
    	
        $scope.queryTerm = "";
    };
});