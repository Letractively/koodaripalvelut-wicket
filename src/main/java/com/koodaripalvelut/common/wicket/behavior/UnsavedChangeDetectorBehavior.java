package com.koodaripalvelut.common.wicket.behavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

/**
 * The UnsavedChangeDetectorBehavior class when added to a form, notifies the
 * user if she or he is about to leave unsaved data in the form.
 * @author cencarnacion@kitsd.com
 */
public class UnsavedChangeDetectorBehavior extends AbstractBehavior {

  private static final String DEFAULT_SCRIPT_PREFIX = "change-detector";

  private static final String DEFAULT_MESSAGE =
      "You have unsaved data in this page.";

  private static final String PARAM_MESSAGE = "message";

  public static final String PARAM_FORM_ID = "formId";

  private static final long serialVersionUID = 1L;

  private Component component;

  private final List<ListMember> whiteList = new ArrayList<ListMember>();

  private final List<ListMember> blackList = new ArrayList<ListMember>();

  private static class ListMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Component component;

    public ListMember(final Component component) {
      this.component = component;
      component.setOutputMarkupId(true);
    }

    @Override
    public String toString() {
      return "'" + component.getMarkupId() + "'";
    }

  }

  /**
   * Adds a component to the white list.
   * @param component The component to be added.
   */
  public void addToWhiteList(final Component component) {
    component.setOutputMarkupId(true);
    whiteList.add(new ListMember(component));
  }

  /**
   * Adds a component to the black list.
   * @param component The component to be added.
   */
  public void addToBlackList(final Component component) {
    component.setOutputMarkupId(true);
    blackList.add(new ListMember(component));
  }

  /**
   * @see org.apache.wicket.behavior.AbstractBehavior#bind(org.apache.wicket.Component)
   */
  @Override
  public void bind(final Component component) {
    assertUniqueComponent();
    this.component = component;
    assertComponentType();
    component.setOutputMarkupId(true);
  }

  /**
   * @see org.apache.wicket.behavior.AbstractBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
   */
  @Override
  public void renderHead(final IHeaderResponse response) {

    response.renderJavascriptReference(WicketEventReference.INSTANCE);
    response.renderJavascriptReference(WicketAjaxReference.INSTANCE);

    final TextTemplate searchBoxJs =
        new PackagedTextTemplate(UnsavedChangeDetectorBehavior.class,
            getScriptPrefix() + "-init.js");

    final Map<String, Object> variables = new HashMap<String, Object>();

    setOptions(variables);
    response.renderJavascriptReference(new JavascriptResourceReference(
        UnsavedChangeDetectorBehavior.class, getScriptPrefix() + ".js"));
    response.renderOnDomReadyJavascript(searchBoxJs.asString(variables));
  }

  /**
   * Override this method to change rendering options for the change detector.
   * @param params Parameter map to set.
   */
  protected void setOptions(final Map<String, Object> params) {
    params.put(PARAM_FORM_ID, getFormMarkupId());
    params.put(PARAM_MESSAGE, getMessage());
    params.put("whiteList", whiteList);
    params.put("blackList", blackList);
  }

  /**
   * Override this method to change which javascript files to use. The default
   * return value is: "change-detector".
   * @return javascript prefix relative to the
   *         {@link UnsavedChangeDetectorBehavior} class.
   */
  protected String getScriptPrefix() {
    return DEFAULT_SCRIPT_PREFIX;
  }

  /**
   * Override this method to change the message to be shown to the user. The
   * default return value is: "You have unsaved data in this page".
   * @return The message.
   */
  protected String getMessage() {
    return DEFAULT_MESSAGE;
  }

  /**
   * Gets the id of the form will get attached to.
   * @return The DOM id of the component
   */
  protected final String getFormMarkupId() {
    return component.getMarkupId();
  }

  /**
   * Checks whether the host component is an instance of Form .
   * @throws WicketRuntimeException If component is not an instance of Form.
   */
  private void assertComponentType() throws WicketRuntimeException {
    if (!(component instanceof Form<?>)) {
      throw new WicketRuntimeException(
          "This behavior only support instances of Form components.");
    }
  }

  /**
   * Checks whether this behavior has been added to more than one component.
   * @throws WicketRuntimeException If this behavior has been added to more than
   *           one component.
   */
  private void assertUniqueComponent() throws WicketRuntimeException {
    if (component != null) {
      throw new WicketRuntimeException(
          "This behavior can be added to one component only.");
    }
  }

}
