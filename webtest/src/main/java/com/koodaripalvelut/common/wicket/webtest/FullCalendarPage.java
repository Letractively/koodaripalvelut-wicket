/*
 * Copyright 2010 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */


package com.koodaripalvelut.common.wicket.webtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;

import com.koodaripalvelut.common.wicket.component.fullcalendar.Event;
import com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar;
import com.koodaripalvelut.common.wicket.component.fullcalendar.Views;

/** FullCalendarPage is responsible of
 * @author rhansen@kitsd.com
 */
public class FullCalendarPage extends BasePage {

  private static class E implements Event, Serializable {
    private static final long serialVersionUID = 1L;

    private final Double id = Math.random();
    private Date end;
    private Date start;
    private String title;

    @Override
    public String getCSSClass() {
      return "event";
    }

    @Override
    public String getCalId() {
      return id.toString();
    }

    @Override
    public Date getEnd() {
      return end;
    }

    @Override
    public Date getStart() {
      return start;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public String getURL() {
      return "";
    }

    @Override
    public boolean isAllDay() {
      return false;
    }

    @Override
    public boolean isLocked() {
      return false;
    }

  }


  private final FullCalendar fullCalendar;


  public FullCalendarPage() {
    fullCalendar = new FullCalendar("calendar", Model.ofList(generateEvents()));
    add(fullCalendar);

    add(new AjaxLink<Void>("addEvents") {
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(final AjaxRequestTarget target) {
        fullCalendar.addEventSource(target, generateEvents());
      }
    });

    add(new AjaxLink<Void>("changeView") {
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(final AjaxRequestTarget target) {
        fullCalendar.changeView(target, Views.values()[(int) (Math.random() * Views.values().length)] );
      }
    });

    add(new AjaxLink<Void>("changeDate") {
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(final AjaxRequestTarget target) {
        fullCalendar.gotoDate(target, new Date((long) (Math.random() * 25000000000000l)));
      }
    });
  }


  private List<E> generateEvents() {
    final Calendar c = Calendar.getInstance();
    final List<E> events = new ArrayList<E>(4);
    for (int i = 0; i < 4; i++) {
      final E e = new E();
      e.title = "Test Event " + i;
      c.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * 15 * 24));
      c.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * -15 * 24));
      e.start = c.getTime();
      c.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * 72));
      e.end = c.getTime();
      events.add(e);
    }
    return events;
  }

}