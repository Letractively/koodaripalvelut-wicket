/**
 * Copyright (c) Koodaripalvelut.com Finland 2008
 */
package com.koodaripalvelut.common.wicket.components;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * @author Martin
 *
 * Copyright (c) Koodaripalvelut.com Finland 2008
 * 
 * @param <T>
 */
public class ListMultipleChoiceWithStylingOptions<T> extends ListMultipleChoice<T> implements IOnChangeListener {
  private String previouslyAppendedOptGroupLabel;
  private int choices;

  /**
   * @param id
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<? extends List<? extends T>> choices,
          final IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @param id
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<? extends List<? extends T>> choices) {
    super(id, choices);
  }

  /**
   * @param id
   * @param model
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<Collection<T>> model, final IModel<? extends List<? extends T>> choices,
          final IChoiceRenderer<? super T> renderer) {
    super(id, model, choices, renderer);
  }

  /**
   * @param id
   * @param model
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<Collection<T>> model, final IModel<? extends List<? extends T>> choices) {
    super(id, model, choices);
  }

  /**
   * @param id
   * @param object
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<Collection<T>> object, final List<? extends T> choices,
      final IChoiceRenderer<? super T> renderer) {
    super(id, object, choices, renderer);
  }

  /**
   * @param id
   * @param object
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final IModel<Collection<T>> object, final List<? extends T> choices) {
    super(id, object, choices);
  }

  /**
   * @param id
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final List<? extends T> choices, final IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @param id
   * @param choices
   * @param maxRows
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final List<? extends T> choices, final int maxRows) {
    super(id, choices, maxRows);
  }

  /**
   * @param id
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(final String id,
      final List<? extends T> choices) {
    super(id, choices);
  }

  /**
   * @param id
   */
  public ListMultipleChoiceWithStylingOptions(final String id) {
    super(id);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#appendOptionHtml(org.apache.wicket.util.string.AppendingStringBuffer, java.lang.Object, int, java.lang.String)
   */
  @Override
  protected void appendOptionHtml(final AppendingStringBuffer buffer, final T choice,
      final int index, final String selected) {
    final AppendingStringBuffer tmp = new AppendingStringBuffer(50);
    super.appendOptionHtml(tmp, choice, index, selected);

    if (getChoiceRenderer() instanceof IStyledChoiceRenderer) {
      final IStyledChoiceRenderer<T> styledChoiceRenderer = (IStyledChoiceRenderer<T>) getChoiceRenderer();

      final String currentOptGroupLabel = styledChoiceRenderer.getOptGroupLabel(choice);

      addParentId(tmp, currentOptGroupLabel);

      if (currentOptGroupLabel == previouslyAppendedOptGroupLabel
          || currentOptGroupLabel != null
          && currentOptGroupLabel.equals(previouslyAppendedOptGroupLabel)) {
        // OptGroup changed
        if (previouslyAppendedOptGroupLabel != null) {
          endOptGroup(buffer);
        }

        if (currentOptGroupLabel != null) {
          // OptGroup started
          final int start = tmp.indexOf("<option");
          final StringBuilder label = new StringBuilder(currentOptGroupLabel.length() + 19);
          label.append("<optgroup label=\"").append(currentOptGroupLabel).append("\">");
          tmp.insert(start, label);
        }
      }

      if (currentOptGroupLabel != null && index == choices-1) {
        // Last option group must end too
        endOptGroup(tmp);
      }

      {
        final String cssClass = styledChoiceRenderer.getOptionCssClassName(choice);
        if (cssClass != null) {
          final int start = tmp.indexOf("<option");
          tmp.insert(start + 7, new StringBuilder(" class=\"").append(cssClass).append("\""));
        }
      }

      previouslyAppendedOptGroupLabel = currentOptGroupLabel;
    }


    buffer.append(tmp);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#onComponentTagBody(org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
   */
  @Override
  protected void onComponentTagBody(final MarkupStream markupStream,
      final ComponentTag openTag) {
    previouslyAppendedOptGroupLabel = null;
    choices = getChoices().size();
    super.onComponentTagBody(markupStream, openTag);
  }

  private void addParentId(final AppendingStringBuffer tmp, final String id) {
    final int start = tmp.indexOf("<option");
    tmp.insert(start + 7, " optparentid=\"" + id + "\"");
  }

  /**
   * @param tmp
   */
  private void endOptGroup(final AppendingStringBuffer tmp) {
    // OptGroup ended
    final int start = tmp.indexOf("</option>");
    tmp.insert(start + 9, "</optgroup>");
  }

  /**
   * @see org.apache.wicket.markup.html.form.IOnChangeListener#onSelectionChanged()
   */
  @Override
  public void onSelectionChanged() {
    // TODO Auto-generated method stub
  }
}
