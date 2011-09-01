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
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<? extends List<? extends T>> choices,
      IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @param id
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<? extends List<? extends T>> choices) {
    super(id, choices);
  }

  /**
   * @param id
   * @param model
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<Collection<T>> model, IModel<? extends List<? extends T>> choices,
      IChoiceRenderer<? super T> renderer) {
    super(id, model, choices, renderer);
  }

  /**
   * @param id
   * @param model
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<Collection<T>> model, IModel<? extends List<? extends T>> choices) {
    super(id, model, choices);
  }

  /**
   * @param id
   * @param object
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<Collection<T>> object, List<? extends T> choices,
      IChoiceRenderer<? super T> renderer) {
    super(id, object, choices, renderer);
  }

  /**
   * @param id
   * @param object
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      IModel<Collection<T>> object, List<? extends T> choices) {
    super(id, object, choices);
  }

  /**
   * @param id
   * @param choices
   * @param renderer
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
    super(id, choices, renderer);
  }

  /**
   * @param id
   * @param choices
   * @param maxRows
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      List<? extends T> choices, int maxRows) {
    super(id, choices, maxRows);
  }

  /**
   * @param id
   * @param choices
   */
  public ListMultipleChoiceWithStylingOptions(String id,
      List<? extends T> choices) {
    super(id, choices);
  }

  /**
   * @param id
   */
  public ListMultipleChoiceWithStylingOptions(String id) {
    super(id);
  }
  
  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#appendOptionHtml(org.apache.wicket.util.string.AppendingStringBuffer, java.lang.Object, int, java.lang.String)
   */
  @Override
  protected void appendOptionHtml(AppendingStringBuffer buffer, T choice,
      int index, String selected) {
    AppendingStringBuffer tmp = new AppendingStringBuffer(50);
    super.appendOptionHtml(tmp, choice, index, selected);
    
    if (getChoiceRenderer() instanceof IStyledChoiceRenderer) {
      IStyledChoiceRenderer<T> styledChoiceRenderer = (IStyledChoiceRenderer<T>) getChoiceRenderer();

      String currentOptGroupLabel = styledChoiceRenderer.getOptGroupLabel(choice);

      if (currentOptGroupLabel == previouslyAppendedOptGroupLabel
          || currentOptGroupLabel != null
          && currentOptGroupLabel.equals(previouslyAppendedOptGroupLabel)) {
        // OptGroup changed
        if (previouslyAppendedOptGroupLabel != null) {
          endOptGroup(buffer);
        }
        
        if (currentOptGroupLabel != null) {
          // OptGroup started
          int start = tmp.indexOf("<option");
          StringBuilder label = new StringBuilder(currentOptGroupLabel.length() + 19);
          label.append("<optgroup label=\"").append(currentOptGroupLabel).append("\">");
          tmp.insert(start, label);
        }
      }

      if ((currentOptGroupLabel != null) && (index == (choices-1))) {
        // Last option group must end too
        endOptGroup(tmp);
      }

      {
        String cssClass = styledChoiceRenderer.getOptionCssClassName(choice);
        if (cssClass != null) {
          int start = tmp.indexOf("<option");
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
  protected void onComponentTagBody(MarkupStream markupStream,
      ComponentTag openTag) {
    previouslyAppendedOptGroupLabel = null;
    choices = getChoices().size();
    super.onComponentTagBody(markupStream, openTag);
  }

  /**
   * @param tmp
   */
  private void endOptGroup(AppendingStringBuffer tmp) {
    // OptGroup ended
    int start = tmp.indexOf("</option>");
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
