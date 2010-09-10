package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.toJavascriptTimestamp;

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
  public static final String JSON_EDITABLE = "editable";
  public static final String JSON_CLASS_NAME = "className";
  public static final String JSON_URL = "url";
  public static final String JSON_END = "end";
  public static final String JSON_START = "start";
  public static final String JSON_ALL_DAY = "allDay";
  public static final String JSON_TITLE = "title";
  public static final String JSON_ID = "id";


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
      jsonHeader.addProperty(JSON_ID, src.getCalId());
      jsonHeader.addProperty(JSON_TITLE, src.getTitle());
      jsonHeader.addProperty(JSON_ALL_DAY, src.isAllDay());
      jsonHeader.addProperty(JSON_START, toJavascriptTimestamp(src.getStart()));
      if (src.getEnd() != null) {
        jsonHeader.addProperty(JSON_END, toJavascriptTimestamp(src.getEnd()));
      }
      jsonHeader.addProperty(JSON_URL, src.getURL());
      jsonHeader.addProperty(JSON_CLASS_NAME, src.getCSSClass());
      jsonHeader.addProperty(JSON_EDITABLE, !src.isLocked());
      return jsonHeader;
    }

  };

}
