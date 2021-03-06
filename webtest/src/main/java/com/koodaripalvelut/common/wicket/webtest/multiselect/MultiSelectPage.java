package com.koodaripalvelut.common.wicket.webtest.multiselect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.koodaripalvelut.common.wicket.behavior.MultiSelectBehavior;
import com.koodaripalvelut.common.wicket.behavior.TristateMultiSelectBehavior;
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
    persons.add(0, null);
    persons.add(2, null);
    persons.add(6, null);

    final IChoiceRenderer<String> rend = new ChoiceRenderer<String>();

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

    final ListMultipleChoice<String> singleFilter =
      new ListMultipleChoice<String>("select", new Model<ArrayList<String>>(
          new ArrayList<String>()), LIST, rend);

    final ListMultipleChoiceWithStylingOptions<Person> tristate =
      new ListMultipleChoiceWithStylingOptions("select",
          new Model<ArrayList<Person>>(new ArrayList<Person>()), persons,
          tristateRenderer);

    final ListMultipleChoiceWithStylingOptions<Person> tristateFilter =
      new ListMultipleChoiceWithStylingOptions("select",
          new Model<ArrayList<Person>>(new ArrayList<Person>()), persons,
          tristateRenderer);

    final ListMultipleChoiceWithStylingOptions<Person> tristateSingle =
      new ListMultipleChoiceWithStylingOptions("select",
          new Model<ArrayList<Person>>(new ArrayList<Person>()), persons,
          tristateRenderer);

    final ListMultipleChoiceWithStylingOptions<Person> tristateSingleFilter =
      new ListMultipleChoiceWithStylingOptions("select",
          new Model<ArrayList<Person>>(new ArrayList<Person>()), persons,
          tristateRenderer);

    single.add(new MultiSelectBehavior().single());
    multi.add(new MultiSelectBehavior());
    filter.add(new MultiSelectBehavior().filtering());
    singleFilter.add(new MultiSelectBehavior().single().filtering());
    tristate.add(new TristateMultiSelectBehavior());
    tristateFilter.add(new TristateMultiSelectBehavior().filtering());
    tristateSingle.add(new TristateMultiSelectBehavior().single());
    tristateSingleFilter.add(new TristateMultiSelectBehavior().single()
        .filtering());

    add(new SimpleFeedbackFormPanel<String>("single", single));
    add(new SimpleFeedbackFormPanel<String>("multi", multi));
    add(new SimpleFeedbackFormPanel<String>("filter", filter));
    add(new SimpleFeedbackFormPanel<String>("singleFilter", singleFilter));
    add(new SimpleFeedbackFormPanel<Person>("tristate", tristate));
    add(new SimpleFeedbackFormPanel<Person>("tristateFilter", tristateFilter));
    add(new SimpleFeedbackFormPanel<Person>("tristateSingle", tristateSingle));
    add(new SimpleFeedbackFormPanel<Person>("tristateSingleFilter",
        tristateSingleFilter));


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

      final Form<T> form = new Form<T>("form") {
        @Override
        protected void onSubmit() {
          final Model<T> model =
            new Model((Serializable) choiceComp.getDefaultModelObject());
          rv.setDefaultModel(model);
        }
      };

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

      choiceComp.add(new AjaxFormComponentUpdatingBehavior("onchange") {
        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
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

    for (final String name : Arrays
        .asList(new String[] { "One", "Two", "Three" })) {
      final Person person = createPerson(name);
      persons.add(person);
      persons.addAll(createPersons(person));
    }

    return persons;
  }

  private List<Person> createPersons(final Person parent) {

    final List<Person> list = new ArrayList<Person>();

    list.add(createPerson("First", parent));

    final Person personSecond = createPerson("Second", parent);
    list.add(personSecond);

    list.add(createPerson("Third", parent));

    final Person personFourth = createPerson("Fourth", parent);
    list.add(personFourth);

    for (final String name : Arrays.asList(new String[] { "I", "II" })) {
      final Person personI = createPerson(name, personSecond);
      final Person personII = createPerson(name, personFourth);
      list.add(personI);
      list.add(personII);
      for (final String name2 : Arrays.asList(new String[] { "1", "2" })) {
        final Person person1 = createPerson(name2, personI);
        final Person person2 = createPerson(name2, personII);
        list.add(person1);
        list.add(person2);
        for (final String name3 : Arrays.asList(new String[] { "A", "B" })) {
          final Person personA = createPerson(name3, person1);
          final Person personB = createPerson(name3, person2);
          list.add(personA);
          list.add(personB);
          for (final String name4 : Arrays
              .asList(new String[] { "0001", "0010" })) {
            final Person person0001 = createPerson(name4, personA);
            final Person person0010 = createPerson(name4, personB);
            list.add(person0001);
            list.add(person0010);
          }
        }
      }
    }
    return list;
  }

  private Person createPerson(final String name) {
    return createPerson(name, null);
  }

  private Person createPerson(final String name, final Person parent) {

    final Person person2 = new Person();

    String pName = "";
    if (parent != null) {
      pName = parent.getName() + " - ";
    }

    person2.setName(pName + name);
    person2.setParent(parent);

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
  public String getOptionCssClassName(final Person person) {
    return null;
  }

  @Override
  public String getOptGroupLabel(final Person person) {

    if (person == null) {
      return null;
    }

    final String optGroup =
      person.getParent() == null ? null : person.getParent().getName();

    return optGroup;

  }

  @Override
  public String getIdValue(final Person object, final int index) {
    return super.getIdValue(object, index);
  }

}
