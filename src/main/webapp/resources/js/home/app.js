/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('AppHome', ['ui.router', 'angularFileUpload', 'ngAnimate', 'remoteValidation', 'InputMatch', 'ui.bootstrap', 'controllers', 'Snips', 'DComponents', 'ngSanitize']);
app.constant("usersPath", "/codesnippets/users");
app.constant("regPath", "/codesnippets/registration");
app.constant("spinner", "/codesnippets/resources/images/spinner.gif");
app.constant("imagePath", "/codesnippets/snippets/img");
app.constant("postsPath", "/codesnippets/snippets");
app.constant("settingsPath", "/codesnippets/settings");
app.constant("notifPath", "/codesnippets/notifications");
app.constant("searchPath", "/codesnippets/search");

var ctrls = angular.module('controllers', []);
//app.controller('headerCtrl',headerCtrl);

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider.
            state('login', {
                url: '/login?m',
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
            state('logout', {
                url: '/logout',
//                templateUrl: '/codesnippets/registration/load?p=signup',
                controller: function ($http, $rootScope) {
                    $http.get('/codesnippets/logout').success(function (resp) {
                        location.href = '/codesnippets';
                    });
                },
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
                resolve: {
                    permission: function (authService) {
                        console.log('resloving welcome...');
                        return authService.authenticate('welcome.snippets');
                    }
                },
                data: {displayName: ''}
            }).
            state('welcome.snippets', {
                url: '/snippets',
                templateUrl: '/codesnippets/users/load?p=snippets',
                controller: 'snippetsCtrl',
                data: {displayName: ''}
            }).
            state('welcome.recent', {
                url: '/recent',
                templateUrl: '/codesnippets/users/load?p=snippets',
                controller: 'snippetsCtrl',
                data: {displayName: ''}
            }).
            state('welcome.shared', {
                url: '/shared',
                templateUrl: '/codesnippets/users/load?p=shared',
                controller: 'sharedCtrl',
                data: {displayName: ''}
            }).
            state('settings', {
                url: '/settings',
                templateUrl: '/codesnippets/users/settings/load?p=settings',
                controller: 'settingsCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving settings...');
                        return authService.authenticate('settings.account');
                    }
                },
                data: {displayName: ''}
            }).
            state('settings.account', {
                url: '/account',
                templateUrl: '/codesnippets/users/settings/load?p=account',
                controller: 'accountCtrl',
                data: {displayName: ''}
            }).
            state('settings.email', {
                url: '/email',
                templateUrl: '/codesnippets/users/settings/load?p=email',
                controller: 'emailCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving email settings...');
                        return authService.authenticate('settings.email');
                    }
                },
                data: {displayName: 'Email settings'}
            }).
            state('settings.password', {
                url: '/password',
                templateUrl: '/codesnippets/users/settings/load?p=change-password',
                controller: 'pwordCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving password settings...');
                        return authService.authenticate('settings.password');
                    }
                },
                data: {displayName: 'Password settings'}
            }).
            state('settings.notifications', {
                url: '/notifications',
                templateUrl: '/codesnippets/users/settings/load?p=notif-settings',
                controller: 'notifCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving notifications settings...');
                        return authService.authenticate('settings.notifications');
                    }
                },
                data: {displayName: 'Notification settings'}
            }).
            state('snippets', {
                url: '/snippets',
                templateUrl: '/codesnippets/snippets/view?page=snippets',
                controller: 'snippetsCtrl',
                data: {displayName: 'Snippets'}
            }).
            state('snippet', {
                url: '/snippet/{id}',
                templateUrl: '/codesnippets/snippets/view/open?page=snippet-view',
                controller: 'viewsnCtrl',
                data: {displayName: 'Snippet'}
            }).
            state('snipedit', {
                url: '/snippet/edit/{id}',
                templateUrl: '/codesnippets/snippets/view?page=snippet-edit',
                controller: 'editsnCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving edit snips...');
                        return authService.authenticate('welcome.snippets');
                    }
                },
                data: {displayName: 'Edit Snippet'}
            }).
            state('snip', {
                url: '/snip',
                templateUrl: '/codesnippets/snippets/view?page=snip',
                controller: 'snipCtrl',
                resolve: {
                    permission: function (authService) {
                        console.log('resloving...');
                        return authService.authenticate('snip');
                    }
                },
                data: {displayName: ''}
            }).
            state('profile', {
                url: '/profile/{user}',
                templateUrl: '/codesnippets/users/load?p=profile',
                controller: 'profileCtrl',
                data: {displayName: 'profile'}
            }).
            state('search', {
                url: '/search/{q}',
                templateUrl: '/codesnippets/search',
                controller: 'searchCtrl',
                data: {displayName: ''}
            });

    $urlRouterProvider.otherwise('/home');

