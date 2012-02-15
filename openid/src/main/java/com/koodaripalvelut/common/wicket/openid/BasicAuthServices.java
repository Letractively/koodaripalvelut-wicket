package com.koodaripalvelut.common.wicket.openid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;
import org.codehaus.jettison.json.JSONException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.ParameterList;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koodaripalvelut.common.wicket.openid.info.Info;
import com.koodaripalvelut.common.wicket.openid.provider.oauth1.OAuth1Provider;
import com.koodaripalvelut.common.wicket.openid.provider.oauth2.OAuth2Provider;
import com.koodaripalvelut.common.wicket.openid.provider.openid.OpenIDProvider;

public enum BasicAuthServices implements AuthenticationService {

  OPENID {
    @Override
    public Info loadInfo(final WebRequest request,
        final AuthenticationSession session) {
      final OpenIDProvider provider = (OpenIDProvider) session.getAuthProvider();

      final DiscoveryInformation discoveryInfo =
        provider.getDiscoveryInformation();
      final ConsumerManager manager = OpenIDProvider.getConsumerManager();

      final Map<String, StringValue> map = new HashMap<String, StringValue>();
      
      final IRequestParameters reqParms = request.getRequestParameters();
      
      for (final String key : reqParms.getParameterNames()) {
        map.put(key, reqParms.getParameterValue(key));
      }
      
      map.put("openid.ax.mode", StringValue.valueOf("fetch_response"));

      final ParameterList params = new ParameterList(map);

      return provider.processReturn(discoveryInfo, manager, params);
    }
  },

  OAUTH1 {
    @Override
    public Info loadInfo(final WebRequest req,
        final AuthenticationSession session) {
      final OAuth1Provider provider = (OAuth1Provider) session.getAuthProvider();

      final Verifier verifier = provider.oauth1Verifier(req);

      final Token accessToken = provider.getAccessToken(verifier);

      final OAuthRequest request =
        new OAuthRequest(Verb.GET, provider.getResourcesEndpoint());
      provider.signRequest(accessToken, request);

      final Response response = request.send();

      final String responseBody = response.getBody();
      try {
        return provider.getOAuth1Info(accessToken, responseBody);
      } catch (final JSONException e) {
        LOG.error(GETTING_INFO_MESSAGE, e);
        throw new RuntimeException(GETTING_INFO_MESSAGE, e);
      }
    }



  },
  OAUTH2 {
    @Override
    public Info loadInfo(final WebRequest req,
        final AuthenticationSession session) {
      final HttpServletRequest request = (HttpServletRequest) req.getContainerRequest();
      final OAuth2Provider provider = (OAuth2Provider) session.getAuthProvider();
      Info info = null;
      String accessToken = null;
      try {
        final String code = provider.getAuthorizationCode(request);

        accessToken = provider.getAccessToken(code);

        info = provider.getInfo(accessToken);

      } catch (final Exception e) {
        LOG.error(GETTING_INFO_MESSAGE, e);
        throw new RuntimeException(GETTING_INFO_MESSAGE, e);
      }
      provider.setAccessToken(accessToken);
      return info;
    }

  };

  private static final Logger LOG =
    LoggerFactory.getLogger(BasicAuthServices.class);

  private static final String GETTING_INFO_MESSAGE =
    "Exception occurred while getting info.";


}
