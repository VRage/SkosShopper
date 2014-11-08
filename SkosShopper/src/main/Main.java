package main;


import java.io.IOException;

import controller.MainController;
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
			Main.root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
			Main.scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}