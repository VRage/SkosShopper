package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.hp.hpl.jena.assembler.ImportManager;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;

import model.ModelFacadeTEST;
import model.ServerImporter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SparQlController implements Initializable {
	@FXML
	TextArea txtAreaQuery;
	@FXML
	TextArea txtAreaResult;
	@FXML
	Button SparQlSend;
	@FXML
	ChoiceBox<String> SparQlChoiceBox;
	QueryFactory QF = new QueryFactory();

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		SparQlChoiceBox.getItems().addAll("akt Model", "Server");
		SparQlChoiceBox.getSelectionModel().selectFirst();
	}

	@FXML
	private void buttonSendQuery(MouseEvent Event) {
		// TODO Auto-generated method stub

		if (txtAreaQuery.getText() != null) {
			if (SparQlChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
				try {
					txtAreaResult.setText("Enter");
					Query query = QF.create(txtAreaQuery.getText());
					QueryExecution qe = QueryExecutionFactory.create(query,
							ModelFacadeTEST.getOntModel());
					txtAreaResult.setText(ResultSetFormatter.asText(qe
							.execSelect()));

				} catch (Exception e) {
					// TODO: handle exception
					txtAreaResult.setText(e.getMessage());
				}
			}
			else
			{
				try {
					txtAreaResult.setText(ServerImporter.sendQuery(txtAreaQuery.getText()));
				} catch (Exception e) {
					// TODO: handle exception
					txtAreaResult.setText(e.getMessage());
				}
				
			}

		}

	}

	@FXML
	void TextAreaKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
		}

	}
}
