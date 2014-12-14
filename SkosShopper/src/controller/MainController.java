package controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.ModelFacadeTEST;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController implements Initializable{
	
	@FXML public SkosEditorController skoseditorliteController;
	@FXML private TabPane mainTabPane;
	@FXML private Tab tabSKOSEditorLite;
	@FXML private Tab tabTriplesShow;
	@FXML private Tab tabProductCategoriesSettings;
	@FXML private Tab tabProductsShow;
	@FXML private Tab tabProductCreate;
	
	
	public void adminMode()
	{

	}
	
	private void hideNonImplemented(){
		mainTabPane.getTabs().remove(tabTriplesShow);
		mainTabPane.getTabs().remove(tabProductCategoriesSettings);
		mainTabPane.getTabs().remove(tabProductsShow);
		mainTabPane.getTabs().remove(tabProductCreate);
	}
	
	public void simpleMode()
	{
		System.out.println("Simple mode active");
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		hideNonImplemented();
		mainTabPane.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldTab, newTab) -> {
            if (newTab == tabSKOSEditorLite) {
            	try {
					skoseditorliteController.loadOntology();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            if(oldTab == tabSKOSEditorLite){
            	ModelFacadeTEST.notifyAllControllerEnd();
            	ModelFacadeTEST.notifyAllControllerSart();
            }
        });
		
	}



}
