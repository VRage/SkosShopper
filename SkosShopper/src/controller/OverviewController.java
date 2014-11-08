package controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.FusekiModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;

public class OverviewController implements Initializable{
	
	
	@FXML
	private Button startStopFuseki;
	@FXML
	private Label fusekiStatus;


	public void initialize(URL fxmlPath, ResourceBundle resources) {
		assert startStopFuseki != null : "fx:id=\"startStopFuseki\" was not injected: check your FXML file";
		assert fusekiStatus != null : "fx:id=\"fusekiStatus\" was not injected: check your FXML file";
	}

	
	public void startStopFuseki(ActionEvent event) throws NoDatasetGraphException, NoServerConfigException
	{
		FusekiModel.startStopFuseki();
		
		boolean status = FusekiModel.getServerStatus();
		
		if(status)
		{
			startStopFuseki.setText("Stop");
			fusekiStatus.setText("running");
			fusekiStatus.setTextFill(Color.web("#33BB33"));
		} else if (!status) {
			startStopFuseki.setText("Start");
			fusekiStatus.setText("not running");
			fusekiStatus.setTextFill(Color.web("#BB3333"));
		}
	}
	
	
	public void checkServer(ActionEvent event)
	{

		boolean check = FusekiModel.checkActiveServer();
	}
	
	
	private void cif()
	{
	}

}
