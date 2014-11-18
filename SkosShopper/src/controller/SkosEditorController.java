package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.swing.JOptionPane;

import main.Main;
import model.FusekiModel;
import model.ModelFacade;
import model.ModelFacadeTEST;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author MARDJ-Project
 *
 */

public class SkosEditorController implements Initializable {

	// Define FXML Elements
	@FXML
	private Button btn_addIndi;
	@FXML
	private Button btn_addLabel;
	@FXML
	private Label label_uri;
	@FXML
	private Label label_uri2;
	@FXML
	private TreeView tree_Classes;
	@FXML
	private ListView<String> listview_indi;
	@FXML
	private ListView<String> listview_objprop;
	@FXML
	private ListView<String> listview_dataprop;
	@FXML
	private Button btn_show;
	@FXML
	private Button btn_addProp;
	@FXML
	private Group grp_addProp;
	@FXML
	private ChoiceBox choicebox_properties;
	@FXML
	private ChoiceBox choicebox_indi;

	// local j4log logger
	public static final Logger log = Logger
			.getLogger(SkosEditorController.class);
	
	//Variables for the Ontology Class-Treeview
	private TreeItem<String> root;
	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	
	//Variables for the ListView, Individuals of a Class
	private ObservableList<String> items = FXCollections.observableArrayList();
	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();
	
	//Variables for the Dropdownmenu, ObjectProperties
	private ObservableList<String> props = FXCollections.observableArrayList();
	private ArrayList<String> propNS = new ArrayList<String>();
	
	//Variables for the Dropdownmenu, Individuals
	private ArrayList<String> indiNS = new ArrayList<String>();
	private ObservableList<String> indis = FXCollections.observableArrayList();
	
	//In this class used Ontology Model
	private OntModel model = ModelFactory
			.createOntologyModel( OntModelSpec.OWL_MEM);
	
	//Selected OntClass and Individual
	private static OntClass selectedOntClass;
	private Individual selectedIndividual;
	
	//Namespaces of the OntModel
	private String OntClassNS = "";
	private String baseNS = "";
	private String skosNS = "";
	private String skosxlNS = "";
	
	//Localized Resource Bundle
	private ResourceBundle localizedBundle;

	
	public void initialize(URL location, ResourceBundle resources) {
		root = new TreeItem<String>(resources.getString("SkosTreeView"));

		btn_addLabel.setDisable(true);
		grp_addProp.setDisable(true);
		btn_addIndi.setDisable(true);
		
		label_uri2.setText("");
		label_uri.setText(OntClassNS);
		
		listview_indi.setCursor(Cursor.HAND);
		tree_Classes.cursorProperty().set(Cursor.HAND);
		tree_Classes.setRoot(root);
		
		choicebox_indi.setItems(indis);
		choicebox_properties.setItems(props);
		listview_indi.setItems(items);
		localizedBundle = resources;
	}

