/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.openid.provider.oauth2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.GitHubTokenResponse;
import net.smartam.leeloo.client.response.OAuthAuthzResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;

import org.apache.log4j.Logger;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.lang.PropertyResolver;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.koodaripalvelut.common.wicket.openid.AuthenticationService;
import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;
import com.koodaripalvelut.common.wicket.openid.BasicAuthServices;
import com.koodaripalvelut.common.wicket.openid.info.Info;
import com.koodaripalvelut.common.wicket.openid.provider.AbstractOAuthProvider;
import com.koodaripalvelut.common.wicket.openid.provider.openid.OpenIDProvider;
import com.visural.common.IOUtil;

/**
 * OAuth2Provider is responsible of store the necessary information needed by
 * OAuth 2.0.
 * @author eferreira@kindleit.net
 */
public class OAuth2Provider extends AbstractOAuthProvider<OAuth2Provider> {
  private static final long serialVersionUID = 1L;

  public static final String SCOPE = "scope";

  private String returnUrl;

  private static final Logger LOG = Logger.getLogger(OpenIDProvider.class);

  private static final String CREATING_ENDPOINT_MESSAGE =
    "Exception occurred while creating endpoint url";

  private String scope;

  public OAuth2Provider(final String providerName) {
    super(providerName);
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AbstractProvider#AbstractProvider(String,
   *      Properties).
   */
  public OAuth2Provider(final String providerName, final Properties props) {
    super(providerName, props);
    this.scope = props.getProperty(propKey(SCOPE));
  }

  public OAuth2Provider setScope(final String scope) {
    this.scope = scope;
    return this;
  }

  /**
   * @return the scope of the resource owner information needed by the
   *         application.
   */
  public String getScope() {
    return scope;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#startAuthentication(java.lang.String)
   */
  @Override
  public void startAuthentication(final String returnUrl) {
    this.returnUrl = returnUrl;
    try {
      final OAuthClientRequest request =
        OAuthClientRequest.authorizationLocation(getEndpoint())
        .setClientId(getClientId())
        .setRedirectURI(returnUrl)
        .setScope(scope)
        .buildQueryMessage();

      RequestCycle.get().setRequestTarget(
          new RedirectRequestTarget(request.getLocationUri()));

      final AuthenticationSession session = (AuthenticationSession) Session.get();
      session.setAuthProvider(this);
    } catch (final OAuthSystemException e) {
      LOG.error(CREATING_ENDPOINT_MESSAGE, e);
      throw new RuntimeException(CREATING_ENDPOINT_MESSAGE, e);
    }
  }

  /**
   * @return an authorization grant that represents the authorization provided
   *         by the user.
   */
  public String getAuthorizationCode(final HttpServletRequest request)
  throws OAuthProblemException {

    final OAuthAuthzResponse oar =
      OAuthAuthzResponse.oauthCodeAuthzResponse(request);

    return oar.getCode();
  }

  /**
   * Request an access token by authenticating with the provider using the
   * application credentials and presenting the authorization grant.
   * @param code represents the authorization provided by the resource owner.
   *          See {@link #getAuthorizationCode(HttpServletRequest)}.
   * @param provider wraps the client credentials.
   * @return an access token that represents an authorization issued to the
   *         client.
   */
  public String getAccessToken(final String code) throws OAuthProblemException,
  OAuthSystemException {

    final OAuthClientRequest oAuthRequest =
      OAuthClientRequest.tokenLocation(getTokenEndpoint())
      .setGrantType(GrantType.AUTHORIZATION_CODE)
      .setClientId(getClientId())
      .setClientSecret(getClientSecret())
      .setRedirectURI(returnUrl)
      .setCode(code)
      .buildBodyMessage();

    final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    final GitHubTokenResponse oAuthResponse =
      oAuthClient.accessToken(oAuthRequest, GitHubTokenResponse.class);

    return oAuthResponse.getAccessToken();
  }


  /**
   * @param accessToken is the representation of the authorization issued to the
   *          client.
   * @return an com.koodaripalvelut.common.wicket.openid.model.Info which contains the resource
   *         owner's information.
   */
  public Info getInfo(final String accessToken) throws MalformedURLException,
  JSONException, IOException {

    final JSONObject response =
      new JSONObject(IOUtil.urlToString(new URL(getResourcesEndpoint()
          + accessToken)));

    final Info info = new Info();

    info.setOpenId(accessToken);
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
    return BasicAuthServices.OAUTH2;
  }


}
