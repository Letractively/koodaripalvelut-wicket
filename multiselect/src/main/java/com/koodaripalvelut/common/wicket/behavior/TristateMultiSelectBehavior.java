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
    if (isFiltering()) {
      renderJavascriptReference(response, "jquery.tristateMultiselect.filter.js");
    }
    renderCSSReference(response, "jquery.tristate.css");
    super.renderHead(response);
  }

  @Override
  protected String getMultiselectFileName() {
    return "jquery.tristateMultiselect.js";
  }

  @Override
  protected String getMultiselectMethodName() {
    return "triStateMultiselect";
  }

  @Override
  protected void appendMethods(final StringBuilder script) {
    if (isFiltering()) {
      script.append(".tristatefilter()");
    }
  }

}

