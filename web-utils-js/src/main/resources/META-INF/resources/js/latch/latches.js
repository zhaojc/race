if(clocking === undefined) {
  var clocking = {};
}

clocking.latches = function() {
	var timeLatch = function(interval, latchActions, url) {
		var intervalId;
		
		var start = function(count) {
			intervalId = setInterval(checkRemaining, interval);
		};
		
		var checkRemaining = function() {
			var callback = function (response) {
				var remaining = response.remaining;
				if (remaining < 0) {
					console.log("exp resp: " + JSON.stringify(response));
					expire(response);
				} else {
					console.log("wait resp: " + JSON.stringify(response));
					latchActions.wait(remaining, response);
				}
			};
			
			ajaxRequest().done(callback);
		};
		
		var expire = function(response) {
			console.log("exp2 resp: " + JSON.stringify(response));
			clearInterval(intervalId);
			latchActions.expire(response);
		};
		
		var ajaxRequest = function() {
			return $.ajax({
				url : url,
				type : "GET",
				dataType : "json",
			});
		};
		
		return {
			start : start,
			expire : expire
		};
	};
	
	return {
		timeLatch : timeLatch
	};
}();