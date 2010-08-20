package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class Navigation extends Border {

  private static final long serialVersionUID = 1L;

  public Navigation(String id) {
    super(id);

    add(new BookmarkablePageLink<SearchBoxPage>("search-box-link",
        SearchBoxPage.class));
    add(new BookmarkablePageLink<ChangeDetectorPage>("change-detector-link",
        ChangeDetectorPage.class));
  }

}
