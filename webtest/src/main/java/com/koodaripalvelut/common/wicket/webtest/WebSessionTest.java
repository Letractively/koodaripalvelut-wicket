package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.koodaripalvelut.common.wicket.openid.AuthenticationProvider;
import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;

public class WebSessionTest extends WebSession implements AuthenticationSession {

  private static final long serialVersionUID = 1L;

  private AuthenticationProvider provider;

  public WebSessionTest(final Request request) {
    super(request);
  }

  @Override
  public AuthenticationProvider getAuthProvider() {
    return provider;
  }

  @Override
  public void setAuthProvider(final AuthenticationProvider provider) {
    this.provider = provider;
  }

  public static WebSessionTest get() {
    return (WebSessionTest) Session.get();
  }

}

