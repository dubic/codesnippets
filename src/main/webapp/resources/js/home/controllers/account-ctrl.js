/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('accountCtrl', function ($scope, $http, $rootScope, $upload, postsPath, $timeout,settingsPath) {
    $scope.loadUser = function (broadcast) {
        $rootScope.loading = true;
        $http.get($rootScope.usersPath + '/current').success(function (resp) {
            $scope.user = resp;
            if (broadcast)
                $rootScope.$broadcast('authenticated', resp);
            $rootScope.loading = false;
        }).error(function (data, status) {
            $rootScope.loading = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.loadUser(false);

    $scope.update = function () {
        $scope.saving = true;
        $http.post(settingsPath + '/update', $scope.user).success(function (resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                $scope.user = resp;
                $rootScope.$broadcast('authenticated', resp);
                $scope.alerts = [{class: 'alert-info', msg: 'Account details has been updated'}];
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: 'Unexpeted server error'}];
            }
        }).error(function (data, status) {
            $scope.saving = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.upload = function (files) {
        if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                $scope.uploading = true;
                $scope.progressPercentage = 0;
                var file = files[i];
                $upload.upload({
                    url: $rootScope.usersPath + '/picture/upload',
//                    fields: {'username': $scope.username},
                    file: file
                }).progress(function (evt) {
                    $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
//                    console.log('progress: ' + $scope.progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    $rootScope.profilePixChanged(data);
                    $scope.uploading = false;
//                    console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
                }).error(function (data, status) {
                    $scope.uploading = false;
                    if (status === 403)
                        $rootScope.sessionTimedOut();
                });
            }
        }
    };
    
    $scope.$on('profile.pix.changed', function (evt,pic) {
        $scope.user.picture = pic; 
//        console.log('acct pix broadcast : '+$scope.pcount);
    });


});