package main;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.FusekiModel;
import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;
	


public class Main extends Application {
	
	public static Parent root;
	public static Scene scene;
	
	ResourceBundle bundleEN = ResourceBundle.getBundle("localization.bundle", Locale.forLanguageTag("en"));
	

	@Override
	public void start(Stage primaryStage) 
	{
		try {
			startup();
			Main.root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"), bundleEN);
			Main.scene = new Scene(root);
			primaryStage.setScene(scene);	
			primaryStage.show();
			
			//initialize all items with the correct values
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void stop() throws NoDatasetGraphException, NoServerConfigException
	{
		FusekiModel.startStopFuseki();
	}
	
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	private static void startup()
	{
		try {
			FusekiModel.startStopFuseki();
			MenuController.importOnthologyFile("./fuseki/Data/skostest03.ttl");
			//MenuController.importOnthologyFile("./fuseki/Data/skostest02.ttl");
			//MenuController.importOnthologyFile("./fuseki/Data/dani-ont2.ttl");
		} catch (NoDatasetGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoServerConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
