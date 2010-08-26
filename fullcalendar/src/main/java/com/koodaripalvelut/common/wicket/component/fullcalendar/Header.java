package com.koodaripalvelut.common.wicket.component.fullcalendar;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public interface Header {
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
      jsonHeader.addProperty("left", src.getLeft());
      jsonHeader.addProperty("center", src.getCenter());
      jsonHeader.addProperty("right", src.getRight());
      return jsonHeader;
    }
  };
}

