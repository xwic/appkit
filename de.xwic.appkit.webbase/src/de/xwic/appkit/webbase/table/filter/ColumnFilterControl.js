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
		if (win && (win._positionIndex != $control.positionIdx)) {
			win.hide();
			win.destroy();
			win = null;
		}
		if (!win) {
			// open the window
			
			// find the column
			var myWidth = 300;
			var top = 0;
			var left = 0;
			var header = $("tblViewData_${control.tableViewerId}");
			if (header) {
				var colTbl = header.select("table.tbvColHeader");
				if (colTbl && colTbl.length >= $control.positionIdx) {
					var layout = colTbl[$control.positionIdx].getLayout();
					var lDiv = $("tblViewHead_${control.tableViewerId}");
					top = layout.get('top') + layout.get('height') + 2;
					left = layout.get('left') - lDiv.scrollLeft;
					if (left + myWidth > document.viewport.getDimensions().width - 4) {
						left = document.viewport.getDimensions().width - myWidth - 4;
					}
					
				}
			}
			
			win = new Window({
				className : "j-combo-content",
				#* title: "$escape.escapeJavaScript($control.column.title)", *#
				top : top,
				left : left,
				width : myWidth,
				height: ${control.height},
				resizable : false,
				closable : false,
				minimizable : false,
				maximizable : false,
				draggable : false,
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
			
			win._positionIndex = $control.positionIdx;
			
			if (top == 0 && left == 0) {
				win.showCenter(false);
			} else {
				win.show(false);
			}
			
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