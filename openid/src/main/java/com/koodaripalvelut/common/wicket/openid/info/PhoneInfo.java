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
 * @author fbencosme@kitsd.com
 */
public class PhoneInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String preferred;
  private String home;
  private String work;
  private String mobile;
  private String fax;

  public String getPreferred() {
    return preferred;
  }

  public PhoneInfo setPreferred(final String preferred) {
    this.preferred = preferred;
    return this;
  }

  public String getHome() {
    return home;
  }

  public PhoneInfo setHome(final String home) {
    this.home = home;
    return this;
  }

  public String getWork() {
    return work;
  }

  public PhoneInfo setWork(final String work) {
    this.work = work;
    return this;
  }

  public String getMobile() {
    return mobile;
  }

  public PhoneInfo setMobile(final String mobile) {
    this.mobile = mobile;
    return this;
  }

  public String getFax() {
    return fax;
  }

  public PhoneInfo setFax(final String fax) {
    this.fax = fax;
    return this;
  }

}
