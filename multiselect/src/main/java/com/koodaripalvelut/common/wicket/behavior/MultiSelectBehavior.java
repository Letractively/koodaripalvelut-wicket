package com.koodaripalvelut.common.wicket.behavior;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.util.string.JavascriptUtils;

/**
 * Multi select behavior for wicket {@link ListMultipleChoice} component. Its
 * creates jQuery multiSelect object on wicket {@link ListMultipleChoice}
 * component.
 * 
 * http://www.erichynds.com/examples/jquery-ui-multiselect-widget/demos/#selectedlist
 * 
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/#disqus_thread
 * 
 * @author mikel
 * 
 * Copyright (c) Koodaripalvelut.com Finland 2008
 * 
 * TODO BUG: IF SELECTIONS MADE AND ESC PRESSED, IT SHOULD MAYBE CANCEL SELECTIONS?
 */
public class MultiSelectBehavior extends AbstractDefaultAjaxBehavior {
  private static final long serialVersionUID = 1L;
  private final String noneSelectedKey;
  private final static String STATE_VARIABLE_PREFIX = "multiSelectState";

  /**
   * If set to false, the widget will use radio buttons instead of checkboxes, forcing users to select only one option.
   */
  private boolean multiple = true;
  /**
   * If set to true, filtering is enabled.
   */
  private boolean filtering;

  private boolean tristate;

  private int minWidth = 255;
  private int height = 175;
  private boolean miniButton;

  /**
   *
   */
  public MultiSelectBehavior() {
    this("multiselect-none-selected-text");
  }

  /**
   * @param string
   */
  public MultiSelectBehavior(final String string) {
    this.noneSelectedKey = string;
  }




  /**
   * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
   */
  @Override
  protected void respond(final AjaxRequestTarget pTarget) {
    //
  }

  /**
   * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#onBind()
   */
  @Override
  protected void onBind() {
    if (!AbstractChoice.class.isAssignableFrom(getComponent().getClass())) {
      throw new WicketRuntimeException(
          "Error while adding behavior to component ["
          + getComponent().getPath() + "]. Behavior of type "
          + this.getClass().getName()
          + " may not be added to components other than selects.");
    }
    super.onBind();
  }

  /**
   * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
   */
  @Override
  public void renderHead(final IHeaderResponse response) {
    //    response.renderJavascriptReference(JavaScriptConstants.JS_JQUERY);
    //    response.renderJavascriptReference(JavaScriptConstants.JS_JQUERY_UI);
    response.renderCSSReference("/css/redmond2010/jquery-ui.css");
    response.renderCSSReference(new ResourceReference(
        MultiSelectBehavior.class, "jquery.multiselect.css"));
    if (isTristate()) {
      response.renderJavascriptReference(new ResourceReference(
          MultiSelectBehavior.class, "jquery.tristateMultiselect.js"));
      response.renderJavascriptReference(new ResourceReference(
          MultiSelectBehavior.class, "jquery.tristate.js"));
      response.renderCSSReference(new ResourceReference(
          MultiSelectBehavior.class, "jquery.tristate.css"));
    } else {
      response.renderJavascriptReference(new ResourceReference(
          MultiSelectBehavior.class, "jquery.multiselect.js"));
      if (isFiltering()) {
        response.renderJavascriptReference(new ResourceReference(
            MultiSelectBehavior.class, "jquery.multiselect.filter.js"));
        response.renderCSSReference(new ResourceReference(
            MultiSelectBehavior.class, "jquery.multiselect.filter.css"));
      }
    }
    {
      // Destroy old widget
      final String script = "$('#" + getComponent().getMarkupId() + "')." + getMultiselectMethodName() + "('destroy');"; // Replace
      // previous
      //    script = "$('#" + getComponent().getMarkupId() + "').mouseover(function() {$(this).multiselect('destroy');});"; // Replace previous
      //    JavascriptUtils.writeJavascript(response, script);
      response.renderJavascript(script, null);
    }
  }

