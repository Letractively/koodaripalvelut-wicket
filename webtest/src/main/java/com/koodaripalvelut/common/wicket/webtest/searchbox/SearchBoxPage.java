package com.koodaripalvelut.common.wicket.webtest.searchbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
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
    add(new ListChoice<String>("ddc2", LIST).add(new ChoiceSearchBoxBehavior() {
      @Override
      protected void setOptions(final Map<String, Object> params) {
        super.setOptions(params);
        params.put(PARAM_MODE, Mode.SEARCH_BUTTON);
        params.put(PARAM_SEARCH_LABEL, "onclick + case sensitive");
        params.put(PARAM_REGEX_OPTS, "");
      }
    }));

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
        params.put(PARAM_MODE, Mode.FIELD_ONLY);
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

    final IModel<? extends Collection<String>> choiceModel =
      (IModel<? extends Collection<String>>) (Object) Model.ofList(nestedSelections);

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

    final TextField<String> myTextField = new TextField<String>("myTextField");
    @SuppressWarnings("unchecked")
    final AutoCompleteBehavior<String> autoComplete =
      new AutoCompleteBehavior<String>(StringAutoCompleteRenderer.INSTANCE,
          new AutoCompleteSettings().setCacheDuration(10000).setPreselect(true)) {

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
  }
}
