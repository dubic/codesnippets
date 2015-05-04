/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('editsnCtrl', function ($scope, $http, services, $rootScope, $timeout, postsPath, $stateParams) {
//    console.log($stateParams);
    if (angular.isUndefined($stateParams.id)) {
        console.log('no snippet id passed.So unlikely');
        return;
    }

    $scope.loading = true;
    $http.get(postsPath + '/canedit/' + $stateParams.id).success(function (resp) {
        $scope.loading = false;
        if (resp.code === 500) {
            services.notify(resp.msg);
            $rootScope.route('home');
        } else if (resp.code === 404) {
            services.notify('You cannot edit this snippet');
            $rootScope.route('home');
        } else {
            $scope.loadSnips();
        }
    }).error(function (data, status) {
        $scope.loading = true;
        if (status === 403)
            $rootScope.sessionTimedOut();

    });

    $scope.loadSnips = function () {
        $scope.loading = true;
        $http.get(postsPath + '/snippet/view/' + $stateParams.id + '?edit=true').success(function (resp) {
            $scope.loading = false;
            resp.deps = resp.deps === undefined ? undefined : resp.deps.join();
            $scope.Snip = resp;
            console.log($rootScope.langs.indexOf($scope.Snip.lang));
        }).error(function (r) {
            $scope.loading = false;
        });
    };

    $scope.chek = function () {
        alert($scope.Snip.lang);
    };


    $scope.save = function () {
        $scope.saving = true;
        var s = {
            id: $scope.Snip.id,
            title: $scope.Snip.title,
            deps: $scope.Snip.deps,
            desc: $scope.Snip.desc,
            lang: $scope.Snip.lang,
            code: $scope.Snip.code
        };
//        s.deps = $scope.Snip.deps === undefined ? undefined : $scope.Snip.deps.join();

        $http.post(postsPath + '/edit', s).success(function (resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                services.notify("Snippet has been saved");
                $rootScope.route('snippet', {id: $scope.Snip.id});
            }
            else if (resp.code === 500)
                services.notify("Unexpected Error");
            else if (resp.code === 403) {
                services.notify("Login to continue");
                $rootScope.isAuthenticated = false;
                $rootScope.route('login');
            }

        }).error(function (data, status) {
            $scope.saving = false;
            services.notify("Service unavailable");
        });
    };

});