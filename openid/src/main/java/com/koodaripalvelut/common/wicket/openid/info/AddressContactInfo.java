/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.openid.info;

import java.io.Serializable;

/**
 * Address.
 * @author fbencosme@kitsd.com
 */
public class AddressContactInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String home;
  private String additional;
  private String city;
  private String state;
  private String country;
  private String postalCode;

  public String getHome() {
    return home;
  }

  public AddressContactInfo setHome(final String home) {
    this.home = home;
    return this;
  }

  public String getAdditional() {
    return additional;
  }

  public AddressContactInfo setAdditional(final String additional) {
    this.additional = additional;
    return this;
  }

  public String getCity() {
    return city;
  }

  public AddressContactInfo setCity(final String city) {
    this.city = city;
    return this;
  }

  public String getState() {
    return state;
  }

  public AddressContactInfo setState(final String state) {
    this.state = state;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public AddressContactInfo setCountry(final String country) {
    this.country = country;
    return this;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public AddressContactInfo setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

}
