<%-- 
    Document   : email
    Created on : 17-Apr-2015, 11:04:39
    Author     : Dubic
--%>

<div class="email">
    <div ng-repeat="a in alerts" class="alert {{a.class}}">{{a.msg}}</div>

    <div class="checkbox">
        <label><input type="checkbox" ng-model="user.showEmail" ng-change="updatePossible = true"> Show email</label>
    </div>

    <button class="btn btn-default" ng-click="emailSetting()" ng-show="updatePossible" ng-disabled="settingSaving">
        <span ng-show="!settingSaving">Save</span><span ng-show="settingSaving">saving...</span>
    </button>
    <hr/>

    <div class="margin-top-20">
        <button class="btn btn-primary" ng-click="validateEmail = !validateEmail">change email {{user.email}}</button>
    </div>

    <div class="margin-top-20" collapse="validateEmail">
        <p class="info">
            <i class="icon-info-sign"></i> Your new email will be validated with a passcode so make sure it is valid.</p>
        <form name="newfrm" class="">
            <span class="error-msg" ng-show="newfrm.email.$dirty && newfrm.email.$error.email">invalid email</span>
            <span class="error-msg" ng-show="newfrm.email.$dirty && newfrm.email.$error.required">email is required</span>
            <div class="form-group">
                <input type="email" name="email" ng-model="newEmail" class="form-control input-large inline" required placeholder="new email"/>
            </div>
            <button class="btn btn-primary" ng-click="sendPasscode()" ng-disabled="newfrm.$invalid || validating">
                <span ng-show="!validating">Submit</span><span ng-show="validating">Processing...</span>
            </button>
        </form>
    </div>

    <div class="margin-top-20" collapse="saveEmail">
        <p class="info">
            <i class="icon-info-sign"></i> 
            A passcode has been sent to <strong ng-bind="newEmail"></strong>. Use it to reset your email</p>
        <form name="codefrm">
            <div class="form-group">
                <span class="error-msg" ng-show="codefrm.passcode.$dirty && codefrm.passcode.$error.required">Passcode is required</span>
                <input type="text" ng-model="passcode" class="form-control input-large" placeholder="passcode" required name="passcode"/>
            </div>
            <div class="form-group">
                <span class="error-msg" ng-show="codefrm.password.$dirty && codefrm.password.$error.required">email is required</span>
                <input type="password" ng-model="password" class="form-control input-large" name="password" placeholder="your password" required/>
            </div>
            <button class="btn btn-primary" ng-click="updateEmail()" ng-disabled="codefrm.$invalid || saving">
                <span ng-show="!saving">Save</span><span ng-show="saving">saving...</span>
            </button>
        </form>
    </div>
</div>
