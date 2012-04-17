/**
 * This is a basic construct of control JavaScript snippets.
 */
 
{
	/**
	 * Invoked before the element is updated.
	 */ 
	beforeUpdate: function() {
		
	},
	
	/**
	 * Invoked when the element needs to be updated. If this function returns
	 * false, the existing HTML element is replaced by the rendered part that
	 * comes from the server. If the script is doing the update, it should return
	 * true, to prevent the update.
	 */
	doUpdate: function(element) {
		return false;
	},
	/**
	 * Invoked after the DOM element was updated. This function is NOT updated if
	 * the custom doUpdate function returned true.
	 */
	afterUpdate: function(element) {
		
		
		var elm = $("${control.controlID}_graph");
		var elmScale = $("${control.controlID}_scale");
		var elmScaleH = $("${control.controlID}_hScale");
		if (elm) {
			#if(!$control.hasData)
				elm.update("No data available.");
			#else
				elm.update("Loading...");
		
				var param = new Hash();
				JWic.resourceRequest("${control.controlID}", function(ajaxResponse) {
					try {
						var response = ajaxResponse.responseText.evalJSON(true);
						if (response.maxScale) {
							elmScale.update(parseInt(response.maxScale) + response.measureTitle);
							var code = "<table width=\"100%\" cellspacing=0 cellpadding=0><tr>";
							var data = $A(response.data);
							var height = elm.getHeight() - 2;
							data.each(function(slice) {
								var vH = height * (slice.value / response.maxScale);
								code += "<td valign=\"bottom\">";
								code += "<div style=\"height: " + parseInt(vH) + "px; background-color: #0000FF;\"></div>";
								code +="</td>";
							});
							for (var i = data.length; i < 120; i++) {
								code += "<td></td>";
							}
							
							code += "</tr></table>";
							
							if (data.length > 0) {
								var scale = data[0].title + " - " + data[data.length - 1].title;
								elmScaleH.update(scale);
							}
						}
						elm.update(code);
						
					} catch (x) {
						alert(x);
					}
				}, param);
				
			#end
		}
		
	},
	
	/**
	 * Invoked when the existing element is removed from the DOM tree.
	 */
	destroy : function(element) {
		
	}
}