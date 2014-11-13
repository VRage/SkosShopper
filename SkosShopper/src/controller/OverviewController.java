package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import model.FusekiModel;
import model.ModelFacade;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import exceptions.fuseki_exceptions.NoDatasetGraphException;
import exceptions.fuseki_exceptions.NoServerConfigException;

public class OverviewController implements Initializable{
	
	
	@FXML
	private Button startStopFuseki;
	@FXML Button btnHome;
	@FXML
	private Label fusekiStatus;
	@FXML Label lblIndividuals;
	@FXML Label lblObjektProperties;
	@FXML Label lblDataProperties;
	@FXML Label lblClasses;
	@FXML WebView webView;
	@FXML TextField txtFieldURL;
	WebEngine webEngine;
	WebHistory webHistory;
	String port = "3030";
	String initURL ="http://localhost:"; 
	String url = initURL+port;
	

	
	public static final Logger log = Logger.getLogger(SkosEditorController.class);
	private OntModel model = ModelFactory.createOntologyModel();
	private String baseNS ="";
	private ArrayList<Entry> browserHistory = new ArrayList<WebHistory.Entry>();
	private ArrayList<OntClass> ObjektProperties = new ArrayList<OntClass>();
	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();
	private ArrayList<DatatypeProperty> data_indi = new ArrayList<DatatypeProperty>();
	//start function
	public void initialize(URL fxmlPath, ResourceBundle resources) {
		assert startStopFuseki != null : "fx:id=\"startStopFuseki\" was not injected: check your FXML file";
		assert fusekiStatus != null : "fx:id=\"fusekiStatus\" was not injected: check your FXML file";
		loadOntologie();
		setLabels();
		webEngine = webView.getEngine();
		webEngine.load(url);
		txtFieldURL.setText(url);
		
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
	private void loadOntologie(){
//		Path input = Paths.get("C:\\Users\\VRage\\Documents\\SpiderOak Hive\\studium\\5_Semester\\projekt\\", "test1.rdf");
//		
//		model.read(input.toUri().toString(), "RDF/XML");
		
		model.read("./fuseki/Data/test1.rdf");
//			Model m = FusekiModel.getDatasetAccessor().getModel();
//		model.add(m);
		baseNS = model.getNsPrefixURI("");

		
		log.info("started running through ontologie");
		int counter =0;
		
		ExtendedIterator classes = model.listClasses();
		while(classes.hasNext()){
			OntClass thisClass = (OntClass) classes.next();
			String s = thisClass.toString();
			if(s.contains("http://")){
				liste_classes.add(thisClass);
			}
			ExtendedIterator indi_list = thisClass.listInstances();
			while(indi_list.hasNext()){
				Individual indi = (Individual) indi_list.next();
				liste_indi.add(indi);
				log.info(indi.getClass());
			}
			
		}
		
//		ExtendedIterator DataTypes = model.listDataRanges();
//		log.info(DataTypes..toString());
//		while(DataTypes.hasNext()){
//			DatatypeProperty thisClass = (DatatypeProperty) classes.next();
//			String s = thisClass.toString();
//			if(s.contains("http://")){
//				data_indi.add(thisClass);
//			}
//		}
		log.info("ended running through ontologie");
		
	}
	private void setLabels() {
		// TODO Auto-generated method stub
		int tempAmount =0;
		
		lblClasses.setText(liste_classes.size()+"");
		lblIndividuals.setText(liste_indi.size()+"");
		//lblDataProperties.setText(data_indi.size()+"");
		//lblObjektProperties.setText(model.getIndividual(""));
		for (OntClass ontClass : liste_classes) {
			System.out.println(ontClass.getRDFType());
		}
	}
	public void loadTriplesFromServer(ActionEvent event)
	{
		boolean serverStarted = FusekiModel.getServerStatus();
		
		
		Model model = ModelFacade.getModelActive();
		String tString = model.toString();
		StmtIterator stmti = model.listStatements();
		
		while(stmti.hasNext())
		{
			Statement stmt = stmti.nextStatement();
			

		ModelFacade.getModelActive();
		}
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
	

}
