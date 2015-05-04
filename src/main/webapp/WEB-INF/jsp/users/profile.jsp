<%-- 
    Document   : profile
    Created on : Jan 10, 2015, 11:47:50 PM
    Author     : dubem
--%>
<link href="/codesnippets/resources/css/profile.css" rel="stylesheet" type="text/css"/>
<div class="profile">
    <div class="middle-page-2">
        <div>
            <div class="row margin-bottom-10">
                <div class="col-md-12" ng-if="user.me">
                    <button ng-click="route('settings.account')" class="btn btn-default pull-right"><i class="icon-edit"></i> Edit</button>
                </div> 
            </div>
            <div class="row">
                <div class="col-md-3">
                    <div ng-show="loadingUser">Loading...</div>

                    <div ng-hide="loadingUser">
                        <div class="image margin-bottom-10">
                            <img src="{{usersPath}}/img/{{user.picture}}" width="150" height="170"/>
                        </div>
                        <div class="details">
                            <div class="record">{{user.firstname}} {{user.lastname}}</div>
                            <div class="record">{{user.username}}</div>
                            <div class="record" ng-if="user.showEmail || user.me">{{user.email}}</div>
                            <div class="record">joined {{user.createDt}}</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="panel panel-primary">
                        <div class="panel-heading" ng-hide="loadingSnips">Recent Snippets</div>
                        <div class="panel-heading" ng-show="loadingSnips">Loading...</div>
                        <div class="panel-body">
                            <div class="snippets">
                                <div class="snippet" ng-repeat="s in snips">
                                    <div class="title">{{s.title}}</div>
                                    <div class="desc">{{s.desc}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>
