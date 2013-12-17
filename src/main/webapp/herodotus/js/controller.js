

searchApp.controller('Herodotus', function ($scope, ejsResource) {
	// point to your ElasticSearch server
    var ejs = ejsResource('http://localhost:9200');
    var index = 'herodotus';
    var type = 'page';
    
    // setup the indices and types to search across
    var request = ejs.Request()
        .indices(index)
        .types(type);
    
    // define our search function that will be called when a user
    // submits a search
    $scope.search = function() {
    	    	
        $scope.results = request
        .query(ejs.QueryStringQuery($scope.queryTerm || '*'))
        .facet(ejs.TermsFacet('title').field('title'))
        .fields(['id','url','title', 'content', 'categories', 'outlinks'])

                
        .doSearch(function (data) {
           	$scope.museums = data.hits.hits;
           	$scope.hits = data.hits;
           	$scope.title_facets = data.facets.title.terms;
           	//alert(data.hits.hits[0].fields.title);
          	//alert(data.toSource());
        });


        $scope.queryTerm = "";
    };
});