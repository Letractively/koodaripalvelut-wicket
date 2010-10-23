package com.koodaripalvelut.common.wicket.webtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class MakesModels implements Serializable {

  private static final long serialVersionUID = 1L;

  private String selectedMake;

  private final Map<String, List<String>> modelsMap =
      new HashMap<String, List<String>>();

  private IModel<List<? extends String>> makeChoices;

  private IModel<List<? extends String>> modelChoices;

  public MakesModels() {
    modelsMap.put("AUDI", Arrays.asList(new String[] { "A4", "A6", "TT" }));
    modelsMap.put("CADILLAC", Arrays.asList(new String[] { "CTS", "DTS",
                                                          "ESCALADE", "SRX",
                                                          "DEVILLE" }));
    modelsMap.put("FORD", Arrays.asList(new String[] { "CROWN", "ESCAPE",
                                                      "EXPEDITION", "EXPLORER",
                                                      "F-150" }));

    makeChoices = new AbstractReadOnlyModel<List<? extends String>>() {

      private static final long serialVersionUID = 1L;

      @Override
      public List<String> getObject() {
        Set<String> keys = modelsMap.keySet();
        List<String> list = new ArrayList<String>(keys);
        return list;
      }

    };

    modelChoices = new AbstractReadOnlyModel<List<? extends String>>() {

      private static final long serialVersionUID = 1L;

      @Override
      public List<String> getObject() {
        List<String> models = modelsMap.get(selectedMake);
        if (models == null) {
          models = Collections.emptyList();
        }
        return models;
      }

    };
  }

  /**
   * @return Currently selected make
   */
  public String getSelectedMake() {
    return selectedMake;
  }

  /**
   * @param selectedMake The make that is currently selected
   */
  public void setSelectedMake(String selectedMake) {
    this.selectedMake = selectedMake;
  }

  public IModel<List<? extends String>> getMakeChoices() {
    return makeChoices;
  }

  public IModel<List<? extends String>> getModelChoices() {
    return modelChoices;
  }

}
