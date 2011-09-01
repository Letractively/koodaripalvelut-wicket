package com.koodaripalvelut.common.wicket.behavior;

import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * TristateMultiSelectBehavior is responsible of.
 * @author eferreira@kitsd.com
 */
public class TristateMultiSelectBehavior extends MultiSelectBehavior {

  private static final long serialVersionUID = 1L;

  private static final String PARENT_ID_PREFIX = "tsp-";

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

  public static String formatParentId(final String parentId) {

    return PARENT_ID_PREFIX + parentId.replace(" ", "&nbsp");
  }

}

