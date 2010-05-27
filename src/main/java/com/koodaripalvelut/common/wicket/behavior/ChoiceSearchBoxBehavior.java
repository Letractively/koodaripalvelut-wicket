package com.koodaripalvelut.common.wicket.behavior;

import static com.koodaripalvelut.common.wicket.behavior.ChoiceSearchBoxBehavior.Mode.FULL;
import static com.koodaripalvelut.common.wicket.behavior.ChoiceSearchBoxBehavior.Position.AFTER;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

/**
 * The ChoiceSearchBoxBehavior adds a search field which applies search criteria
 * to a given {@link AbstractChoice}. Note: This behavior cannot be reused
 * between components.
 * @author cencarnacion@kitsd.com
 * @author rhansen@kitsd.com
 */
public class ChoiceSearchBoxBehavior extends AbstractBehavior {

  /**
   * The position enum lists possible positioning for the
   * ChoiceSearchBoxBehavior
   * @author rhansen@kitsd.com
   */
  public enum Position {
    /** Place search field before the select. */
    BEFORE,
    /** Place search field after the select. */
    AFTER;
  }

  /**
   * Mode is responsible of
   * @author rhansen@kitsd.com
   */
  public enum Mode {
    FIELD_ONLY, SEARCH_BUTTON, BOTH, FULL
  }

  public static final String PARAM_SELECT_ID = "selectId";
  public static final String PARAM_REGEX_OPTS = "regexFlags";
  public static final String PARAM_AUTOREMOVE = "autoremove";
  public static final String PARAM_POSITION = "position";
  public static final String PARAM_MODE = "mode";
  public static final String PARAM_CLASS_PREFIX = "classPrefix";
  public static final String PARAM_SEARCH_LABEL = "ChoiceSearchSearchLabel";
  public static final String PARAM_CLEAR_LABEL = "ChoiceSearchClearLabel";
  public static final String PARAM_SUFFIX = "suffix";
  public static final String PARAM_PREFIX = "prefix";

  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_SCRIPT_PREFIX = "search-box";
  private static final String DEFAULT_REGEX_OPTS = "i";
  private static final String DEFAULT_AUTOREMOVE = "true";
  private static final String DEFAULT_SEARCH_LABEL = "Search";
  private static final String DEFAULT_CLEAR_LABEL = "Clear";

  private Component component;

  private String prefix = "";

  private String suffix = "";

  public String getPrefix() {
    return prefix;
  }

  /**
   * @param prefix Prefix to use before the search field.
   * @return this
   */
  public ChoiceSearchBoxBehavior setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * @return Suffix to use after the search field.
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * @param suffix Suffix to use after the search field.
   * @return this
   */
  public ChoiceSearchBoxBehavior setSuffix(final String suffix) {
    this.suffix = suffix;
    return this;
  }

  /**
   * Binds the behavior to a give component.
   * @see AbstractBehavior#bind(Component)
   */
  @Override
  public void bind(final Component component) {
    assertUniqueComponent();
    this.component = component;
    asssertComponentType();
    component.setOutputMarkupId(true);
  }

  /**
   * Renders required javascript references. There should be no reason to
   * override this method. please use {@link #setOptions(Map)} to set the
   * appropriate options, and {@link #getScriptPrefix()} to override the scripts
   * to use.
   * @see AbstractBehavior#renderHead(IHeaderResponse)
   */
  @Override
  public void renderHead(final IHeaderResponse response) {
    final TextTemplate searchBoxJs =
        new PackagedTextTemplate(ChoiceSearchBoxBehavior.class,
            getScriptPrefix() + "-init.js");
    final Map<String, Object> variables = new HashMap<String, Object>();

    setOptions(variables);
    response.renderJavascriptReference(new JavascriptResourceReference(
        ChoiceSearchBoxBehavior.class, getScriptPrefix() + ".js"));
    response.renderOnDomReadyJavascript(searchBoxJs.asString(variables));
  }

  /**
   * Override this method to change which javascript files to use. The default
   * return value is: "search-box".
   * @return javascript prefix relative to the {@link ChoiceSearchBoxBehavior}
   *         class.
   */
  protected String getScriptPrefix() {
    return DEFAULT_SCRIPT_PREFIX;
  }

  /**
   * Override this method to change rendering options for the search box.
   * Remember to set the selectId, position and displayMode properties.
   * @param params Parameter map to set.
   */
  protected void setOptions(final Map<String, Object> params) {
    params.put(PARAM_SELECT_ID, getSelectMarkupId());
    params.put(PARAM_REGEX_OPTS, DEFAULT_REGEX_OPTS);
    params.put(PARAM_AUTOREMOVE, DEFAULT_AUTOREMOVE);
    params.put(PARAM_POSITION, AFTER);
    params.put(PARAM_MODE, FULL);
    params.put(PARAM_CLASS_PREFIX, DEFAULT_SCRIPT_PREFIX);
    params.put(PARAM_SEARCH_LABEL, component.getString(PARAM_SEARCH_LABEL,
        null, DEFAULT_SEARCH_LABEL));
    params.put(PARAM_CLEAR_LABEL, component.getString(PARAM_CLEAR_LABEL, null,
        DEFAULT_CLEAR_LABEL));
    params.put(PARAM_PREFIX, getPrefix());
    params.put(PARAM_SUFFIX, getSuffix());
  }

  /**
   * Gets the id of the component that the search box will get attached to.
   * @return The DOM id of the component
   */
  protected final String getSelectMarkupId() {
    return component.getMarkupId();
  }

  /**
   * Checks whether the host component is an instance of AbstractChoice.
   * @throws WicketRuntimeException If component is not an instance of
   *           AbstractChoice.
   */
  private void asssertComponentType() throws WicketRuntimeException {
    if (!(component instanceof AbstractChoice<?, ?>)) {
      throw new WicketRuntimeException(
          "This behavior only support instances of AbstractChoice components.");
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
