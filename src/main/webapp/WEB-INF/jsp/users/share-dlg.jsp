<%-- 
    Document   : share-dlg
    Created on : Apr 1, 2015, 7:51:23 AM
    Author     : dubem
--%>

<!--LOGIN MODAL BOX-->
    <div class="modal fade"  id="shareModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
        <div class="modal-dialog" style="min-width: 40%">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title" id="myModalLabel">Share Snippet </h4>
                </div>
                <div class="modal-body">
                    <div ng-repeat="a in shareAlerts" class="alert {{a.class}}">{{a.msg}}</div>
                    
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
