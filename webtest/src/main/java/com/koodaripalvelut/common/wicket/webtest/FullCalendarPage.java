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

import com.koodaripalvelut.common.wicket.component.FullCalendar;

/** FullCalendarPage is responsible of
 * @author rhansen@kitsd.com
 */
public class FullCalendarPage extends BasePage {

  public FullCalendarPage() {
    add(new FullCalendar("calendar"));
  }

}
