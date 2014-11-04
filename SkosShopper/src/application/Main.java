package application;

import org.apache.log4j.Logger;
import org.apache.log4j.jmx.LoggerDynamicMBean;
import org.slf4j.impl.Log4jLoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
	


public class Main extends Application {
	
	public static final Logger log = Logger.getLogger(Main.class);
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			log.info("app started without errors");
		} catch(Exception e) {
			log.error(e, e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
