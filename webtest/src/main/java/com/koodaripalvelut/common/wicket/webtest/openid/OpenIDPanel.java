package com.koodaripalvelut.common.wicket.webtest.openid;

import static com.tustor.wicket.openid.info.Info.EMAIL;
import static com.tustor.wicket.openid.info.Info.FIRST_NAME;
import static com.tustor.wicket.openid.info.Info.FULL_NAME;
import static com.tustor.wicket.openid.info.Info.GENDEER;
import static com.tustor.wicket.openid.info.Info.LAST_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestUtils;

import com.koodaripalvelut.common.wicket.webtest.WebApplicationTest;
import com.tustor.wicket.openid.AuthenticationProvider;
import com.tustor.wicket.openid.info.Info;
import com.tustor.wicket.openid.provider.oauth1.OAuth1Provider;
import com.tustor.wicket.openid.provider.oauth2.OAuth2Provider;
import com.tustor.wicket.openid.provider.openid.OpenIDProvider;

/**
 * @author fbencosme
 */
public class OpenIDPanel extends Panel {


  private static final String FORM_ID = "form";
  private static final String PROVIDER_IMAGE_ID = "providerImage";
  private static final String FORM_PROP_PROVIDERS_ID = "formPropProviders";
  private static final String FORM_SETTER_PROVIDERS_ID = "formSetterProviders";
  private static final String LINK_PROP_PROVIDERS_ID = "linkPropProviders";
  private static final String LINK_SETTER_PROVIDERS_ID = "linkSetterProviders";
  private static final String LINK_ID = "link";
  private static final long serialVersionUID = 1L;

  public OpenIDPanel(final String id) {
    super(id);

    // Forms using property files
    add(new DataView<AuthenticationProvider>(FORM_PROP_PROVIDERS_ID,
        getProvidersByProperties()) {

      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final Item<AuthenticationProvider> item) {

        final AuthenticationProvider provider = item.getModelObject();

        final Form<Info> form = new Form<Info>(FORM_ID) {

          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit() {
            provider.startAuthentication(getReturnUrl());
          }
        };
        form.add(new ProviderImage(PROVIDER_IMAGE_ID, Model.of(provider.getLogo())));
        item.add(form);
      }
    });

