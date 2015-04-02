<%-- 
    Document   : jokes
    Created on : Sep 27, 2014, 9:47:38 AM
    Author     : dubem
--%>

<section class="post-container" ng-repeat="s in snippets">
    <div class="row">
        <div class="col-md-12"><a href="#/home/snippet/{{s.id}}" class=""><h4 ng-bind="s.title"></h4></a></div>
    </div>
    <div class="row">
        <div class="col-md-12" ng-bind="s.desc"></div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <span class="date-grey" ng-bind="s.create_dt"></span>
        </div>
    </div>

    <!--LOGIN MODAL BOX-->
    <div class="modal fade"  id="reportModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
        <div class="modal-dialog" style="min-width: 40%">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title" id="myModalLabel">Report inappropriate content </h4>
                </div>
                <div class="modal-body">
                    <div ng-repeat="a in repAlerts" class="alert {{a.class}}">{{a.msg}}</div>
                    <div class="well well-sm" ng-bind="selectedJoke.post"></div>
                    <ul class="list-unstyled rep-checklist">
                        <li><input type="checkbox" jq-uniform value="Offensive" ng-model="reports.offensive"/>Offensive</li>
                        <li><input type="checkbox" jq-uniform value="Vulgar" ng-model="reports.vulgar"/>Vulgar</li>
                        <li><input type="checkbox" jq-uniform value="Salacious" ng-model="reports.salacious"/>Salacious</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning btn-sm" data-dismiss="modal">cancel</button>
                    <button class="btn btn-info btn-sm" ng-disabled="selectedJoke.reporting" ng-click="report()">
                        <span ng-hide="selectedJoke.reporting">submit</span>
                        <span ng-show="selectedJoke.reporting">saving</span>
                    </button>
                </div>

            </div>
        </div>
    </div>
    <!--END OF LOGIN MODAL BOX-->
</section>
