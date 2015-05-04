<%-- 
    Document   : snippet-view
    Created on : Mar 11, 2015, 5:34:09 PM
    Author     : dubem
--%>
<!--<link href="resources/css/posts.css" rel="stylesheet" type="text/css"/>-->

<div class="view">
    <div class="middle-page-2">
        <section class="loading-screen" ng-show="loading"></section>
        <section class="snippet" ng-show="!loading">
            <div class="row">
                <div class="col-md-12">
                    <span class="title" ng-bind="snippet.title"></span>
                    <h4 class="date-grey profile">By <a href="#/profile/{{snippet.screenName}}"><strong>{{snippet.screenName}}</strong></a> on {{snippet.create_dt | date:'medium'}}</h4>
                    <a ng-if="isMe" href="#/snippet/edit/{{snippet.id}}" class="btn btn-default pull-right"><i class="icon-edit"></i> Edit</a>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="label label-primary" ng-bind="snippet.lang"></div>
                    <pre class="code" ng-bind="snippet.code">
                    
                    </pre>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12" ><div class="well well-sm" ng-bind="snippet.desc"></div>

                </div>
            </div>

            <div class="row" ng-show="snippet.deps.length > 0">
                <div class="col-md-12">
                    <h4>Dependencies</h4>
                    <ol>
                        <li  ng-repeat="d in snippet.deps" ng-bind="d"></li>
                    </ol>
                </div>
            </div>
        </section>
    </div>
</div>
