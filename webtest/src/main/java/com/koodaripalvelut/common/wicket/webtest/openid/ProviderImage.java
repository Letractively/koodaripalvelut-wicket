package com.koodaripalvelut.common.wicket.webtest.openid;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

import com.tustor.wicket.openid.provider.openid.OpenIDProvider;

/**
 * Represents the logo of a {@link OpenIDProvider}.
 * @author fbencosme
 */
public class ProviderImage extends WebComponent {

  private static final long serialVersionUID = 1L;

  private static final String IMG_TAG = "img";
  private static final String SOURCE_ATTRIBUTE = "src";
  private static final String STYLE_ATTRIBUTE = "style";
  private static final String IMAGE_FOLDER = "../image/";
  private static final String STYLE_ATTRIBUTE_VALUE = "width: 100px;";

  public ProviderImage(final String id, final IModel<String> model) {
    super(id, model);
  }

  @Override
  protected void onComponentTag(final ComponentTag tag) {
    super.onComponentTag(tag);
    checkComponentTag(tag, IMG_TAG);
    tag.put(SOURCE_ATTRIBUTE, IMAGE_FOLDER + getDefaultModelObjectAsString());
    tag.put(STYLE_ATTRIBUTE, STYLE_ATTRIBUTE_VALUE);
  }
}