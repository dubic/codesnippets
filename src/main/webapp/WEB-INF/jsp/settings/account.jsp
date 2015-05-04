<%-- 
    Document   : account
    Created on : Jan 12, 2015, 1:11:33 PM
    Author     : dubem
--%>
<div class="account">
    <div ng-repeat="a in alerts" class="alert {{a.class}}">{{a.msg}}</div>
    <div class="picture">

        <div class="row">
            <div class="col-md-2">
                <img src="{{usersPath}}/img/{{user.picture}}" width="70" height="70"/>
            </div>
            <div class="col-md-10">
                <div class="inline-block">
                    <div class="btn btn-primary" ng-disabled="uploading" ng-file-select ng-file-change="upload($files)">Upload new picture</div>

                    <div style="width: 100%;height: 5px;background: #ddd" ng-show="uploading">
                        <div class="progress-bar progress-bar-success" role="progressbar" style="width: {{progressPercentage}}%">
                            <!--<span>{{progressPercentage}}% Complete</span>-->
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="upload-controls">


        </div>


        <form name="frm" class="margin-top-10">
            <div class="form-group">
                <label>First Name</label>
                <input ng-model="user.firstname" type="text" class="form-control input-large" placeholder="First Name">
            </div>
            <div class="form-group">
                <label>Last Name</label>
                <input ng-model="user.lastname" type="text" class="form-control input-large" placeholder="Last Name">
            </div>
            <div class="form-group">
                <label>Username</label>
                <span class="error-msg" ng-show="frm.name.$dirty && frm.name.$error.minlength">Must be more than 3 characters</span>
                <span class="error-msg" ng-show="frm.name.$dirty && frm.name.$error.maxlength">Must not be more than 20 characters</span>
                <span class="error-msg" ng-show="frm.name.$dirty && frm.name.$error.pattern">Alphanumeric characters only</span>
                <span class="error-msg" ng-show="frm.name.$dirty && frm.name.$error.required">username is required</span>
                <input ng-model="user.screenName"  ng-pattern="/^[a-z0-9]+$/i" required ng-minlength="4" ng-maxlength="20" name="name" type="text" class="form-control input-large" placeholder="Username">
            </div>
            <div class="form-group">
                <input ng-model="user.email" type="email" class="form-control input-large inline" placeholder="email" disabled>
                <!--<button class="btn btn-primary">change</button>-->
                <a href="#/settings/email">change</a>
            </div>
            <button ng-click="update()" class="btn btn-primary" ng-disabled="loading || saving || frm.$invalid">
                <span ng-show="!saving">Update</span><span ng-show="saving">Saving...</span>
            </button>
            <button ng-click="loadUser()" class="btn btn-warning" ng-disabled="loading || saving">Reset</button>
        </form>
    </div>
</div>
