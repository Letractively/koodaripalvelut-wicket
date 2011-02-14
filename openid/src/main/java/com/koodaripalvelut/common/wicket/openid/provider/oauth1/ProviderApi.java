package com.koodaripalvelut.common.wicket.openid.provider.oauth1;

import org.apache.wicket.Session;
import org.scribe.builder.api.DefaultApi10a;

import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;

public class ProviderApi extends DefaultApi10a {
  final AuthenticationSession session = (AuthenticationSession) Session.get();

  /**
   * @see org.scribe.builder.api.DefaultApi10a#getAccessTokenEndpoint()
   */
  @Override
  protected String getAccessTokenEndpoint() {
    return ((OAuth1Provider) session.getAuthProvider()).getTokenEndpoint();
  }

  /**
   * @see org.scribe.builder.api.DefaultApi10a#getRequestTokenEndpoint()
   */
  @Override
  protected String getRequestTokenEndpoint() {
    return ((OAuth1Provider) session.getAuthProvider())
    .getRequestTokenEndpoint();
  }
}

