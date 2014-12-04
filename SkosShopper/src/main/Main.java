package main;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
	


public class Main extends Application {
	
	public static Parent root;
	public static Scene scene;
	public static FXMLLoader loader;
	ResourceBundle bundleEN = ResourceBundle.getBundle("localization.bundle", Locale.forLanguageTag("en"));
	

	@Override
	public void start(Stage primaryStage) 
	{
		try {
			startup();
			loader= new FXMLLoader(getClass().getResource("/view/Main.fxml"), bundleEN);
			Main.root = loader.load();
			Main.scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
			primaryStage.setScene(scene);	
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	private static void startup()
	{
//		try {
//			FusekiModel.startStopFuseki();
//			MenuController.importOnthologyFile("./fuseki/Data/dani_ont1_02.ttl");
//			MenuController.importOnthologyFile("./fuseki/Data/dani_ont2_01.ttl");
//		} catch (NoDatasetGraphException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoServerConfigException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
	}

}
