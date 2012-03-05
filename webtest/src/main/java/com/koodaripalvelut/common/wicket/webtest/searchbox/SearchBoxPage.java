package com.koodaripalvelut.common.wicket.webtest.searchbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.StringAutoCompleteRenderer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.koodaripalvelut.common.wicket.behavior.ChoiceSearchBoxBehavior;
import com.koodaripalvelut.common.wicket.webtest.BasePage;
import com.koodaripalvelut.common.wicket.webtest.MakesModels;
import com.koodaripalvelut.common.wicket.webtest.ModelsPanel;

public class SearchBoxPage extends BasePage {

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

  final List<String> nestedSelections = new ArrayList<String>();

  @SuppressWarnings("serial")
  public SearchBoxPage(){
    add(new DropDownChoice<String>("ddc", LIST)
        .add(new ChoiceSearchBoxBehavior()));

    // ==============================================================

    add(new ListChoice<String>("ddc2", LIST).add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(final Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_MODE, Mode.SEARCH_BUTTON);
        params.put(PARAM_SEARCH_LABEL, "onclick + case sensitive");
        params.put(PARAM_REGEX_OPTS, "");
      }
    }));

    // ==============================================================

    final Form<Void> form = new Form<Void>("form");
    add(form);

    final MakesModels makesModels = new MakesModels();

    final DropDownChoice<String> makes =
      new DropDownChoice<String>("makes", new PropertyModel<String>(
          makesModels, "selectedMake"), makesModels.getMakeChoices());

    final DropDownChoice<String> models =
      new DropDownChoice<String>("models", new Model<String>(), makesModels
          .getModelChoices());
    models.add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(final Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_POSITION, Position.BEFORE);
        // params.put(PARAM_MODE, Mode.FIELD_ONLY);
      }
    });

    final ModelsPanel modelsPanel = new ModelsPanel("models-panel", models);

    form.add(makes);
    form.add(modelsPanel);

    makes.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(final AjaxRequestTarget target) {
        target.addComponent(modelsPanel);
      }
    });

    // ==============================================================

    add(new DropDownChoice<String>("ddc3", LIST)
        .add(new ChoiceSearchBoxBehavior() {
          @Override
          protected void setOptions(final Map<String, Object> params) {
            super.setOptions(params);
            params.put(PARAM_PREFIX, "Prefix");
            params.put(PARAM_SUFFIX, "Suffix");
          }
        }));

    final WebMarkupContainer nestingParent =
      new WebMarkupContainer("nestingParent");

    final IModel<ArrayList<String>> choiceModel = new Model<ArrayList<String>>() {
      @Override
      public ArrayList<String> getObject() {
        return (ArrayList<String>) nestedSelections;
      }
    };

    final ListMultipleChoice<String> nestingChild =
      new ListMultipleChoice<String>("nestingChild", choiceModel, LIST);

    nestingParent.setOutputMarkupPlaceholderTag(true);
    nestingChild.add(new ChoiceSearchBoxBehavior());
    nestingChild.add(new OnChangeAjaxBehavior() {

      @Override
      protected void onUpdate(final AjaxRequestTarget target) {
        target.addComponent(nestingParent);
      }

    });

    add(nestingParent.add(nestingChild));

    // ==============================================================

    final TextField<String> myTextField = new TextField<String>("myTextField");
    @SuppressWarnings("unchecked")
    final AutoCompleteBehavior<String> autoComplete =
      new AutoCompleteBehavior<String>(StringAutoCompleteRenderer.INSTANCE,
          new AutoCompleteSettings().setPreselect(true)) {
//          new AutoCompleteSettings().setCacheDuration(10000).setPreselect(true)) {

      @Override
      protected Iterator<String> getChoices(final String input) {
        final List<String> filteredList = new ArrayList<String>();
        for(final String str : LIST){
          if(str.startsWith(input)){
            filteredList.add(str);
          }
        }
        return filteredList.iterator();
      }
    };
    myTextField.add(autoComplete);

    add(myTextField);

    // ==============================================================

    createSelectedIndexExample("");
    createSelectedIndexExample("-flicker");

  }

  private void createSelectedIndexExample(final String name) {

    final PersonModels personModel = new PersonModels(name);

    final ChoiceRenderer<Person> personRenderer =
      new ChoiceRenderer<Person>("name");

    // Creating components
    final ListMultipleChoice<Person> person = new ListMultipleChoice<Person>(
        "person" + name,new PropertyModel<ArrayList<Person>>(personModel,
        "selectedPersons"), personModel.getPersonList(), personRenderer);

    final ListMultipleChoice<Person> children = new ListMultipleChoice<Person>(
        "children" + name,new PropertyModel<ArrayList<Person>>(personModel,
        "selectedChildren"), personModel.getChildChoiceModel(),
        personRenderer);

    final ListMultipleChoice<Person> grandChildren = new ListMultipleChoice<Person>(
        "grandChildren" + name,new Model<ArrayList<Person>>(),
        personModel.getGrandChildChoiceModel(), personRenderer);


    // Adding components
    final Form<Person> personFrom = new Form<Person>("personFrom" + name);
    personFrom.add(person);
    personFrom.add(children);
    personFrom.add(grandChildren);
    add(personFrom);

    // Adding behaviors
    person.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(final AjaxRequestTarget target) {
        target.addComponent(children);
        target.addComponent(grandChildren);
      }
    });


    children.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(final AjaxRequestTarget target) {
        target.addComponent(children);
        target.addComponent(grandChildren);
      }
    });

    children.add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(final Map<String, Object> params) {
        super.setOptions(params);
        if (!name.equals("-flicker")) {
          params.put(PARAM_SELECTED_INDEX, 0);
        }
        params.put(PARAM_PREFIX, "<br />");
        params.put(PARAM_SUFFIX, "<br />");
      }
    });

    grandChildren.add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(final Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_PREFIX, "<br />");
        params.put(PARAM_SUFFIX, "<br />");
      }
    });
  }

}

