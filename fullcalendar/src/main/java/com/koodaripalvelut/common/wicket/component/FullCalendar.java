package com.koodaripalvelut.common.wicket.component;

import static java.util.Collections.singletonMap;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

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
  private static TextTemplate JS_FULLCAL_INIT =
    new PackagedTextTemplate(FullCalendar.class, "calendar-init.js");
  private static ResourceReference CSS_FULLCAL =
    new CompressedResourceReference(FullCalendar.class, "fullcalendar.css");

  private class CalendarEvent extends AbstractDefaultAjaxBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    protected void respond(final AjaxRequestTarget target) {
      //TODO Working on it.
    }

  }

  public FullCalendar(final String id) {
    this(id, null);
  }

  public FullCalendar(final String id, final IModel<?> model) {
    super(id, model);
    init();
  }

  private void init() {
    setOutputMarkupId(true);
  }

  @Override
  public void renderHead(final HtmlHeaderContainer container) {
    final IHeaderResponse headerResponse = container.getHeaderResponse();
    final String calendarInit = JS_FULLCAL_INIT.asString(
      singletonMap("calendar-id", (Object) getMarkupId()));

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

    headerResponse.renderCSSReference(CSS_FULLCAL);
    headerResponse.renderJavascript(calendarInit, "cal-init-" + getMarkupId());

    super.renderHead(container);
  }

  @Override
  protected void onRender(final MarkupStream markupStream) {
    renderComponent(markupStream);
  }

  protected boolean includeJQuery() {
    return true;
  }

  protected boolean includeJQueryUI() {
    return true;
  }

}
