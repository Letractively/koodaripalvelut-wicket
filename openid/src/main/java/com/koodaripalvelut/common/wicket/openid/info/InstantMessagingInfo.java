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
 * Instant Messaging.
 * @author fbencosme@kitsd.com
 */
public class InstantMessagingInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String aol;
  private String icq;
  private String msn;
  private String yahoo;
  private String jabber;
  private String skype;

  public String getAol() {
    return aol;
  }

  public InstantMessagingInfo setAol(final String aol) {
    this.aol = aol;
    return this;
  }

  public String getIcq() {
    return icq;
  }

  public InstantMessagingInfo setIcq(final String icq) {
    this.icq = icq;
    return this;
  }

  public String getMsn() {
    return msn;
  }

  public InstantMessagingInfo setMsn(final String msn) {
    this.msn = msn;
    return this;
  }

  public String getYahoo() {
    return yahoo;
  }

  public InstantMessagingInfo setYahoo(final String yahoo) {
    this.yahoo = yahoo;
    return this;
  }

  public String getJabber() {
    return jabber;
  }

  public InstantMessagingInfo setJabber(final String jabber) {
    this.jabber = jabber;
    return this;
  }

  public String getSkype() {
    return skype;
  }

  public InstantMessagingInfo setSkype(final String skype) {
    this.skype = skype;
    return this;
  }

}
