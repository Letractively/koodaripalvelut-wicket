package com.koodaripalvelut.common.wicket.component.fullcalendar;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** The Event Interface
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

  JsonSerializer<Event> SERIALIZER = new JsonSerializer<Event>() {
    @Override
    public JsonElement serialize(final Event src, final Type typeOfSrc,
        final JsonSerializationContext context) {
      final JsonObject jsonHeader = new JsonObject();
      jsonHeader.addProperty("id", src.getCalId());
      jsonHeader.addProperty("title", src.getTitle());
      jsonHeader.addProperty("allDay", src.isAllDay());
      jsonHeader.add("start", context.serialize(src.getStart()));
      jsonHeader.add("end", context.serialize(src.getEnd()));
      jsonHeader.addProperty("url", src.getURL());
      jsonHeader.addProperty("className", src.getCSSClass());
      jsonHeader.addProperty("editable", !src.isLocked());
      return jsonHeader;
    }
  };
}
