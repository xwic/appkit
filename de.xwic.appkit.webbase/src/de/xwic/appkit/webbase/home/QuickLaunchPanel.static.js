
var PulseStart = {
	
	ignoreClick : false,
	fieldId : null,
	initQuickLaunch : function(fldId) {
		
		PulseStart.fieldId = fldId;
		JWic.$("gtQuickLaunchItems").sortable({
			  stop: PulseStart.qlStopHandler
		});
		JWic.$("gtQuickLaunchItems").disableSelection();
		jQuery("#gtQuickLaunchItems li").click(this.qlClickHandler);
		
	},
	
	qlStopHandler : function(e, ui) {
		JWic.log("bla: " + PulseStart.fieldId);
		PulseStart.ignoreClick = true;
		// figure out the new position(s)
		var elms = jQuery("#gtQuickLaunchItems").find("li.gtQLItem");
		JWic.log("Build IDS" + elms);
		var ids = "";
		elms.each(function(idx, val) {
			ids += jQuery(val).attr("pQlItemId") + ";";
		});
		JWic.log("SetValues: " + PulseStart.fieldId);
		JWic.$(PulseStart.fieldId).val(ids);
	},
	
	qlClickHandler : function(e) {
		if (PulseStart.ignoreClick) {
			PulseStart.ignoreClick = false;
		} else {
			JWic.log(e);
			var card = jQuery(e.currentTarget);
			var res = card.attr("pQlItemRes")
			var ctrlId = card.attr("pQlCtrlId");
			JWic.fireAction(ctrlId, "launch", res);
		}
	}
}