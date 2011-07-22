package com.koodaripalvelut.common.wicket.behavior;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.util.string.JavascriptUtils;

public class MultiSelectBehavior extends
AbstactMultiselectBehavior<MultiSelectBehavior> {
  private static final long serialVersionUID = 1L;

  /**
   * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onComponentRendered()
   */
  @Override
  protected void onComponentRendered() {
    super.onComponentRendered();
    final StringBuilder script =
      new StringBuilder("$('#" + getComponent().getMarkupId()
          + "').multiselect(" + prepareOptions() + ")");
    if (isFiltering()) {
      script.append(".multiselectfilter({label: 'Search:'})");
    }
    if (isTriState()) {
      script.append(".tristate()");
    }
    script.append(";");
    JavascriptUtils.writeJavascript(getComponent().getResponse(),
        script.toString());
    // getComponent().getResponse().renderOnDomReadyJavascript(script);
  }

  /**
   * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
   */
  @Override
  public void renderHead(final IHeaderResponse response) {
    super.renderHead(response);
    response.renderJavascriptReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.multiselect.js"));
    response.renderCSSReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.multiselect.css"));
    if (isFiltering()) {
      response.renderJavascriptReference(new ResourceReference(
          AbstactMultiselectBehavior.class,
      "jquery.multiselect.filter.js"));
      response.renderCSSReference(new ResourceReference(
          AbstactMultiselectBehavior.class, "jquery.multiselect.filter.css"));
    }
    if (isTriState()) {
      response.renderJavascriptReference(new ResourceReference(
          AbstactMultiselectBehavior.class, "jquery.tristate.js"));
      response.renderCSSReference(new ResourceReference(
          AbstactMultiselectBehavior.class, "jquery.tristate.css"));
    }
    {
      // Destroy old widget
      final String script =
        "try {$('#" + getComponent().getMarkupId()
        + "').multiselect('destroy');} catch (e) {}"; // Replace previous
      response.renderJavascript(script, null);
    }
  }

  /**
   * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
   */
  @Override
  protected void respond(final AjaxRequestTarget pTarget) {
    //
  }

}