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
    <div class="middle-page-2" >
        <div class="row">
            <div class="col-md-3">
                <a class="" href="/codesnippets">
                    <!--<img src="/codesnippets/resources/images/logo_hollow.png" style="height: 54px;margin-top: -15px;" alt="Cipher Snippets"/>-->
                </a>

            </div>
            <div class="col-md-6 pop-container">
                <div class="">
                    <form name="search" ng-submit="runSearch()">
                        <input name="keyword" placeholder="Search snippets..." class="form-control input-large" ng-class="{'spinner':searching}" ng-model="Search.keyword" style="border-color: #999;" required type="text"/>
                    </form>
                </div>
            </div>
            <div class="col-md-3">
                <!--<button class="btn btn-default">-->
                <!--<i class="icon-cut" style="font-size: 18px"></i>-->
                <!--</button>-->
                <ul class="pull-right list-inline" style="margin-bottom: 0px">
                    <li class="dropdown" ng-show="isAuthenticated">
                        <a href="" ng-mouseover="isOpen()" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                            <i class="icon-clickable icon-bell"></i>
                            <span class="badge" style="position: absolute;top: -5px;right: 20px;" ng-show="Notifications.unreadCount > 0" ng-bind="Notifications.unreadCount | number"></span>
                        </a>
                        <ul class="dropdown-menu extended notification">
                            <li>
                                <p ng-show="Notifications.unreadCount > 0">You have {{Notifications.unreadCount}} new notifications</p>
                                <p ng-show="Notifications.unreadCount < 1">You have No new notifications</p>
                            </li>
                            <li>
                                <ul class="dropdown-menu-list scroller" style="height: 250px;">
                                    <li ng-show="notifLoading">  
                                        <a href="" style="pointer-events: none">
                                            <span class="label label-icon label-primary"><i class="icon-refresh"></i></span>
                                            <img src="{{spinner}}"/> Loading...
                                        </a>
                                    </li>
                                    <li ng-repeat="n in Notifications.list">  
                                        <a href="{{link}}">
                                            <span class="label label-icon label-primary"><i class="icon-share"></i></span>
                                            {{n.msg}} 
                                            <span class="time" ng-bind="n.time | date:'medium'"></span>
                                        </a>
                                    </li>
                                </ul>
                            </li>
                            <li class="external">   
                                <a href="#">See all notifications <i class="m-icon-swapright"></i></a>
                            </li>
                        </ul>
                    </li> 
                    <li>
                        <div ng-hide="isAuthenticated || loading">
                            <!--<div ng-show="isAuthenticated">-->
                            <a href="#/login" class="btn btn-default margin-right-10">sign in</a>
                            <a href="#/signup" class="btn" style="background: #FF6E00;color: #fff">sign up</a>
                        </div>
                    </li>
                    <li class="dropdown">
                        <div ng-show="isAuthenticated">
                            <a href="" ng-click="a = 1" class="dropdown-toggle label-link" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                <img alt="" class="img-circle" width="35" height="35" ng-src="{{usersPath}}/img/{{userDetails.picture}}">
                                <span class="username" ng-bind="userDetails.screenName"></span>
                                <i class="icon-angle-down"></i>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="#/profile/{{userDetails.screenName}}"><i class="icon-user"></i> My Account</a></li>
                                <li><a href="#/u/snippets"><i class="icon-cut"></i> My Snippets</a></li>
                                <li class="divider"></li>
                                <li><a href="#/logout"><i class="icon-key"></i> Log Out</a></li>
                            </ul>
                        </div>
                    </li>
                </ul>



            </div>

        </div>


    </div>
</header>
<script>

</script>
