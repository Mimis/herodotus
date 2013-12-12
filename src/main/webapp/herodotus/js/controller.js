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
    	console.log($scope.queryTerm);
    	
    	testQuery = ejs.MatchQuery('_all', $scope.queryTerm).toString();
    	console.log(testQuery);
    	
        $scope.results = request
            .query(ejs.QueryStringQuery($scope.queryTerm || '*'))
        	//.query(ejs.TermQuery('title', $scope.queryTerm))
            .fields(['title', 'content'])
            .doSearch(function (data) {
            	alert(data.hits.hits[0].fields.title);
            	//alert(data.toSource());
            });
            
        console.log("length :" + $scope.results.toString());
        $scope.queryTerm = "";
    };
});