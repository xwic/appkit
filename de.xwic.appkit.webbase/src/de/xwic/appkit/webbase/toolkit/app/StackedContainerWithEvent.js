{//StackedContainer.js
	afterUpdate: function(element) {
		#if($control.scrollTop != -1)
		window.scrollTo($control.scrollLeft, $control.scrollTop)
		#* if the page is updated "normaly", the scrollTo must be initiated after some time-out *#
		window.setTimeout("window.scrollTo($control.scrollLeft, $control.scrollTop)", 200);
		#end
	}
}