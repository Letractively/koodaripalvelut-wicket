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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.koodaripalvelut.common.wicket.openid.info.Info;


/** The info panel shows the information as held in a RegistrationModel.
 *
 * @see Info
 *
 * @author fbencosme@kitsd.com
 */
public class InfoPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public InfoPanel(final String id, final IModel<Info> infoModel) {
    super(id, new CompoundPropertyModel<Info>(infoModel));
    add(new Label("email"));
    add(new Label("fullName"));
    add(new Label("gender"));
    add(new Label("openId"));
  }

}
