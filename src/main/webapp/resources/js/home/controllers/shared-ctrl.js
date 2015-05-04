/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('sharedCtrl', function ($scope, $http, services, $rootScope, postsPath, $timeout, $state) {

    $scope.shared = function () {
        $scope.active = 1;
        $scope.loadingSnippets = true;
        $scope.snippets = [];
        $http.get(postsPath + '/load/shared?start=0&size=10').success(function (resp) {
            $scope.loadingSnippets = false;//hide loading..
            var i = 0;
            addSnippet(i, resp);

        }).error(function (data, status) {
            $rootScope.loadingSnippets = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.shared();

    function addSnippet(i, resp) {
        if (i < resp.length) {
            $timeout(function () {
                $scope.snippets.push(resp[i]);//display jokes for animation
                i++;
                addSnippet(i, resp);
            }, 200);
        }
    }

    $scope.viewSnippet = function (s) {
        $rootScope.route('snippet', {id: s.id});
    };

});