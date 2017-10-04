{
	afterUpdate:function (){
		var container = jQuery(document.getElementById('ctrl_${control.controlID}'));
		var textarea = container.find('textarea');
		console.log('${control.controlID}');
		console.log(container);
		if(textarea.length > 0) {
			console.log('ok');
			console.log(container.find('table'));			
			textarea.height(container.find('table').height() - 64);
		} else {
			console.log('not ok');
		}
	}
}