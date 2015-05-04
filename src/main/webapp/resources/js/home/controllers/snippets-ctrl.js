/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ctrls.controller('snippetsCtrl', function ($scope, $http, services, $rootScope, postsPath, $timeout, $state) {

    $scope.mySnippets = function () {
        $scope.active = 0;
        $scope.snippets = [];
        $scope.loadingSnippets = true;
        $http.get(postsPath + '/load/mine?start=0&size=10').success(function (resp) {
            //hide loading..
            if (resp.length === 0)
                $scope.loadingSnippets = false;
            var i = 0;
            addSnippet(i, resp);
        }).error(function (data, status) {
            $rootScope.loadingSnippets = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.recent = function () {
        $scope.active = 2;
        $scope.loadingSnippets = true;
        $scope.snippets = [];
        $http.get(postsPath + '/load/latest?start=0&size=10').success(function (resp) {
            $scope.loadingSnippets = false;//hide loading..
            var i = 0;
            addSnippet(i, resp);

        }).error(function (data, status) {
            $rootScope.loadingSnippets = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    $scope.shared = function () {
        $scope.active = 1;
        $scope.loadingSnippets = true;
        $scope.snippets = [];
        $http.get(postsPath + '/load/shared?start=0&size=10').success(function (resp) {
            $scope.loadingSnippets = false;//hide loading..
            var i = 0;
            addSnippet(i, resp);

        }).error(function (data, status) {
            $scope.loadingSnippets = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };


    $scope.viewSnippet = function (s) {
        $rootScope.route('snippet', {id: s.id});
    };

    $scope.blocking = false;
    $scope.deleteSnippet = function (s, i) {
        if (!confirm('delete snippet : ' + s.title))
            return;
        $scope.blocking = true;
        $http.get(postsPath + '/delete/' + s.id).success(function (resp) {
            $scope.blocking = false;
            if (resp.code === 0) {
                services.notify('deleted : ' + s.title);
                $scope.snippets.splice(i, 1);
            } else {
                services.notify(resp.msg);
            }
        }).error(function (data, status) {
            $scope.blocking = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };

    if ($state.current.name === 'welcome.snippets') {
        $scope.mySnippets();
    } else if ($state.current.name === 'welcome.recent') {
        $scope.recent();
    }



    function addSnippet(i, resp) {
        if (i < resp.length) {
            $timeout(function () {
                $scope.snippets.push(resp[i]);//display jokes for animation
                $scope.loadingSnippets = false;
                i++;
                addSnippet(i, resp);
            }, 200);
        }
    }

    //SHARE DETAILS
    $scope.openShare = function (s) {
        $scope.snippet = s;
        $scope.sharelist = [];
        $scope.shareAlerts = [];
        $scope.tipErrMsg = "";
        services.openDialog('shareModal');
    };


    $scope.getNames = function (val) {
//        $scope.viewValue = val;
        return $http.get($rootScope.usersPath + '/users-email?val=' + val).then(function (response) {
            return response.data.map(function (item) {
                return item;
            });
        });
    };

//    $scope.onselect = function (item, model, label) {
//
////        console.log(item);
////        console.log(model);
////        console.log(label);
//        $scope.selectedModel = model;
//    };


    $scope.addToList = function () {
        console.log($scope.asyncSelected);
        $scope.tipErrMsg = "";
        if (angular.isString($scope.asyncSelected)) {//plain email not a user
            if (angular.isUndefined($scope.asyncSelected)) {
                $scope.tipErrMsg = "Enter username or valid email " + $scope.asyncSelected;
                return;
            }
            if (services.containsProperty($scope.sharelist, 'name', $scope.asyncSelected)) {
                $scope.tipErrMsg = "Already added " + $scope.asyncSelected;
                return;
            }
            if (!services.isEmail($scope.asyncSelected)) {
                $scope.tipErrMsg = $scope.asyncSelected + " Not a valid email or username";
                return;
            }
            $scope.sharelist.push({user: false, name: $scope.asyncSelected});
        } else {
            if (services.containsProperty($scope.sharelist, 'name', $scope.asyncSelected.username)) {
                $scope.tipErrMsg = "Already added " + $scope.asyncSelected.username;
                return;
            }
            $scope.sharelist.push({user: true, name: $scope.asyncSelected.username});
        }
        $scope.asyncSelected = undefined;

    };

    $scope.share = function () {
        $scope.sharing = true;
        $http.post(postsPath + '/share/' + $scope.snippet.id, {msg: $scope.sharedmsg, sharelist: $scope.sharelist}).success(function (resp) {
            $scope.sharing = false;
            if (resp.code === 0) {
                if (resp.errMsg.length === 0) {
                    services.notify('Snippet shared successfully');
                    services.closeDialog('shareModal');
                } else {
                    for (var i = 0; i < resp.errMsg.length; i++) {
                        services.notify('Snippet shared successfully');
                        $scope.shareAlerts.push({class: 'alert-info', msg: resp.errMsg[i]});
                        $scope.shareform.$setPristine();
                    }
                }
            } else if (resp.code === 404) {
                $scope.shareAlerts = [{class: 'alert-danger', msg: resp.msg}];
            }
        }).error(function (data, status) {
            $scope.sharing = false;
            if (status === 403)
                $rootScope.sessionTimedOut();
        });
    };
});

