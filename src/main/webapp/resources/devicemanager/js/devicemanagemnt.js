$(document).ready(function() {
	
	//Constants
	var regx = /\d+/g ;
	
	///to get min and max memorymapping text area
	var minMemorymappingCount = $("#memorymappingcount").attr('min');
	var maxMemorymappingCount = $("#memorymappingcount").attr('max');
	var accordionIndex = $("#showaccordion").val();
	console.log("Active accordion : " + accordionIndex);
	
	$("#accordion").accordion({
		header : "> div > h3",
		animate: 200,
		heightStyle: "content",
		icons: { header: "ui-icon-circle-arrow-e", activeHeader: "ui-icon-circle-arrow-s"},
		active: parseInt(accordionIndex), 
		collapsible: true
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
			var response = invokeAPI(url,'POST',data,false);
			
			// update status in UI - success or failure
			$("#adddevice").attr("disabled", true);
			if(response.code == 0 && response.data.statusCode == 0)  {
				$("#adddevice").attr("disabled", false);
				alert(response.data.statusDescription);
			} else {
				alert(response.data.statusDescription);
			}
			
		});
	});
	
	//When there is a change in form, disable Add Device button
	$("#addDeviceForm :input").change(function() {
		 console.log('change detected in the form, disabled until testconnection is succcessful');
		 $("#adddevice").attr("disabled", true);
	});
	
	
	//On click, add additional memory mapping text area with proper array index
	$('.addmemorymapping').on('click',function(e){
		var mappingParent = $(this).parent().closest('form').find("#mappingDetails");
		var memoryMappings = $(this).parent().closest('form').find(".memoryMapping");
		console.log("Current text area count : " + memoryMappings.length);
		
		//Add additional textarea when count not breached
		if(memoryMappings.length < parseInt(maxMemorymappingCount)){
			var additional = $(mappingParent).children(":first").clone();
			//Updating array index
			var name = $(additional).attr('name').replace(regx,memoryMappings.length);
			var id = $(additional).attr('id').replace(regx,memoryMappings.length);
			
			additional  = $(additional).attr('id',id).attr('name',name).val('');
			
			$(additional).appendTo(mappingParent);
			$("#adddevice").attr("disabled", true);//Disable when new memory mapping added
			console.log("additional txt area added");
		} else {
			console.log("Cann't add additional txt area, limit breached");
		}
	});
	
	//On click, remove additional memory mapping text area at the end
	$('.removememorymapping').on('click',function(e){
		var mappingParent = $(this).parent().closest('form').find("#mappingDetails");
		var memoryMappings = $(this).parent().closest('form').find(".memoryMapping");
		console.log("Current text area count : " + memoryMappings.length);
		if(memoryMappings.length > parseInt(minMemorymappingCount)){
			$(mappingParent).children(":last").remove();
			console.log("additional txt area removed");
		} else {
			console.log("Cann't remove txt area, limit breached");
		}
	});
	
	
	function invokeAPI(api,method,data,async = false,contentType = "application/json"){
		var response = {};
		console.log("Invoking API " + api + " ,Data :" + data);
		$.ajax(api, {
		    type: method,  // http method
		    data: data,  // data to submit
		    contentType : contentType,
		    async : async,
		    //code 0 means success and non zero is failure
		    success: function (data, status, xhr) {
		    	response['data'] = data;
		    	response['status'] = status;
		    	response['xhr'] = xhr;
		    	response['code'] = 0;
		    },
		    error: function (jqXhr, textStatus, errorMessage) {
		    	response['code'] = -1;
		    	response['jqXhr'] = jqXhr;
		    	response['textStatus'] = textStatus;
		    	response['errorMessage'] = errorMessage;
		    }
		});
		console.log("Invoked APIs " + api + " ,Response : " + JSON.stringify(response));
		return response;
	}
	
	
});
