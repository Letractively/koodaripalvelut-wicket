package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.EVENT_COLLECTION_TYPE;
import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.GSON;

import java.util.Collection;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.target.basic.StringRequestTarget;

/** CalendarFeedEvent converts the event collection into a json feed for the fullcalendar script.
 * @author rhansen@kindleit.net
 */
public class CalendarFeedEvent extends AbstractAjaxBehavior {
  private static final long serialVersionUID = 1L;

  private final IModel<? extends Collection<? extends Event>> eventModel;

  public CalendarFeedEvent(final IModel<? extends Collection<? extends Event>> eventModel) {
    this.eventModel = eventModel;
  }

  @Override
  public void onRequest() {
    final StringRequestTarget calendarRequestTarget =
      new StringRequestTarget(GSON.toJson(eventModel.getObject(), EVENT_COLLECTION_TYPE));

    RequestCycle.get().setRequestTarget(calendarRequestTarget);
  }

}
