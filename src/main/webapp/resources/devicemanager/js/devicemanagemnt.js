$(document).ready(function() {
	
	$("#accordion").accordion({
		header : "> div > h3",
		animate: 200,
		heightStyle: "content",
		icons: { "header": "ui-icon-plus", "activeHeader": "ui-icon-minus" }
	}).sortable({
		axis : "y",
		handle : "h3",
		stop : function(event, ui) {
			// IE doesn't register the blur when sorting
			// so trigger focusout handlers to remove .ui-state-focus
			ui.item.children("h3").triggerHandler("focusout");

			// Refresh accordion to handle new order
			$(this).accordion("refresh");
		}
	});
	
	$("#testconnection").on("click",function(e){
		e.preventDefault();
		var url = $("#testUrl").val();
		var form = $(this).parent().closest("form");
		var memoryMappings = [];
		
		$(form).find(".memoryMapping").each(function(index){
			memoryMappings.push({"memoryMapping" : $(this).val()});
		});
		console.log(memoryMappings);
		//create memory mapping array object for RestAPI invocation
		
		//Get current elements parent which is form and serialize
		$(form).serializeObject().done(function(object){
			object.memoryMappings = memoryMappings;
			var data = JSON.stringify(object);
			console.log(data);
			var response = invokeAPI(url,'POST',data,true);
		});
	});
	
	
	function invokeAPI(api,method,data,async = false,contentType = "application/json"){
		var response = {};
		$.ajax(api, {
		    type: method,  // http method
		    data: data,  // data to submit
		    contentType : contentType,
		    async : async,
		    success: function (data, status, xhr) {
		    	response.data = data;
		    	response.status = status;
		    	response.data = data;
		    },
		    error: function (jqXhr, textStatus, errorMessage) {
		    	response.jqXhr = jqXhr;
		    	response.textStatus = textStatus;
		    	response.errorMessage = errorMessage;
		    }
		});
		return response;
	}
	
	
});
