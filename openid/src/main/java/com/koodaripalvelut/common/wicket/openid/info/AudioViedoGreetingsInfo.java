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
 * Audio/Video Greetings.
 * @author fbencosme@kitsd.com
 */
public class AudioViedoGreetingsInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String spokenName;
  private URL audio;
  private URL video;

  public String getSpokenName() {
    return spokenName;
  }

  public AudioViedoGreetingsInfo setSpokenName(final String spokenName) {
    this.spokenName = spokenName;
    return this;
  }

  public URL getAudio() {
    return audio;
  }

  public AudioViedoGreetingsInfo setAudio(final URL audio) {
    this.audio = audio;
    return this;
  }

  public URL getVideo() {
    return video;
  }

  public AudioViedoGreetingsInfo setVideo(final URL video) {
    this.video = video;
    return this;
  }

}
