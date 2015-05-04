<%-- 
    Document   : settings
    Created on : Mar 31, 2015, 3:52:10 PM
    Author     : dubem
--%>

<!--<link href="/codesnippets/resources/css/settings.css" rel="stylesheet" type="text/css"/>-->

<link href="/codesnippets/resources/css/welcome.css" rel="stylesheet" type="text/css"/>

<div class="welcome">
    <div class="cover">
        <div class="middle-page-2">
            <div class="row">
                <div class="col-md-3 col-sm-3">
                    <section class="sidebar">
                        <button class="btn btn-default snipbtn" ng-click="route('snip')"><i class="icon-cut"></i> Snip</button>
                        <ul class="list-unstyled  sidebar-menu margin-top-10">
                            <li><a ng-class="{'active':activePage === 'settings.account'}" href="#/settings/account"><i class="icon-user"></i> Account</a></li>
                            <li><a ng-class="{'active':activePage === 'settings.email'}" href="#/settings/email"><i class="icon-envelope-alt"></i> Email</a></li>
                            <li><a ng-class="{'active':activePage === 'settings.password'}" href="#/settings/password"><i class="icon-lock"></i> Password</a></li>
                            <li><a ng-class="{'active':activePage === 'settings.notifications'}" href="#/settings/notifications"><i class="icon-bell"></i> Notifications</a></li>
                        </ul>
                    </section>
                </div>
                <div class="col-md-9 col-sm-9">
                    <ui-view></ui-view>
                </div>
            </div>
        </div>
    </div>
</div>