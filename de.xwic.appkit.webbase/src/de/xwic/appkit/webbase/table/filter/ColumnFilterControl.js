{
	/**
	 * ColumnFilterControl.js
	 */	
	doUpdate: function(){		
		return false;
	},
	
	/**
	 * Initialize the window if it does not already exist.
	 */
	 
	afterUpdate: function(element) {
		
		var dialogWidth = $control.width;
		//dialogWidth += dialogWidth*10/100;
		var dialogHeight= $control.height;
		dialogHeight+= dialogHeight*10/100;
		
		var win = jQuery('#'+JWic.util.JQryEscape('filter_${control.controlID}_div')).dialog({
			autoOpen: false,
			width : dialogWidth,
			//height : dialogHeight,
			closeOnEscape: false,
			open: function(event, ui) {
				jQuery(".ui-dialog-titlebar",jQuery(this).parent()).hide();
				jQuery(".ui-dialog-titlebar-close",jQuery(this).parent()).hide(); 
			},			
			draggable:false,
			resizable : false,
			modal: false,
			cache:false
					
		});
		
		
		win.parent().appendTo(jQuery("#jwicform"));		
		win.parent().removeClass('ui-widget-content');
		win.parent().addClass('whiteBg');
		win.dialog('open');	
		
		
		var header = jQuery('#'+JWic.util.JQryEscape('tblViewData_${control.tableViewerId}'));
		if(header){
			var column = jQuery(jQuery('table.tbvColHeader',header)[$control.positionIdx]);
			var offset = column.offset();
			win.dialog('option','position',[offset.left,offset.top+column.height()])
			
		}	
			
	},
	
	/**
	 * Destroy the window if it still exists.
	 */
	destroy : function(element) {
		var win = jQuery('#'+JWic.util.JQryEscape('filter_${control.controlID}_div'));	
		win.dialog('destroy').remove();
		
	}
}