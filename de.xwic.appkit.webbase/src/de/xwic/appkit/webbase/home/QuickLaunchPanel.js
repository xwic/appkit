#set($fld = $control.getField("qlItemOrder"))
{
	afterUpdate: function(element) {
		PulseStart.initQuickLaunch("$fld.id");
	}
}

