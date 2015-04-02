/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('AppHome', ['ui.router', 'ngAnimate', 'remoteValidation', 'InputMatch', 'ui.bootstrap', 'controllers', 'Snips', 'DComponents']);
app.constant("usersPath", "/codesnippets/users");
app.constant("regPath", "/codesnippets/registration");
app.constant("spinner", "/codesnippets/resources/images/spinner.gif");
app.constant("imagePath", "/codesnippets/snippets/img");
app.constant("postsPath", "/codesnippets/snippets");

var ctrls = angular.module('controllers', []);
//app.controller('headerCtrl',headerCtrl);

app.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider.
            state('login', {
                url: '/login',
                templateUrl: '/codesnippets/registration/load?p=login',
                controller: 'loginCtrl',
                data: {displayName: ''}
            }).
            state('signup', {
                url: '/signup',
                templateUrl: '/codesnippets/registration/load?p=signup',
                controller: 'signupCtrl',
                data: {displayName: ''}
            }).
            state('inactive', {
                url: '/inactive/{id}',
                templateUrl: '/codesnippets/registration/load?p=inactive',
                controller: 'inactiveCtrl',
                data: {displayName: ''}
            }).
            state('forgot-password', {
                url: '/forgot-password',
                templateUrl: '/codesnippets/registration/load?p=forgot-password',
                controller: 'fpasswordCtrl',
                data: {displayName: ''}
            }).
            state('home', {
                url: '/home',
                templateUrl: '/codesnippets/home',
                controller: 'signupCtrl',
                data: {displayName: ''}
            }).
            state('welcome', {
                url: '/u',
                templateUrl: '/codesnippets/users/load?p=welcome',
                controller: 'welcomeCtrl',
//                resolve: {
//                    permission: function(authService) {
//                        console.log('resloving welcome...');
//                        return authService.permissionCheck('welcome');
//                    }
//                },
                data: {displayName: ''}
            }).
            state('welcome.snippets', {
                url: '/snippets',
                templateUrl: '/codesnippets/users/load?p=snippets',
//                controller: 'snippetsCtrl',
//                resolve: {
//                    permission: function(authService) {
//                        console.log('resloving welcome...');
//                        return authService.permissionCheck('welcome');
//                    }
//                },
                data: {displayName: ''}
            }).
            state('welcome.settings', {
                url: '/settings',
                templateUrl: '/codesnippets/users/load?p=settings',
                controller: 'welcomeCtrl',
//                resolve: {
//                    permission: function(authService) {
//                        console.log('resloving welcome...');
//                        return authService.permissionCheck('welcome');
//                    }
//                },
                data: {displayName: ''}
            }).
            state('snippets', {
                url: '/snippets',
                templateUrl: '/codesnippets/snippets/view?page=snippets',
                controller: 'snippetsCtrl',
                data: {displayName: 'Snippets'}
            }).
            state('snippet', {
                url: '/snippet/{id}',
                templateUrl: '/codesnippets/snippets/view?page=snippet-view',
                controller: 'viewsnCtrl',
                data: {displayName: 'Snippet'}
            }).
            state('snipedit', {
                url: '/snippet/edit/{id}',
                templateUrl: '/codesnippets/snippets/view?page=snippet-edit',
                controller: 'editsnCtrl',
                resolve: {
                    permission: function(authService) {
                        console.log('resloving...');
                        return authService.checkIsMe('snipedit');
                    }
                },
                data: {displayName: 'Edit Snippet'}
            }).
            state('snip', {
                url: '/snip',
                templateUrl: '/codesnippets/snippets/view?page=snip',
                controller: 'snipCtrl',
                resolve: {
                    permission: function(authService) {
                        console.log('resloving...');
                        return authService.permissionCheck('snip');
                    }
                },
                data: {displayName: ''}
            }).
            state('profile', {
                url: '/profile/{user}',
                templateUrl: '/codesnippets/profile/home',
                controller: 'profileCtrl',
                data: {displayName: 'profile'}
            }).
            state('profile.activity', {
//                url: '/profile/activity',
                templateUrl: '/codesnippets/profile/activity',
                controller: 'activityCtrl',
                data: {displayName: 'profile activity'}
            }).
            state('profile.account', {
                templateUrl: '/codesnippets/profile/account',
                controller: 'accountCtrl',
                data: {displayName: 'profile account'}
            }).
            state('profile.pword', {
                templateUrl: '/codesnippets/profile/pword',
                controller: 'pwordCtrl',
                data: {displayName: 'change password'}
            });

