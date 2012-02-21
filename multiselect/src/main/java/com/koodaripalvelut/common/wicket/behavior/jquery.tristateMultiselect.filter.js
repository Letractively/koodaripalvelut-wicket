/**
 * 
 */

(function($) {

  $.widget("ech.tristatefilter", {

    _create : function() {
      
      instance = (this.instance = $(this.element).data("multiselect"));

      var filterInstance = (this.filterInstance = $(this.element).data("multiselectfilter"));
      
      instance._toggleChecked = this._toggleChecked;
      
      filterInstance.nodes = instance.menu.find('.tristate-node:not(.tristate-root' + (instance.options.multiple ? ')' : ', .checkbox)'));
      filterInstance.nodeItems = instance.menu.find('.node-item-checkbox');
      filterInstance.inputs = instance.menu.find('.element:not(.node-item-clone)');
      filterInstance.handler = this._handler;
      filterInstance.updateCache = this.updateCache;
      filterInstance.updateCache();
      
    },
    
    _toggleChecked: function(flag, group, type){
      
      //On "uncheck all" clean selectedHidden if any.
      if (group == undefined && !flag) self.selectedHidden = {};
      
      var $inputs = (group && group.length) ?
          group :
            this.menu.find('ul.triState').find((flag ? 'a' : '') + '.checkbox'),
        
        _self = this,

        selector = (type == 'radio') ? '' :
          // do not include hidden elems if the menu isn't open.
          _self.isOpen() ?
          ":disabled, :hidden" :
          ":disabled";

      $inputs = $inputs.not( selector ).each(this._toggleCheckbox('checked', flag));
      
      if (flag) {
        $inputs.addClass('checked');
      } else {
        $inputs.removeClass('checked partial');
      }
      
      // update text
      this.update();
      
      // figure out which option tags need to be selected
      var values = $inputs.map(function(){
        return $(this).attr('optionvalue');
      }).get();
      
      // select option tags
      this.element
        .find('option')
        .filter(function(){
          if( !this.disabled && $.inArray(this.value, values) > -1 ){
            _self._toggleCheckbox('selected', flag).call( this );
          }
        });
    },
    
    // thx for the logic here ben alman
    _handler: function( e ){
      var $this = this;
      var term = $.trim( this.input[0].value.toLowerCase() ),
      
        // speed up lookups
        rows = this.rows, inputs = this.inputs, cache = this.cache, nodes = this.nodes;
      
      var inst = $($this.element).data("triStateMultiselect");
      if( !term ){
        rows.show();
        nodes.show();
        
      } else {
        rows.hide();
        nodes.hide();
        
        var regex = new RegExp(term.replace(this.rEscape, "\\$&"), 'gi');
        
        this._trigger( "filter", e, $.map(cache, function(v,i){
          if( v.search(regex) !== -1 ){
            
            function showParents ($el) {
              var $parentList = $el.parent('li').parent('ul');
              $parentList.parent().children('.tristate-node:not(.tristate-root' + (this.instance.options.multiple ? ')' : ', .checkbox)')).show();
              var $ancestorInput = $parentList.parent('li').children('a.checkbox');
              
              if ($ancestorInput.length > 0) {
                showParents($ancestorInput);
              }
            }
            
            var row = rows.eq(i);
            
            row.siblings("label").show();
            row.show();
            
            $input = $(inputs.get(i));
            
            showParents($input);
            
            return $input;
          }
          
          return null;
        }));
      }

      // show/hide optgroups
      this.instance.menu.find(".ui-multiselect-optgroup-label").each(function(){
        var $this = $(this);
        $this[ $this.nextUntil('.ui-multiselect-optgroup-label').filter(':visible').length ? 'show' : 'hide' ]();
      });
    },
    
    updateCache: function(){
      // each list item
      this.rows = this.instance.menu.find(".ui-multiselect-checkboxes li:not(.ui-multiselect-optgroup-label, .node-li, .node-item-clone-li), .tristate-node-element");
      
      // cache
      this.cache = this.inputs.map(function(){
        var self = $(this);
        
        return self.attr("title").toLowerCase();
      }).get();
    }
  });
})(jQuery);