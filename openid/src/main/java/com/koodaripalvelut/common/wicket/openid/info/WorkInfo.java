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
 * Work.
 * @author fbencosme@kitsd.com
 */
public class WorkInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String companyName;
  private String jobTitle;

  public String getCompanyName() {
    return companyName;
  }

  public WorkInfo setCompanyName(final String companyName) {
    this.companyName = companyName;
    return this;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public WorkInfo setJobTitle(final String jobTitle) {
    this.jobTitle = jobTitle;
    return this;
  }
}
