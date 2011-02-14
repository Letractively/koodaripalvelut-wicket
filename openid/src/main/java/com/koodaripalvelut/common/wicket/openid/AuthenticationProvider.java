package com.koodaripalvelut.common.wicket.openid;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;

import com.koodaripalvelut.common.wicket.openid.info.Info;

/**
 * This interface contains the basic information of the server that host the
 * protected resources of the client. The client is who will grant access to the
 * application by providing a username and a password.
 * <ul>
 * <li>providerName: The name of the providers.</li>
 * <li>endpoint:The provider URL used to obtain the user authorization for
 * Application access.</li>
 * <li>logo: It is a convenient way to store the provider logo path if needed.</li>
 * </ul>
 * @author rhansen@kindleit.net *
 */
public interface AuthenticationProvider extends Serializable {

  /**
   * @return a {@link AuthenticationService} protocol implementation where this
   *         provider works on.
   */
  AuthenticationService getService();

  /**
   * Starts the authentication process by redirecting the resource owner to the
   * provider login form.
   * @param returnUrl the URL where the resource owner will be redirect once
   *          authenticated with the provider.
   */
  void startAuthentication(final String returnUrl);

  /**
   * Gets the provider logo path.
   * @return provider logo path.
   */
  String getLogo();

  /**
   * Sets the provider logo path.
   * @param logo - the provider log path.
   */
  AuthenticationProvider setLogo(final String logo);

  /**
   * @return the String representation of the provider URL endpoint.
   */
  String getEndpoint();

  /**
   * @param endpoint - sets the URL endpoint String representation. This URL
   *          used to start the authentication process.
   */
  AuthenticationProvider setEndpoint(final String endpoint);

  /**
   * Gets the {@link org.apache.wicket.model.IModel} containing the resource
   * owner information.
   * @param request - The request after the provider redirect back to the
   *          application.
   * @return a {@link org.apache.wicket.model.IModel} object.
   */
  IModel<Info> getInfoModel(final WebRequest request,
      final AuthenticationSession session);

}
