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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.koodaripalvelut.common.wicket.behavior.MultiSelectBehavior;
import com.koodaripalvelut.common.wicket.components.IStyledChoiceRenderer;
import com.koodaripalvelut.common.wicket.components.ListMultipleChoiceWithStylingOptions;
import com.koodaripalvelut.common.wicket.webtest.BasePage;

public class MultiSelectPage extends BasePage {

  private static final long serialVersionUID = 1L;

  private static final List<? extends String> LIST =
    Arrays
    .asList(new String[] { null, "African Alligator", "American Ant", null,
                           "Antelope", "Ape", "Ass/Donkey", "Baboon", null,
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
                           "Turkey", "Turtle", "Water", "Weasel", "Whale"});

  private List<String> select;
  public MultiSelectPage() {

    final List<Person> persons = getPersons();

    final IChoiceRenderer<String> rend = new ChoiceRenderer<String>() {
      @Override
      public String getIdValue(final String object, final int index) {
        if (object == null) {
          return "";
        }
        return super.getIdValue(object, index);
      }
    };

    final IChoiceRenderer<Person> tristateRenderer =
      new PersonTreeStyledChoiceRenderer("name");

    final ListMultipleChoice<String> single =
      new ListMultipleChoice<String>("select",
          new Model<ArrayList<String>>(
              new ArrayList<String>()), LIST, rend);

    final ListMultipleChoice<String> multi =
      new ListMultipleChoice<String>("select", new Model<ArrayList<String>>(
          new ArrayList<String>()), LIST, rend);

    final ListMultipleChoice<String> filter =
      new ListMultipleChoice<String>("select",
          new Model<ArrayList<String>>(new ArrayList<String>()), LIST, rend);

    final IModel<ArrayList<Person>> model =
      new Model<ArrayList<Person>>(new ArrayList<Person>());


    final ListMultipleChoiceWithStylingOptions<Person> tristate =
      new ListMultipleChoiceWithStylingOptions("select", model, persons,
          tristateRenderer);

    single.add(new MultiSelectBehavior().single());
    multi.add(new MultiSelectBehavior());
    filter.add(new MultiSelectBehavior().filtering());
    tristate.add(new MultiSelectBehavior().tristate());

    add(new SimpleFeedbackFormPanel<String>("single", single));
    add(new SimpleFeedbackFormPanel<String>("multi", multi));
    add(new SimpleFeedbackFormPanel<String>("filter", filter));
    add(new SimpleFeedbackFormPanel<Person>("tristate", tristate));

    final List<String> LIST2 = new ArrayList<String>(LIST);
    LIST2.remove(0);
    final DropDownChoice<String> ddc =
      new DropDownChoice<String>("select", new PropertyModel<String>(this,
      "select"), LIST2);
    ddc.add(new MultiSelectBehavior().filtering().single());
    ddc.setNullValid(true);

    final Label rv = new Label("label");
    rv.setOutputMarkupId(true);

    final Form<String> form = new Form<String>("form") {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit() {
        final Model<String> model =
          new Model((Serializable) ddc.getDefaultModelObject());
        rv.setDefaultModel(model);
      }
    };

    form.add(new AjaxSubmitLink("save") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        final Model<String> model =
          new Model((Serializable) ddc.getDefaultModelObject());
        rv.setDefaultModel(model);
        target.addComponent(rv);
      }
    });

    add(rv);
    form.add(ddc);

    add(form);

    final DropDownChoice<String> ddc2 =
      new DropDownChoice<String>("select", new Model<String>(), LIST2);
    ddc2.add(new MultiSelectBehavior().filtering().single());

    final Label rv2 = new Label("label2");
    rv2.setOutputMarkupId(true);

    final Form<String> form2 = new Form<String>("form2") {
      @Override
      protected void onSubmit() {
        final Model<String> model =
          new Model((Serializable) ddc2.getDefaultModelObject());
        rv2.setDefaultModel(model);
      }
    };

    form2.add(new AjaxSubmitLink("save") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        final Model<String> model =
          new Model((Serializable) ddc2.getDefaultModelObject());
        rv2.setDefaultModel(model);
        target.addComponent(rv2);
      }
    });

    add(rv2);
    form2.add(ddc2);

    add(form2);

  }

  private class SimpleFeedbackFormPanel<T extends Serializable> extends Panel {

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
          final T modelObject = item.getModelObject();
          if (modelObject instanceof Person) {
            item.add(new Label("value", ((Person) modelObject).getName()));
          } else {
            item.add(new Label("value", String.valueOf(modelObject)));
          }
        }

      };

      final Form<T> form = new Form<T>("form");

      form.add(new AjaxSubmitLink("save") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(final AjaxRequestTarget target,
            final Form<?> form) {
          final Model<T> model =
            new Model((Serializable) choiceComp.getDefaultModelObject());
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

  private List<Person> getPersons() {
    final List<Person> persons = new ArrayList<Person>();
    persons.add(null);

    for (final String name : Arrays
        .asList(new String[] { "One", "Two", "Three" })) {
      final Person person = createPerson(name);
      persons.add(person);
      persons.addAll(createPersons(person));
    }

    return persons;
  }

  private List<Person> createPersons(final Person person1) {

    final List<Person> list = new ArrayList<Person>();

    list.add(createPerson("First", person1));

    final Person person2 = createPerson("Second", person1);

    list.add(createPerson("Third", person1));

    final Person person3 = createPerson("Fourth", person1);
    list.add(person3);

    for (final String name : Arrays.asList(new String[] { "I", "II" })) {
      list.add(createPerson(name, person2));
      list.add(createPerson(name, person3));
    }
    return list;
  }

  private Person createPerson(final String name) {
    return createPerson(name, null);
  }

  private Person createPerson(final String name, final Person person1) {

    final Person person2 = new Person();

    String pName = "";
    if (person1 != null) {
      pName = person1.getName() + " - ";
    }

    person2.setName(pName + name);
    person2.setParent(person1);

    return person2;
  }

}

class Person implements Serializable {

  private static final long serialVersionUID = 1L;
  private Person parent;
  private String name;

  public Person setParent(final Person parent) {
    this.parent = parent;
    return this;
  }

  public Person getParent() {
    return parent;
  }

  public Person setName(final String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }
}

class PersonTreeStyledChoiceRenderer extends ChoiceRenderer<Person> implements
IStyledChoiceRenderer<Person> {

  private static final long serialVersionUID = 1L;

  public PersonTreeStyledChoiceRenderer(final String displayExpression) {
    super(displayExpression);
  }

  @Override
  public String getOptGroupLabel(final Person person) {
    if (person == null) {
      return null;
    }
    final Person parent = person.getParent();
    return parent != null ? parent.getName() : null;
  }

  @Override
  public String getOptionCssClassName(final Person person) {
    return null;
  }

  @Override
  public String getIdValue(final Person object, final int index) {
    if (object == null) {
      // return super.getIdValue(object, index);
      return "";
    }
    return super.getIdValue(object, index);
  }

}
