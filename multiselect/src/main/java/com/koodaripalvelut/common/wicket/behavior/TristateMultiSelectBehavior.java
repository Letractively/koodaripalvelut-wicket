package com.koodaripalvelut.common.wicket.behavior;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.util.string.JavascriptUtils;

public class TristateMultiSelectBehavior extends
AbstactMultiselectBehavior<TristateMultiSelectBehavior> {

  private static final long serialVersionUID = 1L;

  private final String labelClass = "label";

  private String subListText;

  public TristateMultiSelectBehavior(final String subListText) {
    this.subListText = subListText;
  }

  public TristateMultiSelectBehavior() {
    this.subListText = "sub-list";
  }

  @Override
  protected void onComponentRendered() {

    super.onComponentRendered();

    final StringBuilder script = new StringBuilder("$('#");
    script.append(getComponent().getMarkupId());
    script.append("').triStateMultiselect(");
    script.append(prepareOptions());
    script.append(");");

    JavascriptUtils.writeJavascript(getComponent().getResponse(), script.toString());
  }

  @Override
  public void renderHead(final IHeaderResponse response) {
    super.renderHead(response);
    response.renderJavascriptReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.tristateMultiselect.js"));
    response.renderCSSReference("/css/redmond2010/jquery-ui.css");
    response.renderCSSReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.multiselect.css"));
    response.renderJavascriptReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.tristate.js"));
    response.renderCSSReference(new ResourceReference(
        AbstactMultiselectBehavior.class, "jquery.tristate.css"));
    final StringBuffer script =
      new StringBuffer(
      "$(document).ready(function() {$('ul.triState','').tristate({heading: 'span.heading', labelClass: '");
    script.append(labelClass);
    script.append("'});});");
    response.renderJavascript(script, null);
  }

  @Override
  protected void respond(final AjaxRequestTarget target) {
  }

  @Override
  protected String addProperties() {
    final StringBuilder sb = new StringBuilder(", sublistText:'");
    sb.append(subListText);
    sb.append("'");
    return sb.toString();
  }

  public TristateMultiSelectBehavior setSubListText(final String text) {
    this.subListText = text;
    return this;
  }

}