//    $urlRouterProvider.when('/profile','/home/jokes');
    $urlRouterProvider.otherwise('/home');

//      $locationProvider.html5Mode(true);
});

app.run(function($rootScope, $state, $window, imagePath, $http, usersPath, regPath, spinner) {

    $rootScope.imagePath = imagePath;
    $rootScope.usersPath = usersPath;
    $rootScope.regPath = regPath;
    $rootScope.spinner = spinner;

    $rootScope.navigate = function(state, params) {
        $state.go(state, params, {location: false});
    };
    $rootScope.route = function(state, params) {
        $state.go(state, params);
    };
    $rootScope.back = function() {
        $window.history.back();
    };

    $rootScope.alerts = [];
    $rootScope.$on('$stateChangeStart', function(e, to) {
        $rootScope.loading = true;
        $rootScope.loadingMsg = 'Loading ' + to.data.displayName + '...';
//        $rootScope.previous = $state.current.name;
//        console.log('prev : ' + $rootScope.previous);
//        console.log();

    });
    $rootScope.$on('$stateChangeSuccess', function(e, to) {
        $rootScope.loading = false;
        $rootScope.activePage = to.name;
        if(to.name === 'welcome')$rootScope.pageSize = 'middle-page-2';
        else $rootScope.pageSize = 'middle-page';
        
    });

    $rootScope.getCurrentUser = function() {
        $http.get(usersPath + '/current').success(function(resp) {
            if (resp.code === 0) {
                $rootScope.userDetails = resp;
                $rootScope.isAuthenticated = true;
            } else {
                $rootScope.isAuthenticated = false;
            }
        });
    };

    $rootScope.isMefn = function(snipId) {
        $http.get(usersPath + '/isme/' + snipId).success(function(resp) {
            if (resp.code === 200) {
                $rootScope.isMe = true;
            } else {
                $rootScope.isMe = false;
            }
        });
    };

    $rootScope.getCurrentUser();

    $rootScope.langs = 'C,C#,C++,HTML,CSS,Java,JavaScript,Objective-C,Perl,Android,ASP,PHP,Python,Ruby,Shell,SQL,File,ABAP,ActionScript,Ada,Arc,Apex,Arduino,Assembly,AutoIt,BlitzMax,Ceylon,Clojure,Coco,CoffeeScript,ColdFusion,Common Lisp,CUDA,D,Dart,Delphi,Duby,Dylan,Eiffel,Elixir,Emacs Lisp,Erlang,Euphoria,F#,Fantom,Forth,Fortran,FoxPro,Gambas,Go,Groovy,Hack,Haskell,Haxe,IGOR Pro,Inform,Io,Julia,LabVIEW,Lasso,Limbo,LiveScript,Lua,LilyPond,M,Markdown,Mathematica,MATLAB,Max/MSP,Mercury,Nemerle,Nimrod,Node.js,Nu,Pascal,Objective-J,OCaml,occam,Oxygene,PowerBASIC,PowerShell,Processing,Prolog,Puppet,Quorum,R,Racket,Rust,Sass/SCSS,Scala,Scheme,Scilab,sclang,Self,Smalltalk,SourcePawn,Standard ML,SuperCollider,Swift,Tcl,TeX,TypeScript,UnityScript,UnrealScript,Vala,Verilog,VHDL,VimL,Visual Basic,VB.NET,XML,Xojo,XPages,XQuery,XTend,Z Shell'.split(',');

//    console.log($rootScope.langs);
});

//console.log("angular configured");

