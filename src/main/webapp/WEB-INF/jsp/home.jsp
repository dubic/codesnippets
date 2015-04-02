<%-- 
    Document   : home
    Created on : Sep 10, 2014, 4:11:12 PM
    Author     : dubem
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="/codesnippets/resources/css/home.css" rel="stylesheet" type="text/css"/>
<div class="home">
    <div class="msg-board msg-board-error" ng-show="error">
        Server error occurred
        <i d-close-button class="pull-right" style="font-size: 15px" ng-click="inact()"></i>
    </div>
    <section class="intro_section">
        <div class="middle-page">
            <div class="row">
                <div class="col-md-8 col-sm-8 desc">
                    <h1 class="heading">Snip and save important codes and configurations</h1>
                    <p class="subheading">Easily share code with developers</p>
                </div>
                <!--SIGNUP FORM-->
                <div class="col-md-4 col-sm-4">
                    <form class="pull-right" name="regfrm" ng-submit="signup()">
                        <div class="form-group">
                            <span class="error-msg" ng-show="regfrm.name.$dirty && regfrm.name.$error.required">A Screen name is required</span>
                            <span class="error-msg" ng-show="regfrm.name.$dirty && regfrm.name.$error.minlength">Must be more than 3 characters</span>
                            <span class="error-msg" ng-show="regfrm.name.$dirty && regfrm.name.$error.maxlength">Must not be more than 20 characters</span>
                            <span class="error-msg" ng-show="regfrm.name.$dirty && regfrm.name.$error.ngRemoteValidate">Screen name already used</span>
                            <span class="error-msg" ng-show="regfrm.name.$dirty && regfrm.name.$error.pattern">Alphanumeric characters only</span>
                            <input class="form-control" ng-class="{'spinner':regfrm.name.$pending}" name="name" placeholder="User Name" type="text" ng-pattern="/^[a-z0-9]+$/i"
                                   ng-model="user.screenName" required ng-minlength="4" ng-maxlength="20" ng-remote-validate="{{usersPath}}/name-unique" ng-remote-throttle="1000"/>
                        </div>

                        <div class="form-group">
                            <span class="error-msg" ng-show="regfrm.email.$dirty && regfrm.email.$error.required">Email is required</span>
                            <span class="error-msg" ng-show="regfrm.email.$dirty && regfrm.email.$error.email">Email is not valid</span>
                            <span class="error-msg" ng-show="regfrm.email.$dirty && regfrm.email.$error.ngRemoteValidate">Email is already used</span>
                            <input class="form-control" ng-class="{'spinner':regfrm.email.$pending}" name="email" placeholder="email" type="email" ng-model="user.email" 
                                   required ng-remote-validate="{{usersPath}}/email-unique" ng-remote-throttle="1000" update-on-blur/>
                        </div>
                        <div class="form-group">
                            <span class="error-msg" ng-show="regfrm.password.$dirty && regfrm.password.$error.minlength">Must be more than 5 characters</span>
                            <span class="error-msg" ng-show="regfrm.password.$dirty && regfrm.password.$error.maxlength">Must not be more than 20 characters</span>
                            <input class="form-control" name="password" placeholder="password" type="password" ng-model="user.password" required ng-minlength="6" ng-maxlength="20" autocomplete="off"/>
                        </div>
                        <div class="form-group">
                            <span class="error-msg" ng-show="regfrm.vpword.$dirty && regfrm.vpword.$error.match">Passwords do not match</span>
                            <input class="form-control" name="vpword" placeholder="Retype password" type="password" ng-model="user.vpword" match="user.password"/>
                        </div>
                        <button class="btn btn-block btn-success" ng-disabled="regfrm.$invalid || loading">
                            <span ng-hide="loading">Create Account</span><span ng-show="loading">Saving...</span>
                        </button>
                        <p class="text-muted">
                            By clicking "Create Account", you agree to our
                            <a href="https://help.github.com/terms" target="_blank">terms of service</a> and
                            <a href="https://help.github.com/privacy" target="_blank">privacy policy</a>. <span class="js-email-notice">We will send you account related emails occasionally.</span>
                        </p>
                    </form>
                </div>
            </div>

        </div>
    </section>

    <section class="features">
        <div class="middle-page">
            <div class="row">
                <div class="col-md-6 col-sm-6">
                    <div class="feature-block">
                        <h2>Easy Access</h2>
                        <h4>To all your important codes without browsing through a large code base.</h4>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6">
                    <div class="feature-block">
                        <h2>Share Snippets,tutorials</h2>
                        <h4>Share your codes,configs to others via email etc.</h4>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <section class="footer">
        <div class="middle-page">
            <ul class="list-unstyled list-inline">
                <li>&copy; 2015 CipherSnippets</li>
                <li><a href="terms">Terms</a></li>
                <li><a href="privacy">Privacy</a></li>
                <li><a href="contact">Contact</a></li>
            </ul>
        </div>
    </section>
    <!--    <div class="middle-page" ng-controller="mainCtrl">
            <div class="row">
                <div class="col-md-12 margin-top-10">
                    <div class="action-bar">
                        <button class="btn btn-default" ng-click="route('snip')">Snip <i class="icon-cut"></i></button>
                    </div>
                </div>
            </div>
           
            <section class="container">
                <div ui-view></div>
            </section>
        </div>-->
</div>

