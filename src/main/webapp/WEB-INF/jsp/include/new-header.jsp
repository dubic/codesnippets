<%-- 
    Document   : new-header
    Created on : Jan 17, 2015, 1:19:05 PM
    Author     : dubem
--%>

<header ng-controller="headerCtrl">
    <style>
        .username{
            margin: 0px 5px;
        }
        .label-link:hover{
            text-decoration: none;
        }
    </style>
    <div class="{{pageSize}}" >
        <div class="row">
            <div class="col-md-3">
                <a class="" href="/codesnippets">
                    <img src="/codesnippets/resources/images/logo_hollow.png" style="height: 54px;margin-top: -15px;" alt="Cipher Snippets"/>
                </a>

            </div>
            <div class="col-md-6 pop-container">
                <div class="">
                    <input placeholder="Search snippets..." class="form-control input-large" ng-class="{'spinner':searching}" ng-model="Search.keyword" ng-change="search()" type="text"/>
                </div>
                <div class="search-resuts bordered pop full-width display-none">
                    <div class="group" ng-repeat="g in searched">
                        <div class="head"><span ng-bind="g.name"></span><span class="pull-right badge badge-default" ng-bind="g.count | number"></span></div>
                        <a href="">
                            <div class="list" ng-repeat="l in g.lists">
                                <!--<div class="title">one bright summer</div>-->
                                <div class="body" ng-bind="l"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <!--<button class="btn" ng-click="testDialog()">close</button>-->
                <div class="pull-right" ng-hide="isAuthenticated">
                    <a href="#/login" class="btn btn-default margin-right-10">sign in</a>
                    <a href="#/signup" class="btn" style="background: #FF6E00;color: #fff">sign up</a>
                </div>
                <div class="pull-right" ng-show="isAuthenticated">
                    <a href="#" class="dropdown-toggle label-link" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                        <img alt="" class="circle" width="30" height="30" src="{{imagePath}}/{{userDetails.picture}}">
                        <span class="username" ng-bind="userDetails.screenName"></span>
                        <i class="icon-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#/profile"><i class="icon-user"></i> My Account</a></li>
                        <li class="divider"></li>
                        <li><a href="/codesnippets/logout"><i class="icon-key"></i> Log Out</a></li>
                    </ul>
                </div>
            </div>

        </div>


    </div>
</header>
