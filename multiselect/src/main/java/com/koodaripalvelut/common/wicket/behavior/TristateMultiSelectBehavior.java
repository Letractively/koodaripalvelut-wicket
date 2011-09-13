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
    renderJavascriptReference(response, "jquery.tristateMultiselect.js");
    if (isFiltering()) {
      renderJavascriptReference(response, "jquery.tristateMultiselect.filter.js");
    }
    renderCSSReference(response, "jquery.tristate.css");
    super.renderHead(response);
  }

  @Override
  protected void appendMethods(final StringBuilder script) {
    script.append(".triStateMultiselect()");
    if (isFiltering()) {
      script.append(".tristatefilter()");
    }
  }

}

