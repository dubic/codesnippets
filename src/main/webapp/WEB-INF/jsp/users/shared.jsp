<%-- 
    Document   : shared
    Created on : 11-Apr-2015, 16:28:18
    Author     : Dubic
--%>

<section class="main">
    <div ng-show="loadingSnippets" style="padding: 50px"><div class="loading-screen"></div><div class="text-center"> Loading...</div></div>
    <div ng-show="!loadingSnippets" class="snippet animation-fade" ng-repeat="s in snippets">
        <div style="padding-top: 65%;position: relative" ng-click="viewSnippet(s)">
            <div class="inner-snip">
                <div class="file ">
                    <h5 ng-bind="s.title"></h5>
                </div>
                <div><span class="lang" ng-bind="s.lang"></span></div>
            </div>
        </div>
        <div class="actions">
            <button class="btn btn-block btn-primary btn-sm" ng-click="viewSnippet(s)"><i class="icon-eye-open"></i> View</button>
        </div>
    </div>
    <div class="clearfix"></div>
    <div style="padding: 50px" ng-if="snippets.length === 0 && !loadingSnippets" class="text-center">No Snippets found</div>
</section>
