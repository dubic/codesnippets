/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('welcomeCtrl', function($scope, $http, $rootScope, services, postsPath, $timeout) {
    $scope.snippets = [];
    $scope.viewSnippet = function(s) {
        $rootScope.route('snippet', {id: s.id});
    };
    $scope.mySnippets = function() {
        $scope.active = 0;
        $scope.loadingSnippets = true;
        $http.get(postsPath + '/load/mine?start=0&size=10').success(function(resp) {
            $scope.loadingSnippets = false;//hide loading..
            var i = 0;
            addSnippet(i, resp);
        }).error(function(data,status) {
            $rootScope.loadingSnippets = false;
        });
    };

    $scope.sharedWithMe = function() {
        $scope.active = 1;
//        $scope.loadingSnippets = true;
        $scope.snippets = [];
//        $http.get(postsPath + '/load/latest?start=0&size=10').success(function(resp) {
//            $scope.loadingSnippets = false;//hide loading..
//            var i = 0;
//            addSnippet(i, resp);
//
//        }).error(function(r) {
//            $rootScope.loadingSnippets = false;
//        });
    };
    
    $scope.recent = function() {
        $scope.active = 2;
        $scope.loadingSnippets = true;
        $scope.snippets = [];
        $http.get(postsPath + '/load/latest?start=0&size=10').success(function(resp) {
            $scope.loadingSnippets = false;//hide loading..
        var i = 0;
            addSnippet(i, resp);

        }).error(function(r) {
            $rootScope.loadingSnippets = false;
        });
    };

    function addSnippet(i, resp) {
        if (i < resp.length) {
            $timeout(function() {
                $scope.snippets.push(resp[i]);//display jokes for animation
                i++;
                addSnippet(i, resp);
            }, 200);
        }
    }

//    $timeout(function() {
    $scope.mySnippets();
//    }, 1000);
});
