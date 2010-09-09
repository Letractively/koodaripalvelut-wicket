var Feedback = new (function() {
  function setupSend(xhr) {
    xhr.setRequestHeader("Wicket-Ajax", "true");
    if (typeof(Wicket.Focus.lastFocusId) != "undefined" && Wicket.Focus.lastFocusId != "" && Wicket.Focus.lastFocusId != null)
        xhr.setRequestHeader("Wicket-FocusedElementId", Wicket.Focus.lastFocusId);                
    xhr.setRequestHeader("Accept", "text/xml");
  };
  
  function success(data) {
    var xml = data;
    if (typeof xml == "string") {
      xml = new ActiveXObject("Microsoft.XMLDOM");
      xml.async = false;
      xml.loadXML(data);
    }
    (new Wicket.Ajax.Call("${feedbackURL}")).loadedCallback(xml);
  };
  
  function call(data, revertFunc) {
    $.ajax({
      url : "${feedbackURL}", type: 'POST', contentType: 'application/json;charset=UTF-8'
     ,dataType: (($.browser.msie) ? "text" : "xml"), data : $.toJSON(data)
     ,beforeSend : setupSend ,success : success, error : revertFunc
    });
  };
  
  this.forSelect = function(startDate, endDate, allDay, jsEvent, view) {
    call({"feedbackFor" : "select", "startDate" : startDate, "endDate" : endDate, 
      "allDay" : allDay}, null);
  };
  
  this.forUnselect = function(view, jsEvent) {
    call({"feedbackFor" : "unselect"}, null);
  };
  
  this.forViewDisplay = function(view) {
    call({"feedbackFor" : "viewDisplay"}, null);
  };
  
  this.forDayClick = function(date, allDay, jsEvent, view) {
    call({"feedbackFor" : "dayClick", "date" : date, "allDay" : allDay}, null);
  };
  
  this.forEvent = function (eventName) {
    return function(event, jsEvent, ui, view) {
      call({"feedbackFor" : eventName, "event" : event}, null);
    };
  };
  
  this.forEventAlter = function (eventName) {
    return function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
      if (arguments.length == 8)
        call({"feedbackFor" : eventName, "event" : event, 
          "dayDelta" : dayDelta, "minuteDelta" : minuteDelta, "allDay" : allDay}, revertFunc);
      else
        call({"feedbackFor" : eventName, "event" : event, 
          "dayDelta" : dayDelta, "minuteDelta" : minuteDelta}, allDay);
    };
  };
});

$('#${calendar-id}').fullCalendar({
  editable: ${editable}
 ,defaultView: "${defaultView}"
 ,year: ${year}, month: ${month}, date: ${day}
 ,header: ${header}
 ,weekends: ${weekends}
 ,events: "${eventFeedURL}"
 ${feedbackHandlers}
 ${customOptions}
});
