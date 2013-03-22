
{
	afterUpdate: function(element) {
		#if($control.closeMe)
			JWic.fireAction('$control.controlID', 'closeDialog', '');
		#end
	}
}
