<%-- 
    Document   : snip
    Created on : Mar 2, 2015, 12:50:51 PM
    Author     : dubem
--%>
<style>
    textarea.codearea{
        min-height: 300px;
    }
</style>
<div class="view">
    <div class="middle-page-2">
        <h3>Create a new snippet</h3>
        <hr/>
        <form name="frm" class="form-horizontal">
            <div class="form-group">
                <span class="error-msg" ng-show="frm.title.$dirty && frm.title.$error.required">Title is required</span>
                <label class="col-sm-2 control-label">Title</label>
                <div class="col-sm-6">
                    <input class="form-control" name="title" ng-model="Snip.title" required/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Description</label>
                <div class="col-sm-6">
                    <textarea class="form-control" spellcheck="false" name="desc" ng-model="Snip.desc"></textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Dependencies</label>
                <div class="col-sm-6">
                    <input class="form-control" name="deps" ng-model="Snip.deps"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Language</label>
                <span class="error-msg" ng-show="frm.lang.$dirty && frm.lang.$error.required">Language is required</span>
                <div class="col-sm-6">
                    <select class="input-medium" select2 name="lang" ng-model="Snip.lang" required>
                        <option value="">--Please select--</option>
                        <option ng-repeat="l in langs track by $index" value="{{l}}" ng-bind="l"></option>
                    </select>
                    <!--<button class="btn btn-primary btn-sm">Create <i class="icon-plus"></i></button>-->
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Code</label>
                <div class="col-sm-10">
                    <!--<div class="action-bar"><button class="btn btn-default"><i class="icon-bold"></i></button></div>-->
                    <textarea class="form-control codearea" spellcheck="false" name="code" ng-model="Snip.code" required></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary" ng-click="snip()" ng-disabled="frm.$invalid || saving">
                        <span ng-show="!saving">Save</span>
                        <span ng-show="saving">Saving...</span>
                    </button>
                    <button type="cancel" class="btn btn-danger" ng-click="route('welcome.snippets')">cancel</button>
                </div>
            </div>
        </form>

    </div>
</div>
