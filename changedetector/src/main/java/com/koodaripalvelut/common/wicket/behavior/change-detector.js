
processList = function(list, action) {
	for (var i in list) {
		var e = document.getElementById(list[i]);
		
		var prevOnClick;
		if (e.onclick)
			prevOnClick = e.onclick;
				
		e.onclick = function() {
			action(prevOnClick, this, arguments);
		}
		
		var prevOnChange;
		if (e.onchange) {
			prevOnChange = e.onchange;
			e.onclick = prevOnClick;
			prevOnClick = null;
			e.onchange = function() {
				action(prevOnChange, this, arguments);
			}
		}
	
	}	
}

changeDetectorInit = function(formId, message, whiteList, blackList, disabledList, formComponents) {
  
  function disableList(b) {
    for(i in disabledList) {
      var comp = document.getElementById(disabledList[i]);
      comp.disabled = b;
    }
  }
	
	var formOnRender;
	var detectModification = false;
	var form = document.getElementById(formId);
	disableList(true);
	
	whiteListAction = function(prevEvent) {
		if (prevEvent)
			prevEvent();
		detectModification = false;
	};	
	
	blackListAction = function(prevEvent, obj, args) {
		var formBeforeSubmit = wicketSerializeForm(form);
		if (formOnRender != formBeforeSubmit) {
			if (confirm(message)) {
				if (prevEvent) {
					prevEvent.apply(obj, args);
				}
				formOnRender = wicketSerializeForm(form);
			}
		}
	};
	
	formComponentsActions = function (prevEvent) {
	  var formBeforeSubmit = wicketSerializeForm(form);
    if (formOnRender == formBeforeSubmit) {
      disableList(true);
    } else {
      whiteListAction(prevEvent); 
      disableList(false);
    }
	};
	
	processList(whiteList, whiteListAction);
	processList(blackList, blackListAction);
	processList(formComponents, formComponentsActions);
	
	var prevOnLoad;
	if (window.onload)
		prevOnLoad = window.onload;
	
	window.onload = function(event) {
		if (prevOnLoad)
			prevOnLoad();
		
		var prevOnSubmit;
		if (form.onsubmit)
			prevOnSubmit = form.onsubmit();
		
		form.onsubmit = function() {
			if (prevOnSubmit)
				prevOnSubmit();
			detectModification = false;
		};
		
		detectModification = true;
		formOnRender = wicketSerializeForm(form);
	}	
	
	//Not only set the form on window onload 
	//but also set it when the form is re-rendered.
	formOnRender = wicketSerializeForm(form);
	
	var prevOnBeforeUnload;
	if (window.onbeforeunload)
		prevOnBeforeUnload = window.onbeforeunload; 
	
	window.onbeforeunload = function(event) {
		
		if (prevOnBeforeUnload)
			prevOnBeforeUnload();
		
		if (detectModification) {
			var formBeforeSubmit = wicketSerializeForm(form);
		    if (formOnRender != formBeforeSubmit) {
		        return message;
		    }
		}
	}
	
}
