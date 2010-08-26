package com.koodaripalvelut.common.wicket.component.fullcalendar;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class FullCalendarTest {

  public static final class TestPage extends WebPage {
    {add(new FullCalendar("calendar"));}
  }

  @Test
  public void testRenderHeadHtmlHeaderContainer() throws Exception {
    final WicketTester wt = new WicketTester();
    wt.startPage(TestPage.class);
    wt.assertResultPage(FullCalendarTest.class, "ExpectedCalendar.html");
  }

}
