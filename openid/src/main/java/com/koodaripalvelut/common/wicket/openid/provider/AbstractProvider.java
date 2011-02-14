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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.WebRequest;

import com.koodaripalvelut.common.wicket.openid.AuthenticationProvider;
import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;
import com.koodaripalvelut.common.wicket.openid.info.Info;

/**
 * This class provides a skeletal implementation of the
 * {@link AuthenticationProvider} interface.
 * @author eferreira@kitsd.com
 */
public abstract class AbstractProvider<T extends AbstractProvider<T>>
    implements AuthenticationProvider {
  private static final long serialVersionUID = 1L;

  public static final String PROVIDER_NAME = "name";
  public static final String ENDPOINT = "endpoint";
  public static final String LOGO = "logo";

  private final String providerName;
  private String endpoint;
  private String logo;
  private Map<String, String> infoProperties =
    new HashMap<String, String>();

  public AbstractProvider(final String providerName) {
    this.providerName = providerName;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AbstractProvider#AbstractProvider(String,
   *      Properties).
   */
  public AbstractProvider(final String providerName, final Properties props) {
    this.providerName = providerName;
    this.endpoint = props.getProperty(propKey(ENDPOINT));
    this.logo = props.getProperty(propKey(LOGO));

    final String infoProps = props.getProperty(propKey("properties"));

    final StringTokenizer pairValueTokenizer =
      new StringTokenizer(infoProps, ",");

    while (pairValueTokenizer.hasMoreElements()) {
      final String pairValue = (String) pairValueTokenizer.nextElement();
      final StringTokenizer st = new StringTokenizer(pairValue, ":");

      final String infoProp = (String) st.nextElement();
      final String provProp = (String) st.nextElement();

      infoProperties.put(infoProp, provProp);
    }
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#getEndpoint()
   */
  @Override
  public String getEndpoint() {
    return endpoint;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#setEndpoint(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public T setEndpoint(final String endpoint) {
    this.endpoint = endpoint;
    return (T) this;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#getLogo()
   */
  @Override
  public String getLogo() {
    return logo;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#setLogo(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public T setLogo(final String logo) {
    this.logo = logo;
    return (T) this;
  }

  public Map<String, String> getInfoProperties() {
    return this.infoProperties;
  }

  @SuppressWarnings("unchecked")
  public T setInfoProperties(
      final Map<String, String> infoProperties) {
    this.infoProperties = infoProperties;
    return (T) this;
  }

  /**
   * Format the key used to retrieve the provider information from the
   * {@link Properties} param in the
   * {@link AbstractProvider#AbstractProvider(String, Properties)} constructor.
   * @return a formatted key.
   */
  protected final String propKey(final String key) {
    return providerName + "." + key;
  }

  /**
   * @see com.koodaripalvelut.common.wicket.openid.AuthenticationProvider#getInfoModel(org.apache.wicket.protocol.http.WebRequest,
   *      com.koodaripalvelut.common.wicket.openid.AuthenticationSession)
   */
  @Override
  public IModel<Info> getInfoModel(final WebRequest request,
      final AuthenticationSession session) {
    return new LoadableDetachableModel<Info>() {

      private static final long serialVersionUID = 1L;

      @Override
      protected Info load() {
        return getService().loadInfo(request, session);
      }
    };
  }

  @Override
  public String toString() {
    return providerName;
  }

}

