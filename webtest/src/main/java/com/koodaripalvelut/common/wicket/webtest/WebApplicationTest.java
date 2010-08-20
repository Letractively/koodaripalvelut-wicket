package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

public class WebApplicationTest extends WebApplication {

  public Class<SearchBoxPage> getHomePage() {
    return SearchBoxPage.class;
  }

  @Override
  protected void init() {
    super.init();
    getResourceSettings().setResourcePollFrequency(null);
  }

  @Override
  protected ISessionStore newSessionStore() {
    return new HttpSessionStore(this);
  }

}
