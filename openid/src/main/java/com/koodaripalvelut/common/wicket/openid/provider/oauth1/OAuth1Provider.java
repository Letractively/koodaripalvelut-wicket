/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.openid.provider.oauth1;

import java.util.Properties;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.lang.PropertyResolver;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.koodaripalvelut.common.wicket.openid.AuthenticationProvider;
import com.koodaripalvelut.common.wicket.openid.AuthenticationService;
import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;
import com.koodaripalvelut.common.wicket.openid.BasicAuthServices;
import com.koodaripalvelut.common.wicket.openid.info.Info;
import com.koodaripalvelut.common.wicket.openid.provider.AbstractOAuthProvider;

/**
 * Implementation of the {@link AuthenticationProvider} that uses the OAuth 1.0a
 * protocol to carry out the authentication.
 * <p>
 * requestTokenEndpoint: The URL used to obtain an unauthorized Request Token.
 * Provider.
 * @author eferreira@kindleit.net
 */
public class OAuth1Provider extends AbstractOAuthProvider<OAuth1Provider> {
  private static final long serialVersionUID = 1L;

  public static final String REQUEST_TOKEN_ENDPOINT = "requestTokenEndpoint";

  private String requestTokenEndpoint;

  private OAuthService service;
  private Token requestToken;


  public OAuth1Provider(final String providerName) {
    super(providerName);
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AbstractProvider#AbstractProvider(String,
   *      Properties).
   */
  public OAuth1Provider(final String providerName, final Properties props) {
    super(providerName, props);
    this.requestTokenEndpoint =
      props.getProperty(propKey(REQUEST_TOKEN_ENDPOINT));
  }

  public OAuth1Provider setRequestTokenEndpoint(final String requestTokenEndpoint) {
    this.requestTokenEndpoint = requestTokenEndpoint;
    return this;
  }

  public String getRequestTokenEndpoint() {
    return requestTokenEndpoint;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#startAuthentication(java.lang.String)
   */
  @Override
  public void startAuthentication(final String returnUrl) {
    final AuthenticationSession session = (AuthenticationSession) Session.get();

    // This provider Must be saved in the session at this point, thus ProviderApi can access it.
    session.setAuthProvider(this);

    final OAuthService service = new ServiceBuilder()
    .provider(ProviderApi.class)
    .apiKey(getClientId())
    .apiSecret(getClientSecret())
    .callback(returnUrl)
    .build();

    final Token requestToken = service.getRequestToken();
    final String token = requestToken.getToken();

    // Must be saved to be used later.
    this.service = service;
    this.requestToken = requestToken;

    RequestCycle.get().setRequestTarget(
        new RedirectRequestTarget(getEndpoint() + token));

    session.setAuthProvider(this);
  }

  public Verifier oauth1Verifier(final WebRequest request) {
    final String verif = request.getParameter("oauth_verifier");
    final Verifier verifier = new Verifier(verif);
    return verifier;
  }

  public Token getAccessToken(final Verifier verifier) {
    return service.getAccessToken(requestToken, verifier);
  }

  public void signRequest(final Token accessToken, final OAuthRequest request) {
    service.signRequest(accessToken, request);
  }

  public Info getOAuth1Info(final Token accessToken, final String responseBody)
  throws JSONException {
    final JSONObject response = new JSONObject(responseBody);

    final Info info = new Info();

    info.setOpenId(accessToken.getToken());
    for (final Object infoProp : getInfoProperties().keySet()) {
      final String provProp = getInfoProperties().get(infoProp);
      final String value = response.getString(provProp);
      PropertyResolver.setValue((String) infoProp, info, value, null);
    }
    return info;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#getService()
   */
  @Override
  public AuthenticationService getService() {
    return BasicAuthServices.OAUTH1;
  }

}