  /**
   * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onComponentRendered()
   */
  @Override
  protected void onComponentRendered() {
    super.onComponentRendered();

    final StringBuilder script = new StringBuilder("$('#" + getComponent().getMarkupId()
        + "')." + getMultiselectMethodName() + "("
        + prepareOptions() + ")");
    if (isFiltering() && !isTristate()) {
      script.append(".multiselectfilter({label: 'Search'})");
      //      script.append(".multiselectfilter('updateCache')"); is created new?
    }
    script.append(";");
    JavascriptUtils.writeJavascript(getComponent().getResponse(), script.toString());
    //    getComponent().getResponse().renderOnDomReadyJavascript(script);
  }

  private String prepareOptions() {
    final StringBuilder sb = new StringBuilder("{");
    sb.append("selectedList: " + (noneSelectedKey == null ? "false, selectedText: '', miniButton: true" : "2, miniButton: " + miniButton + ", selectedText: '#" + getComponent().getString("pcs_selected") + "'"));
    sb.append(", minWidth: " + minWidth);
    sb.append(", height: " + height);
    sb.append(", checkAllText: '");
    sb.append(getComponent().getString("multiselect-check-all-text"));
    sb.append("', close: function() {");
    sb.append("  var serialized" + getSerializationScript());
    sb.append("  if (serialized != " + getStateVariable() + ") {");
    sb.append("   $(\"#" + getComponent().getMarkupId() + "\").trigger(\"change\");");
    sb.append("  }");
    sb.append("}, beforeopen: function() {");
    sb.append(getStateVariable() + getSerializationScript());
    sb.append("}, click: function(event, ui) { if (ui.radio) {} ");
    //    sb.append("$('#" + getComponent().getMarkupId() + "').val(ui.value);"); bug bug
    //    sb.append("$('#" + getComponent().getMarkupId() + "').multiselect('close'); }"); bug bug
    sb.append("}, uncheckAllText: '");
    sb.append(getComponent().getString("multiselect-uncheck-all-text"));
    sb.append("', noneSelectedText: '");
    sb.append(noneSelectedKey == null ? "" : getComponent().getString(noneSelectedKey));
    sb.append("', multiple: ");
    sb.append(multiple);
    sb.append(", nullItemLabel: '");
    sb.append(getComponent().getString("nullValid"));
    sb.append("'");
    sb.append("}");
    return sb.toString();
  }

  /**
   * @return String
   */
  private String getSerializationScript() {
    return " = wicketSerialize(Wicket.$('" + getComponent().getMarkupId() + "'));";
  }

  /**
   * @return String
   */
  private String getStateVariable() {
    return STATE_VARIABLE_PREFIX + getComponent().getMarkupId();
  }

  private String getMultiselectMethodName() {
    return isTristate() ? "triStateMultiselect" : "multiselect";
  }

  /**
   * 
   * @return this
   */
  public MultiSelectBehavior single() {
    this.multiple = false;
    return this;
  }

  /**
   * 
   * @return this
   */
  public MultiSelectBehavior filtering() {
    this.filtering = true;
    return this;
  }

  public MultiSelectBehavior tristate() {
    this.tristate = true;
    return this;
  }

  /**
   * @return the minWidth
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * @param minWidth the minWidth to set
   * @return MultiSelectBehavior
   */
  public MultiSelectBehavior setMinWidth(final int minWidth) {
    this.minWidth = minWidth;
    return this;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @param height the height to set
   * @return MultiSelectBehavior
   */
  public MultiSelectBehavior setHeight(final int height) {
    this.height = height;
    return this;
  }

  /**
   * @return the filtering
   */
  public boolean isFiltering() {
    return filtering;
  }

  /**
   * @return the miniButton
   */
  public boolean isMiniButton() {
    return miniButton;
  }

  public boolean isTristate() {
    return tristate;
  }

  /**
   * @param miniButton the miniButton to set
   * @return MultiSelectBehavior
   */
  public MultiSelectBehavior setMiniButton(final boolean miniButton) {
    this.miniButton = miniButton;
    return this;
  }
}