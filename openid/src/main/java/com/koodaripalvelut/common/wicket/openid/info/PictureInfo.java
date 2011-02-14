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
import java.net.URL;

/**
 * Images.
 * @author fbencosme@kitsd.com
 */
public class PictureInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private URL defaultPicture;
  private URL squareImage;
  private URL aspectImage43;
  private URL aspectImage34;
  private URL favicon;

  public URL getDefaultPicture() {
    return defaultPicture;
  }

  public PictureInfo setDefaultPicture(final URL defaultPicture) {
    this.defaultPicture = defaultPicture;
    return this;
  }

  public URL getSquareImage() {
    return squareImage;
  }

  public PictureInfo setSquareImage(final URL squareImage) {
    this.squareImage = squareImage;
    return this;
  }

  /** Image (web URL) 4:3 aspect ratio - landscape */
  public URL getAspectImage43() {
    return aspectImage43;
  }

  public PictureInfo setAspectImage43(final URL aspectImage43) {
    this.aspectImage43 = aspectImage43;
    return this;
  }

  /** Image (web URL) 3:4 aspect ratio - portrail. */
  public URL getAspectImage34() {
    return aspectImage34;
  }

  public PictureInfo setAspectImage34(final URL aspectImage34) {
    this.aspectImage34 = aspectImage34;
    return this;
  }

  public URL getFavicon() {
    return favicon;
  }

  public PictureInfo setFavicon(final URL favicon) {
    this.favicon = favicon;
    return this;
  }

}
