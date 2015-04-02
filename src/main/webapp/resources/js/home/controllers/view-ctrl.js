/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

ctrls.controller('viewsnCtrl', function($scope, $http, services, $rootScope, $timeout, postsPath, $stateParams) {

    $scope.code = '<![CDATA[LoadModule proxy_ajp_module modules/mod_proxy_ajp.so'
            + '<VirtualHost localhost:80>'
            + 'ServerName  adapter'
            + 'ServerAdmin root@ucitech.com.ng'

            + 'ProxyPass        / ajp://localhost:8009/'
            + 'ProxyPassReverse / ajp://localhost:8009/'
            + '</VirtualHost>]]>';

//    console.log($stateParams);
    $rootScope.isMefn($stateParams.id);
    if (angular.isUndefined($stateParams.id)) {
        console.log('no snippet id passed.So unlikely');
        return;
    }
    $scope.loading = true;
    $http.get(postsPath + '/snippet/view/' + $stateParams.id+'?edit=false').success(function(resp) {
        $scope.loading = false;
        $scope.snippet = resp;
//        $scope.prettyprint = 'prettyprint';
//        prettyPrint();
//        for (var i = 0; i < $scope.snippet.code.length; i++) {
//            if($scope.snippet.code[i].startsWith(' ')){
//                $scope.snippet.code[i] = '    '+$scope.snippet.code[i];
//                console.log($scope.snippet.code[i]);
//            }
//            
//        }
//        $scope.snippet.code = $scope.code;
    }).error(function(r) {
        $scope.loading = false;
    });
    
    $scope.dom = '&lt;!DOCTYPE html&gt;&lt;html lang="en"&gt;&lt;/html&gt;';

});
