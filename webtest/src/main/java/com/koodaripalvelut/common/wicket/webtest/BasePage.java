package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.WebPage;

public class BasePage extends WebPage {

  private static final long serialVersionUID = 1L;

  public BasePage() {
    add(new Navigation("border") {

      private static final long serialVersionUID = 1L;

      @Override
      public boolean isTransparentResolver() {
        return true;
      }
    });
  }

}
