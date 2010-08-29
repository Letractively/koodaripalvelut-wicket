var Feedback = new (function() {
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
     ,dataType: (($.browser.msie) ? "text" : "xml"), data : data 
     ,success : success, error : revertFunc
    });
  };
  
  this.forSelect = function(startDate, endDate, allDay, jsEvent, view) {
    call({"feedbackFor" : "select", "startDate" : startDate, "endDate" : endDate, 
      "allDay" : allDay, "jsEvent" : jsEvent, "view" : view}, null);
  };
  
  this.forUnSelect = function(view, jsEvent) {
    call({"feedbackFor" : "unselect", "jsEvent" : jsEvent, "view" : view}, null);
  };
  
  this.forDayClick = function(date, allDay, jsEvent, view) {
    call({"feedbackFor" : "dayClick", "date" : date, "allDay" : allDay, "jsEvent" : jsEvent, "view" : view}, null);
  };
  
  this.forEvent = function (eventName) {
    return function(event, jsEvent, ui, view) {
      if (arguments.length == 3)
        call({"feedbackFor" : eventName, "event" : event, "jsEvent" : jsEvent, "view" : ui}, null);
      else
        call({"feedbackFor" : eventName, "event" : event, "jsEvent" : jsEvent, "ui" : ui, "view" : view}, null);
    };
  };
  
  this.forEventAlter = function (eventName) {
    return function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
      if (arguments.length == 8)
        call({"feedbackFor" : eventName, "event" : event, 
          "dayDelta" : dayDelta, "minuteDelta" : minuteDelta, "allDay" : allDay, 
          "jsEvent" : jsEvent, "ui" : ui, "view" : view}, revertFunc);
      else
        call({"feedbackFor" : eventName, "event" : event, 
          "dayDelta" : dayDelta, "minuteDelta" : minuteDelta, 
          "jsEvent" : revertFunc, "ui" : jsEvent, "view" : ui}, allDay);
    };
  };
});

$('#${calendar-id}').fullCalendar({
  editable: ${editable}
 ,defaultView: "${defaultView}"
 ,header: ${header}
 ,weekends: ${weekends}
 ,events: "${eventFeedURL}"
 ${feedbackHandlers}
 ${customOptions}
});
