<%-- 
    Document   : welcome
    Created on : Mar 26, 2015, 4:23:17 PM
    Author     : dubem
--%>

<link href="/codesnippets/resources/css/welcome.css" rel="stylesheet" type="text/css"/>

<div class="welcome">
    <div class="cover">
        <div class="middle-page-2">
            <div class="row">
                <div class="col-md-3 col-sm-3">
                    <section class="sidebar">
                        <button class="btn btn-default snipbtn" ng-click="route('snip')"><i class="icon-cut"></i> Snip</button>
                        <ul class="list-unstyled  sidebar-menu margin-top-10">
                            <li><a ng-class="{'active':active === 0}" href="" ng-click="mySnippets()"><i class="icon-cut"></i> My Snippets</a></li>
                            <li><a ng-class="{'active':active === 1}" href="" ng-click="sharedWithMe()"><i class="icon-group"></i> Shared with me</a></li>
                            <li><a ng-class="{'active':active === 2}" href="" ng-click="recent()"><i class="icon-time"></i> Recent</a></li>
                            <li><a ng-class="{'active':active === 3}" href=""><i class="icon-cog"></i> Settings</a></li>
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