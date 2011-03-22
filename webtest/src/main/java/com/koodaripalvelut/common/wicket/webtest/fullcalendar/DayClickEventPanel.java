/*
 * Copyright 2010 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */


package com.koodaripalvelut.common.wicket.webtest.fullcalendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.koodaripalvelut.common.wicket.component.fullcalendar.AjaxFeedBack;

/** DayClickEventPanel is responsible of
 * @author rhansen@kitsd.com
 */
public class DayClickEventPanel extends Panel {
  private static final long serialVersionUID = 1L;

  public DayClickEventPanel(final String id, final IModel<AjaxFeedBack> feedback) {
    super(id, new CompoundPropertyModel<AjaxFeedBack>(feedback));
    add(new Label("date"));
  }

}
