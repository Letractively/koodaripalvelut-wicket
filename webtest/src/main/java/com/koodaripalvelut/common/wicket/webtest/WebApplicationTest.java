package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.session.ISessionStore;

public class WebApplicationTest extends WebApplication {

  @Override
  public Class<SearchBoxPage> getHomePage() {
    return SearchBoxPage.class;
  }

  @Override
  protected void init() {
    super.init();
    getResourceSettings().setResourcePollFrequency(null);
    getResourceSettings().setDefaultCacheDuration(3);
    mount(new IndexedParamUrlCodingStrategy("searchBox", SearchBoxPage.class));
    mount(new IndexedParamUrlCodingStrategy("changeDetector", ChangeDetectorPage.class));
    mount(new IndexedParamUrlCodingStrategy("calendar", FullCalendarPage.class));
  }

  @Override
  protected ISessionStore newSessionStore() {
    return new HttpSessionStore(this);
  }

}
