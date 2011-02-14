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
 * Web Sites.
 * @author fbencosme@kitsd.com
 */
public class WebSitesInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String webPageUrl;
  private String blogUrl;
  private String linkedInUrl;
  private String amazonUrl;
  private String delicousUrl;

  public String getWebPageUrl() {
    return webPageUrl;
  }

  public WebSitesInfo setWebPageUrl(final String webPageUrl) {
    this.webPageUrl = webPageUrl;
    return this;
  }

  public String getBlogUrl() {
    return blogUrl;
  }

  public WebSitesInfo setBlogUrl(final String blogUrl) {
    this.blogUrl = blogUrl;
    return this;
  }

  public String getLinkedInUrl() {
    return linkedInUrl;
  }

  public WebSitesInfo setLinkedInUrl(final String linkedInUrl) {
    this.linkedInUrl = linkedInUrl;
    return this;
  }

  public String getAmazonUrl() {
    return amazonUrl;
  }

  public WebSitesInfo setAmazonUrl(final String amazonUrl) {
    this.amazonUrl = amazonUrl;
    return this;
  }

  public String getDelicousUrl() {
    return delicousUrl;
  }

  public WebSitesInfo setDelicousUrl(final String delicousUrl) {
    this.delicousUrl = delicousUrl;
    return this;
  }

}
