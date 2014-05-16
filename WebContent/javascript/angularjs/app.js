'use strict';

function isNumberKey(ele, evt){
	if($(ele).val().length > 2) {
		return false;
	}
	
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

var ezScrum = angular.module('ezScrum', ['ng-context-menu', 'ui.utils', 'ui.bootstrap'])
	.directive('draggable', ['$document' , function($document) {
	  return {
	    restrict: 'A',
	    link: function(scope, elm, attrs) {
	      var startX, startY, initialMouseX, initialMouseY;
	      
	      elm.css({position: 'absolute'});
	
	      elm.bind('mousedown', function($event) {
	        startX = elm.prop('offsetLeft');
	        startY = elm.prop('offsetTop');
	        initialMouseX = $event.clientX;
	        initialMouseY = $event.clientY;
	        $document.bind('mousemove', mousemove);
	        $document.bind('mouseup', mouseup);
	        return false;
	      });
	
	      function mousemove($event) {
	        var dx = $event.clientX - initialMouseX;
	        var dy = $event.clientY - initialMouseY;
	        elm.css({
	          top:  startY + dy + 'px',
	          left: startX + dx + 'px'
	        });
	        return false;
	      }
	
	      function mouseup() {
	        $document.unbind('mousemove', mousemove);
	        $document.unbind('mouseup', mouseup);
	      }
	    }
	  };
	}]);

ezScrum.directive('multiselectDropdown', [function() {
    return function(scope, element, attributes) {
        
        qq = $(element[0]); // Get the element as a jQuery element
        
        qq.multiselect();
        // Below setup the dropdown:
        
//        element.multiselect({
//            buttonClass : 'btn btn-small',
//            buttonWidth : '200px',
//            buttonContainer : '<div class="btn-group" />',
//            maxHeight : 200,
//            enableFiltering : true,
//            enableCaseInsensitiveFiltering: true,
//            buttonText : function(options) {
//                if (options.length == 0) {
//                    return element.data()['placeholder'] + ' <b class="caret"></b>';
//                } else if (options.length > 1) {
//                    return _.first(options).text 
//                    + ' + ' + (options.length - 1)
//                    + ' more selected <b class="caret"></b>';
//                } else {
//                    return _.first(options).text
//                    + ' <b class="caret"></b>';
//                }
//            },
//            // Replicate the native functionality on the elements so
//            // that angular can handle the changes for us.
//            onChange: function (optionElement, checked) {
//                optionElement.removeAttr('selected');
//                if (checked) {
//                    optionElement.attr('selected', 'selected');
//                }
//                element.change();
//            }
//            
//        });
        
        element.multiselect();
        // Watch for any changes to the length of our select element
        scope.$watch(function () {
            return element[0].length;
        }, function () {
            element.multiselect('rebuild');
        });
        
        // Watch for any changes from outside the directive and refresh
        scope.$watch(attributes.ngModel, function () {
            element.multiselect('refresh');
        });
        
        // Below maybe some additional setup
    }
}]);

ezScrum.controller('ProductBacklogController', function($scope, $http) {
	$scope.isEditMode = false;
	$scope.isCreateMode = false;
	$scope.currentStory = {};
	
	var init = function() {
		$http({method: 'GET', url: '/ezScrum/web-service/' + getQueryStringByName('PID') + '/product-backlog/storylist?userName=' + getCookie('username') + '&password=' + getCookie('userpwd')}).
		    success(function(data, status, headers, config) {
		    	$scope.storyList = data;
		    }).
		    error(function(data, status, headers, config) {
		    });
		
		$http({method: 'GET', url: '/ezScrum/web-service/' + getQueryStringByName('PID') + '/product-backlog/taglist?userName=' + getCookie('username') + '&password=' + getCookie('userpwd')}).
		    success(function(data, status, headers, config) {
		    	$scope.tagList = data;
		    }).
		    error(function(data, status, headers, config) {
		    });
	
	}
	
	var updateStory = function(tmpStory) {
		var data = {
			name: tmpStory.name,
			value: tmpStory.value,
			estimation: tmpStory.estimation,
			notes: tmpStory.notes,
			importance: tmpStory.importance,
			howToDemo: tmpStory.howToDemo,
			id: tmpStory.id
		}
		$http.put('/ezScrum/web-service/' + getQueryStringByName('PID') + '/story/update?userName=' + getCookie('username') + '&password=' + getCookie('userpwd'), data).
			success(function(data, status, headers, config) {
		    	init();
		    });
	}
	
	var createStory = function(tmpStory) {
		var data = {
			name: tmpStory.name,
			value: tmpStory.value,
			estimation: tmpStory.estimation,
			notes: tmpStory.notes,
			importance: tmpStory.importance,
			howToDemo: tmpStory.howToDemo
		}
		$http.post('/ezScrum/web-service/' + getQueryStringByName('PID') + '/story/create?userName=' + getCookie('username') + '&password=' + getCookie('userpwd'), data).
			success(function(data, status, headers, config) {
		    	init();
		    });
	}
	
	var deleteStory = function(story) {
		$http.delete('/ezScrum/web-service/' + getQueryStringByName('PID') + '/product-backlog/storylist/' + story.id + '?userName=' + getCookie('username') + '&password=' + getCookie('userpwd')).
			success(function(data, status, headers, config) {
		    	init();
		    });
	}
	
	$scope.enterCreateMode = function() {
		$scope.boxTitle = 'Create new story';
		$scope.isCreateMode = true;
	}
	
	$scope.enterEditMode = function(story) {
		$scope.boxTitle = 'Story #' + story.id;
		$scope.tmpStory = {
			id: story.id,
			name: story.name,
			notes: story.notes,
			howToDemo: story.howToDemo,
			estimation: story.estimation,
			value: story.value,
			importance: story.importance
		}
		
		$scope.isEditMode = true;
	}
	
	$scope.save = function(tmpStory) {
		if($scope.tmpStory.name.trim() == '') {
			alert("Please enter story's name");
			return;
		}
		
		if($scope.isEditMode) {
			updateStory(tmpStory);
		} else if($scope.isCreateMode) {
			createStory(tmpStory);
		}
		$scope.cancel();
	}
	
	$scope.apply = function(tmpStory) {
		if($scope.tmpStory.name == '') {
			alert("Please enter story's name");
			return;
		}
		updateStory(tmpStory);
	}
	
	$scope.cancel = function() {
		$scope.isEditMode = false;
		$scope.isCreateMode = false;
		
		$scope.tmpStory = {};
	}

	$scope.onRightClick = function(story) {
		$scope.currentStory = story;
	}
	
	$scope.isNumber = function(input) {
		if(!isNumber(input)) {
			input = 0;
		}
	}
	
	$scope.delete = function(story) {
		if(confirm('Delete this story?')) {
			deleteStory(story);
		}
	}
	
	$scope.goBack = function() {
		location.assign('/ezScrum/viewProject.do?PID=' + getQueryStringByName('PID'));
	}
	
	$scope.escTriggered = function() {
		$scope.cancel();
	}
	
	init();
});