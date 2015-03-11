var MenuControl = (function($, JWic, undefined){
    "use strict";
    //some dependencies stored and some local variables
    var util = JWic.util,
        map = util.map,
        each = util.each,
        reduce = util.reduce,
        action = JWic.fireAction,
        //TODO put the templates in the vtl file
        menuHeaderItemTemplate = '<div class="h_item" >'+
                                    '<div class="hmi_body"></div>'+
                                    '<div class="hmi_submenu"></div>'+
                                '</div>',
        menuListItemTemplate = '<li class="hmi_item ui-menu-item" style="display: block;position:relative;">'+
                                    '<a href="javascript:void(0)">'+
                                        '<span class="ui-menu-icon ui-icon ui-icon-carat-1-e"></span>'+
                                        '<span class="title"></span>'+
                                    '</a>'+
                               '</li>',
        menuListTemplate = '<ul class="ui-menu ui-widget ui-widget-content ui-corner-all" style="position:absolute;"></ul>';


    function MenuItem(data, controlId){
        if(!(this instanceof MenuItem)){
            return new MenuItem(data,controlId);
        }
        //private props
        this._controlId = controlId;
        this._key = data.key;
        this._title = data.title;
        this._id = data.id;
        this._selected = false;
        this._children = map(data.children || [], function(data){
            return new MenuItem(data,controlId);
        });
    }

    MenuItem.prototype.children = function(){
        return this._children;
    };
    MenuItem.prototype.addChildren = function(/*...children*/){
        this._children.push.apply(this._children, arguments);
    };
    MenuItem.prototype.hasChildren = function(){
        return this._children.length > 0;
    };
    MenuItem.prototype.title = function(){
        return this._title;
    };
    MenuItem.prototype.key = function(){
        return this._key;
    };
    MenuItem.prototype.id= function(){
        return this._id;
    };
    MenuItem.prototype.selected = function(val){
        if(val != null){
            this._selected = val;
            return this;
        }
        return this._selected;
    };
    MenuItem.prototype.activate = function() {
        action(this._controlId,'MainMenuClick',this.key());
    };
    MenuItem.prototype.each = function(cb) {
        each(this._children, function(i){
            var ret = cb(i);
            return ret || i.each(cb);
        });
    };
    MenuItem.prototype.find = function(cb) {
        var ret;
        this.each(function(i){
            if(cb(i)){
              ret = i;
              return true;
            }
        });
        return ret;
    }

    function ExternalMenuItem(data){
        this._id = null;
        this._key = data.path;
        this._url = data.url;
        this._title = data.title;
        this._selected = false;
        this._children = map(data.children, function(item){
            return new ExternalMenuItem(item);
        });
    }
    ExternalMenuItem.prototype = Object.create(MenuItem.prototype);
    ExternalMenuItem.prototype.activate =  function(){
        window.location.href = this._url;
    }

    ExternalMenuItem.prototype.constructor = ExternalMenuItem;


    MenuItem.itemToNode = function(item){
        var textNode,subMenu,timerId,
            node = $(menuHeaderItemTemplate);

        //set the text and the correct css classes(relevant for style only).
        textNode = node.find('.hmi_body').text(item.title());
        if(!item.hasChildren()){
            textNode.on('click', item.activate.bind(item));//triggers a refresh of the current page
        }else{
            textNode.addClass('hmi_hasChilds');
        }
        textNode.addClass('hmi_isSelected');
        subMenu = node.find('.hmi_submenu');

        buildMenu(item, subMenu, node);
        //bind actions
        node.on('mouseenter',function (){
            var that = $(this);
            that.addClass('h_item_hover').find('.hmi_submenu').show().children('ul').show();
            if(timerId){
                window.clearTimeout(timerId);
            }
        });
        node.on('mouseleave',function (){
            var that = $(this);
            timerId = window.setTimeout(function(){
                that.removeClass('h_item_hover').find('ul');
                that.find('.hmi_submenu').hide();
                that.find('li').removeClass('ui-state-focus');
                that.find('ul').hide();

            },150);
            return false;
        });

        return node;
    };

    function buildTopLevel(rootItem, path, control, options){
        var menuItems,nodes,rootNode,
            paths = path.split(';');//create the path

        //convert path to menuItems
        menuItems = map(paths, function(p){
            //find the correct item from the rootItem
            return rootItem.find(function(i){
                return i.id() === p;//where the id of the item is equal to the current path string
            });
        });
        each(menuItems, function(item){
            item.selected(true);
        });
        //create the root item to a node
        //the rootNode is a special case
        //so treat it separately the the rest
        var additionalMenuItems = map(options.additionalItems, function (item){
            return new ExternalMenuItem(item);
        });

        rootItem.addChildren.apply(rootItem, additionalMenuItems);

        rootNode = MenuItem.itemToNode(rootItem);
        //convert menuItems to nodes
        nodes = map(menuItems, MenuItem.itemToNode);


        //collapse the nodes to into a single element (i.e. append to container div)
        var container = reduce(nodes, function(control, item){
            return control.append(item);
        }, control.append(rootNode));
    }

    function findParent(element,parentType){
        var clazz = element.hasClass(parentType),
            tag = element.prop('tagName');

        if(clazz || tag === 'BODY'){
            return element;
        }
        return findParent(element.parent(),parentType);
    }

    function activateListItem(anchor){
        var a = $(anchor).parent(),
            ul = $(anchor).children('ul');

        $(anchor).addClass('ui-state-focus');
        ul.css({
            left : a.width(),
            top : -1
        });
        ul.show();
    }

    function deactivateListItem(li){
        $(li).removeClass('ui-state-focus');
        $(li).find('ul').hide();
    }

    function exitListItem(li){
        return true;
    }
    function buildMenu(item, container){
        var list;
        if(!item.hasChildren()){
            return;
        }
        list = $(menuListTemplate);
        container.append(list);
        each(item.children(),function(i){
            var li = $(menuListItemTemplate),
                a  = li.find('a').find('.title');
            list.append(li);
            a.text(i.title());
            if(i.selected()){
                a.addClass('menu_selected');
            }
            if(!i.hasChildren()){
                li.on('click',i.activate.bind(i));
                a.parent().find('.ui-menu-icon').removeAttr('class');
            }
            buildMenu(i, li);
        });
        //activate menuAim plugin
        list.menuAim({
           activate: activateListItem,
           deactivate: deactivateListItem,
           exit : exitListItem,
           exitMenu : exitListItem,
           tolerance : 100
        }).hide();

    }



     return {/*exports*/
        initialize : function(options){
            var menuItem = new MenuItem(options.rootItem, options.controlID),
                control = JWic.$(options.controlID);

            buildTopLevel(menuItem,options.activeModuleKey, control, options);
        },
        destroy : function(options){
        }
    };
}(jQuery, JWic));
