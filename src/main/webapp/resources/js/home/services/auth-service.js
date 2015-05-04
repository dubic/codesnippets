/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.factory('authService', function ($http, $rootScope, $q, $stateParams, services, postsPath) {
    var factory = {};
    factory.authenticate = function (dest) {
        var deferred = $q.defer();
        $http.get($rootScope.usersPath + '/authenticated').success(function (resp) {
            if (resp.authenticated === false) {
                console.log('source - '+$rootScope.previous+' & dest - '+dest);
//                if ($rootScope.previous === dest) {
//                    console.log('nothing done');
////                    deferred.resolve();
//                } else {
//                    deferred.resolve();
                    $rootScope.signedOut();
                    $rootScope.previous = dest;
//                    deferred.resolve();
//                }
            } else {
                deferred.resolve();
            }
        }).error(function (r) {
            console.log('error in connection');
            deferred.resolve();
        });
        return deferred.promise;
    };

    factory.canEditSnippet = function (dest) {
        var deferred = $q.defer();
        console.log('canEditSnippet');
        if (angular.isUndefined($stateParams.id)) {
            services.notify('Unauthorized operation');
//            deferred.resolve();
            $rootScope.route('welcome.snippets');
//            $rootScope.signedOut();

            console.log('Unauthorized operation routing');

        } else {
            $http.get(postsPath + '/canedit/' + $stateParams.id).success(function (resp) {
                if (resp.code === 500) {
                    services.notify(resp.msg);
                    $rootScope.route('welcome.snippets');
                } else if (resp.code === 404) {
                    services.notify('You cannot edit this snippet');
                    $rootScope.route('welcome.snippets');
                } else {
                    deferred.resolve();
                }
            }).error(function (data, status) {
//            deferred.resolve();
                if (status === 403)
                    $rootScope.sessionTimedOut();

            });
        }

        return deferred.promise;
    };
    factory.isAuthenticated = false;
    return factory;
});
// we will return a promise .


//this is just to keep a pointer to parent scope from within promise scope.


//Checking if permission object(list of roles for logged in user) is already filled from service
//if (this.permissionModel.isPermissionLoaded) {
//    //Check if the current user has required role to access the route
//    this.getPermission(this.permissionModel, roleCollection, deferred);
//} else {
//    //if permission is not obtained yet, we will get it from  server.
//    // 'api/permissionService' is the path of server web service , used for this example.
//
//    $resource('/api/permissionService').get().$promise.then(function(response) {
//        //when server service responds then we will fill the permission object
//        parentPointer.permissionModel.permission = response;
//
//        //Indicator is set to true that permission object is filled and 
//        //can be re-used for subsequent route request for the session of the user
//        parentPointer.permissionModel.isPermissionLoaded = true;
//
//        //Check if the current user has required role to access the route
//        parentPointer.getPermission(parentPointer.permissionModel, roleCollection, deferred);
//    });
//}
            