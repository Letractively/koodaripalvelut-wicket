package com.koodaripalvelut.common.wicket.webtest.multiselect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.koodaripalvelut.common.wicket.behavior.MultiSelectBehavior;
import com.koodaripalvelut.common.wicket.behavior.TristateMultiSelectBehavior;
import com.koodaripalvelut.common.wicket.component.multiselect.ListMultipleChoiceWithNull;
import com.koodaripalvelut.common.wicket.component.multiselect.TristateListMultipleChoice;
import com.koodaripalvelut.common.wicket.webtest.BasePage;

public class MultiSelectPage extends BasePage {

  private static final long serialVersionUID = 1L;

  private static final List<? extends String> LIST =
    Arrays.asList(new String[] { "African Alligator", "American Ant",
                                 "Antelope", "Ape", "Ass/Donkey", "Baboon",
                                 "Badger", "Bat", "Bear", "Beaver", "Bee",
                                 "Bison", "Boar", "Butterfly", "Camel", "Cat",
                                 "Cattle[note", "Chamois", "Cheetah",
                                 "Chicken", "Cobra", "Cockroach", "Cormorant",
                                 "Coyote", "Crab", "Crane", "Crocodile",
                                 "Crow", "Deer", "Dog", "Dogfish", "Dolphin",
                                 "Donkey/Ass", "Dove", "Duck", "Eagle",
                                 "Echidna", "Eel", "Eland", "Elephant",
                                 "Elephant", "Elk", "Falcon", "Ferret",
                                 "Finch", "Fly", "Fox", "Frog", "Gazelle",
                                 "Gerbil", "Giant", "Giraffe", "Gnu", "Goat",
                                 "Goose", "Gorilla", "silverback", "Guanaco",
                                 "Guinea", "Gull", "Hamster", "Hare", "Hawk",
                                 "Hedgehog", "Heron", "Hippopotamus",
                                 "Hornet", "Horse[note", "Human", "Hyena",
                                 "Jackal", "Jaguar", "Jellyfish", "Kangaroo",
                                 "Kudu", "Lark", "Leopard", "Lion", "Llama",
                                 "Lobster", "Louse", "Lyrebird", "Magpie",
                                 "Mallard", "Manatee", "Meerkat", "Mink",
                                 "Mole", "Monkey", "Moose", "Mosquito",
                                 "Mouse", "Mule", "Nightingale", "Oryx",
                                 "Ostrich", "Otter", "Owl", "Ox", "Oyster",
                                 "Panther", "Partridge", "Peafowl", "Pelican",
                                 "Pig[note", "Pigeon", "Pony", "Porcupine",
                                 "Rabbit", "Raccoon", "Rail", "Ram", "Rat",
                                 "Raven", "Red", "Reindeer", "Rhinoceros",
                                 "Sea", "Seal", "Seastar", "Shark", "Sheep",
                                 "Skunk", "Snail", "Snake", "Spider",
                                 "Squirrel", "Swan", "Tiger", "Toad",
                                 "Turkey", "Turtle", "Water", "Weasel",
    "Whale"});

  public MultiSelectPage(){

    final List<Object> l1 = new ArrayList<Object>();

    final List<String> l4 = new ArrayList<String>();
    l4.add("Chicken");
    l4.add("Cobra");
    l4.add("Cockroach");
    l4.add("Cormorant");

    final List<Object> l3 = new ArrayList<Object>();
    l3.add("Bison");
    l3.add("Boar");
    l3.add("Butterfly");
    l3.add("Camel");
    l3.add("Cat");
    l3.add(l4);

    final List<Object> l2 = new ArrayList<Object>();
    l2.add("Badger");
    l2.add("Bat");
    l2.add("Bear");
    l2.add("Beaver");
    l2.add("Bee");


    l2.add("African Alligator");
    l2.add("American Ant");
    l2.add("Antelope");
    l1.add(l2);
    l1.add(l3);

    final ListMultipleChoice<String> single =
      new ListMultipleChoice<String>("select",
          new Model<ArrayList<String>>(
              new ArrayList<String>()), LIST);

    final ListMultipleChoice<String> singleWithNull =
      new ListMultipleChoiceWithNull<String>("select",
          new Model<ArrayList<String>>(new ArrayList<String>()), LIST);

    final ListMultipleChoice<String> multi =
      new ListMultipleChoice<String>("select", new Model<ArrayList<String>>(
          new ArrayList<String>()), LIST);

    final ListMultipleChoice<String> filter =
      new ListMultipleChoice<String>("select",
          new Model<ArrayList<String>>(new ArrayList<String>()), LIST);

    final IModel<ArrayList<Object>> model = new Model<ArrayList<Object>>(
        new ArrayList<Object>());

    final TristateListMultipleChoice tristate =
      new TristateListMultipleChoice("select", model, l1);

    single.add(new MultiSelectBehavior().single());
    singleWithNull.add(new MultiSelectBehavior().single());
    multi.add(new MultiSelectBehavior());
    filter.add(new MultiSelectBehavior().filtering());
    tristate.add(new TristateMultiSelectBehavior().setSubListText("Sub-List"));

    add(new SimpleFeedbackFormPanel<String>("single", single));
    add(new SimpleFeedbackFormPanel<String>("singleWithNull", singleWithNull));
    add(new SimpleFeedbackFormPanel<String>("multi", multi));
    add(new SimpleFeedbackFormPanel<String>("filter", filter));
    add(new SimpleFeedbackFormPanel<Object>("tristate", tristate));

  }

  private class SimpleFeedbackFormPanel<T> extends Panel {

    private static final long serialVersionUID = 1L;


    public SimpleFeedbackFormPanel(final String id,
        final AbstractChoice<Collection<T>, T> choiceComp) {
      super(id);

      final WebMarkupContainer container =
        new WebMarkupContainer("listContainer");

      container.setOutputMarkupId(true);

      final ListView<T> rv = new ListView<T>("feedbackId") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void populateItem(final ListItem<T> item) {
          String value;

          if(item.getModelObject() == null) {
            value = "null";
          } else {
            value = item.getModelObject().toString();
          }
          item.add(new Label("value", value));
        }

      };

      final Form<T> form = new Form<T>("form");

      form.add(new AjaxSubmitLink("save") {

        @Override
        protected void onSubmit(final AjaxRequestTarget target,
            final Form<?> form) {
          final Model model =
              new Model((Serializable) choiceComp.getModelObject());
          rv.setDefaultModel(model);
          target.addComponent(container);
        }
      });

      form.add(choiceComp);
      container.add(rv);

      add(form);
      add(container);

    }

  }
}
