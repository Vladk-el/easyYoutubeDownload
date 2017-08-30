(function (angular) {
	'use strict';

	angular.module('ngApp', [])

	.controller('MainController', function ($scope, $http, $log) {
        $log.debug("Enter MainController");

        $scope.busy = false;
		$scope.link = "";

		$scope.getMetaDatas = function() {
			$scope.busy = true;
			$http.get("http://www.youtubeinmp3.com/fetch/?format=JSON&video=" + $scope.link)
				.then(function(response) {
					if(response.data && response.data.link) {
						$scope.music = response.data;
						$scope.music.length = parseInt($scope.music.length/60) + ":" + $scope.music.length%60;
						delete $scope.error;
					} else {
						$scope.error = "Does not look to be a valid youtube single video url ...";
					}
					$scope.busy = false;
				})
				.catch(function(reason) {
					$log.error("failed !", reason)
					$scope.error = "Does not look to be a valid youtube single video url ...";
					$scope.busy = false;
				});
		};

		$scope.reset = function() {
			$scope.busy = false;
			delete $scope.link;
			delete $scope.music;
			delete $scope.error;
		};
	});

})(window.angular);
