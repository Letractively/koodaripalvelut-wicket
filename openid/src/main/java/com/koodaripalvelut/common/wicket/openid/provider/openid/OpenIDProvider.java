/*
 * Copyright 2011 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.openid.provider.openid;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.lang.PropertyResolver;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.server.RealmVerifier;

import com.koodaripalvelut.common.wicket.openid.AuthenticationProvider;
import com.koodaripalvelut.common.wicket.openid.AuthenticationService;
import com.koodaripalvelut.common.wicket.openid.AuthenticationSession;
import com.koodaripalvelut.common.wicket.openid.BasicAuthServices;
import com.koodaripalvelut.common.wicket.openid.info.Info;
import com.koodaripalvelut.common.wicket.openid.provider.AbstractProvider;

/**
 * Implementation of the {@link AuthenticationProvider} that uses the OpenID
 * protocol to carry out the authentication.
 * @author eferreira@kindleit.net
 */
public class OpenIDProvider extends AbstractProvider<OpenIDProvider> {

  private static final long serialVersionUID = 1L;

  public static final String ATTRIBUTE_EXCHANGE_SCHEMA =
    "attributeExchangeSchema";

  public static ConsumerManager consumerManager;

  private static final Logger LOG = Logger.getLogger(OpenIDProvider.class);

  private static final String DISCOVERY_ERROR_MESSAGE = "Discovery error";

  private static final String AUTH_REQUEST_ERROR_MESSAGE =
    "Exception occurred while building authRequest";

  private static final String VERIFYING_RESPONSE_MESSAGE =
    "Exception occurred while verifying response";

  private static final String AUTH_FAIL_MESSAGE =
    "OpenID Authentication Failure. ";

  private String attributeExchangeSchema;

  private DiscoveryInformation discoveryInfo;

  private String returnUrl;

  public OpenIDProvider(final String providerName) {
    super(providerName);
  }

  /**
   * @param providerName represent the name of the OpenID provider and it is
   *          also the suffix of the provider's attribute located in the
   *          Properties parameter.
   * @param props contains the OpenId provider information.
   */
  public OpenIDProvider(final String providerName, final Properties props) {
    super(providerName, props);
    this.setAttributeExchangeSchema(props
        .getProperty(propKey(ATTRIBUTE_EXCHANGE_SCHEMA)));
  }

  /**
   * Setter for the <tt>attributeExchangeSchema</tt>.
   * @param attributeExchangeSchema the attributeExchangeSchema to set
   * @return the OpenIDProvider object (builder ideology).
   */
  public OpenIDProvider setAttributeExchangeSchema(
      final String attributeExchangeSchema) {
    this.attributeExchangeSchema = attributeExchangeSchema;
    return this;
  }

  /**
   * Getter for the <tt>attributeExchangeSchema</tt>.
   * @return the attributeExchangeSchema
   */
  public String getAttributeExchangeSchema() {
    return attributeExchangeSchema;
  }

  @Override
  public AuthenticationService getService() { return BasicAuthServices.OPENID; }

  public DiscoveryInformation getDiscoveryInformation() {
    return discoveryInfo;
  }

  public void setDiscoveryInformation(final DiscoveryInformation discoveryInfo) {
    this.discoveryInfo = discoveryInfo;
  }

  public synchronized static ConsumerManager getConsumerManager() {
    try {
      if (consumerManager == null) {
        consumerManager = new ConsumerManager();
        final RealmVerifier rv = new RealmVerifier(false);
        rv.setEnforceRpId(false);
        consumerManager.setRealmVerifier(rv);
        consumerManager.setAssociations(new InMemoryConsumerAssociationStore());
        consumerManager.setNonceVerifier(new InMemoryNonceVerifier(10000));
      }
    } catch (final ConsumerException e) {
      final String message = "Exception creating ConsumerManager!";
      throw new RuntimeException(message, e);
    }
    return consumerManager;
  }

  /**
   * Perform discovery on the User-Supplied identifier and return the
   * DiscoveryInformation object that results from Association with the OP. This
   * will probably be needed by the caller (stored in Session perhaps?). I'm not
   * thrilled about ConsumerManager being static, but it is very important to
   * openid4java that the ConsumerManager object be the same instance all
   * through a conversation (discovery, auth request, auth response) with the
   * OP. I didn't dig terribly deeply, but suspect that part of the key exchange
   * or the nonce uses the ConsumerManager's hash, or some other
   * instance-specific construct to do its thing.
   * @param userSuppliedIdentifier The User-Supplied identifier. It may already
   *          be normalized.
   * @return DiscoveryInformation - The resulting DisoveryInformation object
   *         returned by openid4java following successful association with the
   *         OP.
   */
  @SuppressWarnings("unchecked")
  public DiscoveryInformation performDiscoveryOnUserSuppliedIdentifier(
      final ConsumerManager consumerManager) {
    try {
      final List<DiscoveryInformation> discoveries =
        consumerManager.discover(getEndpoint());
      return consumerManager.associate(discoveries);
    } catch (final DiscoveryException e) {
      LOG.error(DISCOVERY_ERROR_MESSAGE, e);
      throw new RuntimeException(DISCOVERY_ERROR_MESSAGE, e);
    }
  }

