/*
 * Copyright 2010 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */


package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;

/** DoubleModalTest is responsible of
 * @author rhansen@kitsd.com
 */
public class DoubleModalTestPage extends BasePage {

  private class InsidePage extends WebPage {
    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(windowB);
      add(new CloseWindowA());
      add(new CloseWindowB());
    }
  }

  private class InnerPanel extends Panel {
    private static final long serialVersionUID = 1L;

    public InnerPanel(final String id) {
      super(id);
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(new CloseWindowA());
      add(new CloseWindowB());
    }
  }

  private class CloseWindowA extends AjaxLink<Void> {
    private static final long serialVersionUID = 1L;

    public CloseWindowA() {
      super("closeWindow");
    }

    @Override
    public void onClick(final AjaxRequestTarget target) {
      if (windowA.isShown()) {
        windowA.close(target);
      } else {
        windowA.show(target);
      }
    }
  }

  private class CloseWindowB extends AjaxLink<Void> {
    private static final long serialVersionUID = 1L;

    public CloseWindowB() {
      super("closeInsider");
    }

    @Override
    public void onClick(final AjaxRequestTarget target) {
      if (windowB.isShown()) {
        windowB.close(target);
      } else {
        windowB.show(target);
      }
    }
  }

  private final PageCreator windowACreator = new PageCreator() {
    private static final long serialVersionUID = 1L;

    @Override
    public Page createPage() {
      return new InsidePage();
    }
  };

  private final ModalWindow windowA = new SafeModalWindow("windowA");
  private final ModalWindow windowB = new SafeModalWindow("windowB");

  public DoubleModalTestPage() {
    windowA.setPageCreator(windowACreator);
    windowB.setContent(new InnerPanel(windowB.getContentId()));
    add(windowA);
    add(new CloseWindowA());
    add(new CloseWindowB());
  }

}
