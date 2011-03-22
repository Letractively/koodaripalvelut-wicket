package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;


public class BasePage extends WebPage {

  private static final long serialVersionUID = 1L;

  public BasePage() {
    add(new NavigationPanel("menu"));
  }
}
