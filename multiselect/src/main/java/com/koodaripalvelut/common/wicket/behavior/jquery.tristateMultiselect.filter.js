/*
 * jQuery MultiSelect UI Widget Filtering Plugin 1.2
 * Copyright (c) 2011 Eric Hynds
 *
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/
 *
 * Depends:
 *   - jQuery UI MultiSelect widget
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
*/
(function($){
  var rEscape = /[\-\[\]{}()*+?.,\\^$|#\s]/g;
  
  $.widget("ech.tristatemultiselectfilter", {
    
    options: {
      label: "Filter:",
      width: null, /* override default width set in css file (px). null will inherit */
      placeholder: "Enter keywords"
    },
    
    _create: function(){
      var self = this,
        opts = this.options,
        instance = (this.instance = $(this.element).data("triStateMultiselect")),
        
        // store header; add filter class so the close/check all/uncheck all links can be positioned correctly
        header = (this.header = instance.menu.find(".ui-multiselect-header").addClass("ui-multiselect-hasfilter")),
        
        // wrapper elem
        wrapper = (this.wrapper = $('<div class="ui-multiselect-filter">'+(opts.label.length ? opts.label : '')+'<input placeholder="'+opts.placeholder+'" type="search"' + (/\d/.test(opts.width) ? 'style="width:'+opts.width+'px"' : '') + ' /></div>').prependTo( this.header ));

      // reference to the actual inputs
      this.inputs = instance.menu.find('.element:not(.node-item-clone)');
      
      //TODO selector should change, it is parameterized.
      this.nodes = instance.menu.find('.sublist');
      
      this.nodeItems = instance.menu.find('.node-item-checkbox');
      
      // build the input box
      this.input = wrapper
      .find("input")
      .bind({
        keydown: function( e ){
          // prevent the enter key from submitting the form / closing the widget
          if( e.which === 13 ){
            e.preventDefault();
          }
        },
        keyup: $.proxy(self._handler, self),
        click: $.proxy(self._handler, self)
      });
      
      // cache input values for searching
      this.updateCache();
      
      // rewrite internal _toggleChecked fn so that when checkAll/uncheckAll is fired,
      // only the currently filtered elements are checked
      instance._toggleChecked = function(flag, group, type){
        var $inputs = (group && group.length) ?
            group :
//            this.labels.find('input'),
              this.menu.find('ul.triState').find('a.checkbox'),
          
          _self = this,

          selector = (type == 'radio') ? '' :
        	  // do not include hidden elems if the menu isn't open.
        	  self.instance._isOpen ?
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
      };
      
      // rebuild cache when multiselect is updated
      $(document).bind("multiselectrefresh", function(){
        self.updateCache();
        self._handler();
      });
    },
    
    // thx for the logic here ben alman
    _handler: function( e ){
      var term = $.trim( this.input[0].value.toLowerCase() ),
      
        // speed up lookups
        rows = this.rows, inputs = this.inputs, cache = this.cache, nodes = this.nodes;
      
      if( !term ){
        rows.show();
        nodes.show();
      } else {
        rows.hide();
        nodes.hide();
        
        var regex = new RegExp(term.replace(rEscape, "\\$&"), 'gi');
        
        this._trigger( "filter", e, $.map(cache, function(v,i){
          if( v.search(regex) !== -1 ){
            rows.eq(i).show();
            
            function showParents ($el) {
              var $parentList = $el.parent('li').parent('ul');
              $parentList.parent().children('.sublist').show();
              var $ancestorInput = $parentList.parent('li').children('a.checkbox');
              
              if ($ancestorInput.length > 0) {
                showParents($ancestorInput);
              }
            }
            
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
        
        // account for optgroups
//        if( this.tagName.toLowerCase() === "optgroup" ){
//          self = self.children();
//        }
        
        // see _create() in jquery.multiselect.js
//        if( !self.val().length ){
//          return null;
//        }
        
//        return self.map(function(){
//          return this.innerHTML.toLowerCase();
//        }).get();
        
        return self.attr("title").toLowerCase();
      }).get();
    },
    
    widget: function(){
      return this.wrapper;
    },
    
    destroy: function(){
      $.Widget.prototype.destroy.call( this );
      this.input.val('').trigger("keyup");
      this.wrapper.remove();
    }
  });
})(jQuery);
