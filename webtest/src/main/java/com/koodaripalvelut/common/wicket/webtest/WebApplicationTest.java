package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.session.HttpSessionStore;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.time.Duration;

import com.koodaripalvelut.common.wicket.webtest.changedetector.ChangeDetectorPage;
import com.koodaripalvelut.common.wicket.webtest.fullcalendar.FullCalendarPage;
import com.koodaripalvelut.common.wicket.webtest.multimodal.MultiModalPage;
import com.koodaripalvelut.common.wicket.webtest.multiselect.MultiSelectPage;
import com.koodaripalvelut.common.wicket.webtest.openid.InfoPage;
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
    setSessionStoreProvider(new IProvider<ISessionStore>() {

      @Override
      public ISessionStore get() {
        return new HttpSessionStore();
      }
    });
    getResourceSettings().setResourcePollFrequency(null);
    getResourceSettings().setDefaultCacheDuration(Duration.seconds(3));
    mountPage("searchBox", SearchBoxPage.class);
    mountPage("changeDetector", ChangeDetectorPage.class);
    mountPage("calendar", FullCalendarPage.class);
    mountPage("dropable", DropablePage.class);
    mountPage("nestedModalWindow", MultiModalPage.class);
    mountPage("openID", InfoPage.class);
    mountPage("multiSelect", MultiSelectPage.class);
    mountPackage(PATH, ResultPage.class);
  }

  @Override
  public Session newSession(final Request request, final Response response) {
    return new WebSessionTest(request);
  }

}
