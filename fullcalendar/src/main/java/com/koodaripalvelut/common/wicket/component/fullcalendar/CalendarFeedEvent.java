package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.EVENT_COLLECTION_TYPE;
import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.GSON;

import java.util.Collection;
import java.util.Date;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.target.basic.StringRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** CalendarFeedEvent converts the event collection into a json feed for the fullcalendar script.
 * @author rhansen@kindleit.net
 */
public class CalendarFeedEvent extends AbstractAjaxBehavior {
  private static final long serialVersionUID = 1L;
  private static final Logger LOG =
    LoggerFactory.getLogger(CalendarFeedEvent.class);

  private final IModel<? extends Collection<? extends Event>> eventModel;

  public CalendarFeedEvent(final IModel<? extends Collection<? extends Event>> eventModel) {
    this.eventModel = eventModel;
  }

  @Override
  public void onRequest() {

    /** allow the model to filter what data to show, based on the view. */
    try {
      if (eventModel instanceof IEventFeedModel) {
        final Request request = RequestCycle.get().getRequest();
        final Long start = Long.parseLong(request.getParameter("start"));
        final Long end = Long.parseLong(request.getParameter("end"));
        ((IEventFeedModel) eventModel).setStartFilter(new Date(start*1000));
        ((IEventFeedModel) eventModel).setEndFilter(new Date(end*1000));
      }
    } catch (final NumberFormatException nfe) {
      LOG.error("error parsing event filter dates", nfe);
    }

    RequestCycle.get().setRequestTarget(
        new StringRequestTarget(GSON.toJson(eventModel.getObject(),
                                EVENT_COLLECTION_TYPE)));
  }

}
