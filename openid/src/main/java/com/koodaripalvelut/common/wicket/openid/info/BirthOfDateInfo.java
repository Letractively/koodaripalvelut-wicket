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
import java.util.Date;

/**
 * Date of Birth.
 * @author fbencosme@kitsd.com
 */
public class BirthOfDateInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private Date birthday;
  private int birthdayYear;
  private int birthdayMonth;
  private int birthdayDay;

  public Date getBirthday() {
    return birthday;
  }

  public BirthOfDateInfo setBirthday(final Date birthday) {
    this.birthday = birthday;
    return this;
  }

  public int getBirthdayYear() {
    return birthdayYear;
  }

  public BirthOfDateInfo setBirthdayYear(final int birthdayYear) {
    this.birthdayYear = birthdayYear;
    return this;
  }

  public int getBirthdayMonth() {
    return birthdayMonth;
  }

  public BirthOfDateInfo setBirthdayMonth(final int birthdayMonth) {
    this.birthdayMonth = birthdayMonth;
    return this;
  }

  public int getBirthdayDay() {
    return birthdayDay;
  }

  public BirthOfDateInfo setBirthdayDay(final int birthdayDay) {
    this.birthdayDay = birthdayDay;
    return this;
  }

}
