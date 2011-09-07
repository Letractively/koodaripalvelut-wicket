/**********************************************************************************
**
**    jQuery Tristate Checkbox Plugin
**    version: 1.0
**
**    Dual licensed under the MIT and GPL licenses:
**      http://www.opensource.org/licenses/mit-license.php
**      http://www.gnu.org/licenses/gpl.html
**
**    author: Ben Belcourt
**    creation date: 04.11.2010
**    dependencies: "jquery-1.3.2.js" or higher
**
**    This file contains the functionality for implementing 3 state checkboxes.
**
**********************************************************************************/

(function ($){
  $.fn.tristate = function(options) {  
  
    var config = {
      checkedClass: 'checked',
      fullClass: 'full',
      heading: 'span.label',
      partialClass: 'partial',
      labelClass: 'label',
      sublist: 'sublist',
      multiple: true,
      showOriginalInputs: false
    };
    var opts = $.extend(config,options);
    
    return this.each(function() {  
      var obj = $(this);

      var triState = {
        init: function () {
          // Add proxy checkbox for each heading
          obj.find(opts.heading).each(function() {
            var $this = $(this),
            stateAnchor = $('<a href="#" class="checkbox ' + opts.sublist + '">Heading</a>');
            $this.parent().addClass("node-li");
            $this.before(stateAnchor);
            if($this.children().length > 0) {
              var optionItem = $this.children()[0],
              anchor = triState.convertToAnchor($(optionItem), 'node-item-checkbox'),
              radio = triState.convertToRadio($(optionItem)),
              span = $('<span class="tristate-node-element"></span> ');
              stringAnchorItem = $('<div>').append(anchor).remove().html(),
              stringRadioItem = $('<div>').append(radio).remove().html();
              
              $this.before(span.append(radio).append(anchor));
              
              $this.wrap('<label class="ui-corner-all tristateMultiselect-label ' + opts.sublist + '"></label>');
              
              this.innerHTML = $(optionItem).attr('title');
              
              if(opts.multiple){
                triState.bindLabel(stateAnchor);
              } else {
                anchor.hide();
                triState.bindLabel(radio, radio.parent().siblings('label'));
              }
            } else {
              $this.wrap('<label class="ui-corner-all tristateMultiselect-label ' + opts.sublist + ($this.html() == "" ? ' ui-multiselect-empty-label' : '') + '"></label>');
              if(opts.multiple) {
                triState.bindLabel(stateAnchor);
              }
            }
          });
          
          var $inputs = obj.find('input[type="checkbox"]');
          for (var i=0, y=$inputs.length; i<y; i++) {
            // hide the input fields
            triState.hideInput($($inputs[i]));
          }
          
          // Initialize proxy links
          triState.updateProxyStates();
          
          obj.find('a.node-item-checkbox').each(function() {
            var $this = $(this),
            $parentList = $this.parent('span').parent('li').parent('ul'),
            $clone = $this.clone(),
            $liClone = $clone.wrap('<li class="node-item-clone-li"></li>').parent();
            $clone.attr('id', 'clone-' + $this.attr('id'));
            $clone.addClass('node-item-clone');
            $clone.removeClass('node-item-checkbox');
            $parentList.append($liClone.hide());
            
            $this.bind('click', function(e) {
              e.preventDefault();
              $this.attr()
              $clone.trigger('click');
              if ($this.hasClass('checked')) {
                $clone.removeClass('checked');
              } else {
                $clone.addClass('checked');
              }
            });
            
          });
          
          // Set click behavior on appropriate input fields
          obj.find('a.checkbox').bind('click', function (e) {
            var $this = $(this);
            e.preventDefault();
            triState.initProxyClick($this);
          });
        },
        
        checkSiblings: function ($el, bChecked) {
          var $siblings = $el.parent('li').siblings().children('a.checkbox');
          var iCheckCount = 0;
          var $parentList = $el.parent('li').parent('ul');
          var $descendantLists = $parentList.find('ul');
          var sResult;
          
          if (bChecked) { iCheckCount++; }
          
          // Loop through the list of siblings to see if any are checked
          for (var i=0; i<$siblings.length; i++) {
            if ($($siblings[i]).hasClass(opts.checkedClass) || $($siblings[i]).hasClass(opts.partialClass)) { iCheckCount++; }
          }
          
          // Depending on how many siblings are checked set the appropriate result string
          (iCheckCount < $siblings.length+1) ? sResult = opts.partialClass : sResult = opts.fullClass;
          for (var x=0; x<$descendantLists.length; x++) {
            if ($($descendantLists[x]).hasClass(opts.partialClass)) { sResult = opts.partialClass; }
          }
          if (iCheckCount === 0) { sResult = ''; }
          
          // Set the appropriate class to the parent list
          $parentList.removeClass(opts.partialClass + ' ' + opts.fullClass).addClass(sResult);
        },
        
        initProxyClick: function ($el) {
          var bChecked;
          triState.setInputField($el);
          
          // Initialize the input field
          ($($el).hasClass(opts.checkedClass) === true || $($el).hasClass(opts.partialClass) === true) ? bChecked = true : bChecked = false ;
                      
          // Check for 'checked' siblings
          triState.checkSiblings($el, bChecked);
          
          // Check/uncheck this elements associated label
          triState.setLabel($el, bChecked);
          
          // Check/uncheck this elements descendants appropriately
          triState.setDescendants($el, bChecked);
          
          // Check/uncheck this element's ancestors appropriately
          triState.setAncestors($el, bChecked);
          
          triState.updateProxyStates();
//          triState.updateClons();
        },
        
        updateClons: function() {
        	$clones = $(".node-item-clone");
        	
        	$clones.each( function() {
        		originalobj = ($(this).attr('id').substr(6))
        		
        		if (!$(originalobj).hasClass('checked')) {
				  $(this).removeClass('checked');
				} else {
				  $(this).addClass('checked');
				}
        		
        	});
        },
        
        updateProxyStates: function () {
          var $inputs = $('input:checked', obj);
          var $proxyLink;
          for (var i=0, y=$inputs.length; i<y; i++) {
            $proxyLink = $($inputs[i]).siblings('a.checkbox');
            triState.setInputField($proxyLink, 'checked');
            triState.checkSiblings($($inputs[i]), true);
            triState.setAncestors($($inputs[i]), true);
            $inputs.removeAttr('checked');
          }
        },
        
        hideInput: function ($el) {
          var $anchor = triState.convertToAnchor($el);
          var $radio = triState.convertToRadio($el);
          $el.before($radio).before($anchor);
          
          if(!opts.multiple) {
            triState.hideInputs($($anchor.siblings('label')[0]), $anchor, $radio);
          } else {
            triState.bindLabel($anchor);
          }
          if (!opts.showOriginalInputs) { $el.hide(); }
        },
        
        hideInputs: function($label, $anchor, $radio) {
          $radio.attr('id', 'radio-' + $anchor.attr('id'));
          $($anchor.siblings('label')[0]).attr('for', $radio.attr('id'));
          $label.attr('for', $radio.attr('id'));
          $radio.hide();
          $anchor.hide();
        },
        
        convertToAnchor: function ($el, classes) {
          var sName = $el.attr('name'),
          id = $el.attr('id'),
          title = $el.attr('title').replace(/\"/gi, "&quot;"),
          value = $el.attr('value'),
          optid = $el.attr('optid'),
          isSelected = $el.attr('checked'),
          $newCheckbox = $('<a id="anchor-'+ id +'" href="#" class="checkbox element '+ (isSelected ? "checked " : "") + (classes ?  classes : "")+'" title="'+title+'" optionvalue="'+ value +'" optid="'+optid+'">'+sName+'</a>');
          $newCheckbox.data("name", sName);
          return $newCheckbox;
        },
        
        convertToRadio: function($el) {
          var checkboxId = $el.attr('id');
          
          return $('<input type="radio" class="checkbox radiobutton" checkbox="' + checkboxId + '" style="float: left;"/>')
        },
        
        
        bindLabel: function($target, $label) {
          
          if ($label == undefined) {
            $label = $($target.siblings('label')[0]).attr('for', '');
          }
          
          $label.bind('click', function() {
            $target.trigger('click');
          });
          
        },
        
        setAncestors: function ($el, bChecked) {
          var bAncestorCheck;
          var $parentList = $el.parent('li').parent('ul');
          var $ancestorInput = $parentList.parent('li').children('a.checkbox');
          
          // Check the class on the parentList and check the ancestorInput appropriately
          if (!$parentList.hasClass(opts.partialClass) && !$parentList.hasClass(opts.fullClass)) {
            $ancestorInput.removeClass(opts.checkedClass +' '+ opts.partialClass);
            triState.setInputField($ancestorInput, 'unchecked');
            triState.setLabel($ancestorInput, bChecked);
            bAncestorCheck = false;
          } else {
            triState.setInputField($ancestorInput, 'checked');
            triState.setLabel($ancestorInput, bChecked);
            if ($parentList.hasClass(opts.partialClass)) { $ancestorInput.removeClass(opts.checkedClass).addClass(opts.partialClass); }
            if ($parentList.hasClass(opts.fullClass)) { $ancestorInput.removeClass(opts.partialClass).addClass(opts.checkedClass); }
            bAncestorCheck = true;
          }
          
          // As long as we're not at the root element then keep traversing up the chain to check each input appropriately
          if ($ancestorInput.length > 0) {
            triState.checkSiblings($ancestorInput, bAncestorCheck);
            triState.setAncestors($ancestorInput, $ancestorInput.hasClass(opts.checkedClass));
          }
        },
        
        setDescendants: function ($el, bChecked) {
          var $parentItem = $el.parent('li');
          var $descendantList = $parentItem.find('ul');
          var $not_included = $el.siblings().not('ul').find('.node-item-checkbox');
          var $descendantInputs = $parentItem.find('a.checkbox').not($not_included);
          //var $descendantInputs = $parentItem.find('a.checkbox');
          
          if (bChecked) {
            for (var i=0, j=$descendantInputs.length; i<j; i++) {
              triState.setInputField($($descendantInputs[i]), 'checked');
              triState.setLabel($($descendantInputs[i]), bChecked);
            }
            $descendantList.removeClass(opts.partialClass + ' ' + opts.fullClass).addClass(opts.fullClass);
          } else {
            for (var g=0, y=$descendantInputs.length; g<y; g++) {
              triState.setInputField($($descendantInputs[g]), 'unchecked');
              triState.setLabel($($descendantInputs[g]), bChecked);
            }
            $descendantList.removeClass(opts.partialClass + ' ' + opts.fullClass);
          }
        },
        
        setInputField: function ($el, state) {
          var sState = state || '';
          switch (sState) {
            case '':
              ($el.hasClass(opts.checkedClass) === true) ? triState.uncheckInput($el) : triState.checkInput($el);
              break;
            case 'checked':
              triState.checkInput($el);
              break;
            case 'unchecked':
              triState.uncheckInput($el);
              break;
          }
        },
        
        setLabel: function ($el, state) {
          $label = $el.siblings('label');
          (state) ? triState.checkLabel($label) : triState.uncheckLabel($label);
        },
        
        checkInput: function ($el) {
          $el.removeClass(opts.partialClass).addClass(opts.checkedClass).siblings('input[name="'+$el.data('name')+'"]').attr('checked', 'checked');
        },
        
        uncheckInput: function ($el) {
          $el.removeClass(opts.checkedClass +' '+ opts.partialClass).siblings('input[name="'+$el.data('name')+'"]').attr('checked', '');
        },
        
        checkLabel: function ($el) {
          $el.removeClass(opts.partialClass).addClass(opts.checkedClass);
        },
        
        uncheckLabel: function ($el) {
          $el.removeClass(opts.checkedClass +' '+ opts.partialClass);
        }
      };
      
      triState.init();      

    });  
  };
})(jQuery);