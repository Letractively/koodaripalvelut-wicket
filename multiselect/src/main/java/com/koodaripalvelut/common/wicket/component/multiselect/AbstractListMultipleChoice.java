package com.koodaripalvelut.common.wicket.component.multiselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractListMultipleChoice<T> extends
AbstractChoice<Collection<T>, T> {
  private static final long serialVersionUID = 1L;

  /** Meta key for the retain disabled flag */
  static MetaDataKey<Boolean> RETAIN_DISABLED_META_KEY =
    new MetaDataKey<Boolean>() {
    private static final long serialVersionUID = 1L;
  };

  /** Log. */
  private static final Logger log = LoggerFactory
  .getLogger(AbstractListMultipleChoice.class);

  /** The default maximum number of rows to display. */
  private static int defaultMaxRows = 8;

  /** The maximum number of rows to display. */
  private int maxRows = defaultMaxRows;

  /**
   * Gets the default maximum number of rows to display.
   * @return Returns the defaultMaxRows.
   */
  protected static int getDefaultMaxRows() {
    return defaultMaxRows;
  }

  /**
   * Sets the default maximum number of rows to display.
   * @param defaultMaxRows The defaultMaxRows to set.
   */
  protected static void setDefaultMaxRows(final int defaultMaxRows) {
    AbstractListMultipleChoice.defaultMaxRows = defaultMaxRows;
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String)
   */
  public AbstractListMultipleChoice(final String id) {
    super(id);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      List)
   */
  public AbstractListMultipleChoice(final String id,
      final List<? extends T> choices) {
    super(id, choices);
  }

  /**
   * Creates a multiple choice list with a maximum number of visible rows.
   * @param id component id
   * @param choices list of choices
   * @param maxRows the maximum number of visible rows.
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      List)
   */
  public AbstractListMultipleChoice(final String id,
      final List<? extends T> choices,
      final int maxRows) {
    super(id, choices);
    this.maxRows = maxRows;
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      List,IChoiceRenderer)
   */
  public AbstractListMultipleChoice(final String id,
      final List<? extends T> choices,
      final IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel, List)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends Collection<T>> object,
          final List<? extends T> choices) {
    super(id, (IModel<Collection<T>>) object, choices);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel, List,IChoiceRenderer)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends Collection<T>> object,
          final List<? extends T> choices, final IChoiceRenderer<? super T> renderer) {
    super(id, (IModel<Collection<T>>) object, choices, renderer);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends List<? extends T>> choices) {
    super(id, choices);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel,IModel)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends Collection<T>> model,
          final IModel<? extends List<? extends T>> choices) {
    super(id, (IModel<Collection<T>>) model, choices);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel,IChoiceRenderer)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends List<? extends T>> choices,
          final IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel, IModel,IChoiceRenderer)
   */
  public AbstractListMultipleChoice(final String id,
      final IModel<? extends Collection<T>> model,
          final IModel<? extends List<? extends T>> choices,
              final IChoiceRenderer<? super T> renderer) {
    super(id, (IModel<Collection<T>>) model, choices, renderer);
  }

  /**
   * Sets the number of visible rows in the listbox.
   * @param maxRows the number of visible rows
   * @return this
   */
  public final AbstractListMultipleChoice<T> setMaxRows(final int maxRows) {
    this.maxRows = maxRows;
    return this;
  }

  /**
   * @see FormComponent#getModelValue()
   */
  @Override
  public String getModelValue() {
    final Collection<T> selectedValues = getModelObject();
    final AppendingStringBuffer buffer = new AppendingStringBuffer();
    if (selectedValues != null) {
      final List<? extends T> choices = getChoices();
      for (final T object : selectedValues) {
        final int index = choices.indexOf(object);
        buffer.append(getChoiceRenderer().getIdValue(object, index));
        buffer.append(VALUE_SEPARATOR);
      }
    }
    return buffer.toString();
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#isSelected(Object,int,
   *      String)
   */
  @Override
  protected final boolean isSelected(final T choice, final int index,
      final String selected) {
    // Have a value at all?
    if (selected != null) {
      // Loop through ids
      for (final StringTokenizer tokenizer =
        new StringTokenizer(selected, VALUE_SEPARATOR); tokenizer
        .hasMoreTokens();) {
        final String id = tokenizer.nextToken();
        if (id.equals(getChoiceRenderer().getIdValue(choice, index))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @see org.apache.wicket.Component#onComponentTag(ComponentTag)
   */
  @Override
  protected void onComponentTag(final ComponentTag tag) {
    super.onComponentTag(tag);
    tag.put("multiple", "multiple");

    if (!tag.getAttributes().containsKey("size")) {
      tag.put("size", Math.min(maxRows, getChoices().size()));
    }
  }

  /**
   * @see org.apache.wicket.markup.html.form.FormComponent#convertValue(String[])
   */
  @Override
  protected Collection<T> convertValue(final String[] ids)
  throws ConversionException {
    if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
      return convertChoiceIdsToChoices(ids);
    } else {
      // TODO 1.3: check if its safe to return Collections.EMPTY_LIST here
      final ArrayList<T> result = new ArrayList<T>();
      addRetainedDisabled(result);
      return result;
    }
  }

  /**
   * Converts submitted choice ids to choice objects.
   * @param ids choice ids. this array is nonnull and always contains at least
   *          one id.
   * @return list of choices.
   */
  protected abstract List<T> convertChoiceIdsToChoices(final String[] ids);

  private void addRetainedDisabled(final ArrayList<T> selectedValues) {
    if (isRetainDisabledSelected()) {
      final Collection<T> unchangedModel = getModelObject();
      String selected;
      {
        final StringBuilder builder = new StringBuilder();
        for (final T t : unchangedModel) {
          builder.append(t);
          builder.append(";");
        }
        selected = builder.toString();
      }
      final List<? extends T> choices = getChoices();
      for (int i = 0; i < choices.size(); i++) {
        final T choice = choices.get(i);
        if (isDisabled(choice, i, selected)) {
          if (unchangedModel.contains(choice)) {
            if (!selectedValues.contains(choice)) {
              selectedValues.add(choice);
            }
          }
        }
      }
    }
  }

  /**
   * If the model object exists, it is assumed to be a Collection, and it is
   * modified in-place. Then {@link Model#setObject(Object)} is called with the
   * same instance: it allows the Model to be notified of changes even when
   * {@link Model#getObject()} returns a different {@link Collection} at every
   * invocation.
   * @see FormComponent#updateModel()
   * @throws UnsupportedOperationException if the model object Collection cannot
   *           be modified
   */
  @Override
  public void updateModel() {
    Collection<T> selectedValues = getModelObject();
    if (selectedValues != null) {
      if (getDefaultModelObject() != selectedValues) {
        throw new WicketRuntimeException(
        "Updating a ListMultipleChoice works by modifying the underlying model object in-place, so please make sure that getObject() always returns the same Collection instance!");
      }

      modelChanging();
      selectedValues.clear();
      final Collection<T> converted = getConvertedInput();
      if (converted != null) {
        selectedValues.addAll(converted);
      }
      modelChanged();
      // call model.setObject()
      try {
        getModel().setObject(selectedValues);
      } catch (final Exception e) {
        // ignore this exception because it could be that there
        // is not setter for this collection.
        log.info("no setter for the property attached to " + this);
      }
    } else {
      selectedValues = new ArrayList<T>(getConvertedInput());
      setDefaultModelObject(selectedValues);
    }
  }

  /**
   * If true, choices that were selected in the model but disabled in rendering
   * will be retained in the model after a form submit. Example: Choices are [1,
   * 2, 3, 4]. Model collection is [2, 4]. In rendering, choices 2 and 3 are
   * disabled ({@link #isDisabled(Object, int, String)}). That means that four
   * checkboxes are rendered, checkboxes 2 and 4 are checked, but 2 and 3 are
   * not clickable. User checks 1 and unchecks 4. If this flag is off, the model
   * will be updated to [1]. This is because the browser does not re-submit a
   * disabled checked checkbox: it only submits [1]. Therefore Wicket will only
   * see the [1] and update the model accordingly. If you set this flag to true,
   * Wicket will check the model before updating to find choices that were
   * selected but disabled. These choices will then be retained, leading to a
   * new model value of [1, 2] as (probably) expected by the user. Note that
   * this will lead to additional calls to
   * {@link #isDisabled(Object, int, String)}.
   * @return flag
   */
  public boolean isRetainDisabledSelected() {
    final Boolean flag = getMetaData(RETAIN_DISABLED_META_KEY);
    return flag != null && flag;
  }

  /**
   * @param retain flag
   * @return this
   * @see #isRetainDisabledSelected()
   */
  public AbstractListMultipleChoice<T> setRetainDisabledSelected(
      final boolean retain) {
    setMetaData(RETAIN_DISABLED_META_KEY, retain ? true : null);
    return this;
  }
}

