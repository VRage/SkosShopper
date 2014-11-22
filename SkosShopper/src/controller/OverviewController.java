package controller;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.JOptionPane;

import model.FusekiModel;
import model.ImportManager;
import model.ModelFacade;
import model.ModelFacadeTEST;
import model.TempSparql;
import model.ModelFacadeTEST.ModelState;

import org.apache.jena.fuseki.Fuseki;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

import exceptions.fuseki_exceptions.NoDatasetAccessorException;
import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;

public class OverviewController implements Initializable{
	
	
	@FXML	private Button startStopFuseki;
	@FXML Button btnHome;
	@FXML	private Label fusekiStatus;
	@FXML Label lblIndividuals;
	@FXML Label lblObjektProperties;
	@FXML Label lblDataProperties;
	@FXML Label lblClasses;
	@FXML Label OverviewlblState;
	@FXML WebView webView;
	@FXML TextField txtFieldURL;
	@FXML ChoiceBox OverviewChoiceBoxSource;
	@FXML TextField OverviewtxtField;
	@FXML Button OverviewbtnLoadFromStorage;
	File localFile= null;
	@FXML ListView OverviewListView;
	
	WebEngine webEngine;
	WebHistory webHistory;
	String port = "3030";
	String initURL ="http://localhost:"; 
	String url = initURL+port;
	ModelState pickedState = ModelState.FUSEKI;

	
	public static final Logger log = Logger.getLogger(SkosEditorController.class);
	private OntModel model = ModelFactory.createOntologyModel();
	private ArrayList<Entry> browserHistory = new ArrayList<WebHistory.Entry>();
	private ArrayList<OntClass> ObjektProperties = new ArrayList<OntClass>();
	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();
	private ArrayList<DatatypeProperty> data_indi = new ArrayList<DatatypeProperty>();
	//start function
	@SuppressWarnings("unchecked")
	public void initialize(URL fxmlPath, ResourceBundle resources) {
		assert startStopFuseki != null : "fx:id=\"startStopFuseki\" was not injected: check your FXML file";
		assert fusekiStatus != null : "fx:id=\"fusekiStatus\" was not injected: check your FXML file";
//		setLabels();
		webEngine = webView.getEngine();
		webEngine.load(url);
		txtFieldURL.setText(url);
		OverviewChoiceBoxSource.getItems().addAll(ModelFacadeTEST.getStates());
		OverviewChoiceBoxSource.getSelectionModel().selectFirst();
		OverviewChoiceBoxSource.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> Item,
					String arg1, String newValue) {
				// TODO Auto-generated method stub
				switch (newValue) {
				case "FUSEKI":
					OverviewtxtField.setVisible(false);
					OverviewbtnLoadFromStorage.setVisible(false);
					pickedState = ModelState.FUSEKI;
					break;
				case "WEB":
					OverviewtxtField.setVisible(true);
					OverviewbtnLoadFromStorage.setVisible(false);
					pickedState=ModelState.WEB;
				break;
				case "LOCAL":
					OverviewtxtField.setVisible(false);
					OverviewbtnLoadFromStorage.setVisible(true);
					pickedState=ModelState.LOCAL;
					
				break;

				default:
					break;
				}
			}
			

		});
		
		
		
		webHistory = webEngine.getHistory();
		webHistory.setMaxSize(3);
		webHistory.getEntries().addListener(new 
				ListChangeListener<WebHistory.Entry>(){

					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Entry> c) {
						// TODO Auto-generated method stub
						c.next();
						for (Entry e : c.getAddedSubList()) {
							browserHistory.add(e);
							url =e.getUrl();
							txtFieldURL.setText(url);
							System.out.println(e.getUrl());
						}
					}
			
		});
		setLabels();
	}
	@FXML private void backButtonOnAction(ActionEvent event ){
		url = browserHistory.get(browserHistory.size()-2).getUrl();
		System.out.println("URRRRL"+url);
		for (Entry e : browserHistory) {
			System.out.println("LOLOL"+e.getUrl());
		}
		System.out.println();
		webEngine.load(url);
		txtFieldURL.setText(url);
		
	}
	private void onHistoryChanged (){
		
	}
	@FXML private void btnHomeOnAction(ActionEvent event){
		webEngine.load(initURL+port);
		txtFieldURL.setText(initURL+port);
	}
	@FXML private void OverviewBtnLoadDataFromStorageOnAction(ActionEvent event){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		localFile = fileChooser.showOpenDialog(null);
	}
	@FXML private void OverviewbtnReloadDatasetOnAction(ActionEvent event) throws Exception {
		boolean ERROR =false;
		String errorMessage="";
		switch (pickedState) {
		case FUSEKI:
			try {
				ImportManager.setURI(txtFieldURL.getText()); 
				Model m = ImportManager.getDataAccesor().getModel("http://example.org/book/");
				ImportManager.importServer();
				this.model = ModelFactory.createOntologyModel(ImportManager.getOntModelSpec());
				this.model.add(m);
				
				
				ModelFacadeTEST.setModel(m);
			} catch (NoDatasetAccessorException e) {
				// TODO Auto-generated catch block
				ERROR =true;
				errorMessage = e.getMessage();
				e.printStackTrace();
			}
			ModelFacadeTEST.setState(ModelState.FUSEKI);
			
			break;
		case WEB:
			try {
				ModelFacadeTEST.loadModelFromWeb(OverviewtxtField.getText());
				ModelFacadeTEST.setState(ModelState.WEB);
			} catch (Exception e) {
				// TODO: handle exception
				ERROR =true;
				errorMessage = e.getMessage();
			}
			
		break;
		case LOCAL:
			try {
				ModelFacadeTEST.loadModelFromLocal(URLEncoder.encode(localFile.getAbsolutePath()));
				ModelFacadeTEST.setState(ModelState.LOCAL);
			} catch (Exception e) {
				// TODO: handle exception
				ERROR =true;
				errorMessage =e.getMessage();
			}
			
		break;

		default:
			break;
		}
		Stage dialog = new Stage();
		if(ERROR){
	
			JOptionPane.showMessageDialog(
				    null, errorMessage,"FEHLER",
				    
				    JOptionPane.ERROR_MESSAGE);
		}
		else {
			
			JOptionPane.showMessageDialog(null, ModelFacadeTEST.modelToString());
		}
		
		setLabels();
		setListView();
		
		
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
	



	private void setLabels() {
		// TODO Auto-generated method stub
		//ModelFacadeTEST.modelToString();
		OverviewlblState.setText(ModelFacadeTEST.aktState.toString().toLowerCase());
	}
	private void setListView() {
		// TODO Auto-generated method stub
		OverviewListView.setItems(ModelFacadeTEST.Strins());
	}
	public void loadTriplesFromServer(ActionEvent event)
	{
		boolean serverStarted = FusekiModel.getServerStatus();
		
		
		Model model = ModelFacade.getAllTriples();
		//Model model = ModelFacade.getModelActive();			von Matze, weil error gab hab ich mal auskommentiert und das dr�ber hin gemacht, 13.11.2014, 17:43 Uhr
		String tString = model.toString();
		StmtIterator stmti = model.listStatements();
		
		while(stmti.hasNext())
		{
			Statement stmt = stmti.nextStatement();

		//ModelFacade.getModelActive();							von Matze, weil error gab hab ich mal auskommentiert, 13.11.2014, 17:43 Uhr
		}
	}
	
	

}
