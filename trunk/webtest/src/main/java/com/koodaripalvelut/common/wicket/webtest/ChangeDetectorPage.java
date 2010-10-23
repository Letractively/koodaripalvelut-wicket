package com.koodaripalvelut.common.wicket.webtest;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.koodaripalvelut.common.wicket.behavior.UnsavedChangeDetectorBehavior;

public class ChangeDetectorPage extends BasePage {

  @SuppressWarnings("unused")
  private static class FormModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String text;

    public String getText() {
      return text;
    }

    public FormModel setText(String text) {
      this.text = text;
      return this;
    }

  }

  public ChangeDetectorPage() {

    final ExternalLink googleLink =
        new ExternalLink("google-link", "http://www.google.com",
            "Google (white list member)");

    final ExternalLink gmailLink =
        new ExternalLink("gmail-link", "http://www.gmail.com",
            "Gmail (black list member -- default)");

    final Form<FormModel> form =
        new Form<FormModel>("form", new CompoundPropertyModel<FormModel>(
            new FormModel()));
    form.add(new TextField<String>("text"));

    MakesModels makesModels = new MakesModels();

    final DropDownChoice<String> makes =
        new DropDownChoice<String>("makes", new PropertyModel<String>(
            makesModels, "selectedMake"), makesModels.getMakeChoices());

    final DropDownChoice<String> models =
        new DropDownChoice<String>("models", new Model<String>(), makesModels
            .getModelChoices());

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

    AjaxLink<Void> ajaxLink = new AjaxLink<Void>("ajax-link") {

      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {
        target.addComponent(form);
      }

    };
    form.add(ajaxLink);

    AjaxLink<Void> ajaxLink2 = new AjaxLink<Void>("ajax-link2") {

      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {
        target.addComponent(form);
      }

    };
    form.add(ajaxLink2);

    UnsavedChangeDetectorBehavior unsavedChangeDetectorBehavior =
        new UnsavedChangeDetectorBehavior();
    unsavedChangeDetectorBehavior.addToWhiteList(googleLink);
    unsavedChangeDetectorBehavior.addToBlackList(ajaxLink);
    unsavedChangeDetectorBehavior.addToBlackList(makes);
    form.add(unsavedChangeDetectorBehavior);

    add(form);

    add(googleLink);
    add(gmailLink);

  }

}
