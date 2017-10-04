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
	
		var contentField = JWic.$("changeLog_$control.controlID");
		var dataField = JWic.$('$control.getField("data").getId()');
		Sandbox.ChangeLogViewer.render(contentField, dataField);
		
	},
	
	/**
	 * Invoked when the existing element is removed from the DOM tree.
	 */
	destroy : function(element) {
		
	}
}