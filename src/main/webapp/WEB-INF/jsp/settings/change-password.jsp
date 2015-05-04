<%-- 
    Document   : change-password
    Created on : Jan 12, 2015, 9:32:02 PM
    Author     : dubem
--%>
<div class="password">
    <div class="row">
        <div class="col-md-12"><div ng-repeat="a in alerts" class="alert {{a.class}}">{{a.msg}}</div></div>
    </div>
    
    <h4>Reset your current password.</h4>
    <form name="frm2">
        <div class="form-group">
            <label class="control-label">Current Password</label>
            <span class="error-msg" ng-show="frm2.pword.$dirty && frm2.pword.$error.required">Enter your current password</span>
            <input class="form-control input-large" autocomplete="off" type="password" name="pword" required ng-model="current">
        </div>
        <div class="form-group">
            <label class="control-label">New Password</label>
            <span class="error-msg" ng-show="frm2.npword.$dirty && frm2.npword.$error.required">Enter a new password</span>
            <input class="form-control input-large" autocomplete="off" type="password" name="npword" required ng-model="newpword">
        </div>
        <div class="form-group">
            <label class="control-label">Re-type New Password</label>
            <span class="error-msg" ng-show="frm2.vpword.$error.match">Passwords do not match</span>
            <input class="form-control input-large" autocomplete="off" type="password" name="vpword" required ng-model="retypenew" val-match="newpword">
        </div>
        <div class="margin-top-10">
            <button class="btn btn-primary" ng-click="savePassword()" ng-disabled="saving || frm2.$invalid">
                <span ng-show="!saving">Save</span><span ng-show="saving">saving...</span>
            </button>
        </div>
    </form>
</div>
