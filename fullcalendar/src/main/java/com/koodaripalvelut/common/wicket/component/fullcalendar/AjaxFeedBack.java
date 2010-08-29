/*
 * Copyright 2010 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */


package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.GSON;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.IClusterable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** AjaxFeedBack is responsible of
 * @author rhansen@kitsd.com
 */
public class AjaxFeedBack implements IClusterable {
  private static final long serialVersionUID = 1L;

  JsonObject feedback;

  public AjaxFeedBack(final JsonObject obj) {
    feedback = obj;
  }

  public Set<Map.Entry<String, JsonElement>> entrySet() {
    return feedback.entrySet();
  }

  public boolean has(final String key) {
    return feedback.has(key);
  }

  public JsonElement get(final String member) {
    return feedback.get(member);
  }

  public String getFeedbackFor() {
    return feedback.get("feedbackFor").getAsString();
  }

  public Date getDate() {
    if (!has("date")) {
      return null;
    }
    return GSON.fromJson(feedback.get("date"), Date.class);
  }

  public Date getStartDate() {
    if (!has("startDate")) {
      return null;
    }
    return GSON.fromJson(feedback.get("startDate"), Date.class);
  }

  public Date getEndDate() {
    if (!has("endDate")) {
      return null;
    }
    return GSON.fromJson(feedback.get("endDate"), Date.class);
  }

  public Boolean isAllDay() {
    if (!has("allDay")) {
      return null;
    }
    return feedback.get("allDay").getAsBoolean();
  }

  public Event getEvent() {
    if (!has("event")) {
      return null;
    }
    return GSON.fromJson(feedback.get("event"), Event.class);
  }
}

