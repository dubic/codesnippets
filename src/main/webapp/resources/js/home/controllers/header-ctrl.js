/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('headerCtrl', function ($scope, $http, $timeout, $rootScope, notifPath) {

    $scope.$on('authenticated', function (evt, user) {
        console.log(user);
        $scope.userDetails = user;
        $scope.isAuthenticated = true;
        $scope.notifUnreadCount();
    });

    $scope.$on('signed.out', function (evt) {
        $scope.isAuthenticated = false;
    });

    $scope.$on('timed.out', function (evt) {
        $scope.isAuthenticated = false;
    });

    $scope.$on('profile.pix.changed', function (evt, pic) {
//        $rootScope.userDetails.picture = pic;
        console.log('header pix broadcast : ' + pic);
        $scope.loadUser();
    });

    $scope.loadUser = function () {
        $http.get($rootScope.usersPath + '/current').success(function (resp) {
            $scope.heading = true;
            if (resp.code === 0) {
                $scope.userDetails = resp;
                $rootScope.isAuthenticated = true;
                $scope.heading = true;
            } else {
                $rootScope.isAuthenticated = false;
            }
        });
    };

    $scope.Notifications = {};
    $scope.notifUnreadCount = function () {
        $http.get(notifPath + '/unread/count').success(function (resp) {
            $scope.Notifications.unreadCount = resp.count;
            $scope.Notifications.loaded = false;
        });
    };

    $scope.Notifications.loaded = false;
    $scope.isOpen = function () {
//      alert('is open wow'); 
        if ($scope.Notifications.loaded)
            return;
        $scope.notifLoading = true;
        $http.get(notifPath + '/unread/list').success(function (resp) {
            $scope.notifLoading = false;
            $scope.Notifications.list = resp.notifs;
            $scope.Notifications.unreadCount = 0;
            $scope.Notifications.loaded = true;
        });
    };

    $scope.notifUnreadCount();

    $scope.loadUser();

    $scope.runSearch = function () {
        $rootScope.route('search', {q: $scope.Search.keyword});
    };


    $scope.testDialog = function () {
        $scope.insearch = false;
    };

    $scope.logout = function () {
        $http.get('logout').success(function () {

        });
    };
});