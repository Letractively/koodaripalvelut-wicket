/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.webtest.openid;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * @author fbencosme
 */
public class InfoPage extends WebPage {

  private static final long serialVersionUID = 1L;

  private static final String SIGNIN_PANEL_ID = "signinPanel";

  public InfoPage() {
    super();
    add(new AjaxLazyLoadPanel(SIGNIN_PANEL_ID) {

      private static final long serialVersionUID = 1L;

      @Override
      public Component getLazyLoadComponent(final String markupId) {
        return new OpenIDPanel(markupId);
      }

      @Override
      public Component getLoadingComponent(final String markupId) {
        final Label label = (Label) super.getLoadingComponent(markupId);
        final StringBuilder sb =
            new StringBuilder(label.getDefaultModelObjectAsString());
        sb.append("<div>");
        sb.append(InfoPage.this.getString("loadingMesssage"));
        sb.append("</div>");
        label.setDefaultModel(Model.of(sb.toString()));
        return label;
      }
    });
  }
}
