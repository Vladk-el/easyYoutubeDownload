(function (angular) {
	'use strict';

	angular.module('ngApp', [])

	.controller('MainController', function ($scope, $http, $log) {
        $log.debug("Enter MainController");

    var init = function() {
      $scope.now = new Date();
      $scope.busy = false;
      $scope.link = "";
      $scope.location = {
        download: true,
        history: false
      };
      $scope.getHistory();
    };

		$scope.toggle = function(loc) {
		  Object.keys($scope.location).forEach(function(key) {
		    $scope.location[key] = key === loc ? true : false;
		  });
		};

		$scope.downloadOnServer = function() {
			$scope.busy = true;
			delete $scope.error;
			delete $scope.download;
			if($scope.link.indexOf("&list=") != -1) {
				$scope.link = $scope.link.substring(0, $scope.link.indexOf("&list="));
			}
			$http.post("/downloadOnServer", {url: $scope.link})
				.then(function(response) {
					$log.debug("response : ", response);
					$scope.busy = false;
					if(response.data.name) {
					  $scope.download = response.data.name;
            $scope.getHistory();
					} else {
            $scope.error = "An error occurred, please contact your administrator : "
             + response.data.error;
					}
				})
				.catch(function(reason) {
					$log.error("failed !", reason)
					$scope.error = reason.error;
					$scope.busy = false;
				});
		};

		$scope.getHistory = function() {
      $http.get("/history")
        .then(function(response) {
          $log.debug(response.data);
          $scope.history = response.data;
        })
        .catch(function(reason){
          $log.error("Error during retrieving history", reason);
        });
		};

		$scope.reset = function() {
			$scope.busy = false;
			delete $scope.link;
			delete $scope.error;
			delete $scope.download;
		};

		init();

	});

})(window.angular);
