<%-- 
    Document   : search
    Created on : 27-Apr-2015, 19:47:58
    Author     : Dubic
--%>
<link href="resources/css/search.css" rel="stylesheet" type="text/css"/>
<div class="search">
    <section class="intro_section">
        <div class="middle-page-2">
            <h3>Search Results for "{{keyword}}"</h3>
            <div class="results">
                <div class="snippets">
                    <h4><strong ng-bind="snipcount"></strong> Snippets found</h4>
                    <div class="result" ng-repeat="s in snippets">
                        <a href="#/snippet/{{s.id}}" class="no-decoration"><i class="icon-file-alt margin-right-10"></i><h4 ng-bind="s.title"></h4></a>
                        <p class="desc" ng-bind="s.desc"></p>
                    </div>
                    <div id="adminTable_paginate" class="dataTables_paginate paging_two_button" ng-hide="searching">
                        <pagination max-size="5" boundary-links="true" total-items="snipcount" ng-model="snippage" items-per-page="snipsize" ng-change="snipChanged()" class="pagination-sm" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></pagination>
                    </div>
                </div>
                <div class="users">
                    <h4><strong ng-bind="userscount"></strong> Persons found</h4>
                    <div class="result" ng-repeat="p in persons">
                        <a href="#/profile/{{p.username}}" class="no-decoration"><i class="icon-user margin-right-10"></i><h4 ng-bind="p.username"></h4></a>
                        <p class="desc">{{p.firstname}} {{p.lastname}}</p>
                    </div>
                    <div id="adminTable_paginate" class="dataTables_paginate paging_two_button" ng-hide="searching">
                        <pagination max-size="5" boundary-links="true" total-items="userscount" ng-model="userspage" items-per-page="userssize" ng-change="usersChanged()" class="pagination-sm" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></pagination>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
