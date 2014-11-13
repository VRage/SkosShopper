package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import localozation.Language;
import main.Main;
import model.FusekiModel;
import model.LanguageModel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

import exceptions.fuseki_exceptions.NoDatasetAccessorException;
import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;

public class MenuController implements Initializable{
	
	@FXML Menu menuFile;
	@FXML MenuItem menuItemClose;
	@FXML MenuItem menuItemDelete;

	@FXML Menu menuLanguage;
	@FXML MenuItem menuItemEN;
	@FXML MenuItem menuItemGER;

	@FXML Menu menuHelp;
	@FXML MenuItem menuItemAbout;

	@FXML MenuItem settingsItem;
	@FXML MenuItem importOnthologyFileItem;
	@FXML MenuItem onStartItem;
	
	
	
	public void initialize(URL fxmlPath, ResourceBundle resources) {
		assert importOnthologyFileItem != null : "fx:id=\"importOnthologyFileItem\" was not injected: check your FXML file";
	}
	
	
	public void importOnthologyFile(ActionEvent e) throws NoDatasetGraphException, NoServerConfigException
	{
		
		System.out.println("Import Onthology File");
	    FileChooser chooser = new FileChooser();
	    chooser.setTitle("Open File");
	    File file = chooser.showOpenDialog(Main.scene.getWindow());
	    
	    System.out.println(file.getAbsolutePath());
	    
	    Model model = FileManager.get().loadModel(file.getAbsolutePath());
	    
	    try {
			FusekiModel.getDatasetAccessor().add(model);
		} catch (NoDatasetAccessorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void importOnthologyFile(String path)
	{
	    Model model = FileManager.get().loadModel(path);
	    
	    try {
			FusekiModel.getDatasetAccessor().add(model);
		} catch (NoDatasetAccessorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void showSettingsOnStart(ActionEvent e) throws NoDatasetGraphException, NoServerConfigException
	{
		System.out.println("show settings window");
		
		try {
			Parent popup = FXMLLoader.load(getClass().getResource("/view/SettingsStartup.fxml"));
			Scene popupScene = new Scene(popup);
			Stage popupStage = new Stage();
			popupStage.setScene(popupScene);
			popupStage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
        //Fill stage with content
		
	}
	
	@FXML
	private void setEnglish(){
		LanguageModel.setLanguage(Language.ENGLISH_enEN);
		updateMenuView();
	}
	
	@FXML
	private void setGerman(){
		LanguageModel.setLanguage(Language.GERMAN_deDE);
		updateMenuView();
	}
	
	/**
	 * because it is not possible to cast Menu or MenuItem from nodes given by
	 * scene.lookup(String #id) the Menubar must be changed directly in this controller!
	 */
	private void updateMenuView(){
		menuFile.setText(LanguageModel.getSelectedLanguage().menuFile);
		menuItemClose.setText(LanguageModel.getSelectedLanguage().menuItemClose);
		menuItemDelete.setText(LanguageModel.getSelectedLanguage().menuItemDelete);

		menuLanguage.setText(LanguageModel.getSelectedLanguage().menuLanguage);
		menuItemEN.setText(LanguageModel.getSelectedLanguage().menuItemEN);
		menuItemGER.setText(LanguageModel.getSelectedLanguage().menuItemGER);

		menuHelp.setText(LanguageModel.getSelectedLanguage().menuHelp);
		menuItemAbout.setText(LanguageModel.getSelectedLanguage().menuItemAbout);

		settingsItem.setText(LanguageModel.getSelectedLanguage().settingsItem);
		importOnthologyFileItem.setText(LanguageModel.getSelectedLanguage().importOnthologyFileItem);
		onStartItem.setText(LanguageModel.getSelectedLanguage().onStartItem);
		
	}
	
}
