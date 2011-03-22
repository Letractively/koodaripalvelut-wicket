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

import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;

import com.koodaripalvelut.common.wicket.openid.info.Info;
import com.koodaripalvelut.common.wicket.webtest.BasePage;
import com.koodaripalvelut.common.wicket.webtest.WebSessionTest;

/**
 * ResultPage is responsible of
 * @author rhansen@kitsd.com
 */
public class ResultPage extends BasePage {

  private static final String INFO_PANEL_ID = "infoPanel";

  public ResultPage(final PageParameters pageParameters) {

    final WebSessionTest session = (WebSessionTest) Session.get();

    final IModel<Info> model =
      WebSessionTest.get().getAuthProvider().getInfoModel(
          (WebRequest) getRequest(), session);
    add(new InfoPanel(INFO_PANEL_ID, model));
  }

}
