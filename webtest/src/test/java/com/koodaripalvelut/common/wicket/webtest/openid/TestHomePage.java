/**
 * Copyright (c) Koodaripalvelut.com Finland 2008
 */
package com.koodaripalvelut.common.wicket.webtest.openid;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;


/**
 * @author fbencosme
 */
public class TestHomePage extends TestCase {
  private WicketTester tester;

  @Override
  public void setUp() {
    tester = new WicketTester();
  }

  public void testRenderMyPage() {
    tester.startPage(InfoPage.class);

    // assert rendered page class
    tester.assertRenderedPage(InfoPage.class);

  }
}
