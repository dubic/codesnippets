/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('snipCtrl', function($scope, $http, services, $rootScope, $timeout,postsPath) {
    $scope.$on('newPostBroadcast', function(e, j) {
//        console.log(j);
        $scope.proverbs.unshift(j);
    });
    
    $scope.Snip = {};

    $scope.snip = function() {
       $scope.saving = true;
       $http.post(postsPath + '/save',$scope.Snip).success(function(resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                services.notify("Snippet has been saved");
                $scope.frm.$setPristine();
                $scope.Snip = {};
            }
            else if (resp.code === 500)
                services.notify("Unexpected Error");
            else if (resp.code === 403){
                services.notify("Login to continue");
                $rootScope.isAuthenticated = false;
                $rootScope.route('login');
            }
        }).error(function(r) {
            $scope.saving = false;
            services.notify("Service unavailable");
        });
    };
    
    


    $scope.reports = {};
    $scope.report = function() {
        console.log($scope.reports);
        $scope.selectedProverb.reporting = true;
        $timeout(function() {
            $scope.selectedProverb.reported = true;
            $scope.selectedProverb.reporting = false;
            $scope.reports = {};
            services.closeDialog('reportModal');
            services.notify("post reported successfully",$rootScope);
            
        }, 1000);
    };



    ///////INIT FUNCTIONS///////////
    
///////////////////////////
    

    $scope.openReport = function(index) {

        $scope.selectedProverb = $scope.proverbs[index];
        $scope.reportAlerts = [];
//            $scope.selectedJoke
        services.openDialog('reportModal');
    };



});
