/**
 * 
 */


(function($) {
  $.widget("ech.triStateMultiselect", {
    
    options: {
      labelClass: 'tristateMultiselect-label',
      showMissingParent: false
    },

    _create : function() {
      
      this.instance = $(this.element).data("multiselect");
      
      $.extend(this.instance.options, this.options);

      // add class needed by tristate plugin.
      this.instance.checkboxContainer.addClass('triState');

      this.instance.nodeCount = 0;
      
      this.instance.update = this.update;
      
      this.instance.refresh = this.refresh;
      
      this.instance._toggleChecked = this._toggleChecked;
      
      this._bindEvents();
      this.instance.refresh();
      
    },

    _init : function(multInitFn) {

      // Apply tristate plugin.
      this.instance.checkboxContainer.tristate({
        heading : 'span.heading',
        multiple : this.instance.options.multiple
      });

      // Uncheck all radios. Only checkboxes need to be checked.
      this.instance.checkboxContainer.find('input[type="radio"]').each(function() {
        $(this).attr('checked', false);
      });

      this.instance.button[0].defaultValue = this.instance.update();
    },
    
    refresh: function( init ) {
      var el = this.element,
        o = this.options,
        menu = this.menu,
        checkboxContainer = this.checkboxContainer,
        optgroups = [],
        html = [],
        result = {},
        parentIdElementMap = {},
        parentElements = {}, //Elements that are nodes and item at a time.
        nullOptions = [],
        nullId = "tristate-multiselect-nullId"
        $this = this,
        id = el.attr('id') || multiselectID++; // unique ID for the label &
                                                // option tags
      
      function getParentId(el) {
        
        function getClassParentId() {
          
          var elClasses = $(el).attr('class');
          
          if(elClasses == undefined || elClasses == "") {
            return;
          }
          
          for (i=0; i <  elClasses.split(" ").length; i++) {
            
            var elClass = elClasses[i];
            
            if (elClass.indexOf("tsp-") == 0) {
              return elClass.substring(4, elClass.length);
            }
            
          }
        }
        
        var parentId = getClassParentId();
        
        if (parentId == undefined) {
          var parent = el.parentNode;
          if (parent.tagName.toLowerCase() === 'optgroup') {
            var optLabel = parent.getAttribute('label');
            $(el).addClass("tsp-" + optLabel.replace(/\s/gi, '&nbsp;'));
            return optLabel;
          }
          return nullId;
        }
      }
      
      var optionss = this.element.find('option');
      
      // Organizes options hierarchically
      var emptyElements = {};
      this.element.find('option').each(function( i ) {
        
        function createParent(optParentId) {
          
          var parent = {};
          
          parent["itself"] = undefined;
          parent["array"] = {};
          
          //set parent in root node.
          result[optParentId] = parent;
          
          parentIdElementMap[optParentId] = parent;
          
          return parent;
        }
        
        // Generate random string for id's
        var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        var randomstring = '';
        for (var i=0; i<4; i++) {
          var rnum = Math.floor(Math.random() * chars.length);
          randomstring += chars.substring(rnum,rnum+1);
        }
        
        $this.optionIds = [];
        if($(this).attr('id')) {
          $this.optionIds.push($(this).attr('id'));
        } else {
          var elid = "tristate-option-"+randomstring+i;
          $(this).attr('id',elid);
          $this.optionIds.push(elid);
        }
        
        var optParentId = getParentId(this),
        title = $(this).text(),
        parent = parentIdElementMap[optParentId]
        obj = {};
        obj["itself"] = this;
        obj["array"] = {};
        
        if (parentIdElementMap[title]) {//it is an existing node
          var element = parentIdElementMap[title];
          obj["array"] = element["array"];
          delete parentIdElementMap[title];
          delete result[title];
        } 
        
        if(title == "") { //Empty value
          emptyElements[this.value] = obj;
          return;
          
        } else if (optParentId == nullId) { // belongs to root.
          result[title] = obj;
          parentIdElementMap[title] = obj;
          return;
        } else if (parent == undefined) { // Parent is not yet registered.
          parent = createParent(optParentId);
        }
        
        parentIdElementMap[title] = obj;
        // add option to its parent.
        var parentArray = parent["array"]
        parentArray[title] = obj;
        
      });
      
      function build(arr, level, emptyElement) {
        var i = 0;
        var mmm = arr;
        $.each(arr, function(title, v) {
          var obj = v["itself"],
          array = v["array"],
          $this = $(obj), 
          parent = obj == undefined ? undefined : obj.parentNode,
          value =  obj == undefined ? undefined : obj.value,
          inputID = 'ui-multiselect-'+id+'-option-'+ (i++), 
          isDisabled = obj == undefined ? undefined : obj.disabled,
          isSelected = obj == undefined ? undefined : obj.selected,
          optid = $this.attr('id'),
          labelClasses = ['ui-corner-all'],
          className = obj == undefined ? undefined : obj.label,
          optcontainer = $this.attr('optcontainer'),
          optLabel;
          
          var escTitle = emptyElement ? "" : title.replace(/\"/gi, "&quot;");
          
          function createIsSelectedAttr() {
            var buf = "";
            if ( isSelected ) {
              buf += ' checked="checked"';
              buf += ' aria-selected="true" ';
            }
            return buf;
          }
          
          function createIsDisabledAttr() {
            var buf = "";
            if ( isDisabled ) {
              buf += ' disabled="disabled"' ;
              buf += ' aria-disabled="true" ';
            }
            return buf;
          }
          
          function convertToAnchor(classes) {
            var buf = '<a id="anchor-'+ inputID +'"';
            buf += ' href="#" ';
            buf += ' class="checkbox element '+ (isSelected ? 'checked ' : '') + (classes ?  classes : '')+'"';
            buf += ' title="'+escTitle+'"';
            buf += ' optionvalue="'+ value +'"';
            buf += createIsSelectedAttr();
            buf += createIsDisabledAttr();
            buf += ' ></a>';
            return buf;
          }
          
          function convertToRadio() {
            var buf = '<input id="radio-' + inputID + '"';
            buf += ' type="radio"';
            buf += ' class="checkbox radiobutton"'; 
            buf += ' checkbox="' + inputID +'"';
            buf += ' style="float: left;"';
            buf += createIsSelectedAttr();
            buf += createIsDisabledAttr();
            buf += ' /a>';
            return buf;
          }
          
          function createLabel(classes, isANode) {
            //this span fixes single selection weird behavior.
            var buf = '<span class="tristate-node" style="float: left;">&nbsp;</span>'; 
            buf += '<label class="ui-corner-all ' + o.labelClass + ' ' + classes +'"';
            buf += ' for = "' + (o.multiple ? 'anchor-' : 'radio-') + inputID +'"';
            buf += ' style= "margin-left: ' + (-18.5 * level + (isANode ? 2 : 0))  + 'px;"';
            buf += ' level = ' + level + '>';
            return buf;
          }
          
          function isEmpty(obj) {
            for (key in obj) {
                return false;
            }
            return true;
          };
          
          if (!o.showMissingParent && obj == undefined) {
            build(array, level);
          } else if (isEmpty(array)) {
            html.push('<li class="' + (isDisabled ? 'ui-multiselect-disabled' : 'normal-li') + '">');
            
            html.push(convertToRadio())
            html.push(convertToAnchor());
            
            html.push(createLabel( escTitle == "" ? ' ui-multiselect-empty-label' : ''));
            html.push('<span class="heading">' + escTitle + '</span></label>');
            
          } else {
            this.nodeCount++;
            var isAItem = obj != undefined;
            html.push('<li class="title node-li">');
            html.push('<a href="#" class="checkbox tristate-node">Heading</a>');
            
            if(isAItem) {
              html.push('<span class="tristate-node-element">');
              html.push(convertToRadio())
              html.push(convertToAnchor("node-item-checkbox"));
              html.push('</span> ');
            }
            
            var classes = ' tristateMultiselect-label tristate-node '
            if(isAItem) {
              html.push(createLabel(classes, true));
            } else {
              html.push(createLabel(classes + $this.html() == "" ? ' ui-multiselect-empty-label' : ''));
            }
            html.push('<span class="heading">' + escTitle + '</span>');
            html.push('</label><ul> ');
            build(array, level + 1);
            html.push('</ul></li>');
            
          }
        });
      };
      
      //builds empty values first.
      build(emptyElements, 1, true);
      build(result, 1);
      
      // insert into the DOM
      checkboxContainer.html( html.join('') );

      // cache some moar useful elements
      this.labels = menu.find('label');
      
      this.labels.each(function() {
        var $this = $(this);
        var targetId = $this.attr('for');
        var $target;
        
        if ($this.hasClass('tristate-node')) {
          $target = $($this.siblings('span')[0]).find('#' + targetId);
        } else {
          $target = $this.siblings('#' + targetId);
        }
        
        $this.attr('for', '');
        
        
        $this.bind('click', function() {
          $target.trigger('click');
        });
      });
      
      // set widths
      this._setButtonWidth();
      this._setMenuWidth();
      
      // remember default value
      this.button[0].defaultValue = this.update();
      
      // broadcast refresh event; useful for widgets
      if( !init ){
        this._trigger('refresh');
      }
    },
    
 // updates the button text. call refresh() to rebuild
    update: function(){
      var o = this.options,
        $inputs = this.checkboxContainer.find('a.checkbox:not(.node-item-clone)'),
        $checked = $inputs.filter('.checked').filter('.element'),
        numChecked = $checked.length,
        value;
      
      if( numChecked === 0 ){
        value = o.noneSelectedText;
      } else {
        if($.isFunction(o.selectedText)){
          value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
        } else if( /\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList){
          value = $checked.map(function(){ return this.title; }).get().join(', ');
        } else {
          value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length - this.nodeCount);
        }
      }
      
      this.buttonlabel.html( value );
      return value;
    },
    
 // binds events
    _bindEvents: function(){
      
      var self = this;
      
      this.instance.header.undelegate('a', 'click.multiselect')
      .delegate('a', 'click.multiselect', function(e){
        // close link
        if( $(this).hasClass('ui-multiselect-close') ){
          self.instance.close();
      
        // check all / uncheck all
        } else {
          self.instance[ $(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll' ]();
        }
      
        e.preventDefault();
      });
      
      this.instance.menu.undelegate('a', 'click.multiselect')
      .delegate('.tristate-node', 'click.multiselect', function(e){
        e.preventDefault();
        
        var $this = $(this),
          $not_inc = $this.siblings().not('ul').find('.node-item-checkbox');
          $inputs = $this.parent().find('.checkbox:not(.radiobutton)').not($not_inc).not(".tristate-node").not(".node-item-clone"),
            tags = self.instance.element.find('option'),
            checked = $inputs.filter('.checked').length === $inputs.length;
        
        $inputs.each(function(){
          $this = $(this);
          $this.attr('aria-selected', checked);
          var val = $this.attr('optionvalue');
              tags.each(function(){
                  if( this.value === val ){
                    this.selected = checked;
                  } 
                });
        });
      })
      .undelegate('label', 'mouseenter.multiselect')
      .delegate('label', 'mouseenter.multiselect', function(){
        if( !$(this).hasClass('ui-state-disabled') ){
          $('.'+ self.instance.options.labelClass).removeClass('ui-state-hover');
          if (!(!self.instance.options.multiple && $(this).hasClass('node-checkbox'))) {
            $(this).addClass('ui-state-hover').find('input').focus();
          }
        }
      })
      .delegate('.radiobutton', 'click.multiselect', function(e) {
        var $this = $(this);
          self.instance._toggleChecked(false, self.instance.menu.find('ul.triState').find('.checkbox')
              .not(this), this.type);
          var checkBox =  $this.siblings('#anchor-' + $this.attr("checkbox"));
          checkBox.trigger('click');
          checkBox.addClass('checked');
          $this.attr('checked', 'checked');
          self.instance.close();
      })
      .undelegate('input[type="checkbox"], input[type="radio"]', 'click.multiselect')
      .delegate('.checkbox:not(".radiobutton", ".node-item-checkbox"), input[type="checkbox"], input[type="radio"]:not(".radiobutton")', 'click.multiselect', function(e){
        var $this = $(this),       
          anchor = $($this.parent().children('a.checkbox').get(0)),
          val = anchor.attr('optionvalue'),
          checked = anchor.hasClass('checked'),
          type = this.type,
          tags = self.instance.element.find('option');
        
          // bail if this input is disabled or the event is cancelled
        if( this.disabled || self.instance._trigger('click', e, { value:val, text:this.title, checked:checked }) === false ){
          e.preventDefault();
          return;
        }
        
        self.instance._toggleChecked(false, self.instance.menu.find('ul.triState').find('input[type="radio"]'));
          
          if ($($this.siblings('input[type="radio"]')[0]).attr("checked")) {
            $this.attr('checked', true);
            return;
          }
          $this.attr('aria-selected', checked);
        
        // set the original option tag to selected
        tags.each(function() {
          if (this.value == "" && tags.length > 0) {
            this.selected = $('a[optid="'+this.id+'"]').hasClass('checked');
          } else if( this.value === val ){
            this.selected = checked;

          // deselect all others in a single select
          } else if( !self.instance.options.multiple ){
            this.selected = false;
          }
        });
        
        // some additional single select-specific logic
        if( !self.instance.options.multiple ){
          self.instance.labels.removeClass('ui-state-active');
          var $label = $this.siblings('label');
          if ($label == undefined) {
            $label = $this.closest('label');
          }
          $label.toggleClass('ui-state-active', checked );
        }
        
        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery
        // issue #3827
        // http://bugs.jquery.com/ticket/3827
        setTimeout($.proxy(self.instance.update, self.instance), 10);
      });
    },
    
    _toggleChecked: function(flag, group, type){
      var $inputs = (group && group.length) ?
        group :
          this.checkboxContainer.find((flag ? 'a' : '') + '.checkbox'),

        self = this;
      
      // toggle state on inputs
      $inputs.each(this._toggleCheckbox('checked', flag));
      
      if (flag) {
        $inputs.addClass('checked');
      } else {
        $inputs.removeClass('checked partial');
      }
      
      // update button text
      this.update();
      
      // gather an array of the values that actually changed
      var values = $inputs.map(function(){
        return $(this).attr('optionvalue');
      }).get();

      // toggle state on original option tags
      this.element
        .find('option')
        .each(function(){
          if( !this.disabled && $.inArray(this.value, values) > -1 ){
            self._toggleCheckbox('selected', flag).call( this );
          }
        });
    }

  });
})(jQuery);
