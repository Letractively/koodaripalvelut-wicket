package com.koodaripalvelut.common.wicket.openid;

import java.io.Serializable;

import org.apache.wicket.protocol.http.WebRequest;

import com.koodaripalvelut.common.wicket.openid.info.Info;

public interface AuthenticationService extends Serializable {
  /** Loads the supplied authentication information from the given request and
   * OpenID session. */
  Info loadInfo(WebRequest request, AuthenticationSession session);

}
