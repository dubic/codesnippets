    var app = angular.module('InputMatch', []);
    app.directive('valMatch', function () {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope: {
                valMatch: '='
            },
            link: function(scope, elem, attrs, ctrl) {
                scope.$watch(function() {
                    var modelValue = ctrl.$modelValue || ctrl.$$invalidModelValue;
                    return (ctrl.$pristine && angular.isUndefined(modelValue)) || scope.valMatch === modelValue;
                }, function(currentValue) {
                    ctrl.$setValidity('match', currentValue);
                });
            }
        };
    });
