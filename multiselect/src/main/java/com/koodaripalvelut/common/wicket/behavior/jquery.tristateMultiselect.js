/*
 * jQuery MultiSelect UI Widget 1.10
 * Copyright (c) 2011 Eric Hynds
 *
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/
 *
 * Depends:
 *   - jQuery 1.4.2+
 *   - jQuery UI 1.8 widget factory
 *
 * Optional:
 *   - jQuery UI effects
 *   - jQuery UI position utility
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
*/
(function($, undefined){

var multiselectID = 0;

$.widget("ech.triStateMultiselect", {
  
  // default options
  options: {
    header: true,
    height: 175,
    minWidth: 225,
    classes: '',
    checkAllText: 'Check all',
    miniButton: true,
    uncheckAllText: 'Uncheck all',
    noneSelectedText: 'Select options',
    selectedText: '# selected',
    defaultListLabel: 'sub-list',
    selectedList: 0,
    show: '',
    hide: '',
    autoOpen: false,
    multiple: true,
    labelClass: 'tristateMultiselect-label',
    sublistClass: 'sublist', 
    nullItemLabel: 'null / emptyValue',
    position: {}
  },

  _create: function(){
    var el = this.element.hide(),
      o = this.options;
    
    this.speed = $.fx.speeds._default; // default speed for effects
    this._isOpen = false; // assume no
  
    var 
      button = (this.button = $('<button type="button"><span class="ui-icon ui-icon-triangle-2-n-s"></span></button>'))
        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
        .addClass( o.classes )
        .attr({ 'title':el.attr('title'), 'aria-haspopup':true, 'tabIndex':el.attr('tabIndex') })
        .insertAfter( el ),
      
      buttonlabel = (this.buttonlabel = $('<span />'))
        .html( o.noneSelectedText )
        .appendTo( button ),
        
      menu = (this.menu = $('<div />'))
        .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')
        .addClass( o.classes )
        .insertAfter( button ),
        
      header = (this.header = $('<div />'))
        .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
        .appendTo( menu ),
        
      headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
        .addClass('ui-helper-reset')
        .html(function(){
          if( o.header === true ){
            return '<li><a class="ui-multiselect-all" href="#"><span class="ui-icon ui-icon-check"></span><span>' + o.checkAllText + '</span></a></li><li><a class="ui-multiselect-none" href="#"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
          } else if(typeof o.header === "string"){
            return '<li>' + o.header + '</li>';
          } else {
            return '';
          }
        })
        .append('<li class="ui-multiselect-close"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
        .appendTo( header ),
      
      checkboxContainer = (this.checkboxContainer = $('<ul />'))
        .addClass('ui-multiselect-checkboxes ui-helper-reset triState')
        .appendTo( menu ), 
      sublistCount;
    
    // perform event bindings
    this._bindEvents();
    
    // build menu
    this.refresh( true );
    
    // some addl. logic for single selects
    if( !o.multiple ){
      menu.addClass('ui-multiselect-single');
    }
  },
  
  _init: function(){
    if( this.options.header === false ){
      this.header.hide();
    }
    if( !this.options.multiple ){
      this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
    }
    if( this.options.autoOpen ){
      this.open();
    }
    if( this.element.is(':disabled') ){
      this.disable();
    }
    
    $(this.menu).find('ul.triState').tristate({heading: 'span.heading', multiple: this.options.multiple});
    this.button[0].defaultValue = this.update();
  },
  
  refresh: function( init ){
    var el = this.element,
      o = this.options,
      menu = this.menu,
      checkboxContainer = this.checkboxContainer,
      optgroups = [],
      html = [],
      result = {},
      parentIdElementMap = {},
      parentElements = {},
      nullOptions = [],
      id = el.attr('id') || multiselectID++; // unique ID for the label & option tags
    
    //Organizes options hierarchically
    this.element.find('option').each(function( i ) {
      
      if(this.value == "") {
        this.innerHTML = o.nullItemLabel;
        nullOptions.push(this);
        return;
      }
      
      var optParentId = $(this).attr('optparentid'),
      parent = parentIdElementMap[optParentId];
      
      if(optParentId == "null") { //belongs to root.
        parent = parentIdElementMap["null"];
        if (parent == undefined) {
          parent = [];
          parentIdElementMap["null"] = parent;
        }
      } else if (parent == undefined) { //Parent is not yet registered. 
        
        var elementsWithNoParent = parentIdElementMap["null"];
        
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
      } else if (parent instanceof Array) { //parent is registered.
        parentIdElementMap[this.innerHTML] = this;
      } else { //Parent is an exiting option.
        
        var grandParent = parentIdElementMap[$(parent).attr('optparentid')];
        
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
      
      if(this.value == "") {
        this.innerHTML = o.nullItemLabel;
      }
      parent.push(this);
    });
    
    var elementsWithNoParent = parentIdElementMap["null"];
    if(elementsWithNoParent != undefined) {
      var counter = 0;
      for(obj in elementsWithNoParent) {
        result["withnoparent" + counter++] = elementsWithNoParent[obj];
      }
    }

    
    function flat( arr ) {
      var array = [];
      var startOpt = document.createElement('OPTION');
      $(startOpt).attr('optcontainer', 'startOpt');
      array.push(startOpt);
      for (keyVar in arr) {
        var obj = arr[keyVar];
        if (obj instanceof Array) {
          array = array.concat(flat(obj));
        } else {
          array.push(obj);
          if (startOpt.innerHTML == "") {
            startOpt.innerHTML = $(obj).attr('optparentid');
          }
        }
      }
      var endOpt = document.createElement('OPTION');
      $(endOpt).attr('optcontainer', 'endOpt');
      array.push(endOpt);
      return array;
    }
    
    var flatResult = nullOptions.concat(flat(result));
    
    $(flatResult).filter(function(){return ($(this).attr('optcontainer')  == 'startOpt')}).each(function(){
      var parentElement = parentElements[this.innerHTML];
      if (parentElement != undefined) {
        $(parentElement).attr({optcontainer: 'startOpt', isItem: true});
        flatResult[jQuery.inArray( this, flatResult )] = parentElement;
      }
    });
    
    // build items
    $(flatResult).each(function( i ){
      var $this = $(this), 
        parent = this.parentNode,
        title = this.innerHTML,
        value = this.value,
        inputID = this.id || 'ui-multiselect-'+id+'-option-'+i, 
        isDisabled = this.disabled,
        isSelected = this.selected,
        labelClasses = ['ui-corner-all'],
        className = this.label,
        optcontainer = $this.attr('optcontainer'),
        optLabel;
      
      function convertToInput() {
        var buf = '<input id="'+ inputID +'" type="checkbox" value="'+value+'" title="'+title+'"';
        
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
    	  this.sublistCount++;
    	  title;
    	  if($(this).attr('isItem')) {
    	    title = convertToInput();
    	  } else {
    	    title = (title === "null" ? o.defaultListLabel : title)
    	  }
    	  html.push('<li class="title"><span class="heading">' + title + '</span><ul>');
      } else if ( optcontainer == "endOpt" ) {
    	  html.push('</ul></li>');
      } else {
    	  html.push('<li class="' + (isDisabled ? 'ui-multiselect-disabled' : '') + '">');
    	  
    	  html.push(convertToInput());
    	  
    	  // add the title and close everything off
    	  html.push('<label href="#" for="' + inputID + '" class="ui-corner-all ' + o.labelClass + '"><span>' + title + '</span></label></li>');
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
  
  // updates the button text.  call refresh() to rebuild
  update: function(){
    var o = this.options,
      $inputs = this.menu.find('ul.triState').find('a.checkbox:not(.node-item-clone)'),
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
        value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length - this.sublistCount);
      }
    }
    
    this.buttonlabel.html( value );
    return value;
  },
  
  // binds events
  _bindEvents: function(){
    var self = this, button = this.button;
    
    function clickHandler(){
      self[ self._isOpen ? 'close' : 'open' ]();
      return false;
    }
    
    // webkit doesn't like it when you click on the span :(
    button
      .find('span')
      .bind('click.multiselect', clickHandler);
    
    // button events
    button.bind({
      click: clickHandler,
      keypress: function(e){
        switch(e.which){
          case 27: // esc
          case 38: // up
          case 37: // left
            self.close();
            break;
          case 39: // right
          case 40: // down
            self.open();
            break;
        }
      },
      mouseenter: function(){
        if( !button.hasClass('ui-state-disabled') ){
          $(this).addClass('ui-state-hover');
        }
      },
      mouseleave: function(){
        $(this).removeClass('ui-state-hover');
      },
      focus: function(){
        if( !button.hasClass('ui-state-disabled') ){
          $(this).addClass('ui-state-focus');
        }
      },
      blur: function(){
        $(this).removeClass('ui-state-focus');
      }
    });

    // header links
    this.header
      .delegate('a', 'click.multiselect', function(e){
        // close link
        if( $(this).hasClass('ui-multiselect-close') ){
          self.close();
      
        // check all / uncheck all
        } else {
          self[ $(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll' ]();
        }
      
        e.preventDefault();
      });
    
    // optgroup label toggle support
    this.menu
      .delegate('li.ui-multiselect-optgroup-label a', 'click.multiselect', function(e){
        e.preventDefault();
        
        var $this = $(this),
          $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)'),
            nodes = $inputs.get(),
            label = $this.parent().text();
        
        // trigger event and bail if the return is false
        if( self._trigger('beforeoptgrouptoggle', e, { inputs:nodes, label:label }) === false ){
          return;
        }
        
        // toggle inputs
        self._toggleChecked(
          $inputs.filter(':checked').length !== $inputs.length,
          $inputs
        );

        self._trigger('optgrouptoggle', e, {
            inputs: nodes,
            label: label,
            checked: nodes[0].checked
        });
      })
      .delegate('.' + self.options.sublistClass, 'click.multiselect', function(e){
    	e.preventDefault();
    	
    	var $this = $(this),
    	  $inputs = $this.parent().find('.checkbox:not(".radiobutton")'),
          tags = self.element.find('option'),
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
      .delegate('label', 'mouseenter.multiselect', function(){
        if( !$(this).hasClass('ui-state-disabled') ){
        	$('.'+ self.options.labelClass).removeClass('ui-state-hover');
          $(this).addClass('ui-state-hover').find('input').focus();
        }
      })
      .delegate('label', 'keydown.multiselect', function(e){
        e.preventDefault();
        
        switch(e.which){
          case 9: // tab
          case 27: // esc
            self.close();
            break;
          case 38: // up
          case 40: // down
          case 37: // left
          case 39: // right
            self._traverse(e.which, this);
            break;
          case 13: // enter
            $(this).find('input')[0].click();
            break;
        }
      })
      .delegate('.radiobutton', 'click.multiselect', function(e) {
        var $this = $(this);
          self._toggleChecked(false, self.menu.find('ul.triState').find('.checkbox')
              .not(this));
          $this.siblings('#anchor-' + $this.attr("checkbox")).trigger('click');
          $this.attr('checked', 'checked');
          self.close();
      })
      .delegate('.checkbox:not(".radiobutton", ".node-item-checkbox"), input[type="checkbox"], input[type="radio"]:not(".radiobutton")', 'click.multiselect', function(e){
        var $this = $(this),       
          anchor = $($this.parent().children('a.checkbox').get(0)),
          val = anchor.attr('optionvalue'),
          checked = anchor.hasClass('checked'),
          type = this.type,
          tags = self.element.find('option');
        
        // bail if this input is disabled or the event is cancelled
        if( this.disabled || self._trigger('click', e, { value:val, text:this.title, checked:checked }) === false ){
          e.preventDefault();
          return;
        }
          
          if ($($this.siblings('input[type="radio"]')[0]).attr("checked")) {
            $this.attr('checked', true);
            return;
          }
          $this.attr('aria-selected', checked);
        
        // set the original option tag to selected
        tags.each(function() {
          if (this.value == "" && tags.length > 0) {
            this.selected = false;
          } else if( this.value === val ){
            this.selected = checked;

          // deselect all others in a single select
          } else if( !self.options.multiple ){
            this.selected = false;
          }
        });
        
        // some additional single select-specific logic
        if( !self.options.multiple ){
          self.labels.removeClass('ui-state-active');
          var $label = $this.siblings('label');
          if ($label == undefined) {
            $label = $this.closest('label');
          }
          $label.toggleClass('ui-state-active', checked );
          
          // close menu
          self.close();
        }
        
        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
        // http://bugs.jquery.com/ticket/3827 
        setTimeout($.proxy(self.update, self), 10);
      });
    
    // close each widget when clicking on any other element/anywhere else on the page
    $(document).bind('mousedown.multiselect', function(e){
      if(self._isOpen && !$.contains(self.menu[0], e.target) && !$.contains(self.button[0], e.target) && e.target !== self.button[0]){
        self.close();
      }
    });
    
    // deal with form resets.  the problem here is that buttons aren't
    // restored to their defaultValue prop on form reset, and the reset
    // handler fires before the form is actually reset.  delaying it a bit
    // gives the form inputs time to clear.
    $(this.element[0].form).bind('reset.multiselect', function(){
      setTimeout(function(){ self.update(); }, 10);
    });
  },

  // set button width
  _setButtonWidth: function(){
    var width = this.element.outerWidth(),
      o = this.options;
    
    if (o.miniButton) {
      //    var elsu = this.element;
//          alert("this.element=" + elsu[0].tagName + " outerWidth: " + width + " vs. width: " + elsu[0].style.width);
          this.button.width( width );
          o.buttonWidth = o.minWidth;
          return;
        }
      
    if( /\d/.test(o.minWidth) && width < o.minWidth){
      width = o.minWidth;
    }
    
    // set widths
    this.button.width( width );
    
    o.buttonWidth = this.button.outerWidth();
  },
  
  // set menu width
  _setMenuWidth: function(){
    var m = this.menu,
    o = this.options,
    width = o.buttonWidth
        parseInt(m.css('padding-left'),10)-
        parseInt(m.css('padding-right'),10)-
        parseInt(m.css('border-right-width'),10)-
        parseInt(m.css('border-left-width'),10);
        
    m.width( width || this.button.outerWidth() );
  },
  
  // move up or down within the menu
  _traverse: function(which, start){
    var $start = $(start),
      moveToLast = which === 38 || which === 37,
      
      // select the first li that isn't an optgroup label / disabled
      $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)')[ moveToLast ? 'last' : 'first']();
    
    // if at the first/last element
    if( !$next.length ){
      var $container = this.menu.find('ul.triState');
      
      // move to the first/last
      this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');
      
      // set scroll position
      $container.scrollTop( moveToLast ? $container.height() : 0 );
      
    } else {
      $next.find('label').trigger('mouseover');
    }
  },

  // This is an internal function to toggle the checked property and
  // other related attributes of a checkbox.
  //
  // The context of this function should be a checkbox; do not proxy it.
  _toggleCheckbox: function( prop, flag ){
    return function(){
      !this.disabled && (this[ prop ] = flag);

      if( flag ){
        this.setAttribute('aria-selected', true);
      } else {
        this.removeAttribute('aria-selected');
      }
    }
  },

  _toggleChecked: function(flag, group){
    var $inputs = (group && group.length) ?
      group :
    	  this.menu.find('ul.triState').find('a.checkbox'),

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
  },

  _toggleDisabled: function( flag ){
    this.button
      .attr({ 'disabled':flag, 'aria-disabled':flag })[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
    
    this.menu
      .find('input')
      .attr({ 'disabled':flag, 'aria-disabled':flag })
      .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
    
    this.element
      .attr({ 'disabled':flag, 'aria-disabled':flag });
  },
  
  // open the menu
  open: function(e){
    var self = this,
      button = this.button,
      menu = this.menu,
      speed = this.speed,
      o = this.options;
    
    // bail if the multiselectopen event returns false, this widget is disabled, or is already open 
    if( this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen ){
      return;
    }
    
    var $container = menu.find('ul.triState'),
      effect = o.show,
      pos = button.position();
    
    // figure out opening effects/speeds
    if( $.isArray(o.show) ){
      effect = o.show[0];
      speed = o.show[1] || self.speed;
    }
    
    // set the scroll of the checkbox container
    $container.scrollTop(0).height(o.height);
    
    // position and show menu
    if( $.ui.position && !$.isEmptyObject(o.position) ){
      o.position.of = o.position.of || button;
      
      menu
        .show()
        .position( o.position )
        .hide()
        .show( effect, speed );
    
    // if position utility is not available...
    } else {
      menu.css({ 
        top: pos.top+button.outerHeight(),
        left: pos.left
      }).show( effect, speed );
    }
    
    // select the first option
    // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
    // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
    this.labels.eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');
    
    button.addClass('ui-state-active');
    this._isOpen = true;
    this._trigger('open');
  },
  
  // close the menu
  close: function(){
    if(this._trigger('beforeclose') === false){
      return;
    }
  
    var o = this.options, effect = o.hide, speed = this.speed;
    
    // figure out opening effects/speeds
    if( $.isArray(o.hide) ){
      effect = o.hide[0];
      speed = o.hide[1] || this.speed;
    }
  
    this.menu.hide(effect, speed);
    this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
    this._isOpen = false;
    this._trigger('close');
  },

  enable: function(){
    this._toggleDisabled(false);
  },
  
  disable: function(){
    this._toggleDisabled(true);
  },
  
  checkAll: function(e){
    this._toggleChecked(true);
    this._trigger('checkAll');
  },
  
  uncheckAll: function(){
    this._toggleChecked(false);
    this._trigger('uncheckAll');
  },
  
  getChecked: function(){
    return this.menu.find('input').filter(':checked');
  },
  
  destroy: function(){
    // remove classes + data
    $.Widget.prototype.destroy.call( this );
    
    this.button.remove();
    this.menu.remove();
    this.element.show();
    
    return this;
  },
  
  isOpen: function(){
    return this._isOpen;
  },
  
  widget: function(){
    return this.menu;
  },
  
  // react to option changes after initialization
  _setOption: function( key, value ){
    var menu = this.menu;
    
    switch(key){
      case 'header':
        menu.find('div.ui-multiselect-header')[ value ? 'show' : 'hide' ]();
        break;
      case 'checkAllText':
        menu.find('a.ui-multiselect-all span').eq(-1).text(value);
        break;
      case 'uncheckAllText':
        menu.find('a.ui-multiselect-none span').eq(-1).text(value);
        break;
      case 'height':
        menu.find('ul:last').height( parseInt(value,10) );
        break;
      case "miniButton":
        this.options[ key ] = value;
        this._setButtonWidth();
        break;
      case 'minWidth':
        this.options[ key ] = parseInt(value,10);
        this._setButtonWidth();
        this._setMenuWidth();
        break;
      case 'selectedText':
      case 'selectedList':
      case 'noneSelectedText':
        this.options[key] = value; // these all needs to update immediately for the update() call
        this.update();
        break;
      case 'classes':
        menu.add(this.button).removeClass(this.options.classes).addClass(value);
        break;
    }
    
    $.Widget.prototype._setOption.apply( this, arguments );
  }
});

})(jQuery);