	/**
	 * This function recognizes which ontology class the user has selected, displays the URI and
	 * call a function to get all Individual in this ontology class.
	 * 
	 * Also if no valid Class is selected you can't use buttons to add Individuals/Properties or Labels
	 * 
	 * @param e
	 */
	@FXML public void selectOntClass(MouseEvent e){
				TreeItem treeItem = (TreeItem) tree_Classes.getSelectionModel().getSelectedItem();
		if (treeItem != null) {
			if (!treeItem.getValue().toString().equals(localizedBundle.getString("SkosTreeView"))) {
				
				btn_addLabel.setDisable(false);
				grp_addProp.setDisable(false);
				btn_addIndi.setDisable(false);
				
				int index = tree_Classes.getSelectionModel().getSelectedIndex() - 1;
				OntClassNS = liste_classes.get(index).getNameSpace();
				label_uri.setText(OntClassNS);
				
				String s = treeItem.getValue().toString();
				selectedOntClass = model.getOntClass(OntClassNS + s);
				showIndividualsOfOntClass(selectedOntClass);

			}else{
				btn_addLabel.setDisable(true);
				grp_addProp.setDisable(true);
				btn_addIndi.setDisable(true);
			}
		}else{
			btn_addLabel.setDisable(true);
			grp_addProp.setDisable(true);
			btn_addIndi.setDisable(true);
		}
	}
	
	
	/**
	 * 
	 * This Method loads an Ontology which this Controller works with. Also it gets the Namespaces, Objectproperties and all Individuals of 
	 * the Ontology Model for the TreeView and Choiceboxes. 
	 * 
	 */
	public void loadOntology() {
//		if (model.isEmpty()) {
			try {
				// Path input =
				// Paths.get("C:\\Users\\VRage\\Documents\\SpiderOak Hive\\studium\\5_Semester\\projekt\\",
				// "test1.rdf");
				//
				// model.read(input.toUri().toString(), "RDF/XML");

				model.read("./fuseki/Data/test1.rdf");
//				 model = TripleModel.getAllTriples();
//				 Model m = FusekiModel.getDatasetAccessor().getModel();
//				 model.add(ModelFacadeTEST.getOntModel());

//				model = ModelFacadeTEST.getAktModel();
				
				baseNS = model.getNsPrefixURI("");
				log.info("Base NS set to: " + baseNS);
				skosNS = model.getNsPrefixURI("skos");
				log.info("Skos NS set to: " + skosNS);
				skosxlNS = model.getNsPrefixURI("skos-xl");
				log.info("Skosxl NS set to: " + skosxlNS);


				showOPropertiesInChoicebox();
				showAllIndividualsInChoicebox();
				showOntClassInTree();
			
				log.info("Ontologie loaded");
			} catch (Exception e) {
				log.error(e, e);
			}
//		}
	}

	
	/**
	 * 
	 * All skos and skosxl OntologyClasses will be shown in the TreeView by this method
	 * 
	 */
	public void showOntClassInTree() {
		ExtendedIterator<OntClass> classes = model.listClasses();
		while (classes.hasNext()) {
			OntClass thisClass = (OntClass) classes.next();
			String s = thisClass.toString();
			if (s.contains(skosNS) || s.contains(skosxlNS)) {
				liste_classes.add(thisClass);
			}
		}
		
		for (OntClass ontclass : liste_classes) {
			TreeItem<String> empLeaf = new TreeItem<String>(
					ontclass.getLocalName());
			// while(ontclass.listInstances().hasNext()){
			// Individual indi = (Individual) ontclass.listInstances().next();
			// TreeItem<String> childLeaf = new
			// TreeItem<String>(indi.getLocalName());
			// empLeaf.getChildren().add(childLeaf);
			// }
			root.getChildren().add(empLeaf);
		}
		log.info("class added to List");

	}