class PersonModels implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int CHILDREN_COUNT = 20;

  private List<Person> selectedPersons = new ArrayList<Person>();
  private List<Person> selectedChildren = new ArrayList<Person>();

  private IModel<List<? extends Person>> childChoiceModel;
  private IModel<List<? extends Person>> grandChoiceChildModel;

  private IModel<List<? extends Person>> childModel;
  private IModel<List<? extends Person>> grandChildModel;

  public PersonModels(final String name) {


    setChildChoiceModel(new AbstractReadOnlyModel<List<? extends Person>>() {

      private static final long serialVersionUID = 1L;

      @Override
      public List<? extends Person> getObject() {
        if (selectedPersons == null || selectedPersons.size() == 0) {
          return Collections.emptyList();
        }
        final List<Person> result = new ArrayList<Person>();
        for (final Person person : selectedPersons) {
          result.addAll(person.getChildren());
        }

        if (name.equals("-flicker")) {
          selectedChildren.clear();
          selectedChildren.add(result.get(0));
        }
        result.iterator().next();
        return result;
      }

    });

    setGrandChildChoiceModel(new AbstractReadOnlyModel<List<? extends Person>>() {

      private static final long serialVersionUID = 1L;

      @Override
      public List<? extends Person> getObject() {
        if (selectedChildren == null || selectedChildren.size() == 0) {
          return Collections.emptyList();
        }
        final List<Person> result = new ArrayList<Person>();
        for (final Person person : selectedChildren) {
          result.addAll(person.getChildren());
        }
        return result;
      }

    });
  }

  public List<Person> getPersonList() {
    final List<Person> persons = new ArrayList<Person>();
    for (int i = 0; i < CHILDREN_COUNT; i++) {
      final Person person = new Person(String.valueOf(i));
      persons.add(person);
      person.setChildren(getChildren(person.getName()));
    }
    return persons;
  }

  private List<Person> getChildren(final String name) {
    final List<Person> children = new ArrayList<Person>();
    for (int i = 0; i < CHILDREN_COUNT; i++) {
      final Person child = new Person(name + "." + String.valueOf(i));
      children.add(child);
      child.setChildren(getGrandChildren(child.getName()));
    }
    return children;
  }

  private List<Person> getGrandChildren(final String name) {
    final List<Person> grandChildren = new ArrayList<Person>();
    for (int i = 0; i < CHILDREN_COUNT; i++) {
      final Person grandChild = new Person(name + "." + String.valueOf(i));
      grandChildren.add(grandChild);
    }
    return grandChildren;
  }

  public PersonModels setSelectedPersons(final List<Person> selectedPersons) {
    this.selectedPersons = selectedPersons;
    return this;
  }

  public List<Person> getSelectedPersons() {
    return selectedPersons;
  }

  public PersonModels setSelectedChildren(final List<Person> selectedChildren) {
    this.selectedChildren = selectedChildren;
    return this;
  }

  public List<Person> getSelectedChildren() {
    return selectedChildren;
  }

  public PersonModels setChildChoiceModel(
      final IModel<List<? extends Person>> childChoiceModel) {
    this.childChoiceModel = childChoiceModel;
    return this;
  }

  public IModel<List<? extends Person>> getChildChoiceModel() {
    return childChoiceModel;
  }

  public PersonModels setGrandChildChoiceModel(
      final IModel<List<? extends Person>> grandChoiceChildModel) {
    this.grandChoiceChildModel = grandChoiceChildModel;
    return this;
  }

  public IModel<List<? extends Person>> getGrandChildChoiceModel() {
    return grandChoiceChildModel;
  }

  public PersonModels setChildModel(
      final IModel<List<? extends Person>> childModel) {
    this.childModel = childModel;
    return this;
  }

  public IModel<List<? extends Person>> getChildModel() {
    return childModel;
  }

  public PersonModels setGrandChildModel(
      final IModel<List<? extends Person>> grandChildModel) {
    this.grandChildModel = grandChildModel;
    return this;
  }

  public IModel<List<? extends Person>> getGrandChildModel() {
    return grandChildModel;
  }

}

class Person implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private List<Person> children;

  public Person(final String name) {
    this.name = name;
  }

  public Person setName(final String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public Person setChildren(final List<Person> children) {
    this.children = children;
    return this;
  }

  public List<Person> getChildren() {
    return children;
  }
}
