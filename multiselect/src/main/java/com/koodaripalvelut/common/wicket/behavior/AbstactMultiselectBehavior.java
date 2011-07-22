package com.koodaripalvelut.common.wicket.behavior;


import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;

import com.koodaripalvelut.common.wicket.component.multiselect.ListMultipleChoiceWithNull;

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
public class AbstactMultiselectBehavior<T extends AbstactMultiselectBehavior<T>>
extends AbstractDefaultAjaxBehavior {
  private static final long serialVersionUID = 1L;
  private static final String STATE_VARIABLE_PREFIX = "multiSelectState";

  private final String noneSelectedKey;

  /**
   * If set to false, the widget will use radio buttons instead of checkboxes, forcing users to select only one option.
   */
  protected boolean multiple = true;
  /**
   * If set to true, filtering is enabled.
   */
  private boolean filtering;

  /**
   * If set to true, the widget will display in a heirarchical try state fashion.
   */
  private boolean triState;

  private int minWidth = 255;
  private int height = 175;
  private boolean miniButton;

  /**
   *
   */
  public AbstactMultiselectBehavior() {
    this("multiselect-none-selected-text");
  }

  /**
   * @param string
   */
  public AbstactMultiselectBehavior(final String string) {
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
   * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onComponentRendered()
   */

  protected String prepareOptions() {
    final StringBuilder sb = new StringBuilder("{");
    sb.append("selectedList: " + (noneSelectedKey == null ? "false, selectedText: '', miniButton: true" : "2, miniButton: " + miniButton + ", selectedText: '# " + getComponent().getString("pcs_selected") + "'"));
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
    sb.append("}, click: function(event, ui) { if (ui.radio) { ");
    sb.append("$('#" + getComponent().getMarkupId() + "').val(ui.value);");
    sb.append("$('#" + getComponent().getMarkupId() + "').multiselect('close'); }");
    sb.append("}, uncheckAllText: '");
    sb.append(getComponent().getString("multiselect-uncheck-all-text"));
    sb.append("', noneSelectedText: '");
    sb.append(noneSelectedKey == null ? "" : getComponent().getString(noneSelectedKey));
    sb.append("', multiple: ");
    sb.append(multiple);
    if (getComponent() instanceof ListMultipleChoiceWithNull<?>) {
      sb.append(", acceptNull: ");
      sb.append("true");
    }
    sb.append(addProperties());
    sb.append("}");
    return sb.toString();
  }

  protected String addProperties() {
    return "";
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

  /**
   * 
   * @return this
   */
  public T single() {
    this.multiple = false;
    return (T) this;
  }

  /**
   * 
   * @return this
   */
  public T filtering() {
    this.filtering = true;
    return (T) this;
  }

  public T triState() {
    this.triState = true;
    return (T) this;
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
  public T setMinWidth(final int minWidth) {
    this.minWidth = minWidth;
    return (T) this;
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
  public T setHeight(final int height) {
    this.height = height;
    return (T) this;
  }

  /**
   * @return the filtering
   */
  public boolean isFiltering() {
    return filtering;
  }

  public boolean isTriState() {
    return triState;
  }

  /**
   * @return the miniButton
   */
  public boolean isMiniButton() {
    return miniButton;
  }

  /**
   * @param miniButton the miniButton to set
   * @return MultiSelectBehavior
   */
  public T setMiniButton(final boolean miniButton) {
    this.miniButton = miniButton;
    return (T) this;
  }
}