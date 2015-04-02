<%-- 
    Document   : snippet-view
    Created on : Mar 11, 2015, 5:34:09 PM
    Author     : dubem
--%>
<div class="middle-page-2">
    <section class="loading-screen" ng-show="loading"></section>
    <section class="view-snip" ng-show="!loading">
        <div class="row">
            <div class="col-md-12">
                <span class="title" ng-bind="snippet.title"></span>
                <a ng-if="isMe" href="#/snippet/edit/{{snippet.id}}" class="btn btn-default pull-right"><i class="icon-edit"></i> Edit</a>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="label label-primary" ng-bind="snippet.lang"></div>
                <pre class="code prettyprint linemus" ng-bind="snippet.code">
                    
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

        <div class="row margin-bottom-15">
            <div class="col-md-6"></div>
            <div class="col-md-6">
                <div class=" pull-right">
                    <div class="col-md-2"><img src="{{imagePath}}/{{snippet.picture}}" width="40" height="40" style="margin-right: 10px"/></div>
                    <div class="col-md-10">
                        <div ng-bind="snippet.screenName"></div>
                        <div class="date-grey" ng-bind="snippet.create_dt | date:'medium'"></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
