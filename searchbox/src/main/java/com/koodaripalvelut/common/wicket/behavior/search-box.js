/** Simple implementation of a deep clone operation. */
function searchBoxCloneObject(source) {
   var objA = [];
   for (var i = 0; i < source.length; i++ )
       objA[i] = source[i];
   return objA;
}

function searchBoxCreateTextField(id, cssClass) {
	var value = searchBoxInit['searchText' + id];
	e = document.createElement('input');
	e.setAttribute('id', id);
	e.setAttribute('type', 'text');
	e.setAttribute('class', cssClass);
	e.setAttribute('value', value == null ? '' : value);
	return e;
}

function searchBoxCreateButton(id, label, cssClass) {
	e = document.createElement('button');
	e.setAttribute('type', 'button');
	e.setAttribute('id', id);
	e.setAttribute('class', cssClass);
	e.innerHTML = label;
	return e;
}

function searchBoxCreateFix(html) {
	e = document.createElement('span');
	e.innerHTML = html;
	return e;
}

searchBoxInit = function(selectId, regexFlags, autoremove, position, mode,
		classPrefix, searchLabel, clearLabel, prefix, suffix) {

	var select = document.getElementById(selectId);
	var allValues = searchBoxCloneObject(select.options);

	var searchFieldId  = selectId + '-search-field';
	var searchButtonId = selectId + '-search';
	var clearButtonId  = selectId + '-clear';

	var searchField  = document.getElementById(searchFieldId);
	var searchButton = document.getElementById(searchButtonId);
	var clearButton  = document.getElementById(clearButtonId);


	function search() {
	  searchBoxInit['searchText' + searchFieldId] = searchField.value;
    var re = new RegExp(searchField.value, regexFlags);
    var i = 0;
    var updateValue = false;
    select.options.length = 0;
    for (var j = 0; j < allValues.length; j++) {
      if (re.test(allValues[j].text)) {
        select.options[i++] = allValues[j];
      } else if (autoremove === 'true') {
        if (allValues[j].selected == true)
          updateValue = true;
        allValues[j].selected = false;
      }
    }

    // make sure the change is correctly associated and correctly notified
    if (updateValue) {
      select.onchange();
    }
	}

	function clear() {
		searchField.value = "";
		select.options.length = 0;
		for (var i = 0; i < allValues.length; i++) {
			select.options[i] = allValues[i];
		}
	}

	function setupFieldSearch() {
		if (searchField == null) {
			searchField = searchBoxCreateTextField(searchFieldId, classPrefix + '-field');
		    select.parentNode.insertBefore(searchField, (position == 'BEFORE') ? select : select.nextSibling);
		    if (prefix != '') {
		    	searchField.parentNode.insertBefore(searchBoxCreateFix(prefix), searchField);
		    }
		}
	}

	function setupSearchButton() {
		if (searchButton == null) {
			searchButton = searchBoxCreateButton(searchButtonId, searchLabel, classPrefix + '-search');
			searchField.parentNode.insertBefore(searchButton, searchField.nextSibling);
		    if (suffix != '') {
		    	searchButton.parentNode.insertBefore(searchBoxCreateFix(suffix), searchButton);
		    }
		}
	}

	function setupClearButton() {
		if (clearButton == null) {
			clearButton = searchBoxCreateButton(clearButtonId, clearLabel, classPrefix + '-clear');
			searchField.parentNode.insertBefore(clearButton, searchField.nextSibling);
		}
	}

	setupFieldSearch();
	switch (mode) {
	case "FULL":
		setupClearButton();
		clearButton.onclick = clear;
	case "BOTH":
		searchField.onkeyup = search;
	case "SEARCH_BUTTON":
		setupSearchButton();
		searchButton.onclick = search;
		break;
	case "FIELD_ONLY":
		searchField.onkeyup = search;
	}
	search();

};
