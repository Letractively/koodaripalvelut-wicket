package com.koodaripalvelut.common.wicket.component.fullcalendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.target.basic.StringRequestTarget;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author rhansen@kitsd.com
 */
public class FullCalendar extends Component
{
  private static final long serialVersionUID = 1L;

  private static ResourceReference JS_JQUERY =
    new CompressedResourceReference(FullCalendar.class, "jquery/jquery.js");
  private static ResourceReference JS_JQUERY_UI =
    new CompressedResourceReference(FullCalendar.class, "jquery/jquery-ui-custom.js");
  private static ResourceReference JS_FULLCAL =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.min.js");
  private static ResourceReference JS_FULLCAL_DBG =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.js");
  private static TextTemplate FULLCAL_INI =
    new PackagedTextTemplate(FullCalendar.class, "calendar-init.js");
  private static ResourceReference FULLCAL_CSS =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.css");

  private static final Gson GSON = new GsonBuilder()
    .registerTypeAdapter(Event.class, Event.SERIALIZER)
    .registerTypeAdapter(Header.class, Header.SERIALIZER)
    .create();

  private class CalendarFeedEvent extends AbstractAjaxBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    public void onRequest() {
      final StringRequestTarget calendarRequestTarget =
        new StringRequestTarget(GSON.toJson(getDefaultModelObject(),
            new TypeToken<Collection<Event>>(){}.getType()));

      RequestCycle.get().setRequestTarget(calendarRequestTarget);
    }

  }

  private boolean editable = true;
  private final CalendarFeedEvent eventFeed = new CalendarFeedEvent();

  public FullCalendar(final String id) {
    this(id, null);
  }

  public FullCalendar(final String id,
      final IModel<? extends Collection<? extends Event>> model) {

    super(id, model);
    init();
  }

  public boolean isEditable() {
    return editable;
  }
  public FullCalendar setEditable(final boolean editable) {
    this.editable = editable;
    return this;
  }

  @Override
  public void renderHead(final HtmlHeaderContainer container) {
    final IHeaderResponse headerResponse = container.getHeaderResponse();
    final Map<String, Object> initParams = new HashMap<String, Object>();

    setOptions(initParams);

    if (includeJQuery()) {
      headerResponse.renderJavascriptReference(JS_JQUERY);
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
    headerResponse.renderOnDomReadyJavascript(FULLCAL_INI.asString(initParams));

    super.renderHead(container);
  }

  @Override
  protected void onRender(final MarkupStream markupStream) {
    renderComponent(markupStream);
  }

  protected void setOptions(final Map<String, Object> params) {
    params.put("calendar-id", getMarkupId());
    params.put("editable", isEditable());
    params.put("weekends", true);
    params.put("defaultView", getDefaultView());
    params.put("header", GSON.toJson(getHeader(), Header.class));
    params.put("customOptions", "");
    params.put("eventFeedURL", eventFeed.getCallbackUrl());
  }

  protected Views getDefaultView() {
    return Views.MONTH;
  }

  protected boolean includeJQuery() {
    return true;
  }

  protected boolean includeJQueryUI() {
    return true;
  }

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

  private void init() {
    setOutputMarkupId(true);
    add(eventFeed);
  }

}
