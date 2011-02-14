package com.koodaripalvelut.common.wicket.openid;

public interface AuthenticationSession {
  void setAuthProvider(final AuthenticationProvider provider);

  AuthenticationProvider getAuthProvider();
}

