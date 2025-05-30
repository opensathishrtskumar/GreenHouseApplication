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
	
});
