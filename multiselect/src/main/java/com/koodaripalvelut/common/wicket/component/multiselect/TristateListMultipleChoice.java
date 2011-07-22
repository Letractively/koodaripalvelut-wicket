package com.koodaripalvelut.common.wicket.component.multiselect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

public class TristateListMultipleChoice<T> extends
AbstractListMultipleChoice<Object> {

  private static final long serialVersionUID = 1L;
  private static final String START_OPTION_FLAG = "startOpt";
  private static final String END_OPTION_FLAG = "endOpt";

  private Map<Integer, ItemWraper> choiceIndexMap =
    new HashMap<Integer, ItemWraper>();

  private final Map<String, List<? extends Object>> innerListMap =
    new HashMap<String, List<? extends Object>>();

  private final RendererWraper renderer;

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String)
   */
  public TristateListMultipleChoice(final String id) {
    super(id);
    this.renderer = new RendererWraper();
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      List)
   */
  public TristateListMultipleChoice(final String id, final List<Object> choices) {
    super(id, choices);
    this.renderer = new RendererWraper();
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      List,IChoiceRenderer)
   */
  public TristateListMultipleChoice(final String id,
      final List<Object> choices, final IChoiceRenderer<T> renderer) {

    this(id, choices);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel, List)
   */
  public TristateListMultipleChoice(final String id,
      final IModel<? extends Collection<Object>> model,
          final List<Object> choices) {

    super(id, model, choices);
    this.renderer = new RendererWraper();
  }

  public TristateListMultipleChoice(final String id,
      final IModel<? extends Collection<Object>> object,
          final List<? extends T> choices, final IChoiceRenderer<T> renderer) {

    super(id, object, choices);
    this.renderer = new RendererWraper(renderer);
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel)
   */
  public TristateListMultipleChoice(final String id,
      final IModel<? extends List<Object>> choices) {

    super(id, choices);
    this.renderer = new RendererWraper();
  }

  /**
   * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
   *      IModel,IModel)
   */
  public TristateListMultipleChoice(final String id,
      final IModel<? extends Collection<Object>> model,
          final IModel<? extends List<Object>> choices) {

    super(id, model, choices);
    this.renderer = new RendererWraper();
  }

  public TristateListMultipleChoice(final String id,
      final IModel<? extends List<? extends Object>> choices,
          final IChoiceRenderer<T> renderer) {
    super(id, choices);

    this.renderer = new RendererWraper(renderer);
  }

  public TristateListMultipleChoice(final String id,
      final IModel<? extends Collection<Object>> model,
          final IModel<? extends List<Object>> choices,
              final IChoiceRenderer<T> renderer) {

    this(id, model, choices);
  }

  public IChoiceRenderer<T> getRenderer() {

    return this.renderer.getChoiceRenderer();
  }

  public void setRenderer(final IChoiceRenderer<T> renderer) {

    this.renderer.setChoiceRenderer(renderer);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void appendOptionHtml(final AppendingStringBuffer buffer,
      final Object choice, final int index, final String selected) {

    final T objectValue =
      (T) renderer.getChoiceRenderer().getDisplayValue((T) choice);
    final Class<T> objectClass =
      (Class<T>) (objectValue == null ? null : objectValue.getClass());

    String displayValue = "";
    if (objectClass != null && objectClass != String.class) {
      final IConverter converter = getConverter(objectClass);

      displayValue = converter.convertToString(objectValue, getLocale());
    } else if (objectValue != null) {
      displayValue = objectValue.toString();
    }
    buffer.append("\n<option ");
    if (isSelected(choice, index, selected)) {
      buffer.append("selected=\"selected\" ");
    }
    if (isDisabled(choice, index, selected)) {
      buffer.append("disabled=\"disabled\" ");
    }
    buffer.append("value=\"");
    buffer.append(Strings.escapeMarkup(renderer.getChoiceRenderer().getIdValue(
        (T) choice, index)));
    buffer.append("\">");

    String display = displayValue;
    if (localizeDisplayValues()) {
      display = getLocalizer().getString(displayValue, this, displayValue);
    }
    CharSequence escaped = display;
    if (getEscapeModelStrings()) {
      escaped = escapeOptionHtml(display);
    }
    buffer.append(escaped);
    buffer.append("</option>");
  }

  @Override
  protected void onComponentTagBody(final MarkupStream markupStream,
      final ComponentTag openTag) {
    final List<? extends Object> choices = getChoices();
    final AppendingStringBuffer buffer =
      new AppendingStringBuffer(choices.size() * 50 + 16);
    final String selected = getValue();

    buffer.append(getDefaultChoice(selected));

    choiceIndexMap = new HashMap<Integer, ItemWraper>();
    convertChoices(buffer, choices, selected, "0");

    buffer.append("\n");
    replaceComponentTagBody(markupStream, openTag, buffer);
  }

  private void convertChoices(final AppendingStringBuffer buffer,
      final List<? extends Object> list, final String selected,
      final String level) {

    innerListMap.put("0", list);

    final Comparator<ItemWraper> comparator = new Comparator<ItemWraper>() {

      @Override
      public int compare(final ItemWraper iw1, final ItemWraper iw2) {
        return iw1.compareTo(iw2);
      }
    };

    final Set<TristateListMultipleChoice<T>.ItemWraper> result =
      new ConcurrentSkipListSet<TristateListMultipleChoice<T>.ItemWraper>(
          comparator);

    while (innerListMap.size() > 0) {
      final String key = innerListMap.keySet().iterator().next();
      prepareList(result, innerListMap.get(key), key);
      innerListMap.remove(key);
    }

    int index = 0;
    for(final ItemWraper iw: result) {

      if (iw.getItem() != null) {

        choiceIndexMap.put(index, iw);
        appendOptionHtml(buffer, iw.getItem(), index, selected);
        index++;

      } else {

        final String flag = iw.getFlag();

        if (START_OPTION_FLAG.equals(flag)) {

          buffer.append("<option label=\"" + START_OPTION_FLAG + "\">");

        } else if (END_OPTION_FLAG.equals(flag)) {

          buffer.append("<option label=\"" + END_OPTION_FLAG + "\">");

        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void prepareList(
      final Set<TristateListMultipleChoice<T>.ItemWraper> result,
      final List<? extends Object> list, final String level) {

    result.add(new ItemWraper(null, level, START_OPTION_FLAG));
    int i = 0;
    for (; i < list.size(); i++) {
      final Object ele = list.get(i);

      if (ele instanceof List) {

        innerListMap.put(level + "." + i, (List<Object>) ele);

      } else {

        result.add(new ItemWraper((T) ele, level + "." + i));

      }
    }
    result.add(new ItemWraper(null, level + "." + i, END_OPTION_FLAG));
  }

  @Override
  protected List<Object> convertChoiceIdsToChoices(final String[] ids) {
    final ArrayList<ItemWraper> selectedValues = new ArrayList<ItemWraper>();

    // If one or more ids is selected
    if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
      // Get values that could be selected

      // Loop through selected indices
      for (int i = 0; i < ids.length; i++) {

        for (int index = 0; index < choiceIndexMap.size(); index++) {

          // Get next choice
          final ItemWraper choice = choiceIndexMap.get(Integer.valueOf(ids[i]));

          if (renderer.getIdValue(choice, index).equals(ids[i])) {

            selectedValues.add(choice);
            break;
          }
        }
      }
    }

    return convertChoicesId(selectedValues);
  }

  @SuppressWarnings("unchecked")
  private List<Object> convertChoicesId(
      final ArrayList<ItemWraper> values) {

    final List<Object> result = new ArrayList<Object>();

    innerListMap.put("0", result);


    for (final ItemWraper itemWraper : values) {

      String parentLevel = getParentLevel(itemWraper.getLevel());

      List<Object> list =
        (List<Object>) innerListMap.get(parentLevel);

      if (list == null) {

        list = new ArrayList<Object>();
        list.add(itemWraper.getItem());
        innerListMap.put(parentLevel, list);

        List<Object> parentList = null;


        while (true) {
          parentList =
            (List<Object>) innerListMap.get(getParentLevel(parentLevel));

          if (parentList == null) {
            parentList = new ArrayList<Object>();
            parentList.add(list);
            list = parentList;
            innerListMap.put(parentLevel, parentList);
            parentLevel = getParentLevel(parentLevel);
          } else {
            parentList.add(list);
            break;
          }
        }
      } else {
        list.add(itemWraper.getItem());
      }
    }

    innerListMap.clear();

    return result;

  }

  private String getParentLevel(final String level) {
    return level.substring(0, level.lastIndexOf("."));
  }

  @Override
  public String getModelValue() {

    final Collection<Object> selectedValues = getModelObject();
    final AppendingStringBuffer buffer = new AppendingStringBuffer();

    if (selectedValues != null) {

      for (final int index : choiceIndexMap.keySet()) {

        final ItemWraper object = choiceIndexMap.get(index);

        if (selectedValues.contains(object)) {

          buffer.append(renderer.getIdValue(object, index));
          buffer.append(VALUE_SEPARATOR);
        }
      }
    }
    return buffer.toString();
  }

  private class ItemWraper implements Serializable, Comparable<ItemWraper> {

    private static final long serialVersionUID = 1L;

    private final T item;
    private final String level;
    private final String flag;

    private ItemWraper(final T item, final String order) {
      this(item, order, "");
    }

    private ItemWraper(final T item, final String level, final String flag) {

      this.item = item;
      this.level = level;
      this.flag = flag;
    }

    public T getItem() {

      return this.item;
    }


    public String getLevel() {

      return this.level;
    }

    public String getFlag() {
      return flag;
    }

    @Override
    public String toString() {

      return level + ": " + item.toString();
    }

    @Override
    public int compareTo(final ItemWraper that) {
      final StringTokenizer thisSt = new StringTokenizer(this.level, ".");
      final StringTokenizer thatSt = new StringTokenizer(that.level, ".");

      while (thisSt.hasMoreTokens() && thatSt.hasMoreTokens()) {
        final String thisNextToken = thisSt.nextToken();
        final String thatNextToken = thatSt.nextToken();
        final int comparation =
          compareNumberValue(thisNextToken, thatNextToken);
        if (comparation < 0) {
          return -1;
        } else if (comparation > 0) {
          return 1;
        }
      }
      return thisSt.countTokens() < thatSt.countTokens() ? -1 : 1;
    }
  }

  private int compareNumberValue(final String n1, final String n2) {
    final int number1 = Integer.valueOf(n1);
    final int number2 = Integer.valueOf(n2);

    if (number1 < number2) {
      return -1;
    } else if (number2 < number1) {
      return 1;
    } else {
      return 0;
    }
  }

  private class RendererWraper implements IChoiceRenderer<ItemWraper> {

    private static final long serialVersionUID = 1L;

    private IChoiceRenderer<T> renderer;

    public RendererWraper(final IChoiceRenderer<T> renderer) {
      this.renderer = renderer;
    }

    public RendererWraper() {
      this.renderer = new ChoiceRenderer<T>();
    }

    @Override
    public Object getDisplayValue(final ItemWraper itemWraper) {
      return renderer.getDisplayValue(itemWraper.getItem());
    }

    @Override
    public String getIdValue(final ItemWraper itemWraper, final int index) {
      return renderer.getIdValue(itemWraper.getItem(), index);
    }

    public IChoiceRenderer<T> getChoiceRenderer() {
      return this.renderer;
    }

    public void setChoiceRenderer(final IChoiceRenderer<T> renderer) {
      this.renderer = renderer;
    }

  }

}
