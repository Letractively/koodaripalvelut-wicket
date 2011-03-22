package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.koodaripalvelut.common.wicket.webtest.changedetector.ChangeDetectorPage;
import com.koodaripalvelut.common.wicket.webtest.fullcalendar.FullCalendarPage;
import com.koodaripalvelut.common.wicket.webtest.multimodal.MultiModalPage;
import com.koodaripalvelut.common.wicket.webtest.openid.InfoPage;
import com.koodaripalvelut.common.wicket.webtest.searchbox.SearchBoxPage;

public class NavigationPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public NavigationPanel(final String id) {
    super(id);

    add(new BookmarkablePageLink<SearchBoxPage>("search-box-link",
        SearchBoxPage.class));
    add(new BookmarkablePageLink<ChangeDetectorPage>("change-detector-link",
        ChangeDetectorPage.class));
    add(new BookmarkablePageLink<FullCalendarPage>("full-calendar-link",
        FullCalendarPage.class));
    add(new BookmarkablePageLink<MultiModalPage>("multi-modal-link",
        MultiModalPage.class));
    add(new BookmarkablePageLink<InfoPage>("wicket-openid",
        InfoPage.class));
  }

}
