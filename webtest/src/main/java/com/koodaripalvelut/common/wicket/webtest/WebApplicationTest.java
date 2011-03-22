package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.lang.PackageName;

import com.koodaripalvelut.common.wicket.webtest.changedetector.ChangeDetectorPage;
import com.koodaripalvelut.common.wicket.webtest.fullcalendar.FullCalendarPage;
import com.koodaripalvelut.common.wicket.webtest.openid.ResultPage;
import com.koodaripalvelut.common.wicket.webtest.searchbox.SearchBoxPage;

public class WebApplicationTest extends WebApplication {

  public static final String PATH = "auth";

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
    mount(PATH, PackageName.forClass(ResultPage.class));
  }

  @Override
  protected ISessionStore newSessionStore() {
    return new HttpSessionStore(this);
  }

  @Override
  public Session newSession(final Request request, final Response response) {
    return new WebSessionTest(request);
  }

}
