package com.koodaripalvelut.common.wicket.behavior;

import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * TristateMultiSelectBehavior is responsible of.
 * @author eferreira@kitsd.com
 */
public class TristateMultiSelectBehavior extends MultiSelectBehavior {

  private static final long serialVersionUID = 1L;

  @Override
  public void renderHead(final IHeaderResponse response) {
    renderJavascriptReference(response, "jquery.tristate.js");
    renderCSSReference(response, "jquery.tristate.css");
    super.renderHead(response);
  }

  @Override
  protected String getMultiselectFileName() {
    return "jquery.tristateMultiselect.js";
  }

  @Override
  protected String getFilterFileName() {
    return "jquery.tristateMultiselect.filter.js";
  }

  @Override
  protected String getMultiselectMethodName() {
    return "triStateMultiselect";
  }

  @Override
  protected String getFilterMethodName() {
    return "tristatemultiselectfilter";
  }

  @Override
  protected String getOtherOptions() {

    final StringBuilder sb = new StringBuilder();
    final String nullItemLabel = getComponent().getString("nullLabel");

    if (!nullItemLabel.equals("")) {
      sb.append(", nullItemLabel: '");
      sb.append(nullItemLabel);
      sb.append("'");
    }

    return sb.toString();
  }

}

