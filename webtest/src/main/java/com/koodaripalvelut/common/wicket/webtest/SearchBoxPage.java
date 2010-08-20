package com.koodaripalvelut.common.wicket.webtest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.koodaripalvelut.common.wicket.behavior.ChoiceSearchBoxBehavior;

public class SearchBoxPage extends BasePage {

  private static final List<String> LIST =
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
                                  "Whale" });

  @SuppressWarnings("serial")
  public SearchBoxPage() {

    add(new DropDownChoice<String>("ddc", LIST)
        .add(new ChoiceSearchBoxBehavior()));
    add(new ListChoice<String>("ddc2", LIST).add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_MODE, Mode.SEARCH_BUTTON);
        params.put(PARAM_SEARCH_LABEL, "onclick + case sensitive");
        params.put(PARAM_REGEX_OPTS, "");
      }
    }));

    Form<Void> form = new Form<Void>("form");
    add(form);

    MakesModels makesModels = new MakesModels();

    final DropDownChoice<String> makes =
        new DropDownChoice<String>("makes", new PropertyModel<String>(
            makesModels, "selectedMake"), makesModels.getMakeChoices());

    final DropDownChoice<String> models =
        new DropDownChoice<String>("models", new Model<String>(), makesModels
            .getModelChoices());
    models.add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_POSITION, Position.BEFORE);
        params.put(PARAM_MODE, Mode.FIELD_ONLY);
      }
    });

    final ModelsPanel modelsPanel = new ModelsPanel("models-panel", models);

    form.add(makes);
    form.add(modelsPanel);

    makes.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        target.addComponent(modelsPanel);
      }
    });

    add(new DropDownChoice<String>("ddc3", LIST)
        .add(new ChoiceSearchBoxBehavior() {
          @Override
          protected void setOptions(Map<String, Object> params) {
            super.setOptions(params);
            params.put(PARAM_PREFIX, "Prefix");
            params.put(PARAM_SUFFIX, "Suffix");
          }
        }));

  }

}
