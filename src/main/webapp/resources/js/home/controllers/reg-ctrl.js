/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('loginCtrl', function($scope, $http, $rootScope, services, usersPath) {
    $scope.login = function() {
        $scope.loading = true;
        $http.post('login?' + $.param($scope.user)).success(function(resp) {
            $scope.loading = false;
//            $rootScope.isAuthenticated = true;

            if (angular.isUndefined($rootScope.previous) || $rootScope.previous === '')
                $rootScope.route('welcome.snippets');
            else
                $rootScope.route($rootScope.previous);

            $rootScope.getCurrentUser();
        }).error(function(resp, e) {
            console.log(e);
            services.isAuthenticated = false;
            if (e === 404)
                $scope.alerts = services.buildAlerts([{code: e, msg: 'email or password wrong'}]);
            else if (e === 501)
                $scope.alerts = services.buildAlerts([{code: e, msg: 'your account has not been activated'}]);
        });
    };
})

        .controller('signupCtrl', function($scope, $http, regPath, services, $rootScope, $timeout) {
            $scope.inact = function() {
                $rootScope.route('inactive', {id: 1});
            };

            $scope.error = true;
            $timeout(function() {
                $scope.error = false;
                $timeout(function() {
                    $scope.error = true;
                }, 2000);
            }, 2000);
            $scope.signup = function() {
//            console.log($scope.user);
                $scope.loading = true;
                $http.post(regPath + '/signup', $scope.user).success(function(resp) {
                    $scope.alerts = services.buildAlerts([{code: resp.code, msg: resp.msg}]);
                    $scope.loading = false;
                    if (resp.code === 0) {
                        $rootScope.navigate('inactive');
                    }
                });
            };
        })
        .controller('inactiveCtrl', function($scope, $http, services, $rootScope, $stateParams) {
            $scope.pageMsg = 'Your account has been created successfully. You need to activate your account through the link sent to your email.';
//            $scope.boardMsg = 'ttt';
//            $scope.msgtype = 'msg-board-success';
//            console.log($stateParams);
            $http.get($rootScope.usersPath + '/user/' + $stateParams.id).success(function(user) {
                $scope.email = user.email;
            });

            $scope.resend = function() {
                $scope.resending = true;
                $http.post($rootScope.regPath + '/resend/' + $stateParams.id).success(function(resp) {
                    $scope.resending = false;
                    if (resp.code === 0) {
                        $scope.msgtype = 'msg-board-success';
                        $scope.boardMsg = 'Activation email resent';
                    }
                    else {
                        $scope.msgtype = 'msg-board-error';
                        $scope.boardMsg = resp.msg;
                    }
                }).error(function(data, status) {
                    $scope.resending = false;
                    $scope.msgtype = 'msg-board-error';
                    $scope.boardMsg = 'Connection Error';
                });
            };
        })

        .controller('fpasswordCtrl', function($scope, $http, usersPath, services, regPath) {
            $scope.isCollapsed = true;
            $scope.loading = false;

            $scope.forgetToken = function() {
                $scope.loading = true;
                $http.get(regPath + '/password/forgot?email=' + $scope.User.email).success(function(resp) {
                    $scope.alerts = services.buildAlerts([{code: resp.code, msg: resp.msg}]);
                    $scope.loading = false;

                    if (resp.code === 0) {
                        $scope.isCollapsed = false;
                    }
                });
            };

            $scope.resetPassword = function() {
                if (confirm('reset password?') === false)
                    return;
                $scope.loading = true;
                $http.post(regPath + '/password/reset', $scope.User).success(function(resp) {
                    $scope.loading = false;
                    $scope.alerts = services.buildAlerts([{code: resp.code, msg: resp.msg}]);
                    if (resp.code === 0)
                        $scope.isCollapsed = true;
                });
            };
        });