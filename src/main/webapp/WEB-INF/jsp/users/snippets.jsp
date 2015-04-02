<%-- 
    Document   : snippets
    Created on : Mar 31, 2015, 3:51:58 PM
    Author     : dubem
--%>

<section class="main">
    <div ng-show="loadingSnippets" style="padding: 50px"><div class="loading-screen"></div><div class="text-center"> Loading...</div></div>
    <div ng-show="!loadingSnippets" class="snippet " ng-repeat="s in snippets">
        <div style="padding-top: 65%;position: relative" ng-click="viewSnippet(s)">
            <div class="inner-snip">
                <div class="file ">
                    <h5 ng-bind="s.title"></h5>
                </div>
                <div><span class="lang" ng-bind="s.lang"></span></div>
            </div>
        </div>
        <div class="actions">
            <div class="col-md-6 part" style="border-right: solid 1px #DDD;">
                <button class="btn btn-block btn-primary btn-sm"><i class="icon-share"></i> Share</button>
            </div>
            <div class="col-md-6 part">
                <button class="btn btn-block btn-primary btn-sm" ng-click="viewSnippet(s)"><i class="icon-eye-open"></i> View</button>
            </div>
        </div>
    </div>
    <div class="clearfix"></div>
    <div style="padding: 50px" ng-if="snippets.length === 0 && !loadingSnippets" class="text-center">No Snippets found</div>
</section>

<jsp:include page="share-dlg.jsp"/>
