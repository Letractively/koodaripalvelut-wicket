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

import java.util.Collection;
import java.util.Date;

import org.apache.wicket.model.IModel;

/** IEventFeedModel is used by {@link CalendarFeedEvent} to know about models
 * that can use the shown interval to filter out what events to return.
 *
 * Models implementing this will be about to show only events visible inside
 * the selected view.
 *
 * @author rhansen@kitsd.com
 * @since 1.1
 */
public interface IEventFeedModel extends IModel<Collection<? extends Event>> {

  void setIntervalFilter(Date start, Date end);

}
