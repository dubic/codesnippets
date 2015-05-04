/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('emailCtrl', function ($scope, $http, $rootScope, services, settingsPath, $timeout) {
    $scope.validateEmail = true;
    $scope.saveEmail = true;

    $scope.loadUser = function () {
        $rootScope.loading = true;
        $http.get($rootScope.usersPath + '/current').success(function (resp) {
            $scope.user = resp;
            $rootScope.loading = false;
        }).error(function (data, status) {
            $rootScope.loading = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.loadUser();


    $scope.sendPasscode = function () {
        $scope.validating = true;
        $http.get(settingsPath + '/validate-email?p=' + $scope.newEmail).success(function (resp) {
            $scope.validating = false;
            if (resp.code === 0) {
                $scope.saveEmail = false;
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: resp.msg}];
            }

        }).error(function (data, status) {
            $scope.validating = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.updateEmail = function () {
        $scope.saving = true;
        $http.post(settingsPath + '/change-email', {pcode: $scope.passcode, pword: $scope.password}).success(function (resp) {
            $scope.saving = false;
            if (resp.code === 0) {
                $scope.alerts = [{class: 'alert-success', msg: "Email updated successfully"}];
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: resp.msg}];
            }

        }).error(function (data, status) {
            $scope.saving = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.emailSetting = function () {
//        alert($scope.user.showEmail);
        $scope.settingSaving = true;
        $http.post(settingsPath + '/email-settings', {show: $scope.user.showEmail}).success(function (resp) {
            if (resp.code === 0) {
                $scope.alerts = [{class: 'alert-success', msg: "update saved successfully"}];
            } else {
                $scope.alerts = [{class: 'alert-danger', msg: resp.msg}];
            }
            $scope.settingSaving = false;
        }).error(function (data, status) {
            $rootScope.loading = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

});