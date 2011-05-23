package com.koodaripalvelut.common.wicket.webtest.multiselect;

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
import com.koodaripalvelut.common.wicket.behavior.MultiSelectBehavior;
import com.koodaripalvelut.common.wicket.webtest.BasePage;
import com.koodaripalvelut.common.wicket.webtest.MakesModels;
import com.koodaripalvelut.common.wicket.webtest.ModelsPanel;

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

  @SuppressWarnings("serial")
  public MultiSelectPage(){
	 ListMultipleChoice<String> single = new ListMultipleChoice<String>("single", LIST);
	 ListMultipleChoice<String> multi = new ListMultipleChoice<String>("multi", LIST);
	 ListMultipleChoice<String> filter = new ListMultipleChoice<String>("filtered", LIST);
	 ListMultipleChoice<String> tristate = new ListMultipleChoice<String>("tristate", LIST);
	 
	 single.add(new MultiSelectBehavior().single());
	 multi.add(new MultiSelectBehavior());
	 filter.add(new MultiSelectBehavior().filtering());
	 tristate.add(new MultiSelectBehavior().triState());
	 
	 add(single);
     add(multi);
     add(filter);
     add(tristate);
  }
}
