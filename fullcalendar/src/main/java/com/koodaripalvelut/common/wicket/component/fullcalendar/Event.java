package com.koodaripalvelut.common.wicket.component.fullcalendar;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** FullCalendar Event description.
 * @see <a href="http://arshaw.com/fullcalendar/docs/event_data/Event_Object/">fullcalendar event</a>
 *
 * @author rhansen@kitsd.com
 */
public interface Event {
  String getCalId();
  String getTitle();
  boolean isAllDay();
  Date getStart();
  Date getEnd();
  String getURL();
  String getCSSClass();
  boolean isLocked();


  /** JsonSerializer in charge of generated an json string for the
   * {@link Event} interface. */
  public static JsonSerializer<Event> SERIALIZER = new JsonSerializer<Event>() {

    @Override
    public JsonElement serialize(final Event src, final Type typeOfSrc,
        final JsonSerializationContext context) {
      if (src == null) {
        return null;
      }
      final JsonObject jsonHeader = new JsonObject();
      jsonHeader.addProperty("id", src.getCalId());
      jsonHeader.addProperty("title", src.getTitle());
      jsonHeader.addProperty("allDay", src.isAllDay());
      jsonHeader.addProperty("start", src.getStart().getTime() / 1000);
      if (src.getEnd() != null) {
        jsonHeader.addProperty("end", src.getEnd().getTime() / 1000);
      }
      jsonHeader.addProperty("url", src.getURL());
      jsonHeader.addProperty("className", src.getCSSClass());
      jsonHeader.addProperty("editable", !src.isLocked());
      return jsonHeader;
    }

  };

}
