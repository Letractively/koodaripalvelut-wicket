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
import java.util.TimeZone;

/**
 * Registration is Proof-of-Concept class which holds pertinent user information
 * which is retrieved via openId.
 * @author rhansen@kitsd.com
 */
public class Info implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String OPEN_ID = "openId";
  public static final String USER_NAME = "username";
  public static final String EMAIL = "email";
  public static final String FULL_NAME = "fullName";
  public static final String GENDEER = "gender";
  public static final String LANGUAGE = "language";
  public static final String TIME_ZONE = "timeZone";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String MIDDLE_NAME = "middleName";
  public static final String SUFFIX = "suffix";
  public static final String BIOGRAPHY = "biography";
  public static final String WORK_CONTACT_INFO = "workContactInfo";
  public static final String INSTANT_MESSAGIN_CONTACT_INFO =
      "instantMessagingContactInfo";
  public static final String ADDRESS_CONTACT_INFO = "addressContactInfo";
  public static final String BUSINESS_CONTACT_INFO = "businessContactInfo";
  public static final String BIRTH_DATE_CONTACT_INFO = "birthdateContactInfo";

  private String openId;
  private String username;
  private String email;
  private String fullName;
  private String gender;
  private String language;
  private TimeZone timeZone;
  private String firstName;
  private String lastName;
  private String middleName;
  private String suffix;
  private String biography;
  private PhoneInfo phone;
  private WorkInfo workContactInfo;
  private InstantMessagingInfo instantMessagingContactInfo;
  private AddressContactInfo addressContactInfo;
  private BusinessAddressInfo businessContactInfo;
  private BirthOfDateInfo birthdateContactInfo;

  /**
   * @return The OpenID string itself.
   */
  public String getOpenId() {
    return openId;
  }

  public Info setOpenId(final String openId) {
    this.openId = openId;
    return this;
  }

  /**
   * @return The User's Full Name as reported by the OpenId server.
   */
  public String getFullName() {
    return fullName;
  }

  public Info setFullName(final String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * @return The User's email address as reported by the OpenId server.
   */
  public String getEmail() {
    return email;
  }

  public Info setEmail(final String email) {
    this.email = email;
    return this;
  }

  /**
   * @return The User's gender as reported by the OpenId server.
   */
  public String getGender() {
    return gender;
  }

  public Info setGender(final String gender) {
    this.gender = gender;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Info setUsername(final String username) {
    this.username = username;
    return this;
  }

  public String getLanguage() {
    return language;
  }

  public Info setLanguage(final String language) {
    this.language = language;
    return this;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public Info setTimeZone(final TimeZone timeZone) {
    this.timeZone = timeZone;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Info setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Info setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getMiddleName() {
    return middleName;
  }

  public Info setMiddleName(final String middleName) {
    this.middleName = middleName;
    return this;
  }

  public String getSuffix() {
    return suffix;
  }

  public Info setSuffix(final String suffix) {
    this.suffix = suffix;
    return this;
  }

  public BirthOfDateInfo getBirthdateContactInfo() {
    return birthdateContactInfo;
  }

  public Info setBirthdateContactInfo(final BirthOfDateInfo birthdateContactInfo) {
    this.birthdateContactInfo = birthdateContactInfo;
    return this;
  }

  public PhoneInfo getPhone() {
    return phone;
  }

  public Info setPhone(final PhoneInfo phone) {
    this.phone = phone;
    return this;
  }

  public InstantMessagingInfo getInstantMessagingContactInfo() {
    return instantMessagingContactInfo;
  }

  public Info setInstantMessagingContactInfo(
      final InstantMessagingInfo instantMessagingContactInfo) {
    this.instantMessagingContactInfo = instantMessagingContactInfo;
    return this;
  }

  public AddressContactInfo getAddressContactInfo() {
    return addressContactInfo;
  }

  public Info setAddressContactInfo(final AddressContactInfo addressContactInfo) {
    this.addressContactInfo = addressContactInfo;
    return this;
  }

  public WorkInfo getWorkContactInfo() {
    return workContactInfo;
  }

  public Info setWorkContactInfo(final WorkInfo workContactInfo) {
    this.workContactInfo = workContactInfo;
    return this;
  }

  public BusinessAddressInfo getBusinessContactInfo() {
    return businessContactInfo;
  }

  public Info setBusinessContactInfo(
      final BusinessAddressInfo businessContactInfo) {
    this.businessContactInfo = businessContactInfo;
    return this;
  }

  public String getBiography() {
    return biography;
  }

  public Info setBiography(final String biography) {
    this.biography = biography;
    return this;
  }

}
