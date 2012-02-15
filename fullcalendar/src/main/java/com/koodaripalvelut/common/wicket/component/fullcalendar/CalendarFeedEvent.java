package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.EVENT_COLLECTION_TYPE;
import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.GSON;
import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.fromJavascriptTimestamp;

import java.util.Collection;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** CalendarFeedEvent converts the event collection into a json feed for the fullcalendar script.
 * @author rhansen@kindleit.net
 */
public class CalendarFeedEvent extends AbstractAjaxBehavior {
  public static final String START_DATE = "start";
  public static final String END_DATE = "end";

  private static final long serialVersionUID = 1L;
  private static final Logger LOG =
    LoggerFactory.getLogger(CalendarFeedEvent.class);

  private final IModel<? extends Collection<? extends Event>> eventModel;

  /**
   * @param eventModel EventModel for the feed.
   */
  public CalendarFeedEvent(final IModel<? extends Collection<? extends Event>> eventModel) {
    this.eventModel = eventModel;
  }

  @Override
  public void onRequest() {

    /** allow the model to filter what data to show, based on the view. */
    try {
      if (eventModel instanceof IEventFeedModel) {
        final Request request = RequestCycle.get().getRequest();
        final Long start = Long.parseLong(request.getRequestParameters()
            .getParameterValue(START_DATE).toString());
        final Long end = Long.parseLong(request.getRequestParameters()
            .getParameterValue(END_DATE).toString());
        ((IEventFeedModel) eventModel).setIntervalFilter(
            fromJavascriptTimestamp(start), fromJavascriptTimestamp(end));
      }
    } catch (final NumberFormatException nfe) {
      LOG.error("error parsing event filter dates", nfe);
    }

    RequestCycle.get().scheduleRequestHandlerAfterCurrent(
        new TextRequestHandler(GSON.toJson(eventModel.getObject(),
                                EVENT_COLLECTION_TYPE)));
  }

}
