
{
	
	afterUpdate: function(element) {
		#if($control.destroyNow)
			JWic.fireAction('$control.controlID', 'closeDialog', '');
		#end
	}
}
