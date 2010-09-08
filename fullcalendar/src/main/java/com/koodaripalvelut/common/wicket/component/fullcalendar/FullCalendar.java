package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/** Full Calendar Component that integrates the
 * <a href="http://arshaw.com/fullcalendar/">JQuery FullCalendar Plugin</a> with Wicket
 *
 * @author rhansen@kitsd.com
 */
public class FullCalendar extends Component
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = LoggerFactory.getLogger(FullCalendar.class);

  private static ResourceReference JS_JQUERY =
    new CompressedResourceReference(FullCalendar.class, "jquery/jquery.js");
  private static ResourceReference JS_JQUERY_JSON =
    new CompressedResourceReference(FullCalendar.class, "jquery/jquery-json.js");
  private static ResourceReference JS_JQUERY_UI =
    new CompressedResourceReference(FullCalendar.class, "jquery/jquery-ui-custom.js");
  private static ResourceReference JS_FULLCAL =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.min.js");
  private static ResourceReference JS_FULLCAL_DBG =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.js");
  private static ResourceReference FULLCAL_CSS =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.css");

  private static final String[] DATE_FORMATS =
    new String[] {"yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss",
                  "EEE, d MMM yyyy HH:mm:ss z", "EEE, d MMM yyyy HH:mm:ss"};

  private static final JsonDeserializer<Date> DATE_DESERIALIZER =
    new JsonDeserializer<Date>() {

    @Override
    public Date deserialize(final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException {
      final SimpleDateFormat format = new SimpleDateFormat();
      for (final String pattern : DATE_FORMATS) {
        try {
          format.applyPattern(pattern);
          return format.parse(json.getAsString());
        } catch (final Exception e1) {
          LOG.debug("Date parsing error", e1);
        }
      }
      try {
        return new Date(json.getAsLong() * 1000);
      } catch (final Exception ef) {
        throw new ClassCastException("Date Parsing options exhausted");
      }
    }
  };

  static final Gson GSON = new GsonBuilder()
    .registerTypeAdapter(Event.class, Event.SERIALIZER)
    .registerTypeAdapter(Header.class, Header.SERIALIZER)
    .registerTypeAdapter(Date.class, DATE_DESERIALIZER)
    .create();
  static final JsonParser PARSER = new JsonParser();

  static final Type EVENT_COLLECTION_TYPE =
    new TypeToken<Collection<Event>>(){}.getType();
  static final Type EVENT_TYPE =
    new TypeToken<Event>(){}.getType();

  /** AjaxEvent processes incoming clicking and hovering events from the fullcalendar script.
   * @author rhansen@kindleit.net
   */
  private class AjaxEvent extends AbstractDefaultAjaxBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    protected void respond(final AjaxRequestTarget target) {
      LOG.debug("event recieved");
      JsonElement req;
      try {
        req =
            PARSER.parse(((WebRequest) RequestCycle.get().getRequest())
                .getHttpServletRequest().getReader());
        LOG.debug("parsed: {}", req);
      } catch (final JsonParseException e) {
        LOG.error("Could not parse json request", e);
        throw new WicketRuntimeException(e);
      } catch (final IOException e) {
        LOG.error("IOException parsing json request", e);
        throw new WicketRuntimeException(e);
      }
      if (!req.isJsonObject()) {
        throw new WicketRuntimeException("json request was not a json object");
      }
      final AjaxFeedBack feedback = new AjaxFeedBack((JsonObject) req);
      if (!feedback.has("feedbackFor")) {
        throw new WicketRuntimeException("json request has no feedbackFor");
      }
      if (!onEvent(target, feedback)) {
        ((WebResponse) RequestCycle.get().getResponse())
        .getHttpServletResponse().setStatus(SC_CONFLICT);
      }
    }

  }


  private final CalendarFeedEvent eventFeed;
  private final AjaxEvent feedbackHandler = new AjaxEvent();

  private boolean editable = true;


  public FullCalendar(final String id) {
    this(id, null);
  }

  public FullCalendar(final String id,
      final IModel<? extends Collection<? extends Event>> model) {
    super(id, model);
    eventFeed = new CalendarFeedEvent(model);
    init();
  }

  /** @return Whether this component allows events to be altered or removed by the client. */
  public boolean isEditable() {
    return editable;
  }

  /** Enables or disables editing.
   * @param editable
   * @return this.
   */
  public FullCalendar setEditable(final boolean editable) {
    this.editable = editable;
    return this;
  }

  /** Override this method to catch events received from the AJAX component.
   * @return true if the event should be processed successfully;
   *         false otherwise. */
  public boolean onEvent(final AjaxRequestTarget target, final AjaxFeedBack feedback) {
    return true;
  }

  /** Renders the {@link FullCalendar} via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/display/render/">render</a>
   * @return this.
   */
  public FullCalendar render(final AjaxRequestTarget target) {
    return callMethod(target, "'render'");
  }

  /** Changes the display's View via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/views/changeView/">changeView</a>
   * @return this.
   */
  public FullCalendar changeView(final AjaxRequestTarget target, final Views view) {
    return callMethod(target, "'changeView'", strWrap(view));
  }

  /** Moves the calendar one step back (either by a month, week, or day) via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/current_date/prev/">prev</a>
   * @return this.
   */
  public FullCalendar prev(final AjaxRequestTarget target) {
    return callMethod(target, "'prev'");
  }

  /** Moves the calendar one step forward (either by a month, week, or day) via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/current_date/next/">next</a>
   * @return this.
   */
  public FullCalendar next(final AjaxRequestTarget target) {
    return callMethod(target, "'next'");
  }

  /** Moves the calendar to the current date via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/current_date/today/">today</a>
   * @return this.
   */
  public FullCalendar today(final AjaxRequestTarget target) {
    return callMethod(target, "'today'");
  }

  /** Moves the calendar to an arbitrary year/month/date via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/current_date/gotoDate/">gotoDate</a>
   * @return this.
   */
  public FullCalendar gotoDate(final AjaxRequestTarget target, final Date date) {
    final Calendar c = Calendar.getInstance();
    c.setTime(date);
    return callMethod(target, "'gotoDate'", c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
  }

  /** Select a period of time via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/selection/select_method/">select_method</a>
   * @return this.
   */
  public FullCalendar select(final AjaxRequestTarget target, final Date start, final Date end,
      final boolean allDay) {
    return callMethod(target, "'select'", strWrap(start), strWrap(end), allDay);
  }

  /** UnSelect a period of time via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/selection/unselect_method/">unselect_method</a>
   * @return this.
   */
  public FullCalendar unselect(final AjaxRequestTarget target) {
    return callMethod(target, "'unselect'");
  }

  /** Removes events from the calendar via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_data/removeEvents/">removeEvents</a>
   * @return this.
   */
  public FullCalendar removeEvents(final AjaxRequestTarget target, final String idOrFilter) {
    return callMethod(target, "'removeEvents'", idOrFilter);
  }

  /** Refetches events from all sources and re-renders them on the screen via AJAX.
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_data/refetchEvents/">refetchEvents</a>
   * @return this.
   */
  public FullCalendar refetchEvents(final AjaxRequestTarget target) {
    return callMethod(target, "'refetchEvents'");
  }

  /** Renders a new event on the calendar via AJAX.
   * <b>Note</b> <i>Adding an event source will not modify the underlying Model for the
   * {@link FullCalendar}; i.e. getDefaultModelObject()} is not modified in any way.</i>
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/">renderEvent</a>
   * @return this.
   */
  public FullCalendar renderEvent(final AjaxRequestTarget target, final Event event,
      final boolean stick) {
    return callMethod(target, "'addEventSource'", GSON.toJson(event, EVENT_TYPE), stick);
  }

  /** Dynamically adds an event source via AJAX.
   * <b>Note</b> <i>Adding an event source will not modify the underlying Model for the
   * {@link FullCalendar}; i.e. getDefaultModelObject()} is not modified in any way.</i>
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_data/addEventSource/">addEventSource</a>
   * @return this.
   */
  public FullCalendar addEventSource(final AjaxRequestTarget target, final String directParam) {
    return callMethod(target, "'addEventSource'", directParam);
  }

  /** Dynamically adds an event source via AJAX.
   * @see #addEventSource(AjaxRequestTarget, String)
   * @return this.
   */
  public FullCalendar addEventSource(final AjaxRequestTarget target,
      final Collection<? extends Event> events) {
    return addEventSource(target, GSON.toJson(events, EVENT_COLLECTION_TYPE));
  }

  /** Dynamically adds an event source via AJAX.
   * @see #addEventSource(AjaxRequestTarget, String)
   * @return a created CalendatFeedEvent.
   */
  public CalendarFeedEvent addEventSource(final AjaxRequestTarget target,
      final IModel<? extends Collection<? extends Event>> eventModel) {
    final CalendarFeedEvent eventFeed = new CalendarFeedEvent(eventModel);
    add(eventFeed);
    addEventSource(target, strWrap(eventFeed.getCallbackUrl()));
    return eventFeed;
  }

  /** Dynamically removes an event source via AJAX.
   * <b>Note</b> <i>Removing an event source will not modify the underlying Model for the
   * {@link FullCalendar}; i.e. getDefaultModelObject()} is not modified in any way.</i>
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_data/removeEventSource/">removeEventSource</a>
   * @return this.
   */
  public FullCalendar removeEventSource(final AjaxRequestTarget target, final String directParam) {
    return callMethod(target, "'removeEventSource'", directParam);
  }

  /** Dynamically removes an event source via AJAX.
   * @see #removeEventSource(AjaxRequestTarget, String)
   * @return this.
   */
  public FullCalendar removeEventSource(final AjaxRequestTarget target,
      final Collection<? extends Event> events) {
    return removeEventSource(target, GSON.toJson(events, EVENT_COLLECTION_TYPE));
  }

  /** Dynamically adds an event source via AJAX.
   * @see #removeEventSource(AjaxRequestTarget, String)
   * @return this.
   */
  public FullCalendar removeEventSource(final AjaxRequestTarget target,
      final CalendarFeedEvent eventFeed) {
    remove(eventFeed);
    return removeEventSource(target, strWrap(eventFeed.getCallbackUrl()));
  }

  /** Calls a method on the constructed fullCalendar javascript object.
   * @return this.
   */
  public final FullCalendar callMethod(final AjaxRequestTarget target, final Object... params) {
    if (params.length == 0) {
      throw new IllegalArgumentException("At least one parameter must be set");
    }
    final StringBuilder sb = new StringBuilder("$('#")
      .append(getMarkupId()).append("').fullCalendar(").append(params[0]);
    for (int i = 1; i < params.length; i++) {
      sb.append(',').append(params[i]);
    }
    target.appendJavascript(sb.append(");").toString());
    return this;
  }

  /** Adds all necesary javascript to the html head.
   * @see org.apache.wicket.Component#renderHead(HtmlHeaderContainer)
   */
  @Override
  public void renderHead(final HtmlHeaderContainer container) {
    final IHeaderResponse headerResponse = container.getHeaderResponse();
    final Map<String, Object> initParams = new HashMap<String, Object>();

    setOptions(initParams);

    if (includeJQuery()) {
      headerResponse.renderJavascriptReference(JS_JQUERY);
    }
    if (includeJQueryJSON()) {
      headerResponse.renderJavascriptReference(JS_JQUERY_JSON);
    }
    if (includeJQueryUI()) {
      headerResponse.renderJavascriptReference(JS_JQUERY_UI);
    }
    if (getApplication().getDebugSettings().isAjaxDebugModeEnabled()) {
      headerResponse.renderJavascriptReference(JS_FULLCAL_DBG);
    } else {
      headerResponse.renderJavascriptReference(JS_FULLCAL);
    }

    headerResponse.renderCSSReference(FULLCAL_CSS);
    headerResponse.renderOnDomReadyJavascript(
        new PackagedTextTemplate(FullCalendar.class, "calendar-init.js").asString(initParams));

    super.renderHead(container);
  }

  /** This component's markup is generated on the client side via the fullcalendar jquery plugin.
   * @see org.apache.wicket.Component#onRender(MarkupStream)
   */
  @Override
  protected void onRender(final MarkupStream markupStream) {
    renderComponent(markupStream);
  }

  /** Override this method to set your own options for the fullcalendar construction
   * @see <a href="http://arshaw.com/fullcalendar/docs/usage/">fullcalendar usage</a>
   * @param params
   */
  protected void setOptions(final Map<String, Object> params) {
    params.put("calendar-id", getMarkupId());
    params.put("editable", isEditable());
    params.put("weekends", true);
    params.put("defaultView", getDefaultView());
    params.put("header", GSON.toJson(getHeader(), Header.class));
    params.put("eventFeedURL", eventFeed.getCallbackUrl());
    params.put("feedbackURL", feedbackHandler .getCallbackUrl());
    params.put("feedbackHandlers", getAjaxFeedHandler());
    final Calendar viewDate = Calendar.getInstance();
    viewDate.setTime(getViewDate());
    params.put("year", viewDate.get(YEAR));
    params.put("month", viewDate.get(MONTH));
    params.put("day", viewDate.get(DAY_OF_MONTH));
    params.put("customOptions", getCustomOptions());
  }

  /** Override to specify additional custom options.
   *
   * <p>To change the firstDay of the calendar to monday, you may return: <br/>
   * <code>return ", firstDay : 1";</code>
   *
   * <p>To change the timeFormat of the calendar display, you may return: <br/>
   * <code>return ", timeFormat : { '' : 'h:mm{ - h:mm}' }";</code>
   *
   * <p>It is also possible to combine any amount of custom options for example:
   * <br/><code>return ", firstDay:1, dayNames:['Domingo', 'Lunes', 'Martes',
   * 'Miercoles', 'Jueves', 'Viernes', 'Sábado']";
   *
   * @return a String that must be either empty "", or start with a comma ","
   */
  protected String getCustomOptions() {
    return "";
  }

  /** Get the default start date for the full calendar view.
   * Defaults to new Date();
   * @return the date to present in the view.
   */
  protected Date getViewDate() {
    return new Date();
  }

  /** Override to specify an alternate defaultView.
   * @see Views
   * @return the defaultView
   */
  protected Views getDefaultView() {
    return Views.MONTH;
  }

  /** Override to specify an alternate calendar header.
   * @see Header
   * @return the calendar header
   */
  protected Header getHeader() {
    return new Header() {
      @Override
      public String getLeft() {
        return TITLE;
      }

      @Override
      public String getCenter() {
        return "";
      }

      @Override
      public String getRight() {
        return Views.MONTH + ADJ + Views.WEEK + ADJ + Views.DAY
        + GAP + PREV_BTN + ADJ + NEXT_BTN;
      }
    };
  }

  /** Override to not include the jquery.js javascript reference.
   * @return true to include the jquery reference in {@link #renderHead(HtmlHeaderContainer)}.
   */
  protected boolean includeJQuery() {
    return true;
  }

  /** Override to not include the jquery-json.js javascript reference.
   * @return true to include the jquery-json reference in {@link #renderHead(HtmlHeaderContainer)}.
   */
  protected boolean includeJQueryJSON() {
    return true;
  }

  /** Override to not include the jquery-ui-custom.js javascript reference.
   * @return true to include the jquery ui reference in {@link #renderHead(HtmlHeaderContainer)}.
   */
  protected boolean includeJQueryUI() {
    return true;
  }

  /** Override to disable or conditionally set the AJAX callback for the dayClick event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/mouse/dayClick/">dayClick</a>
   */
  protected boolean trackDayClick() {
    return true;
  }
  /** Override to disable or conditionally set the AJAX callback for the eventClick event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/mouse/eventClick/">eventClick</a>
   */
  protected boolean trackEventClick() {
    return true;
  }

  /** Override to disable or conditionally set the AJAX callback for the eventDrop event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_ui/eventDrop/">eventDrop</a>
   */
  protected boolean trackEventDrop() {
    return true;
  }

  /** Override to disable or conditionally set the AJAX callback for the eventResize event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/event_ui/eventResize/">eventResize</a>
   */
  protected boolean trackEventResize() {
    return true;
  }

  /** Override to disable or conditionally set the AJAX callback for the select event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/selection/select_callback/">select</a>
   */
  protected boolean trackSelect() {
    return false;
  }

  /** Override to disable or conditionally set the AJAX callback for the unselect event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/selection/unselect_callback/">unselect</a>
   */
  protected boolean trackUnselect() {
    return false;
  }

  /** Override to disable or conditionally set the AJAX callback for the viewDisplay event.
   * @return true to enable AJAX callback.
   * @see <a href="http://arshaw.com/fullcalendar/docs/display/viewDisplay/">viewDisplay</a>
   */
  protected boolean trackViewDisplay() {
    return false;
  }

  private void init() {
    setOutputMarkupId(true);
    add(eventFeed);
    add(feedbackHandler);
  }

  private String getAjaxFeedHandler() {
    final StringBuilder sb = new StringBuilder();
    if (trackDayClick()) {
      sb.append(",dayClick: Feedback.forDayClick");
    }
    if (trackEventClick()) {
      sb.append(",eventClick : Feedback.forEvent('eventClick')");
    }
    if (trackEventDrop()) {
      sb.append(",eventDrop: Feedback.forEventAlter('eventDrop')");
    }
    if (trackEventResize()) {
      sb.append(",eventResize: Feedback.forEventAlter('eventResize')");
    }
    if (trackSelect()) {
      sb.append(",select: Feedback.forSelect");
    }
    if (trackUnselect()) {
      sb.append(",unselect: Feedback.forUnselect");
    }
    if (trackViewDisplay()) {
      sb.append(",viewDisplay: Feedback.forSelect");
    }
    return sb.toString();
  }

  private String strWrap(final Object end) {
    final StringBuilder sb = new StringBuilder();
    return sb.append('\'').append(end.toString()).append('\'').toString();
  }

}
