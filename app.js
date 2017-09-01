(function (angular) {
	'use strict';

	angular.module('ngApp', [])

	.controller('MainController', function ($scope, $http, $log) {
        $log.debug("Enter MainController");

        $scope.busy = false;
		$scope.link = "";

		$scope.getMetaDatas = function() {
			$scope.busy = true;
			if($scope.link.indexOf("&list=") != -1) {
				$scope.link = $scope.link.substring(0, $scope.link.indexOf("&list="));
			}
			$http.get("http://www.youtubeinmp3.com/fetch/?format=JSON&video=" + $scope.link)
				.then(function(response) {
					if(typeof response.data === 'string' || response.data instanceof String) {
					   $scope.protectedLink = response.data.replace('<meta http-equiv="refresh" content="0; url=', '').replace('" />', '');
					   $scope.error = "This video is protected, try the following link :";
				   } else if(response.data && response.data.link && response.data.title) {
 						$scope.music = response.data;
 						$scope.music.length = parseInt($scope.music.length/60) + ":" + $scope.music.length%60;
 						delete $scope.error;
 						delete $scope.protectedLink;
 					} else {
						$scope.error = "Does not look to be a valid youtube single video url ...";
 						delete $scope.protectedLink;
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
			delete $scope.protectedLink;
		};
	});

})(window.angular);
