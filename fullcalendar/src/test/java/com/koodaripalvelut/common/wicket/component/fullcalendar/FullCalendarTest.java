package com.koodaripalvelut.common.wicket.component.fullcalendar;

import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.GSON;
import static com.koodaripalvelut.common.wicket.component.fullcalendar.FullCalendar.toJavascriptTimestamp;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.google.gson.JsonParseException;

public class FullCalendarTest {

  public static final class TestPage extends WebPage {
    {add(new FullCalendar("calendar"));}
  }

  @Test
  public void testDateParsing() {
    final Calendar c = Calendar.getInstance();
    c.set(Calendar.MILLISECOND, 0);
    final TimeZone localTZ = TimeZone.getDefault();
    final TimeZone utcTZ = TimeZone.getTimeZone("UTC");

    c.setTimeZone(utcTZ);
    c.set(2010, 8, 3, 21, 0, 0);
    assertEquals("format 0 error", c.getTime(),
        GSON.fromJson("'2010-09-03T21:00:00.000Z'", Date.class));

    c.setTimeZone(localTZ);
    c.set(2010, 8, 3, 21, 0, 0);
    assertEquals("format 1 error", c.getTime(),
        GSON.fromJson("'2010-09-03T21:00:00.000'", Date.class));

    c.setTimeZone(utcTZ);
    c.set(2010, 8, 3, 21, 0, 0);
    assertEquals("format 2 error", c.getTime(),
        GSON.fromJson("'Tue, 3 Sep 2010 21:00:00 GMT'", Date.class));

    c.setTimeZone(localTZ);
    c.set(2010, 8, 3, 21, 0, 0);
    assertEquals("format 3 error", c.getTime(),
        GSON.fromJson("'Fri, 3 Sep 2010 21:00:00'", Date.class));

    c.setTimeZone(utcTZ);
    c.set(2010, 8, 3, 21, 0, 0);
    assertEquals("format 4 error", c.getTime(),
        GSON.fromJson(Long.toString(toJavascriptTimestamp(c.getTime())), Date.class));
  }

  @Test(expected = JsonParseException.class)
  public void testNoDateParsing() {
    GSON.fromJson("'asdffd3ads'", Date.class);
  }

  @Test
  public void testRenderHeadHtmlHeaderContainer() throws Exception {
    final WicketTester wt = new WicketTester();
    wt.startPage(TestPage.class);
    wt.assertRenderedPage(TestPage.class);
  }

}
