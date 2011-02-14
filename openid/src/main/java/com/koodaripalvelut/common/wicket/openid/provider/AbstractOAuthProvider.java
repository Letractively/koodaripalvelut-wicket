/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.openid.provider;

import java.util.Properties;

import com.koodaripalvelut.common.wicket.openid.AuthenticationProvider;

/**
 * This class provides a skeletal implementation of the
 * {@link AuthenticationProvider} interface. Encapsulating the common properties
 * of OAuth 1.0 and OAuth 2.0.
 * <p>
 * clientId: A value used by the Application to identify itself to the Service
 * Provider.
 * <p>
 * clientSecret: A secret used by the Application to establish ownership of the
 * client Id.
 * <p>
 * tokenEndpoint: The URL used to exchange the User-authorized Request Token for
 * an Access Token access
 * <p>
 * resourcesEndpoint: The URL used by the application to retrieve the user
 * information from the provider.
 * <p>
 * accessToken: Is a code used by the application to retrieve the resource owner
 * information
 * @author eferreira@kindleit.net
 */
public abstract class AbstractOAuthProvider<T extends AbstractOAuthProvider<T>>
    extends AbstractProvider<T> {
  private static final long serialVersionUID = 1L;

  public static final String CLIENT_ID = "clientId";
  public static final String CLIENT_SECRET = "clientSecret";
  public static final String TOKEN_ENDPOINT = "tokenEndpoint";
  public static final String RESOURCES_ENDPOINT = "resourcesEndpoint";

  private String clientId;
  private String clientSecret;
  private String tokenEndpoint;
  private String resourcesEndpoint;

  private String accessToken;

  public AbstractOAuthProvider(final String providerName) {
    super(providerName);
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AbstractProvider#AbstractProvider(String,
   *      Properties).
   */
  public AbstractOAuthProvider(final String providerName, final Properties props) {
    super(providerName, props);
    this.clientId = props.getProperty(propKey(CLIENT_ID));
    this.clientSecret = props.getProperty(propKey(CLIENT_SECRET));
    this.tokenEndpoint = props.getProperty(propKey(TOKEN_ENDPOINT));
    this.resourcesEndpoint =
      props.getProperty(propKey(RESOURCES_ENDPOINT));
  }

  /**
   * Returns the id of the application. This id is used by the application to
   * identify itself to the Provider
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Setter for the <tt>clientId</tt>.
   */
  @SuppressWarnings("unchecked")
  public T setClientId(final String clientId) {
    this.clientId = clientId;
    return (T) this;
  }

  /**
   * Getter for the <tt>clientSecret</tt>.
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * Setter for the <tt>clientSecret</tt>.
   */
  @SuppressWarnings("unchecked")
  public T setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
    return (T) this;
  }

  /**
   * Getter for the <tt>tokenEndpoint</tt>.
   */
  public String getTokenEndpoint() {
    return tokenEndpoint;
  }

  /**
   * Setter for the <tt>tokenEndpoint</tt>.
   */
  @SuppressWarnings("unchecked")
  public T setTokenEndpoint(final String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
    return (T) this;
  }

  /**
   * Getter for the <tt>resourcesEndpoint</tt>.
   */
  public String getResourcesEndpoint() {
    return resourcesEndpoint;
  }

  /**
   * Setter for the <tt>resourcesEndpoint</tt>.
   */
  @SuppressWarnings("unchecked")
  public T setResourcesEndpoint(
      final String resourcesEndpoint) {
    this.resourcesEndpoint = resourcesEndpoint;
    return (T) this;
  }

  /**
   * Getter for the <tt>accessToken</tt>.
   * @returnaccessToken
   */
  protected String getAccessToken() {
    return accessToken;
  }

  /**
   * Getter for the <tt>accessToken</tt>.
   */
  public void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

}