//      $locationProvider.html5Mode(true);
});

app.run(function ($rootScope, $state, $window, imagePath, $http, usersPath, regPath, spinner) {

    $rootScope.imagePath = imagePath;
    $rootScope.usersPath = usersPath;
    $rootScope.regPath = regPath;
    $rootScope.spinner = spinner;

    $rootScope.navigate = function (state, params) {
        $state.go(state, params, {location: false});
    };
    $rootScope.route = function (state, params) {
        $state.go(state, params);
    };
    $rootScope.back = function () {
        $window.history.back();
    };

    $rootScope.alerts = [];
    $rootScope.$on('$stateChangeStart', function (e, to) {
        $rootScope.loading = true;
        $rootScope.loadingMsg = 'Loading ' + to.data.displayName + '...';
//        $rootScope.previous = $state.current.name;
//        console.log('prev : ' + $rootScope.previous);
//        console.log($state);

    });
    $rootScope.$on('$stateChangeSuccess', function (e, to) {
        $rootScope.loading = false;
        $rootScope.activePage = to.name;
    });
    $rootScope.$on('$stateChangeError', function (e, to,t,er,ff,err) {
        $rootScope.loading = false;
        console.log('state chang error');
        console.log(err);
//        console.log(to);
    });

//    $rootScope.getCurrentUser = function() {
//        
//    };

    $rootScope.login = function (user) {
        $rootScope.loggin = false;
        var def = $http.post('login?' + $.param(user));
        def.success(function (resp) {
            $rootScope.loggin = false;
            $rootScope.$broadcast('authenticated', resp);
            console.log('prev is : '+$rootScope.previous);
            console.log('prev index is : '+['login','signup','inactive','forgot-password','home'].indexOf($rootScope.previous));
            if (angular.isUndefined($rootScope.previous) || $rootScope.previous === ''){
                if(['login','signup','inactive','forgot-password','home'].indexOf($rootScope.previous) === -1) $state.go('welcome.snippets');
                else $state.go($rootScope.previous);
            }
            else
                $state.go($rootScope.previous);
        });
        return def;
    };

    $rootScope.sessionTimedOut = function () {
        $rootScope.$broadcast('timed.out');
        $rootScope.route('login', {m: 2});
    };

    $rootScope.signedOut = function () {
        $rootScope.$broadcast('signed.out');
        $rootScope.route('login', {m: 1});
    };

    $rootScope.profilePixChanged = function (pic) {
        $rootScope.$broadcast('profile.pix.changed', pic);
    };

    $rootScope.isMefn = function (snipId) {
        $http.get(usersPath + '/isme/' + snipId).success(function (resp) {
            if (resp.code === 200) {
                $rootScope.isMe = true;
            } else {
                $rootScope.isMe = false;
            }
        });
    };

//    $rootScope.getCurrentUser();

    $rootScope.langs = 'C,C#,C++,HTML,CSS,Java,JavaScript,Objective-C,Perl,Android,ASP,PHP,Python,Ruby,Shell,SQL,File,ABAP,ActionScript,Ada,Arc,Apex,Arduino,Assembly,AutoIt,BlitzMax,Ceylon,Clojure,Coco,CoffeeScript,ColdFusion,Common Lisp,CUDA,D,Dart,Delphi,Duby,Dylan,Eiffel,Elixir,Emacs Lisp,Erlang,Euphoria,F#,Fantom,Forth,Fortran,FoxPro,Gambas,Go,Groovy,Hack,Haskell,Haxe,IGOR Pro,Inform,Io,Julia,LabVIEW,Lasso,Limbo,LiveScript,Lua,LilyPond,M,Markdown,Mathematica,MATLAB,Max/MSP,Mercury,Nemerle,Nimrod,Node.js,Nu,Pascal,Objective-J,OCaml,occam,Oxygene,PowerBASIC,PowerShell,Processing,Prolog,Puppet,Quorum,R,Racket,Rust,Sass/SCSS,Scala,Scheme,Scilab,sclang,Self,Smalltalk,SourcePawn,Standard ML,SuperCollider,Swift,Tcl,TeX,TypeScript,UnityScript,UnrealScript,Vala,Verilog,VHDL,VimL,Visual Basic,VB.NET,XML,Xojo,XPages,XQuery,XTend,Z Shell'.split(',');

//    console.log($rootScope.langs);
});

//console.log("angular configured");

