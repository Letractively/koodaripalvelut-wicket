package com.koodaripalvelut.common.wicket.webtest.multimodal;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;

import com.koodaripalvelut.common.wicket.webtest.BasePage;

public class MultiModalPage extends BasePage {
  
  public MultiModalPage(){
    final ModalWindow modal = new IFrameModal();
    add(modal);
    final Panel actionPanel = new ActionPanel(){

      private static final long serialVersionUID = 1L;

      @Override
      public ModalWindow getModalWindow() {
        return null;
      }

      @Override
      public ModalWindow getNewModalWindow() {
        return modal;
      }
    };
    add(actionPanel);
    actionPanel.get("close").setVisible(false);
  }
  
  private class IFrameModal extends ModalWindow{

    private static final long serialVersionUID = 1L;
    
    private IFrameModal(){
      super("iFrameModal");
      setTitle("iFrameModal");
      setPageCreator(new PageCreator(){

        private static final long serialVersionUID = 1L;

        @Override
        public Page createPage() {
          return new InnerPage(){
            @Override
            public ModalWindow getModalWindow() {
              return IFrameModal.this;
            }
          };
        }
      });
    }
  }
  
  private abstract class InnerPage extends WebPage{
    @Override
    protected void onInitialize() {
      super.onInitialize();
      final ModalWindow newModal = new DivModal();
      add(newModal);
      add(new ActionPanel(){

        private static final long serialVersionUID = 1L;

        @Override
        public ModalWindow getModalWindow() {
          return InnerPage.this.getModalWindow();
        }

        @Override
        public ModalWindow getNewModalWindow() {
          return newModal;
        }
      });
    }
    public abstract ModalWindow getModalWindow();
  }
  
  private class DivModal extends ModalWindow {

    private static final long serialVersionUID = 1L;

    public DivModal() {
      super("divModal");
      setTitle("divModal");
      setContent(new InnerPanel(getContentId()){

        private static final long serialVersionUID = 1L;

        @Override
        public ModalWindow getModalWindow() {
          return DivModal.this;
        }
      });
    }

  }
  
  private abstract class InnerPanel extends Panel{
    
    private static final long serialVersionUID = 1L;
    public InnerPanel(final String id){
      super(id);
      final ModalWindow newModal = new IFrameModal();
      add(newModal);
      add(new ActionPanel(){

        private static final long serialVersionUID = 1L;

        @Override
        public ModalWindow getModalWindow() {
          return InnerPanel.this.getModalWindow();
        }

        @Override
        public ModalWindow getNewModalWindow() {
          return newModal;
        }
      });
    }
    public abstract ModalWindow getModalWindow();
  }
  
  private abstract class ActionPanel extends Panel{
    
    private static final long serialVersionUID = 1L;

    public ActionPanel(){
      super("actionPanel"); 
      add(new AjaxLink<Void>("close"){
        
        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(final AjaxRequestTarget target) {
          getModalWindow().close(target);
        }
      });
      
      add(new AjaxLink<Void>("open"){
        
        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(final AjaxRequestTarget target) {
          getNewModalWindow().show(target);
        }
      });
    }
    
    public abstract ModalWindow getModalWindow();
    public abstract ModalWindow getNewModalWindow();
  }
}
