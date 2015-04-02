/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('editsnCtrl', function($scope, $http, services, $rootScope, $timeout, postsPath, $stateParams) {
//    console.log($stateParams);
    if (angular.isUndefined($stateParams.id)) {
        console.log('no snippet id passed.So unlikely');
        return;
    }
    $scope.loading = true;
    $http.get(postsPath + '/snippet/view/' + $stateParams.id + '?edit=true').success(function(resp) {
        $scope.loading = false;
        $scope.Snip = resp;
        console.log($rootScope.langs.indexOf($scope.Snip.lang));
    }).error(function(r) {
        $scope.loading = false;
    });

    $scope.save = function() {
        $scope.saving = true;
        var s = {
            id: $scope.Snip.id,
            title: $scope.Snip.title,
            desc: $scope.Snip.desc,
            deps: $scope.Snip.deps.join(','),
            lang: $scope.Snip.lang,
            code: $scope.Snip.code
        };

        $http.post(postsPath + '/edit', s).success(function(resp) {
            $scope.saving = false;
            if(resp.code === 0){
                services.notify("Snippet has been saved");
            }
            else if (resp.code === 500)
                services.notify("Unexpected Error");
            else if (resp.code === 403){
                services.notify("Login to continue");
                $rootScope.isAuthenticated = false;
                $rootScope.route('login');
            }

        }).error(function(data, status) {
            $scope.saving = false;
            services.notify("Service unavailable");
        });
    };

});