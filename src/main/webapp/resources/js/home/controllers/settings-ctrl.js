/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('settingsCtrl', function ($scope, $http, $rootScope, services, postsPath, $timeout) {
    $scope.snippets = [];




});
ctrls.controller('pwordCtrl', function ($scope, $http, $rootScope, services, settingsPath, $timeout) {
    $scope.snippets = [];

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
ctrls.controller('notifCtrl', function ($scope, $http, $rootScope, services, settingsPath, $timeout) {
    $scope.htttml='<p>welcome to <strong>java</strong></p>';
    
    $scope.loadNotifications = function () {
        $rootScope.loading = true;
        $http.get(settingsPath + '/notifcations/load').success(function (resp) {
            $scope.notifications = resp;
            $rootScope.loading = false;
        }).error(function (data, status) {
            $rootScope.loading = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.loadNotifications();

    $scope.update = function () {
        $scope.saving= true;
        $http.post(settingsPath+ '/notifcations/update',$scope.notifications).success(function (resp) {
            $scope.notifications = resp;
            $scope.saving= false;
            if (resp.code === 0) {
                $scope.alerts = [{class: 'alert-success', msg: 'Notifications updated!'}];
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: resp.msg}];
            }
        }).error(function (data, status) {
            $scope.saving= false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };


});