  /**
   * Create an OpenID Auth Request, using the DiscoveryInformation object return
   * by the openid4java library. This method also uses the Simple Registration
   * Extension to grant the Relying Party (RP).
   * @param discoveryInformation The DiscoveryInformation that should have been
   *          previously obtained from a call to
   *          performDiscoveryOnUserSuppliedIdentifier().
   * @param returnToUrl The URL to which the OP will redirect once the
   *          authentication call is complete.
   * @return AuthRequest - A "good-to-go" AuthRequest object. The caller must
   *         take this object and forward it on to the OP. Or call
   *         processAuthRequest() - part of this Service Class.
   */
  public AuthRequest createOpenIdAuthRequest(
      final DiscoveryInformation discoveryInformation,
      final ConsumerManager manager, final String returnToUrl) {
    try {
      final AuthRequest authRequest =
        manager.authenticate(discoveryInformation, returnToUrl);

      final FetchRequest fetch = FetchRequest.createFetchRequest();
      fetch.addAttribute("email", attributeExchangeSchema + "/contact/email",
          true);
      fetch.addAttribute("firstName", attributeExchangeSchema + "/namePerson/first",
          true);
      fetch.addAttribute("lastName", attributeExchangeSchema + "/namePerson/last",
          true);
      fetch.addAttribute("fullname", attributeExchangeSchema + "/namePerson",
          false);
      fetch.addAttribute("gender", attributeExchangeSchema + "/person/gender",
          false);
      fetch.addAttribute("language", attributeExchangeSchema + "/pref/language",
          false);
      fetch.addAttribute("timezone", attributeExchangeSchema + "/pref/timezone",
          false);
      fetch.addAttribute("country", attributeExchangeSchema
          + "/contact/country/home/", false);
      authRequest.addExtension(fetch);

      return authRequest;

    } catch (final Exception e) {
      LOG.error(AUTH_REQUEST_ERROR_MESSAGE, e);
      throw new RuntimeException(AUTH_REQUEST_ERROR_MESSAGE, e);
    }

  }

  /**
   * Processes the returned information from an authentication request from the
   * OP.
   * @param queryUrl The "return to" URL that was passed to the OP. It must
   *          match exactly, or openid4java will issue a verification failed
   *          message in the logs.
   * @param discoveryInfo DiscoveryInformation that was created earlier in the
   *          conversation (by openid4java). This will need to be verified with
   *          openid4java to make sure everything went smoothly and there are no
   *          possible problems. This object was probably stored in session and
   *          retrieved for use in calling this method.
   * @param pageParameters PageParameters passed to the page handling the return
   *          verificaion.
   * @return RegistrationModel - null if there was a problem, or a
   *         RegistrationModel object, with parameters filled in as compeletely
   *         as possible from the information available from the OP. If you are
   *         using MyOpenID, most of the time what is returned is from your
   *         "Default" profile, so if you need more information returned, make
   *         sure your Default profile is completely filled out.
   */
  public Info processReturn(final DiscoveryInformation discoveryInfo,
      final ConsumerManager manager,
      final ParameterList params) {
    try {
      final VerificationResult verificationResult =
        manager.verify(returnUrl, params, discoveryInfo);
      final Identifier verifiedIdentifier = verificationResult.getVerifiedId();

      if (verifiedIdentifier == null) {
        throw new WicketRuntimeException(AUTH_FAIL_MESSAGE
            + verificationResult.getStatusMsg());
      }

      return fillRegistrationModel(verificationResult, verifiedIdentifier);
    } catch (final Exception e) {
      LOG.error(VERIFYING_RESPONSE_MESSAGE, e);
      throw new RuntimeException(VERIFYING_RESPONSE_MESSAGE, e);
    }
  }

  public Info processReturn(final DiscoveryInformation discoveryInfo,
      final ConsumerManager manager,
      final PageParameters params) {
    return processReturn(discoveryInfo, manager, new ParameterList(
        params));
  }

  private Info fillRegistrationModel(
      final VerificationResult verificationResult,
      final Identifier verifiedIdentifier) throws MessageException {
    final Info info = new Info();

    final AuthSuccess authSuccess =
      (AuthSuccess) verificationResult.getAuthResponse();

    info.setOpenId(verifiedIdentifier.getIdentifier());

    if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
      final FetchResponse fetchResp =
        (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);

      for (final String infoProp : getInfoProperties().keySet()) {
        final String provProp = getInfoProperties().get(infoProp);
        final String value = fetchResp.getAttributeValue(provProp);
        PropertyResolver.setValue(infoProp, info, value, null);
      }
      if (info.getFullName() == null) {
        info.setFullName(info.getFirstName() + " " + info.getLastName());
      }

    }

    if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
      final SRegResponse sRegResponse =
        (SRegResponse) authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);

      for (final String infoProp : getInfoProperties().keySet()) {
        final String provProp = getInfoProperties().get(infoProp);
        final String value = sRegResponse.getAttributeValue(provProp);
        PropertyResolver.setValue(infoProp, info, value, null);
      }
    }
    return info;
  }

  @Override
  public void startAuthentication(final String returnUrl) {
    this.returnUrl = returnUrl;
    final AuthRequest authReq = getOpenIDAuthRequest(returnUrl);
    RequestCycle.get().setRequestTarget(
        new RedirectRequestTarget(authReq.getDestinationUrl(true)));
    final AuthenticationSession session = (AuthenticationSession) Session.get();
    session.setAuthProvider(this);
  }

  /**
   * Constructs an openID authorization request for a given openID provider
   * using #getReturnUrl().
   */
  private AuthRequest getOpenIDAuthRequest(final String returnUrl) {

    final DiscoveryInformation discoveryInformation =
      performDiscoveryOnUserSuppliedIdentifier(
          OpenIDProvider.getConsumerManager());

    final AuthRequest authRequest =
      createOpenIdAuthRequest(discoveryInformation,
          OpenIDProvider.getConsumerManager(), returnUrl);
    setDiscoveryInformation(discoveryInformation);
    return authRequest;
  }

}

