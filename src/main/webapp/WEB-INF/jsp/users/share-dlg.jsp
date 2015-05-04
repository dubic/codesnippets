<%-- 
    Document   : share-dlg
    Created on : Apr 1, 2015, 7:51:23 AM
    Author     : dubem
--%>
<script type="text/ng-template" id="shareTypeTemplate.html">
  <a>
      <span bind-html-unsafe="match.label | typeaheadHighlight:query"></span>&nbsp;:
      <span bind-html-unsafe="match.model.email | typeaheadHighlight:query"></span>
  </a>
</script>
<!--LOGIN MODAL BOX-->
<div class="modal fade"  id="shareModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
    <div class="modal-dialog" style="min-width: 40%">
        <div class="modal-content">
            <span class="tooltip in tip" ng-show="tipErrMsg.length > 0" ng-bind="tipErrMsg"></span>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" id="myModalLabel">Share Snippet</h4>
            </div>
            <div class="modal-body">
                <div ng-repeat="a in shareAlerts" class="alert {{a.class}}">{{a.msg}}</div>
                <div class="well well-sm" ng-bind="snippet.title"></div>
               
                <form class="share-form" name="shareform">
                    <table class="share-table">
                        <tr>
                            <td>
                                <input class="form-control" ng-class="{'spinner':loadingUsers}" typeahead="user as user.username for user in getNames($viewValue)" spellcheck="false" 
                                       typeahead-loading="loadingUsers" typeahead-on-select="onselect($item, $model, $label)" ng-model="asyncSelected" 
                                       typeahead-template-url="shareTypeTemplate.html" typeahead-wait-ms="300" placeholder="Username or email" type="text"></td>
                                
                            <td><button class="btn btn-default" ng-click="addToList()" ng-disabled="!asyncSelected">Add</button></td>
                        </tr>
                        <tr class="sharelist animation-fade" ng-repeat="u in sharelist">
                            <td ng-bind="u.name"></td>
                            <td><i class="icon-remove-sign icon-clickable" title="remove" ng-click="sharelist.splice($index, 1);"></i></td>
                        </tr>
                    </table>
                    <textarea ng-show="sharelist.length > 0" ng-model="sharedmsg" placeholder="Add a message"></textarea>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning btn-sm" data-dismiss="modal">cancel</button>
                <button class="btn btn-info btn-sm" ng-disabled="sharelist.length === 0" ng-click="share()">
                    <span ng-hide="sharing">share</span>
                    <span ng-show="sharing">processing...</span>
                </button>
            </div>

        </div>
    </div>
</div>
<!--END OF LOGIN MODAL BOX-->
