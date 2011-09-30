/**
 * 
 */


(function($) {
  $.widget("ech.triStateMultiselect", {
    
    options: {
      labelClass: 'tristateMultiselect-label',
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
      
      function escapeString(str) {
        return $("<div/>").html(str).text()
      }
      
      // Organizes options hierarchically
      this.element.find('option').each(function( i ) {
        
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
        escInnerHTML = escapeString(this.innerHTML),
        parent = parentIdElementMap[optParentId];
        
        
        if (parentIdElementMap[escInnerHTML] instanceof Array) {//it is an existing node
          parentElements[escInnerHTML] = this;
          
          if (getParentId(this) != nullId && result[escInnerHTML]) {
            parent.push(result[escInnerHTML]);
            delete result[escInnerHTML];
          }
          
          return;
          
        } else if(optParentId == nullId) { // belongs to root.
          parent = parentIdElementMap[nullId];
          if (parent == undefined) {
            parent = [];
            parentIdElementMap[nullId] = parent;
          }
          
        } else if (parent == undefined) { // Parent is not yet registered.
          
          var elementsWithNoParent = parentIdElementMap[nullId];
          
          if(elementsWithNoParent != undefined) {
            for (var i = 0; i < elementsWithNoParent.length; i++) {
              if (elementsWithNoParent[i].innerHTML == optParentId) {
                parentElements[optParentId] = elementsWithNoParent[i];
                elementsWithNoParent.splice(i, 1);
                break;
              }
            }
          }
          
          parent = [];
          result[optParentId] = parent;
          parentIdElementMap[optParentId] = parent;
        } else if (parent instanceof Array) { // parent is registered.
          parentIdElementMap[escInnerHTML] = this;
        } else { // Parent is an exiting option.
          
          var grandParent = parentIdElementMap[getParentId(parent)];
          
          var obj = parent;
          parentElements[optParentId] = obj;
          
          parent = [];
          parentIdElementMap[optParentId] = parent;
          
          for (var i = 0; i < grandParent.length; i++) {
            if (grandParent[i] == obj) {
              grandParent[i] = parent;
              break;
            }
          }
        }
        
        // add option to its parent.
        parent.push(this);
        
      });
      
      var elementsWithNoParent = parentIdElementMap[nullId];
      
      if(elementsWithNoParent == undefined) {
        elementsWithNoParent = [];
      }
      
      if(nullOptions.length > 0) {
        // set null values top most.
        elementsWithNoParent = nullOptions.concat(elementsWithNoParent);
      }
      
      function flat( arr ) {
        var array = [];
        var startOpt = document.createElement('OPTION');
        $(startOpt).attr('optcontainer', 'startOpt');
        
        array.push(startOpt);
        
        // set root elements to top most.
        if (elementsWithNoParent != undefined) {
          for(i=0; i <  elementsWithNoParent.length; i++) {
            array.push(elementsWithNoParent[i]);
          }
          elementsWithNoParent = undefined;
        }
        
        for (keyVar in arr) {
          var obj = arr[keyVar];
          if (obj instanceof Array) {
            array = array.concat(flat(obj));
          } else {
            array.push(obj);
            if (startOpt.innerHTML == "") {
              startOpt.innerHTML = getParentId(obj);
            }
          }
        }
        var endOpt = document.createElement('OPTION');
        $(endOpt).attr('optcontainer', 'endOpt');
        array.push(endOpt);
        return array;
      }
      
      var flatResult = flat(result);
      
      $(flatResult).filter(function(){return ($(this).attr('optcontainer')  == 'startOpt')}).each(function(){
        var parentElement = parentElements[this.innerHTML];
        if (parentElement != undefined) {
          $(parentElement).attr({
            optcontainer: 'startOpt', 
            isItem: true, 
            selected: parentElement.selected, 
            disabled: parentElement.disabled});
          flatResult[jQuery.inArray( this, flatResult )] = parentElement;
        }
      });
      
      // build items
      $(flatResult).each(function( i ){
        var $this = $(this), 
          parent = this.parentNode,
          title = this.innerHTML,
          value = this.value,
          inputID = 'ui-multiselect-'+id+'-option-'+i, 
          isDisabled = this.disabled,
          isSelected = this.selected,
          optid = $(this).attr('id'),
          labelClasses = ['ui-corner-all'],
          className = this.label,
          optcontainer = $this.attr('optcontainer'),
          optLabel;
        
        function convertToInput() {
          var escTitle = title.replace(/\"/gi, "&quot;");
          var buf = '<input id="'+ inputID +'" type="checkbox" value="'+value+'" title="'+escTitle+'" optid="'+optid+'"';
          
          // pre-selected?
          if( isSelected ){
            buf += ' checked="checked"';
            buf += ' aria-selected="true"';
          }
          
          // disabled?
          if( isDisabled ){
            buf += ' disabled="disabled"' ;
            buf += ' aria-disabled="true"';
          }
          buf += ' />';
          return buf;
        }
      
        if( isDisabled ){
          labelClasses.push('ui-state-disabled');
        }

        // browsers automatically select the first option
        // by default with single selects
        if( isSelected && !o.multiple ){
          labelClasses.push('ui-state-active');
        }
        
        if ( optcontainer == "startOpt" )  {
          this.nodeCount++;
          title;
          if($(this).attr('isItem')) {
            title = convertToInput();
          }
          html.push('<li class="title"><span class="heading">' + title + '</span><ul>');
        } else if ( optcontainer == "endOpt" ) {
          html.push('</ul></li>');
        } else {
          html.push('<li class="' + (isDisabled ? 'ui-multiselect-disabled' : '') + '">');
          
          html.push(convertToInput());
          
          // add the title and close everything off
          html.push('<label href="#" for="' + inputID + '" class="ui-corner-all ' + o.labelClass + (title == "" ? ' ui-multiselect-empty-label' : '')+ '"><span>' + title + '</span></label></li>');
          
        }
      });
      
      // insert into the DOM
      checkboxContainer.html( html.join('') );

      // cache some moar useful elements
      this.labels = menu.find('label');
      
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
          
          if ($(this).attr('level') == undefined) {
            var i =0;
            par = $(this).parent();
            while(!par.is(".ui-multiselect-checkboxes")) {
              if(par.is('ul')) i++;
              par = par.parent();
            }
            
            if(!self.instance.options.multiple) {
              $(this).before($('<span style="float: left;">&nbsp;</span>'));
            }
            
            $(this).css('margin-left',(-20 * i) + "px");
            
            $(this).attr('level', i);
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