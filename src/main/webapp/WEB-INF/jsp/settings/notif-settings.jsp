<%-- 
    Document   : notifications
    Created on : 12-Apr-2015, 16:35:12
    Author     : Dubic
--%>

<div class="password">
    <div class="row">
        <div class="col-md-12"><div ng-repeat="a in alerts" class="alert {{a.class}}">{{a.msg}}</div></div>
    </div>

    <h4>Notify me when a snippet is shared with me</h4>

    <div class="margin-bottom-25">
        <div class="checkbox">
            <input type="checkbox" ng-model="notifications.web" ng-change="updatePossible=true"> <span>Web</span>
            <br>
            <input type="checkbox" ng-model="notifications.email" ng-change="updatePossible=true"> <span>Email</span>
        </div>
        
        <!--<div ng-bind-html="htttml"></div>-->
    </div>

    <div class="margin-top-20">
        <button class="btn btn-primary" ng-disabled="saving" ng-show="updatePossible" ng-click="update()">
            <span ng-show="!saving">Update</span><span ng-show="saving">saving...</span>
        </button>
    </div>
</div>
