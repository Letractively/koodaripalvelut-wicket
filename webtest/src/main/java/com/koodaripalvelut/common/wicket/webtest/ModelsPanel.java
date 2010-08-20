package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;

public class ModelsPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public ModelsPanel(String id, DropDownChoice<String> models) {
    super(id);
    setOutputMarkupId(true);
    add(models);
  }

}
