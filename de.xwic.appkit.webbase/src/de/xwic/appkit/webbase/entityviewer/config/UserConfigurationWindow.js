
{
	beforeUpdate: function() {},
	doUpdate: function(element) {
		return false;
	},
	/**
	 * Initialize the window if it does not already exist.
	 */
	afterUpdate: function(element) {
	#if($control.visible)
		var win = Windows.getWindow("$control.controlID");
		if (!win) {
			// open the window
			win = new Window({
				className : "$control.cssClass",
				#if($control.title) title: "$escape.escapeJavaScript($control.title)", #end
				#if($control.width != 0) width : $control.width, #end
				#if($control.height != 0) height : $control.height, #end
				#if($control.top != 0) top : $control.top, #end
				#if($control.left != 0) left : $control.left, #end
				resizable : $control.resizable,
				closable : $control.closable,
				minimizable : $control.minimizable,
				maximizable : $control.maximizable,
				draggable : $control.draggable,
				showEffect: Element.show,
				#* Add IE6 fix *#
				#if(!$control.modal && $control.sessionContext.userAgent.isIE() && $control.sessionContext.userAgent.getMajorVersion() < 7)
					onMove : function() {JWic.controls.Window.adjustIEBlocker("$control.controlID")},
					onResize : function() {JWic.controls.Window.adjustIEBlocker("$control.controlID")},
					onMaximize : function() {JWic.controls.Window.adjustIEBlocker("$control.controlID")},
					onMinimize : function() {JWic.controls.Window.adjustIEBlocker("$control.controlID")},
				#end
				id : "$control.controlID",
				parent : $("jwicform")
			});
			win.setContent("win_${control.controlID}_div", false, false);
			win.setCloseCallback(function () {
				JWic.fireAction("$control.controlID", "close", "");
				return true;
			});
			#if($control.top == 0 || $control.left == 0)
				win.showCenter($control.modal);
			#else
				win.show($control.modal);
			#end
			JWic.addBeforeRequestCallback("$control.controlID", JWic.controls.Window.updateHandler);
			#if(!$control.modal && $control.sessionContext.userAgent.isIE() && $control.sessionContext.userAgent.getMajorVersion() < 7)
				JWic.controls.Window.adjustIEBlocker("$control.controlID");
			#end
		}
	#end
	},
	
	/**
	 * Destroy the window if it still exists.
	 */
	destroy : function(element) {
		var win = Windows.getWindow("$control.controlID");
		if (win && win.destroy) {
			win.hide();
			win.destroy();
		}
	}
}