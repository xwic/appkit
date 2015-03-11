{//MenuControl.js
    afterUpdate : function(){
        var options = $control.buildJsonOptions();
        options.controlID = '$control.controlID';
        MenuControl.initialize(options);
    },
    destroy : function(){
        var options = $control.buildJsonOptions();
        options.controlID = '$control.controlID';
        MenuControl.destroy(options);
    }
}