    // Forms using setters
    add(new DataView<AuthenticationProvider>(FORM_SETTER_PROVIDERS_ID,
        getProvidersBySetters()) {

      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final Item<AuthenticationProvider> item) {

        final AuthenticationProvider provider = item.getModelObject();

        final Form<Info> form = new Form<Info>(FORM_ID) {

          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit() {
            provider.startAuthentication(getReturnUrl());
          }
        };
        form.add(new ProviderImage(PROVIDER_IMAGE_ID, Model.of(provider
            .getLogo())));
        item.add(form);
      }
    });

    // Direct Links using property files
    add(new DataView<AuthenticationProvider>(LINK_PROP_PROVIDERS_ID,
        getProvidersByProperties()) {

      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final Item<AuthenticationProvider> item) {

        final AuthenticationProvider provider = item.getModelObject();
        final Link<Void> link = new Link<Void>(LINK_ID) {

          private static final long serialVersionUID = 1L;

          @Override
          public void onClick() {
            provider.startAuthentication(getReturnUrl());
          }
        };
        item.add(link);
        link.add(new ProviderImage(PROVIDER_IMAGE_ID, Model.of(provider
            .getLogo())));
      }
    });

    // Direct Links using setters
    add(new DataView<AuthenticationProvider>(LINK_SETTER_PROVIDERS_ID,
        getProvidersBySetters()) {

      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final Item<AuthenticationProvider> item) {

        final AuthenticationProvider provider = item.getModelObject();
        final Link<Void> link = new Link<Void>(LINK_ID) {

          private static final long serialVersionUID = 1L;

          @Override
          public void onClick() {
            provider.startAuthentication(getReturnUrl());
          }
        };
        item.add(link);
        link.add(new ProviderImage(PROVIDER_IMAGE_ID, Model.of(provider
            .getLogo())));
      }
    });
  }

  /**
   * Gets the provider list as specified in each implementation of
   * {@link AuthenticationProvider}
   */
  protected IDataProvider<AuthenticationProvider> getProvidersByProperties() {
    final List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
    loadProvidersByProperties(providers);
    return new ListDataProvider<AuthenticationProvider>(providers);
  }

  /**
   * Loads each provider information from properties files. It is also possible
   * set the providers information by their setters.
   */
  private void loadProvidersByProperties(
      final List<AuthenticationProvider> providers) {
    final Properties openIdProps = loadProperties(OpenIDProvider.class.getSimpleName());
    providers.add(new OpenIDProvider("google", openIdProps));
    providers.add(new OpenIDProvider("yahoo", openIdProps));
    providers.add(new OpenIDProvider("myopenid", openIdProps));

    final Properties OAuth1Props = loadProperties(OAuth1Provider.class.getSimpleName());
    providers.add(new OAuth1Provider("twitter", OAuth1Props));

    final Properties OAuth2Props = loadProperties(OAuth2Provider.class.getSimpleName());
    providers.add(new OAuth2Provider("facebook", OAuth2Props));
  }

  private Properties loadProperties(final String propName){
    final Properties props = new Properties();
    try {
      props.load(OpenIDPanel.class
          .getResourceAsStream(propName + ".properties"));
    } catch (final IOException e) {

    }
    return props;
  }

  protected IDataProvider<AuthenticationProvider> getProvidersBySetters() {
    final List<AuthenticationProvider> providers =
      new ArrayList<AuthenticationProvider>();
    loadProvidersBySetters(providers);
    return new ListDataProvider<AuthenticationProvider>(providers);
  }

  private void loadProvidersBySetters(
      final List<AuthenticationProvider> providers) {
    final Map<String, String> infoProperties = new HashMap<String, String>();
    infoProperties.put(FIRST_NAME, "firstName");
    infoProperties.put(LAST_NAME, "lastName");
    infoProperties.put(FULL_NAME, "fullname");
    infoProperties.put(EMAIL, "email");
    infoProperties.put(GENDEER, "gender");

    final OpenIDProvider google = new OpenIDProvider("google");
    google.setAttributeExchangeSchema("http://axschema.org")
    .setInfoProperties(infoProperties)
    .setEndpoint("https://www.google.com/accounts/o8/id")
    .setLogo("google-logo.jpg");
    providers.add(google);

    final OpenIDProvider yahoo = new OpenIDProvider("yahoo");
    yahoo.setAttributeExchangeSchema("http://axschema.org")
    .setInfoProperties(infoProperties)
    .setEndpoint("http://open.login.yahooapis.com/openid20/www.yahoo.com/xrds")
    .setLogo("yahoo-logo.png");
    providers.add(yahoo);

    final OpenIDProvider myopenid = new OpenIDProvider("myopenid");
    myopenid.setAttributeExchangeSchema("http://schema.openid.net")
    .setInfoProperties(infoProperties)
    .setEndpoint("https://www.myopenid.com/")
    .setLogo("my-openid-logo.png");
    providers.add(myopenid);


    final OAuth1Provider twitter = new OAuth1Provider("twitter");
    final Map<String, String> fInfoProperties = new HashMap<String, String>();
    fInfoProperties.put(FULL_NAME, "name");
    twitter.setRequestTokenEndpoint("http://twitter.com/oauth/request_token")
    .setClientId("n3IXNtdrpZ2ZkqG27r0gEw")
    .setClientSecret("jF5Zj93Z7brpCx9eSAU582QJxOLXAiNwmoE6V4uBweE")
    .setTokenEndpoint("http://twitter.com/oauth/access_token")
    .setResourcesEndpoint("http://api.twitter.com/1/account/verify_credentials.json")
    .setInfoProperties(fInfoProperties)
    .setEndpoint("https://twitter.com/oauth/authorize?oauth_token=")
    .setLogo("twitter-logo.jpg");
    providers.add(twitter);


    final OAuth2Provider facebook = new OAuth2Provider("facebook");
    final Map<String, String> tInfoProperties = new HashMap<String, String>();
    tInfoProperties.put(FIRST_NAME, "first_name");
    tInfoProperties.put(LAST_NAME, "last_name");
    tInfoProperties.put(FULL_NAME, "name");
    tInfoProperties.put(EMAIL, "email");
    tInfoProperties.put(GENDEER, "gender");
    facebook.setScope("email")
    .setClientId("200374409978397")
    .setClientSecret("3eef005d01635cd2787b1a9adb2b6286")
    .setTokenEndpoint("https://graph.facebook.com/oauth/access_token")
    .setResourcesEndpoint("https://graph.facebook.com/me?access_token=")
    .setInfoProperties(tInfoProperties)
    .setEndpoint("https://graph.facebook.com/oauth/authorize")
    .setLogo("facebook-logo.jpg");
    providers.add(facebook);

  }

  /**
   * Formats the returnURL string that will be provided to the openID or OAuth
   * server.
   */
  protected String getReturnUrl() {
    final String pg =
        WebApplicationTest.PATH + "/" + ResultPage.class.getSimpleName();
    return RequestUtils.toAbsolutePath(pg);
  }

}