	/**
	 * 
	 * Method which will be executed after clicked on button btn_addIndi.
	 * Catches the User input and creates an Individual with the base namespace in the current selected Ontology Class
	 * 
	 * @param event
	 */
	@FXML
	private void addIndi(ActionEvent event) {
		Object antwort = JOptionPane.showInputDialog(null,
				localizedBundle.getString("EnterIndi"), localizedBundle.getString("InputWindow"),
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (antwort != null) {
			model.createIndividual(baseNS + ((String) antwort), selectedOntClass);
			int length = baseNS.length();
			String indinamespace = model.getIndividual(
					baseNS + ((String) antwort)).getNameSpace();
			
			/*
			 * Very complicated string operations, probably PhD needed to understand.
			 * 
			 * Recipe to add multiple Individuals with one string input. Furthermore adding "has narrower" to the created Individuals.
			 * 
			 */
			if (!(length == indinamespace.length())) {
				String superindi = indinamespace.substring(length,
						indinamespace.length() - 1);
				log.info(superindi);
				String[] stringarray = superindi.split("/");
				String newindi = baseNS;
				for (String ss : stringarray) {
					Individual tempindi = model.getIndividual(newindi + ss);
					if (tempindi == null) {
						model.createIndividual(newindi + ss, selectedOntClass);
						newindi = newindi + ss + "/";
						log.info("new individual added: " + ss);

					} else {
						newindi = tempindi.getURI() + "/";
					}
					log.info(ss);
				}
				newindi = baseNS;
				if (stringarray.length > 0) {
					for (int i = 0; i < stringarray.length; i++) {
						Individual tempindi = model.getIndividual(newindi
								+ stringarray[i]);
						ObjectProperty oProp = model.getObjectProperty(skosNS
								+ "narrower");
						if (i < stringarray.length - 1) {
							Individual nextindi = model
									.getIndividual(newindi + stringarray[i]
											+ "/" + stringarray[i + 1]);
							model.add(tempindi, oProp, nextindi);
						} else {
							Individual nextindi = model.getIndividual(baseNS
									+ ((String) antwort));
							model.add(tempindi, oProp, nextindi);
						}
						newindi = newindi + stringarray[i] + "/";
					}
				}
			}
			showAllIndividualsInChoicebox();
			showIndividualsOfOntClass(selectedOntClass);
		}
	}

	
	/**
	 * 
	 * Shows all Individuals of selected Ontology Class in a Listview
	 * 
	 * @param oclass
	 */
	public void showIndividualsOfOntClass(OntClass oclass) {
		
		if(oclass != null){			
			items.clear();
			liste_indi.clear();
			selectedIndividual = null;
			
			ExtendedIterator<? extends OntResource> indilist = oclass.listInstances();
			while (indilist.hasNext()) {
				Individual indivi = (Individual) indilist.next();
				liste_indi.add(indivi);
				items.add(indivi.getLocalName());
	
			}
		}else{
			items.clear();
			liste_indi.clear();
			selectedIndividual = null;
		}

	}

	/**
	 * 
	 * Debug Method, saves Ontology into a File (fuseki/Data/outputfromProgramm.owl)
	 * Will be deleted in deployed version.
	 * 
	 * @param event
	 */
	@FXML
	private void printToConsole(ActionEvent event) {
		try {
			File file = new File("./fuseki/Data/outputfromProgramm.owl");
			FileOutputStream out = new FileOutputStream(file);
			model.write(out, "RDF/XML");
			log.info("label hinzufügen");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	/**
	 * 
	 * Adds Label to the selected Individual. Executed after clicking the button btn_addLabel.
	 * 
	 * Recipe: 	Creates Individual in Label-Class and adding DataProperty "literalForm" with the Userinput as literal.
	 * 			Creates Object property "preferred Label" between created Label individual and selected individual.
	 * 			
	 * 
	 * @param event
	 */
	@FXML
	private void addLabel(ActionEvent event) {
		if ((selectedOntClass.getLocalName().equals("Concept"))
				&& (selectedIndividual != null)) {

			Object antwort = JOptionPane.showInputDialog(null,
					localizedBundle.getString("EnterLabel"), localizedBundle.getString("InputWindow"),
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (antwort != null) {
				String name = (String) antwort;
				model.getOntClass(skosxlNS + "Label").createIndividual(
						baseNS + "LabelFor" + name);
				Individual indi = model.getIndividual(baseNS + "LabelFor"
						+ name);
				DatatypeProperty dprop = model.getDatatypeProperty(skosxlNS
						+ "literalForm");
				log.info("datatypeProp" + dprop.getLocalName());
				indi.addProperty(dprop, model.createLiteral(name, "de"));
				ObjectProperty Oprop = model.getObjectProperty(skosxlNS
						+ "prefLabel");
				model.add(selectedIndividual, Oprop, indi);
			}
		}

	}

	/**
	 * 
	 * Method started from MouseClick on Individual of a Ontology Class ListView.
	 * Get the clicked Individual set it to the selectedIndividual and displays object and data property.
	 * Double click gives you the possibility to delete a Individual.
	 * 
	 * @param event
	 */
	/**
	 * @param event
	 */
	@FXML
	private void selectIndividualOfOntClass(MouseEvent event) {
		if (!listview_indi.getSelectionModel().isEmpty()) {
			selectedIndividual = model.getIndividual(liste_indi.get(
					listview_indi.getSelectionModel().getSelectedIndex())
					.getURI());
			label_uri2.setText(liste_indi.get(
					listview_indi.getSelectionModel().getSelectedIndex())
					.getURI());
			btn_addLabel.setDisable(false);
			grp_addProp.setDisable(false);
			showObjectProperties(selectedIndividual);
			showDataProperties(selectedIndividual);

			if (event.getClickCount() == 2) {
				String selected = model.getIndividual(
						liste_indi.get(
								listview_indi.getSelectionModel()
										.getSelectedIndex()).getURI())
						.getLocalName();
				int delete = JOptionPane.showConfirmDialog(null,
						localizedBundle.getString("delIndi")  + selected,
						localizedBundle.getString("delIndiWindow"), JOptionPane.YES_NO_OPTION);
				if (delete == JOptionPane.YES_OPTION) {
					model.getIndividual(
							liste_indi.get(
									listview_indi.getSelectionModel()
											.getSelectedIndex()).getURI())
							.remove();
					showIndividualsOfOntClass(selectedOntClass);
					showObjectProperties(selectedIndividual);
					showDataProperties(selectedIndividual);
				}
			}
		}
	}

	/**
	 * 
	 * Method of Button btn_addprop.
	 * Set a object property between selected Individual and selected individual of a choicebox.
	 * 
	 * @param event
	 */
	@FXML
	private void addProp(ActionEvent event) {
		if (!choicebox_properties.getSelectionModel().isEmpty()
				&& !choicebox_indi.getSelectionModel().isEmpty()) {
			int index_prop = choicebox_properties.getSelectionModel()
					.getSelectedIndex();
			String NSofprop = propNS.get(index_prop);
			ObjectProperty oProp = model.getObjectProperty(NSofprop);
			log.info("Objectpropertie selected: " + oProp.getNameSpace());
			int index_indi = choicebox_indi.getSelectionModel()
					.getSelectedIndex();
			String NSofindi = indiNS.get(index_indi);
			Individual individual = model.getIndividual(NSofindi);
			log.info("Individual selected: " + individual.getLocalName());
			model.add(selectedIndividual, oProp, individual);
		}

	}

	/**
	 * 
	 * Show all Object properties in a choicebox from loaded Ontology
	 * 
	 */
	private void showOPropertiesInChoicebox() {
		ExtendedIterator list_prop = model.listObjectProperties();
		while (list_prop.hasNext()) {
			ObjectProperty prop = (ObjectProperty) list_prop.next();
			props.add(prop.getLabel("en"));
			propNS.add(prop.getURI());
			log.info("property added: " + prop.getLocalName());
		}
	}

	private void showAllIndividualsInChoicebox() {
		indis.clear();
		indiNS.clear();
		ExtendedIterator list_indis = model.listIndividuals();
		while (list_indis.hasNext()) {
			Individual indi = (Individual) list_indis.next();
			indis.add(indi.getLocalName());
			log.info("Individual added: " + indi.getLocalName());
			indiNS.add(indi.getURI());
		}
	}

	/**
	 * 
	 * Displays all object properties of the selected Individual in a Listview.
	 * 
	 * @param selectedIndividual
	 */
	private void showObjectProperties(Individual selectedIndividual) {
		// Property Window ID: listview_dataprop
		listview_dataprop.setItems(null);
		if (selectedIndividual != null) {
			StmtIterator iterProperties = selectedIndividual.listProperties();
			ObservableList<String> items = FXCollections.observableArrayList();
			String predicate = "";
			String object = "";
			while (iterProperties.hasNext()) {
				Statement nextProperty = iterProperties.next();
				if (nextProperty.getPredicate().getNameSpace().equals(skosNS)
						|| nextProperty.getPredicate().getNameSpace()
								.equals(skosxlNS)) {
					try {

						if (nextProperty.getObject().isResource()) {
							predicate = model.getObjectProperty(
									nextProperty.getPredicate().getURI())
									.getLabel("en");
							object = nextProperty.getObject().asResource()
									.getLocalName();
							items.add("'" + predicate + "'" + "  " + object
									+ "\n\n");
						}

					} catch (ResourceRequiredException e) {
						log.error(e, e);
					}
				}
				if (!items.isEmpty()) {
					listview_dataprop.setItems(items);
				}
				// System.out.println(nextProperty);
				// System.out.println(nextProperty.getPredicate().getLocalName());
				// System.out.println(nextProperty.getObject().asResource().getLocalName());
				// System.out.println(skosNS);
			}
		}
	}

	/**
	 * 
	 * Displays all data properties of the selected Individual in a Listview.
	 * 
	 * @param selectedIndividual
	 */
	private void showDataProperties(Individual selectedIndividual) {
		// Property Window ID: listview_objprop
		listview_objprop.setItems(null);
		if (selectedIndividual != null) {
			StmtIterator iterProperties = selectedIndividual.listProperties();
			ObservableList<String> items = FXCollections.observableArrayList();
			String predicate = "";
			String object = "";
			while (iterProperties.hasNext()) {
				Statement nextProperty = iterProperties.next();
				if (nextProperty.getPredicate().getNameSpace().equals(skosNS)
						|| nextProperty.getPredicate().getNameSpace()
								.equals(skosxlNS)) {
					try {

						if (nextProperty.getObject().isLiteral()) {
							predicate = nextProperty.getPredicate()
									.getLocalName();
							object = nextProperty.getObject().asLiteral()
									.toString();
							items.add("'" + predicate + "'" + "  " + object
									+ "\n\n");
						}

					} catch (ResourceRequiredException e) {
						log.error(e, e);
					}
				}
				if (!items.isEmpty()) {
					listview_objprop.setItems(items);
				}
			}
		}
	}

}
