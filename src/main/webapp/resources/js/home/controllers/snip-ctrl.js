/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('snipCtrl', function ($scope, $http, services, $rootScope, $timeout, postsPath) {
    $scope.$on('newPostBroadcast', function (e, j) {
//        console.log(j);
        $scope.proverbs.unshift(j);
    });

    $scope.Snip = {};

    $scope.snip = function () {
        $scope.saving = true;
        $http.post(postsPath + '/save', $scope.Snip).success(function (resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                services.notify("Snippet has been saved");
                $scope.frm.$setPristine();
                $scope.Snip = {};
                $rootScope.route('welcome.snippets');
            }
            else if (resp.code === 500)
                services.notify("Unexpected Error");
            else if (resp.code === 403) {
                services.notify("Login to continue");
                $rootScope.isAuthenticated = false;
                $rootScope.route('login', {m: 1});
            }
        }).error(function (r) {
            $scope.saving = false;
            services.notify("Service unavailable");
        });
    };



    ///////INIT FUNCTIONS///////////

///////////////////////////


    $scope.openReport = function (index) {

        $scope.selectedProverb = $scope.proverbs[index];
        $scope.reportAlerts = [];
//            $scope.selectedJoke
        services.openDialog('reportModal');
    };



});
