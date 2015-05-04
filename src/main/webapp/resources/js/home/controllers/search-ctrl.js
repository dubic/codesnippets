/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('searchCtrl', function ($scope, $http, $rootScope, searchPath, $stateParams) {

    console.log($stateParams);
    $scope.keyword = $stateParams.q;
    $scope.snipsize = 5;
    $scope.snippage = 1;
    $scope.searching = true;
    $scope.searchSnippets = function () {
        $http.post(searchPath + '/results/snippets?q=' + $stateParams.q + '&size=' + $scope.snipsize + '&start=' + ($scope.snippage - 1) * $scope.snipsize).success(function (resp) {
            $scope.searching = false;
            $scope.snipcount = resp.snipcount;
            $scope.snippets = resp.snippets;
        });
    };

    $scope.snipChanged = function () {
        $scope.searchSnippets();
    };


    $scope.userssize = 5;
    $scope.userspage = 1;
    $scope.searchUsers = function () {
        $http.post(searchPath + '/results/users?q=' + $stateParams.q + '&size=' + $scope.userssize + '&start=' + ($scope.userspage - 1) * $scope.userssize).success(function (resp) {
            $scope.searching = false;
            $scope.userscount = resp.userscount;
            $scope.persons = resp.users;
        });
    };

    $scope.searchSnippets();
    $scope.searchUsers();
});
ctrls.controller('pwordCtrl', function ($scope, $http, $rootScope, services, settingsPath, $timeout) {

    $scope.savePassword = function () {
        if (confirm('update password?') === false)
            return;
        $scope.saving = true;
        $http.post(settingsPath + '/change-password', {current: $scope.current, newpword: $scope.newpword}).success(function (resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                $scope.alerts = [{class: 'alert-success', msg: 'Password updated!'}];
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: resp.msg}];
            }
        }).error(function (data, status) {
            $rootScope.loading = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };




});
