<%-- 
    Document   : inactive
    Created on : Mar 25, 2015, 5:59:00 PM
    Author     : dubem
--%>

<link href="/codesnippets/resources/css/home.css" rel="stylesheet" type="text/css"/>

<div class="msg-board {{msgtype}}" ng-show="msgtype">
    {{boardMsg}}
    <i d-close-button class="pull-right" style="font-size: 15px" ng-click="msgtype = ''"></i>
</div>
<div class="inactive">

    <section class="middle-page">
        <div class="row">
            <p class="col-md-12" ng-bind="pageMsg"></p>
        </div>

        <div class="row margin-top-10">
            <div class="col-md-12">
                <p>Activation email not received?</p>
                <form class="form-inline" name="frm">
                    <!--<label>change email</label>-->
                    <!--                    <span class="error-msg" ng-show="frm.email.$dirty && frm.email.$error.required">Email is required</span>
                                        <span class="error-msg" ng-show="frm.email.$dirty && frm.email.$error.email">Email is not valid</span>-->
                    <input type="email" name="email"  class="form-control input-medium" ng-model="email" disabled/>
                    <button class="btn btn-success" ng-click="resend()" ng-disabled="resending">
                        <span ng-show="!resending">resend activation email</span>
                        <span ng-show="resending">resending...</span>
                    </button>
                </form>
            </div>
        </div>
    </section>
</div>
