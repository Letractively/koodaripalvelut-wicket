package com.koodaripalvelut.common.wicket.component.fullcalendar;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** FullCalendar Header description.
 * @see <a href="http://arshaw.com/fullcalendar/docs/display/header/">fullcalendar head</a>
 *
 * @author rhansen@kindleit.net
 */
public interface Header {
  // Key constants
  String JSON_RIGHT = "right";
  String JSON_CENTER = "center";
  String JSON_LEFT = "left";

  // Value constantss
  String TITLE = "title";
  String PREV_BTN = "prev";
  String NEXT_BTN = "next";
  String PREV_YEAR_BTN = "prevYear";
  String Next_YEAR_BTN = "nextYear";

  String GAP = " ";
  String ADJ = ",";

  String getLeft();
  String getCenter();
  String getRight();

  JsonSerializer<Header> SERIALIZER = new JsonSerializer<Header>() {
    @Override
    public JsonElement serialize(final Header src, final Type typeOfSrc,
        final JsonSerializationContext context) {
      final JsonObject jsonHeader = new JsonObject();
      jsonHeader.addProperty(JSON_LEFT, src.getLeft());
      jsonHeader.addProperty(JSON_CENTER, src.getCenter());
      jsonHeader.addProperty(JSON_RIGHT, src.getRight());
      return jsonHeader;
    }
  };

}
