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
 * @since 1.1
 */
public class AjaxFeedBack implements IClusterable {

  /** Feedback for entry key. */
  public static final String FEEDBACK_FOR = "feedbackFor";
  /** Event entry key. */
  public static final String EVENT = "event";
  /** All day entry key. */
  public static final String ALL_DAY = "allDay";
  /** Date entry key. */
  public static final String DATE = "date";
  /** Start Date entry key. */
  public static final String START_DATE = "startDate";
  /** End Date entry key. */
  public static final String END_DATE = "endDate";
  /** Display mode entry key. */
  public static final String VIEW = "view";
  
  /** Day click event. */
  public static final String DAY_CLICK = "dayClick";
  /** Event drop event. */
  public static final String EVENT_DROP = "eventDrop";
  /** Event click event. */
  public static final String EVENT_CLICK = "eventClick";
  /** View display event. */
  public static final String VIEW_DISPLAY = "viewDisplay";

  private static final long serialVersionUID = 1L;

  JsonObject feedback;

  /**
   * @param obj JsonObject for feedback.
   */
  public AjaxFeedBack(final JsonObject obj) {
    feedback = obj;
  }

  /**
   * @return Set<Map.Entry<String, JsonElement>> All feedback entries.
   */
  public Set<Map.Entry<String, JsonElement>> entrySet() {
    return feedback.entrySet();
  }

  /** Test whether a specific entry was given in feedback.
   * @param key Entry key.
   * @return boolean true if the entry was found.
   */
  public boolean has(final String key) {
    return feedback.has(key);
  }

  /**
   * @param key ntry Key.
   * @return JsonElement Entry for given key.
   */
  public JsonElement get(final String key) {
    return feedback.get(key);
  }

  /** Returns the type of feedback, it identifies the javascript 
    event that triggered this feedback.
   * @return String FEEDBACK_FOR entry value.
   */
  public String getFeedbackFor() {
    return feedback.get(FEEDBACK_FOR).getAsString();
  }

  /**
   * @return Date The DATE entry value.
   */
  public Date getDate() {
    if (!has(DATE)) {
      return null;
    }
    return GSON.fromJson(feedback.get(DATE), Date.class);
  }

  /**
   * @return Date The START_DATE entry value.
   */
  public Date getStartDate() {
    if (!has(START_DATE)) {
      return null;
    }
    return GSON.fromJson(feedback.get(START_DATE), Date.class);
  }

  /**
   * @return Date The END_DATE entry value.
   */
  public Date getEndDate() {
    if (!has(END_DATE)) {
      return null;
    }
    return GSON.fromJson(feedback.get(END_DATE), Date.class);
  }

  /**
   * @return Boolean The ALL_DAY entry value.
   */
  public Boolean isAllDay() {
    if (!has(ALL_DAY)) {
      return null;
    }
    return feedback.get(ALL_DAY).getAsBoolean();
  }

  /**
   * @return String The EVENT entry value.
   */
  public Event getEvent() {
    if (!has(EVENT)) {
      return null;
    }
    return GSON.fromJson(feedback.get(EVENT), Event.class);
  }

  /**
   * @return String The DISPLAY_MODE entry value.
   */
  public String getDisplayMode() {
    JsonElement jsonElement = feedback.get(VIEW);
    return (jsonElement != null) ? jsonElement.getAsString() : null; 
  }
}

