package com.koodaripalvelut.common.wicket.webtest;

import static org.apache.wicket.model.Model.of;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

//import com.koodaripalvelut.common.wicket.dropable.Droppable;
//import com.koodaripalvelut.common.wicket.dropable.MultiDraggable;

public class DropablePage extends BasePage {
  private static final String BR = "<br/>";
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("serial")
  public DropablePage(final PageParameters parameters) {
    setOutputMarkupId(true);

    add(new FeedbackPanel("feedback").setOutputMarkupId(true)
        .setEscapeModelStrings(false));

    final List<String> dragList =
      createList("Drag 1A", "Drag 2A", "Drag 1B", "Drag 2B");
    final List<String> dropList = createList("Drop 1", "Drop 2");

    final WebMarkupContainer container =
      new WebMarkupContainer("dragContainer");
    container.setOutputMarkupId(true);
    container.add(new ListView<String>("dragList",
        new LoadableDetachableModel<List<String>>() {

      @Override
      protected List<String> load() {
        return dragList;
      }

    }) {
      @Override
      protected void populateItem(final ListItem<String> item) {
        item.add(new Label("drag", item.getModelObject()))
        .add(
            new AttributeAppender("class",
                of("drag" + item.getIndex() % 2), " "));
//                .add(new MultiDraggable() {
//
//                  @Override
//                  protected void onDrop(final AjaxRequestTarget target,
//                      final Component comp) {
//                    final StringBuilder sb = new StringBuilder();
//                    sb.append(this);
//                    sb.append(BR);
//                    sb.append(" droppend on ");
//                    sb.append(comp);
//                    sb.append(BR);
//                    sb.append(" shiftKey: ");
//                    sb.append(isShiftKey());
//                    sb.append(BR);
//                    sb.append("ctrlKey: ");
//                    sb.append(isCtrlKey());
//                    sb.append(BR);
//                    sb.append("altKey: ");
//                    sb.append(isAltKey());
//                    sb.append(BR);
//                    info(String.valueOf(sb));
//                    target.addChildren(getPage(), FeedbackPanel.class);
//                  }
//                });
      }
    });
    add(container);
    add(new ListView<String>("dropList",
        new LoadableDetachableModel<List<String>>() {

      @Override
      protected List<String> load() {
        return dropList;
      }
    }) {
      @Override
      protected void populateItem(final ListItem<String> item) {
        item.add(new Label("drop", item.getModelObject()))
        .add(
            new AttributeAppender("class", Model.of("drop"
                + item.getIndex()), " "));
//                .add(new Droppable() {
//
//                  @Override
//                  protected void beforeDrop(final AjaxRequestTarget target,
//                      final Component comp) {
//                    info(comp + " will be dropped on " + this + " shiftKey: "
//                        + isShiftKey() + " ctrlKey: " + isCtrlKey() + " altKey: "
//                        + isAltKey());
//                    target.addChildren(getPage(), FeedbackPanel.class);
//                  }
//                });
      }
    });

    add(new Form<Void>("form").add(new AjaxButton("addElementLink") {
      @Override
      protected void onSubmit(final AjaxRequestTarget target, final Form<?> f) {
        final int size = dragList.size();
        dragList.add("Drag[" + size + "]");
        target.add(container);
        DropablePage.this.info("Element #" + size + " added successfully..!");
        target.addChildren(getPage(), FeedbackPanel.class);
      }

      @Override
      protected void onError(final AjaxRequestTarget target, final Form<?> form) {
      }
    }));
  }

  private List<String> createList(final String... values) {
    final List<String> listOfElements = new ArrayList<String>();
    for (final String e : values) {
      listOfElements.add(e);
    }
    return listOfElements;
  }

}
