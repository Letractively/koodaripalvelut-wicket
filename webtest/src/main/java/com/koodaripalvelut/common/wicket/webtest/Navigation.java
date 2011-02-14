package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.koodaripalvelut.common.wicket.webtest.openid.InfoPage;

public class Navigation extends Border {

  private static final long serialVersionUID = 1L;

  public Navigation(final String id) {
    super(id);

    add(new BookmarkablePageLink<SearchBoxPage>("search-box-link",
        SearchBoxPage.class));
    add(new BookmarkablePageLink<ChangeDetectorPage>("change-detector-link",
        ChangeDetectorPage.class));
    add(new BookmarkablePageLink<FullCalendarPage>("full-calendar-link",
        FullCalendarPage.class));
    add(new BookmarkablePageLink<DoubleModalTestPage>("double-modal-link",
        DoubleModalTestPage.class));
    add(new BookmarkablePageLink<InfoPage>("wicket-openid",
        InfoPage.class));
  }

}
