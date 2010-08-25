package com.koodaripalvelut.common.wicket.component;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;

/**
 * @author rhansen@kitsd.com
 */
public class FullCalendar extends Panel
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

  private class CalendarEvent extends AbstractDefaultAjaxBehavior {

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
  }

  @Override
  public void renderHead(final HtmlHeaderContainer container) {
    final IHeaderResponse headerResponse = container.getHeaderResponse();
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
    super.renderHead(container);
  }

  protected boolean includeJQuery() {
    return true;
  }

  protected boolean includeJQueryUI() {
    return true;
  }

}
