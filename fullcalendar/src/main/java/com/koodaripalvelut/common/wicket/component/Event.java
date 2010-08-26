package com.koodaripalvelut.common.wicket.component;

import java.util.Date;

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
}
