/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('DComponents', [])
        .directive('dCloseButton', function() {
            'use strict';
            return {
                restrict: 'A',
                priority: -10,
                link: function(scope, element, attr) {
//                    console.log(element);
                    element.addClass('icon-remove').addClass(attr.class).css('cursor','pointer');
                    if(!attr.title) element.attr('title','close');
                }
            };
        });