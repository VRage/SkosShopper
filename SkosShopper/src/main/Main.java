package main;


import java.io.IOException;

import model.FusekiModel;
import controller.MainController;
import controller.MenuController;
import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
	


public class Main extends Application {
	
	public static Parent root;
	public static Scene scene;
	

	@Override
	public void start(Stage primaryStage) 
	{
		try {
			startup();
			Main.root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
			Main.scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
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
			MenuController.importOnthologyFile("./fuseki/Data/skostest01.ttl");
			MenuController.importOnthologyFile("./fuseki/Data/dani-ont2.ttl");
		} catch (NoDatasetGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoServerConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
