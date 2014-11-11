package controller;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import model.FusekiModel;


public class SparQlController implements Initializable{
	@FXML TextArea txtAreaQuery;	
	@FXML TextArea txtAreaResult;
	@FXML Button SparQlSend;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	@FXML private void buttonSendQuery(MouseEvent Event) {
		// TODO Auto-generated method stub
		if(txtAreaQuery.getText()!= null)
			txtAreaResult.setText(FusekiModel.sendSparQLQuery(txtAreaQuery.getText()));
	}
}
