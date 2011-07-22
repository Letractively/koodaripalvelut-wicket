package com.koodaripalvelut.common.wicket.component.multiselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.ValidationError;

public class ListMultipleChoiceWithNull<T> extends ListMultipleChoice<T> {

  private static final long serialVersionUID = 1L;

  public ListMultipleChoiceWithNull(final String id,
      final IModel<? extends Collection<T>> model,
          final List<? extends T> choices) {
    super(id, model, choices);
  }

  @Override
  protected CharSequence getDefaultChoice(final Object selected) {
    final StringBuilder sb = new StringBuilder("\n<option");
    if (selected == null || selected instanceof String
        && ((String) selected).trim().equals("")) {
      sb.append(" selected=\"selected\"");
    }
    final int length = getChoices().size();
    sb.append(" value=\"" + length + "\">multiselect-null-acceptance</option>");
    return sb.toString();

  }

  @Override
  protected void convertInput() {
    if (getType() == null) {
      try {
        if (getInputAsArray()[0] == null) {
          System.out.println("getInputAsArray()[0]");
        }
        if (getInputAsArray()[0].equals(String.valueOf(getChoices().size()))) {
          final List<T> list = new ArrayList<T>();
          list.add(null);
          setConvertedInput(list);
        } else {
          setConvertedInput(convertValue(getInputAsArray()));
        }
      } catch (final ConversionException e) {
        final ValidationError error = new ValidationError();
        if (e.getResourceKey() != null) {
          error.addMessageKey(e.getResourceKey());
        }
        if (e.getTargetType() != null) {
          error.addMessageKey("ConversionError."
              + Classes.simpleName(e.getTargetType()));
        }
        error.addMessageKey("ConversionError");
      }
    } else {
      super.convertInput();
    }

  }

}
