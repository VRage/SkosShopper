package controller;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.swing.JOptionPane;

import model.IndividualChoiceCell;
import model.IndividualSelectCell;
import model.IndividualofOntClassCell;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasoner;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author MARDJ-Project
 *
 */

public class SkosEditorController implements Initializable {

	public static OntModel endSKOSController() {
		return model;
	}
	public static void removeIndividual(Individual indi){
		if(indi != null){
			indi.remove();
		}else{
			log.error("Individual not found");
		}
	}
	public static void startSKOSController(OntModel ontModel) {
		model = ontModel;
	}
	// Define FXML Elements
	@FXML
	private Label selectedCollectionLabel;
	@FXML
	private TextField TextFieldLabelEditCollection;
	@FXML
	private Accordion accordionpane;
	@FXML
	private TitledPane acc_addIndi;
	@FXML
	private TitledPane acc_editLabel;
	@FXML
	private TitledPane acc_addCollection;
	@FXML
	private TitledPane acc_editCollection;
	@FXML
	private Label label_uri;
	@FXML
	private Label label_uri2;
	@FXML
	private ListView<String> listview_classes;
	@FXML
	private TreeView<Individual> treeview_indi;
	@FXML
	private ListView<String> listview_objprop;
	@FXML
	private ListView<String> listview_dataprop;
	@FXML
	private Button btn_show;
	@FXML
	private Button btn_addIndividual;
	@FXML
	private TextField txtfield_individiaulname;
	@FXML
	private TextField txtfield_IndiLabel0;
	@FXML
	private TextField txtfield_editLabel;
	@FXML
	private Button btn_editLabel;
	@FXML
	private Label selectedIndiLocalname;
	@FXML
	private Label labelCollectionFromText;
	@FXML
	private TextField textfieldCollectionName;
	@FXML
	private TextField textfieldCollectionLabelName;
	@FXML
	private ChoiceBox<String> choiseBoxCollectionFilter;
	@FXML
	private ChoiceBox<String> choiseBoxEditCollection;
	@FXML
	private ListView<Individual> listviewCollectionChoise;
	@FXML
	private ListView<Individual> listviewCollectionSelected;
	@FXML
	private ListView<Individual> listViewEditCollectionSelected;
	@FXML
	private ListView<Individual> listViewEditCollectionChoise;
	@FXML
	private VBox vboxAddPrefLabel;
	@FXML
	private ChoiceBox<String> choiceboxPrefLabel0;
	@FXML
	private Button btnAddPrefLabel0;
	@FXML
	private TextField txtfield_IndialtLabel;
	@FXML
	private TextArea txtarea_indiDescription;
	@FXML
	private TextField txtfield_imageURL;
	@FXML
	private ImageView imageConceptIndividual;
	@FXML
	private VBox vboxEditPrefLabel;
	@FXML
	private ChoiceBox<String> choiceboxeditLabel;
	@FXML
	private Button btnEditPrefLabel;
	@FXML
	private TextField txtfield_EditialtLabel;
	@FXML
	private SplitPane splitpane;
	@FXML
	private TextArea txtarea_EditDescription;
	@FXML
	private TextField txtfield_EditimageURL;
	@FXML
	private ImageView imageEditIndividual;
	// String constant only to make arkadius happy
	private static final String PREFIXLABEL = "LabelFor";
	private static final String COLLECTION = "Collection";
	
	private static final String NARROWER = "narrower";

	private static final String MEMBER = "member";

	private static final String TYPE = "type";
	public TreeItem<Individual> root;

	// local j4log logger
	public static final Logger log = Logger
			.getLogger(SkosEditorController.class);
	protected static int counter;
	protected static int cnt;

	private ObservableList<String> languages = FXCollections
			.observableArrayList("de", "en", "fr", "it", "ru", "pl");
	// Variables for the Ontology Class-Listview
	private ObservableList<String> classes = FXCollections
			.observableArrayList();

	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	// Variables for the ListView, Individuals of a Class
	private ObservableList<String> items = FXCollections.observableArrayList();

	public final Image defaultimage= new Image("file://"+"./drawable/DROP.png");

	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();

	// For Listview Individual choiced
	private ObservableList<Individual> liste_choicedindis = FXCollections
			.observableArrayList();
	private ObservableList<Individual> liste_choisedindisEditCollection = FXCollections
			.observableArrayList();

	private ObservableList<Individual> liste_selectedindis = FXCollections
			.observableArrayList();
	// Variables for the Dropdownmenu, Individuals
	private ObservableList<Individual> indis = FXCollections
			.observableArrayList();

	// In this class used Ontology Model
	private static OntModel model;
	// Selected OntClass and Individual
	public static OntClass selectedOntClass;
	private Individual selectedIndividual;
	// Namespaces of the OntModel
	private String OntClassNS = "";
	private String baseNS = "";

	public String skosNS = "";

	private String skosxlNS = "";

	private String dctNS = "";

	// Localized Resource Bundle
	private ResourceBundle localizedBundle;

	/**
	 * 
	 * Adds Label to the selected Individual. Executed after clicking the button
	 * btn_addLabel.
	 * 
	 * Recipe: Creates Individual in Label-Class and adding DataProperty
	 * "literalForm" with the Userinput as literal. Creates Object property
	 * "preferred Label" between created Label individual and selected
	 * individual.
	 * 
	 * 
	 * @param event
	 */

	@FXML
	public void addLabelInputfields(ActionEvent e) {
		log.info("In method: addLabelInputfields");
		//TODO refactoring
		if(acc_addIndi.isExpanded()){
			if (counter < languages.size()) {
				addTextfields(0, vboxAddPrefLabel,null);
			}
		}else if(acc_editLabel.isExpanded()){
			if(cnt< languages.size()){
				addTextfields(1, vboxEditPrefLabel,null);
			}
		}
	}

	public void addTextfields(int counter, VBox vbox, Literal liter){
		HBox newHBox = new HBox();
		TextField newTextfield = new TextField();
		Button newBtn = new Button();
		Pane newPane = new Pane();
		ChoiceBox<String> newChoiceBox = new ChoiceBox<String>();

		newChoiceBox.setItems(languages);
		newBtn.setId("btndeltxtfields");
		HBox.setHgrow(newTextfield, Priority.ALWAYS);
		newChoiceBox.setPrefWidth(144.0);
		newBtn.setPrefWidth(30.0);
		if(counter==0){
			newBtn.setOnAction((event) -> {
				vbox.getChildren().remove(newHBox);
				SkosEditorController.counter--;
				
			});
		}
		if(counter==1){
			newBtn.setOnAction((event) -> {
				vbox.getChildren().remove(newHBox);
				SkosEditorController.cnt--;
			});
		}
		if(liter!=null){
			newTextfield.setText(liter.getString());
			newChoiceBox.setValue(liter.getLanguage());
		}
		newTextfield.setPromptText(localizedBundle.getString("prefLabel"));
		newHBox.autosize();
		newTextfield.setPrefWidth(231.0);
		newTextfield.setMaxWidth(Region.USE_COMPUTED_SIZE);
		newPane.setPrefWidth(15.0);
		newHBox.getChildren().addAll(newTextfield, newPane, newChoiceBox,
				newBtn);
		vbox.getChildren().add(1, newHBox);
		if(counter==0)
			SkosEditorController.counter++;
		if(counter==1)
			cnt++;
	}
	
	private void addToExistingTreeView(TreeItem<Individual> root, TreeItem<Individual> item){
		Iterator<TreeItem<Individual>> children  = root.getChildren().iterator();
		boolean containsElemntAlready = false;
		while(children.hasNext()){
			TreeItem<Individual> child = children.next();
			child.setExpanded(true);
			if(child.getValue().getLocalName().equals(item.getValue().getLocalName())){ //item already exists -> go deeper jakob
				containsElemntAlready = true;
				if(item.getChildren().size() > 0){	
					log.info(child.getChildren().size());
					addToExistingTreeView(child, item.getChildren().get(0)); //there is only one element in this list or none
				}
			}
		}
		
		//Rekursion or not, thats the question
		if(containsElemntAlready == false){
			root.getChildren().add(item);
		}
	}

	/**
	 * Tries to create a new Collection with the given name as long as the name
	 * doesn't already exist.
	 */
	@FXML
	public void createCollection() {

		String collectionString = "/" + COLLECTION + "/";
		String collectionNameString = baseNS + collectionString
				+ textfieldCollectionName.getText();
		String collectionLabelString = baseNS + "LabelForCollection";
		// + textfieldCollectionName.getText();
		log.info(selectedOntClass == null);
		if (model.getIndividual(collectionNameString) == null
				&& selectedOntClass != null) {

			// Name must not be empty!
			if (!textfieldCollectionName.getText().equals("")) {
				// Collection must be seleted!
				if (selectedOntClass.getLocalName().equals(COLLECTION)) {
					log.info("Collection selected = true");
					// Add Individual
					model.createIndividual(collectionNameString,
							selectedOntClass);

					// optional Label
					if (!textfieldCollectionLabelName.getText().isEmpty()) {
						createLabelRecipe("prefLabel", collectionLabelString,
								textfieldCollectionLabelName.getText(), "de",
								model.getIndividual(collectionNameString));
					} else {
						log.info("No Label given");
					}

					// optional fill created collection with individuals and
					// collections
					insertElemsToCollectionRecipe(
							model.getIndividual(collectionNameString),
							listviewCollectionSelected);
				}
				// not implemented yet!
				else if (selectedOntClass.getLocalName().equals(
						"OrderedCollection")) {
					log.info("Ordered Collection selected = true \n not implemented yet!");
				} else {
					log.error("Neither Collection nor Ordered Collection selected!");
				}// end if-else case collection or ordered collection selected
			} else {
				log.error("Namefield Empty!");
			}// end if-case textfield empty check
		} else {
			log.error("Name for Collection already taken or No Ontclass selected!");
		} // end if-else-case collection name already exist
	}
	public void createDatapropertyNotation(String description, Individual indi) {

		DatatypeProperty dprop = model.getDatatypeProperty(skosNS + "notation");
		log.info("datatypeProp" + dprop.getLocalName());
		indi.addProperty(dprop, model.createLiteral(description));
	}
	
	/**
	 * 
	 * Method which will be executed after clicked on button btn_addIndi.
	 * Catches the User input and creates an Individual with the base namespace
	 * in the current selected Ontology Class
	 * 
	 * @param event
	 */
	@FXML
	private void createIndividualforConcept(ActionEvent event) {
		if(selectedOntClass != null){
			if (!txtfield_individiaulname.getText().isEmpty()) {
				log.info("start method addIndi");
				String antwort = txtfield_individiaulname.getText();
				antwort = antwort.replaceAll("\\s", "_");
				if (model.getIndividual(baseNS + (antwort)) == null) {
					model.createIndividual(baseNS + (antwort), selectedOntClass);
	
					if (!txtfield_IndiLabel0.getText().isEmpty()) {
						if(txtfield_IndiLabel0.getText().isEmpty()){
							txtfield_IndiLabel0.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						}else if ( choiceboxPrefLabel0 ==null){
							choiceboxPrefLabel0.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						}else{
							createLabelRecipe("prefLabel", "",
									txtfield_IndiLabel0.getText(),
									choiceboxPrefLabel0.getValue(),
									model.getIndividual(baseNS + (antwort)));
							txtfield_IndiLabel0.setStyle("");
							choiceboxPrefLabel0.setStyle("");
						}

						for (int i = 1; i < counter; i++) {
							HBox hboxx = (HBox) vboxAddPrefLabel.getChildren().get(
									i);
							TextField txtfield = (TextField) hboxx.getChildren()
									.get(0);
							@SuppressWarnings("unchecked")
							ChoiceBox<String> choicebox = (ChoiceBox<String>) hboxx.getChildren().get(2);
							if(txtfield.getText().isEmpty()){
								txtfield.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
							}else if ( choicebox.getValue() ==null){
								choicebox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
							}else{
								createLabelRecipe("prefLabel", "", txtfield.getText(),
										choicebox.getValue(),
										model.getIndividual(baseNS + (antwort)));
								choicebox.setStyle("");
								txtfield.setStyle("");
							}
						}
					}
					if (!txtfield_IndialtLabel.getText().isEmpty()) {
						createLabelRecipe("altLabel", "alternativ",
								txtfield_IndialtLabel.getText(),
								choiceboxPrefLabel0.getValue(),
								model.getIndividual(baseNS + (antwort)));
					}
					if (!txtfield_imageURL.getText().isEmpty()) {
						createDatapropertyNotation(txtfield_imageURL.getText(),
								model.getIndividual(baseNS + (antwort)));
					}
					if (!txtarea_indiDescription.getText().isEmpty()) {
						AnnotationProperty aprop = model
								.createAnnotationProperty(dctNS + "description");
						log.info("aprop is: " + aprop.toString());
						model.getIndividual(baseNS + (antwort)).addProperty(
								aprop,
								model.createLiteral(
										txtarea_indiDescription.getText(),
										choiceboxPrefLabel0.getValue()));
					}
					int length = baseNS.length();
					String indinamespace = model.getIndividual(baseNS + (antwort))
							.getNameSpace();
	
					/*
					 * Very complicated string operations, probably PhD needed to
					 * understand.
					 * 
					 * Recipe to add multiple Individuals with one string input.
					 * Furthermore adding "has narrower" to the created Individuals.
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
								model.createIndividual(newindi + ss,
										selectedOntClass);
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
								ObjectProperty oProp = model
										.getObjectProperty(skosNS + "narrower");
								if (i < stringarray.length - 1) {
									Individual nextindi = model
											.getIndividual(newindi + stringarray[i]
													+ "/" + stringarray[i + 1]);
									model.add(tempindi, oProp, nextindi);
								} else {
									Individual nextindi = model
											.getIndividual(baseNS
													+ ((String) antwort));
									model.add(tempindi, oProp, nextindi);
								}
								newindi = newindi + stringarray[i] + "/";
							}
						}
					}
					showAllIndividualsInChoicebox();
					showIndividualsOfOntClass(selectedOntClass);
	
					//Clear all Textfields
					txtfield_individiaulname.clear();
					for(Node node : vboxAddPrefLabel.getChildren()){
						if(node instanceof TextField){
							((TextField)node).clear();
						}
						if(node instanceof HBox){
							for(Node hNode : ((HBox)node).getChildren()){
								if(hNode instanceof TextField){
									((TextField)hNode).clear();	
								}
								if(hNode instanceof TextArea){
									((TextArea)hNode).clear();
								}
							}
						}
						if(node instanceof AnchorPane){
							for(Node hNode : ((AnchorPane)node).getChildren()){
								if(hNode instanceof TextField){
									((TextField)hNode).clear();	
								}
								if(hNode instanceof TextArea){
									((TextArea)hNode).clear();
								}
							}
						}
					}
					
					imageConceptIndividual.setImage(null);
				} else {
					log.info("Individual already exist");
				}
			}
		}
	}
	public void createLabelRecipe(String Objectproperty, String name,
			String description, String language, Individual toindividual) {
		String labelname = toindividual.getLocalName();
		model.getOntClass(skosxlNS + "Label").createIndividual(
				baseNS + PREFIXLABEL + name + labelname);
		Individual indi = model.getIndividual(baseNS + PREFIXLABEL + name
				+ labelname);
		DatatypeProperty dprop = model.getDatatypeProperty(skosxlNS
				+ "literalForm");
		log.info("datatypeProp" + dprop.getLocalName());
		indi.addProperty(dprop, model.createLiteral(description, language));
		ObjectProperty Oprop = model.getObjectProperty(skosxlNS
				+ Objectproperty);
		model.add(toindividual, Oprop, indi);

	}

	@FXML
	public void deleteFromCollection() {

	}

	@FXML
	public void displayImageFromURL(ActionEvent e) {
		imageConceptIndividual.setImage(new Image(txtfield_imageURL.getText()));
	}

	@FXML
	private void editLabel(ActionEvent e) {
		log.info("in editLabelmethod");
		if (selectedOntClass != null && selectedIndividual != null) {
			log.info("no null Class or Individual");
			log.info(selectedOntClass.getLocalName());
			if (selectedOntClass.getLocalName().contains("Concept")) {
				
				changeLabelofInvidiaul(selectedIndividual);
			} else if (selectedOntClass.getLocalName().contains("Label")) {
				Individual subjectOfLabel = null;
				Property oproppref = model.getProperty(skosxlNS+"prefLabel");
				Property opropalt = model.getProperty(skosxlNS+"altLabel");
				ResIterator resit = model.listSubjectsWithProperty(oproppref, selectedIndividual);
				while(resit.hasNext()){
					Resource res = (Resource) resit.next();
					subjectOfLabel = model.getIndividual(res.getURI());
					log.info("Subject of this Label is: "+subjectOfLabel);
				}
				ResIterator resit2 = model.listSubjectsWithProperty(opropalt, selectedIndividual);
				while(resit2.hasNext()){
					Resource res = (Resource) resit2.next();
					subjectOfLabel = model.getIndividual(res.getURI());
					log.info("Subject of this Label is: "+subjectOfLabel.getLocalName());
				}
				changeLabelofInvidiaul(subjectOfLabel);
				showIndividualsOfOntClass(selectedOntClass);
			}
			
			Reasoner res = new PelletReasoner();
			InfModel infmodel = ModelFactory.createInfModel(res, model);
			ValidityReport report =infmodel.validate();
			if(report.isValid()){
				log.info("Alles oki doki");
			}else{
				log.error("Model is not valid");
				Iterator<Report> iter = report.getReports();
				while(iter.hasNext()){
					log.info("Modelvalid error: "+iter.next());
				}
			}
		}
	}
	
	public void changeLabelofInvidiaul(Individual selectedIndividual){
		DatatypeProperty dprop = model.getDatatypeProperty(skosNS + "notation");
		DatatypeProperty dproplit = model.getDatatypeProperty(skosxlNS	+ "literalForm");

		//if not exist, create a Label
		if(getIndividualbyObjectProperty(selectedIndividual,"prefLabel") == null){
			createLabelRecipe("prefLabel", "",
					txtfield_editLabel.getText(),
					(choiceboxeditLabel != null) ? choiceboxeditLabel.getValue() : localizedBundle.getLocale().getLanguage(),
					selectedIndividual);
			log.info(getIndividualbyObjectProperty(selectedIndividual,"prefLabel"));

		}
		
		//Save changes for prefered Label
		if (getIndividualbyObjectProperty(selectedIndividual,"prefLabel") != null) {
			Individual labelindi = model.getIndividual(getIndividualbyObjectProperty(selectedIndividual,"prefLabel").getURI());
			selectedIndiLocalname.setText(getIndividualbyObjectProperty(selectedIndividual, "prefLabel").getLocalName());
			btn_editLabel.setText(localizedBundle.getString("btnEditLabel"));
			
				labelindi.removeAll(dproplit);
			
			for(Node node : vboxEditPrefLabel.getChildren()){
				if(node instanceof HBox){
					TextField tmpTextField = (TextField) ((HBox) node).getChildren().get(0);
					@SuppressWarnings("unchecked")
					ChoiceBox<String> tmpChoiceBox = (ChoiceBox<String>)((HBox)node).getChildren().get(2);
					
						if(tmpTextField.getText().isEmpty()){
							tmpTextField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						}else if ( tmpChoiceBox.getValue() ==null){
							tmpChoiceBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						}else{
					 	model.add(labelindi, dproplit, 
								tmpTextField.getText(), 
								(tmpChoiceBox != null) ? tmpChoiceBox.getValue() : localizedBundle.getLocale().getLanguage()	
								);
					 	tmpChoiceBox.setStyle("");
					 	tmpTextField.setStyle("");
						}
					
				}
			}
		}
		
		//save changes for alternate label
		if (getIndividualbyObjectProperty(selectedIndividual,"altLabel") != null) {
			if(!txtfield_EditialtLabel.getText().isEmpty())
				getDatapropertyFromLabel(getIndividualbyObjectProperty(selectedIndividual,"altLabel")).changeObject(txtfield_EditialtLabel.getText(), localizedBundle.getLocale().getLanguage());
			else
				model.getIndividual(getIndividualbyObjectProperty(selectedIndividual,"altLabel").getURI()).remove();
		}else {
			if(!txtfield_EditialtLabel.getText().isEmpty()){
				//create new alternate Label if not exist
				log.info("create new label");
				createLabelRecipe("altLabel", "alternate",
						txtfield_EditialtLabel.getText(), localizedBundle.getLocale().getLanguage(),
						selectedIndividual);
			}
		}
		
		//save changes for description
		Property aprop = model.getProperty(dctNS + "description");
		if(selectedIndividual.getProperty(aprop)!=null){
			if(!txtarea_EditDescription.getText().isEmpty())
				selectedIndividual.getProperty(aprop).changeObject(txtarea_EditDescription.getText());
			else
				selectedIndividual.removeAll(aprop);
		}else{
			if(!txtarea_EditDescription.getText().isEmpty()){
				//create description if not exist
				log.info("create new description");
				selectedIndividual.addProperty(aprop, model.createLiteral(txtarea_EditDescription.getText(), localizedBundle.getLocale().getLanguage()));
			}
		}
		
		//save changes for notation
		if(selectedIndividual.getProperty(dprop)!=null){
			if(!txtfield_EditimageURL.getText().isEmpty()){
				selectedIndividual.getProperty(dprop).changeObject(txtfield_EditimageURL.getText());
			}else{
				selectedIndividual.removeAll(dprop);
			}
		}else{
			if(!txtfield_EditimageURL.getText().isEmpty()){
				//create notation if not exist
				createDatapropertyNotation(txtfield_EditimageURL.getText(), selectedIndividual);
			}
		}
	}
	
	private TreeItem<Individual> generateSubTreeFromBehindLikeJakobLikesIt(OntClass oclass, Individual i, TreeItem<Individual> child){
		if(child == null) { //start of the recursion
			child = new TreeItem<Individual>();
			child.setValue(i);
			return generateSubTreeFromBehindLikeJakobLikesIt(oclass, i, child);
		}
		else{ //search for parents or return the final tree
			ExtendedIterator<? extends OntResource> elems = oclass
					.listInstances();
			while(elems.hasNext()){
				Individual current = (Individual) elems.next();
				ArrayList<Resource> narrowerElems = getIndividualsbyObjectProperty(current, NARROWER);
				if(narrowerElems != null && narrowerElems.contains(i)){
					TreeItem<Individual> parent = new TreeItem<Individual>();
					parent.setValue(current);
					parent.getChildren().add(child);
					return generateSubTreeFromBehindLikeJakobLikesIt(oclass, current, parent);
				}
			}
		}
		return child;
	}

	private Statement getDatapropertyFromLabel(Resource label) {
		StmtIterator iter = label.listProperties();
		while (iter.hasNext()) {
			Statement s = iter.next();
			log.info(s.getPredicate().getLocalName());
			if (s.getPredicate().getLocalName().equals("literalForm")) {
				return s;
			}
		}
		return null;
	}

	private Resource getIndividualbyObjectProperty(Individual individual,
			String objectproperty) {
		if (individual != null) {
			StmtIterator iter = individual.listProperties();
			while (iter.hasNext()) {
				Statement s = iter.next();
				if (s.getPredicate().getLocalName().equals(objectproperty)
						&& s.getObject().isResource()) {
					return s.getObject().asResource();
				}
			}
		}
		return null;
	}

	public ArrayList<Resource> getIndividualsbyObjectProperty(
			Individual individual, String objectproperty) {
		ArrayList<Resource> result = new ArrayList<Resource>();
		if (individual != null) {
			StmtIterator iter = individual.listProperties();
			while (iter.hasNext()) {
				Statement s = iter.next();
				if (s.getPredicate().getLocalName().equals(objectproperty)
						&& s.getObject().isResource()) {
					result.add(s.getObject().asResource());
				}
			}
		}
		if (result.isEmpty())
			return null;
		return result;
	}

	public void initialize(URL location, ResourceBundle resources) {
		localizedBundle = resources;
		counter = 1;
		cnt = 0;
		vboxAddPrefLabel.autosize();
		choiceboxPrefLabel0.setItems(languages);
		choiceboxeditLabel.setItems(languages);
		choiceboxPrefLabel0.setValue(localizedBundle.getLocale().getLanguage());
		btnAddPrefLabel0.setId("btnaddtxtfields");
		btnEditPrefLabel.setId("btnaddtxtfields");
		label_uri2.setText("");
		label_uri.setText(OntClassNS);

		treeview_indi.setCursor(Cursor.HAND);
		listview_classes.setCursor(Cursor.HAND);

		listview_classes.setItems(classes);

		listviewCollectionChoise.setItems(liste_choicedindis);
		listviewCollectionChoise
				.setCellFactory(new Callback<ListView<Individual>, ListCell<Individual>>() {
					@Override
					public ListCell<Individual> call(ListView<Individual> param) {
						return new IndividualChoiceCell(liste_selectedindis);
					}
				});
		
		listViewEditCollectionChoise.setItems(liste_choicedindis);
		listViewEditCollectionChoise
				.setCellFactory(new Callback<ListView<Individual>, ListCell<Individual>>() {
					@Override
					public ListCell<Individual> call(ListView<Individual> param) {
						return new IndividualChoiceCell(liste_selectedindis);
					}
				});

		listviewCollectionSelected.setItems(liste_selectedindis);
		listviewCollectionSelected
				.setCellFactory(new Callback<ListView<Individual>, ListCell<Individual>>() {
					@Override
					public ListCell<Individual> call(ListView<Individual> param) {
						return new IndividualSelectCell(liste_selectedindis);
					}
				});
		
		listViewEditCollectionSelected.setItems(liste_selectedindis);
		listViewEditCollectionSelected
				.setCellFactory(new Callback<ListView<Individual>, ListCell<Individual>>() {
					@Override
					public ListCell<Individual> call(ListView<Individual> param) {
						return new IndividualSelectCell(liste_selectedindis);
					}
				});

		choiseBoxCollectionFilter.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {
						setFilteredItems();

					}
				});
		acc_editCollection.onMouseClickedProperty().addListener((editcevent) -> {
			setFilteredItemsEditCollection();
			setChoiseboxItems();
		});
		choiseBoxEditCollection.getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				setFilteredItemsEditCollection();

			}
		});
		imageConceptIndividual.setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture entered the target */
				log.info("Set On Drag Entered");
				System.out.println("onDragEntered");
				/* show to the user that it is an actual gesture target */
				if (event.getGestureSource() != imageConceptIndividual
						&& event.getDragboard().hasString()) {
					try {
						imageConceptIndividual.setImage(event.getDragboard()
								.getImage());
						txtfield_imageURL.setText(event.getDragboard()
								.getString());
					} catch (Exception e) {
						log.error(e,e);
					}

				}

				event.consume();
			}
		});
		imageEditIndividual.setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture entered the target */
				log.info("Set On Drag Entered");
				System.out.println("onDragEntered");
				/* show to the user that it is an actual gesture target */
				if (event.getGestureSource() != imageConceptIndividual
						&& event.getDragboard().hasString()) {
					try {
						imageEditIndividual.setImage(event.getDragboard()
								.getImage());
						txtfield_EditimageURL.setText(event.getDragboard()
								.getString());
					} catch (Exception e) {
						log.error(e,e);
					}

				}

				event.consume();
			}
		});

	}

	/** supporting method to generalize the insertion of elems into a collection **/
	private void insertElemsToCollectionRecipe(Individual collection,
			ListView<Individual> elems) {
		// is the listview not empty? otherwise we can skip the insertion
		if (!elems.getItems().isEmpty()) {
			// is the collection really a Collection item? otherwise we should
			// not execute this operation!
			boolean collectionIsValid = false;
			// StmtIterator iter = model.getIndividual(skosNS +
			// COLLECTION).listProperties();
			StmtIterator iter = collection.listProperties();
			while (iter.hasNext()) {
				Statement statement = iter.next();
				log.info(statement.getPredicate().getLocalName() + "    "
						+ statement.getObject().asResource().getLocalName());
				if (statement.getPredicate().getLocalName().equals(TYPE)
						&& statement.getObject().asResource().getLocalName()
								.equals(COLLECTION)) {
					// collection is a Collection item
					collectionIsValid = true;
				}
			}
			log.info(collectionIsValid);
			// only continue if you found out that collection is narrower
			// Collection
			if (collectionIsValid) {
				Iterator<Individual> i = elems.getItems().iterator();
				while (i.hasNext()) {
					Individual indi = i.next();
					ObjectProperty property = model.getObjectProperty(skosNS
							+ MEMBER);
					log.info(property.getURI());
					model.add(collection, property, indi);
				}
			}

		}

	}

	@FXML
	public void insertToCollection() {

	}

	/**
	 * 
	 * This Method loads an Ontology which this Controller works with. Also it
	 * gets the Namespaces, Objectproperties and all Individuals of the Ontology
	 * Model for the TreeView and Choiceboxes.
	 * 
	 */
	public void loadOntology() throws URISyntaxException {
	if (model != null) {
			try {
				splitpane.setDisable(false);
	//			model = ModelFacadeTEST.getOntModel();
				// Path input =
				// Paths.get("C:\\Users\\VRage\\Documents\\SpiderOak Hive\\studium\\5_Semester\\projekt\\",
				// "test1.rdf");
				//
				// model.read(input.toUri().toString(), "RDF/XML");
				// model = ModelFacadeTEST.getOntModel();
	//			model.read("./RDFDaten/test1.rdf");
				// model = TripleModel.getAllTriples();
				// Model m = FusekiModel.getDatasetAccessor().getModel();
				// model.add(ModelFacadeTEST.getOntModel());
	
				// model = ModelFacadeTEST.getAktModel();
				// model = ModelFacadeTEST.ontModel;
	
				
				if(model.getNsPrefixURI("")!= null){
					baseNS = model.getNsPrefixURI("");
				}
				if(baseNS.isEmpty()){
					boolean validuri = false;
					String inputValue = JOptionPane.showInputDialog("Base Namespace wasn't found please enter a valid Namespace");
					while(!validuri){
						try {
							URL url = new URL(inputValue);
							url.toURI();
							baseNS = inputValue;
							validuri = true;
						} catch (MalformedURLException | URISyntaxException e) {
							inputValue = JOptionPane.showInputDialog("Invalid Namespace please try again!");
						}
					}
				}
				log.info("Base NS set to: " + baseNS);
				
				if(model.getNsPrefixURI("skos")!= null){
					skosNS = model.getNsPrefixURI("skos");
				}
				if(skosNS.isEmpty())
					skosNS = "http://www.w3.org/2004/02/skos/core#";
				log.info("Skos NS set to: " + skosNS);
				if(model.getNsPrefixURI("skos-xl")!= null){
					skosxlNS = model.getNsPrefixURI("skos-xl");
				}
				if(skosxlNS.isEmpty() )
					skosxlNS = "http://www.w3.org/2008/05/skos-xl#";
				log.info("Skosxl NS set to: " + skosxlNS);
				if(model.getNsPrefixURI("dct")!= null){
					dctNS = model.getNsPrefixURI("dct");
				}
				if(dctNS.isEmpty())
					dctNS = "http://purl.org/dc/terms/";
				log.info("dct NS set to: " + dctNS);
				
				// showOPropertiesInChoicebox();
				// showAllIndividualsInChoicebox();
				showIndividualsOfOntClass(null);
				showOntClassInTree();
				showAllIndividualsInChoicebox();
				setChoiseboxItems();
				setFilteredItems();
				treeview_indi.setCellFactory(new Callback<TreeView<Individual>, TreeCell<Individual>>() {
	
					@Override
					public TreeCell<Individual> call(TreeView<Individual> param) {
						return new IndividualofOntClassCell(param, model);
					}
				});
				log.info("Ontologie loaded");
			} catch (Exception e) {
				log.error(e, e);
			}
		}else{
			log.error("Modell is null");
			JOptionPane.showMessageDialog(null,  localizedBundle.getString("modelnotfound"), localizedBundle.getString("alert"), JOptionPane.ERROR_MESSAGE);
			splitpane.setDisable(true);
		}
	}

	/**
	 * 
	 * Debug Method, saves Ontology into a File
	 * (fuseki/Data/outputfromProgramm.owl) Will be deleted in deployed version.
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
			log.error(e,e);
		}

	}

	/**
	 * 
	 * Method started from MouseClick on Individual of a Ontology Class
	 * ListView. Get the clicked Individual set it to the selectedIndividual and
	 * displays object and data property. Double click gives you the possibility
	 * to delete a Individual.
	 * 
	 * @param event
	 */
	@FXML
	private void selectIndividualOfOntClass(MouseEvent event) {
		System.out.println("Bla");
		if (!treeview_indi.getSelectionModel().isEmpty()) {
			// selectedIndividual = model.getIndividual(liste_indi.get(
			// treeview_indi.getSelectionModel().getSelectedIndex())
			// .getURI());
			// label_uri2.setText(liste_indi.get(
			// treeview_indi.getSelectionModel().getSelectedIndex())
			// .getURI());
	
			selectedIndividual = treeview_indi.getSelectionModel()
					.getSelectedItem().getValue();
			/*
			 * model.getIndividual(
			 * liste_indi.get(liste_indi.indexOf(treeview_indi
			 * .getSelectionModel().getSelectedItem().getValue())).getURI());
			 */
			label_uri2.setText(selectedIndividual.getURI());
	
			label_uri2.setText(selectedIndividual.getURI());
	
			showObjectProperties(selectedIndividual);
			showDataProperties(selectedIndividual);
			txtfield_individiaulname.setText(selectedIndividual.getURI()
					.substring(baseNS.length()) + "/");
			updateEditCollectionView();
			if(acc_editLabel.isExpanded()){
				switch(selectedOntClass.getLocalName()){
					case "Label":
						Individual subjectOfLabel = null;
							selectedIndiLocalname
							.setText(selectedIndividual.getLocalName());
						txtfield_editLabel.setText(getDatapropertyFromLabel(
								selectedIndividual)!=null ? getDatapropertyFromLabel(
										selectedIndividual).getString() : "");
						Property oproppref = model.getProperty(skosxlNS+"prefLabel");
						Property opropalt = model.getProperty(skosxlNS+"altLabel");
						ResIterator resit = model.listSubjectsWithProperty(oproppref, selectedIndividual);
						while(resit.hasNext()){
							Resource res = (Resource) resit.next();
							subjectOfLabel = model.getIndividual(res.getURI());
							log.info("Subject of this Label is: "+subjectOfLabel);
						}
						ResIterator resit2 = model.listSubjectsWithProperty(opropalt, selectedIndividual);
						while(resit2.hasNext()){
							Resource res = (Resource) resit2.next();
							subjectOfLabel = model.getIndividual(res.getURI());
							log.info("Subject of this Label is: "+subjectOfLabel.getLocalName());
						}
						if(subjectOfLabel!=null){
							showLabelsOfIndividual(subjectOfLabel);
						}else{
							log.error("No subject found");
						}
						break;
					case "Concept":
						showLabelsOfIndividual(selectedIndividual);
						break;
						default:
							selectedIndiLocalname.setText("");
							txtfield_editLabel.setText("");
							break;
				}
			}
			if (event.getClickCount() == 2) {
				String selected = model.getIndividual(
						liste_indi.get(
								treeview_indi.getSelectionModel()
										.getSelectedIndex()).getURI())
						.getLocalName();
				int delete = JOptionPane.showConfirmDialog(null,
						localizedBundle.getString("delIndi") + selected,
						localizedBundle.getString("delIndiWindow"),
						JOptionPane.YES_NO_OPTION);
				if (delete == JOptionPane.YES_OPTION) {
					model.getIndividual(
							liste_indi.get(
									treeview_indi.getSelectionModel()
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
	 * This function recognizes which ontology class the user has selected,
	 * displays the URI and call a function to get all Individual in this
	 * ontology class.
	 * 
	 * Also if no valid Class is selected you can't use buttons to add
	 * Individuals/Properties or Labels
	 * 
	 * @param e
	 */
	@FXML
	private void selectOntClass(MouseEvent e) {
		if (!listview_classes.getSelectionModel().isEmpty()) {
			txtfield_IndiLabel0.setDisable(false);
			selectedOntClass = model.getOntClass(liste_classes.get(
					listview_classes.getSelectionModel().getSelectedIndex())
					.getURI());

			OntClassNS = liste_classes.get(
					listview_classes.getSelectionModel().getSelectedIndex())
					.getURI();
			label_uri.setText(OntClassNS);

			showIndividualsOfOntClass(selectedOntClass);
			switch (selectedOntClass.getLocalName()) {
			case "Concept":
				acc_addIndi.setExpanded(true);
				break;
			case "Label":
				acc_editLabel.setExpanded(true);
				txtfield_IndiLabel0.setDisable(true);
				btn_editLabel
						.setText(localizedBundle.getString("btnEditLabel"));
				break;
			case "OrderedCollection":
			case "Collection":
				acc_addCollection.setExpanded(true);
				updateEditCollectionView();
				setFilteredItems();
				break;
			default:
				break;
			}

		}
	}

	private void setChoiseboxItems() {
		ObservableList<String> elems = FXCollections.observableArrayList();
		elems.add(localizedBundle.getString("noFilter"));
		ExtendedIterator<Individual> i = model.listIndividuals();
		while (i.hasNext()) {
			Individual fuck = (Individual) i.next();
			elems.add(fuck.getLocalName());
		}
		choiseBoxCollectionFilter.setItems(elems);
		choiseBoxCollectionFilter.setValue(localizedBundle
				.getString("noFilter"));
		
		choiseBoxEditCollection.setItems(elems);
		choiseBoxEditCollection.setValue(localizedBundle
				.getString("noFilter"));
		
	}

	private void setFilteredItems() {
		liste_choicedindis.clear();
		String selected = choiseBoxCollectionFilter.getValue();
		Individual ind = null;

		if (selected != localizedBundle.getString("noFilter")) {
			ExtendedIterator<Individual> i = model.listIndividuals();
			// search for current individual
			while (i.hasNext()) {
				ind = (Individual) i.next();
				if (ind.getLocalName().equals(selected)) {
					break;
				}
			}
			if (ind != null) {
				StmtIterator properties = ind.listProperties();
				while (properties.hasNext()) {
					Statement s = properties.next();

					System.out.println(s.getPredicate().getLocalName());
					if (s.getPredicate().getLocalName().equals("narrower")) {
						// liste_choicedindis.add((Individual)s.getObject().getModel());
						i = model.listIndividuals();
						while (i.hasNext()) {
							Individual ind2 = (Individual) i.next();
							if (ind2.getLocalName().equals(
									s.getObject().asResource().getLocalName())) {
								liste_choicedindis.add(ind2);
							}
						}
					}
				}
			}
		} else {
			ExtendedIterator<Individual> i = model.listIndividuals();
			while (i.hasNext()) {
				liste_choicedindis.add((Individual) i.next());
			}
		}
		listviewCollectionChoise.setItems(liste_choicedindis);
		
	}
	
	private void setFilteredItemsEditCollection(){
		liste_choisedindisEditCollection.clear();
		String selected = choiseBoxEditCollection.getValue();
		Individual ind = null;

		if (selected != localizedBundle.getString("noFilter")) {
			ExtendedIterator<Individual> i = model.listIndividuals();
			// search for current individual
			while (i.hasNext()) {
				ind = (Individual) i.next();
				if (ind.getLocalName().equals(selected)) {
					break;
				}
			}
			if (ind != null) {
				StmtIterator properties = ind.listProperties();
				while (properties.hasNext()) {
					Statement s = properties.next();

					System.out.println(s.getPredicate().getLocalName());
					if (s.getPredicate().getLocalName().equals("narrower")) {
						// liste_choicedindis.add((Individual)s.getObject().getModel());
						i = model.listIndividuals();
						while (i.hasNext()) {
							Individual ind2 = (Individual) i.next();
							if (ind2.getLocalName().equals(
									s.getObject().asResource().getLocalName())) {
								liste_choisedindisEditCollection.add(ind2);
							}
						}
					}
				}
			}
		} else {
			ExtendedIterator<Individual> i = model.listIndividuals();
			while (i.hasNext()) {
				liste_choisedindisEditCollection.add((Individual) i.next());
			}
		}
		listViewEditCollectionChoise.setItems(liste_choisedindisEditCollection);
		
	}

	/**
	 * 
	 * Show all Object properties in a choicebox from loaded Ontology
	 * 
	 */
	// private void showOPropertiesInChoicebox() {
	// ExtendedIterator list_prop = model.listObjectProperties();
	// while (list_prop.hasNext()) {
	// ObjectProperty prop = (ObjectProperty) list_prop.next();
	// props.add(prop.getLabel("en"));
	// propNS.add(prop.getURI());
	// log.info("property added: " + prop.getLocalName());
	// }
	// }

	private void showAllIndividualsInChoicebox() {
		indis.clear();

		ExtendedIterator<Individual> list_indis = model.listIndividuals();
		while (list_indis.hasNext()) {
			Individual indi = (Individual) list_indis.next();
			indis.add(indi);
			log.info("Individual added: " + indi.getLocalName());

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
							items.add("'" + predicate + "'" + " " + object
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

	/**
	 * 
	 * Shows all Individuals of selected Ontology Class in a Listview
	 * 
	 * @param oclass
	 */
	public void showIndividualsOfOntClass(OntClass oclass) {

		if (oclass != null) {
			items.clear();
			liste_indi.clear();
			selectedIndividual = null;

			//TreeItem<Individual> root = new TreeItem<Individual>();
			root = new TreeItem<Individual>();
			root = showIndividualsOfOntClassRecursive(oclass, root);	
			treeview_indi.setShowRoot(false);
			treeview_indi.setRoot(root);
			for(TreeItem<Individual> bla : root.getChildren()){
				log.info(bla.getValue().getLocalName());
			}
		} else {
			items.clear();
			liste_indi.clear();
			selectedIndividual = null;
			treeview_indi.setRoot(null);
			showObjectProperties(null);
			showDataProperties(null);
		}

	}

	public TreeItem<Individual> showIndividualsOfOntClassRecursive(OntClass oclass, TreeItem<Individual> root){
		ExtendedIterator<? extends OntResource> indilist = oclass
				.listInstances();
		
		while(indilist.hasNext()){
			Individual next =(Individual) indilist.next();
			TreeItem<Individual> elem = generateSubTreeFromBehindLikeJakobLikesIt(oclass, next, null);
			elem.setExpanded(true);
			addToExistingTreeView(root, elem);
		}
		return root;
	}

	
	public void showLabelsOfIndividual(Individual selectedIndividual){
		if(cnt>0){
			for(int i =1; i<cnt ;i++){
				vboxEditPrefLabel.getChildren().remove(1);
			}
			cnt=0;
		}

		DatatypeProperty dprop = model.getDatatypeProperty(skosNS + "notation");
		DatatypeProperty dproplit = model.getDatatypeProperty(skosxlNS	+ "literalForm");
		btn_editLabel.setText(localizedBundle.getString("btnAddLabel"));
		if (getIndividualbyObjectProperty(selectedIndividual,"prefLabel") != null) {
			Individual labelindi = model.getIndividual(getIndividualbyObjectProperty(selectedIndividual,"prefLabel").getURI());
			selectedIndiLocalname.setText(getIndividualbyObjectProperty(selectedIndividual, "prefLabel").getLocalName());
			btn_editLabel.setText(localizedBundle.getString("btnEditLabel"));
			if(labelindi !=null){
				NodeIterator nodeiter = labelindi.listPropertyValues(dproplit);
				while(nodeiter.hasNext()){
					Literal liter = (Literal)nodeiter.next();
					log.info("name of literal: "+liter);
					if(cnt >0){
						//TODO refactoring
						addTextfields(1, vboxEditPrefLabel, liter);
						cnt--;
					}else{
						txtfield_editLabel.setText(liter.getString());
						choiceboxeditLabel.setValue(liter.getLanguage());
					}
					cnt++;
					
				}
			}
		}else {
			txtfield_editLabel.setText("");
		}
		if(getIndividualbyObjectProperty(selectedIndividual, "altLabel")!= null){
			btn_editLabel.setText(localizedBundle.getString("btnEditLabel"));
			if (getDatapropertyFromLabel(getIndividualbyObjectProperty(
					selectedIndividual, "altLabel")) != null){
			txtfield_EditialtLabel.setText(getDatapropertyFromLabel(
					getIndividualbyObjectProperty(
							selectedIndividual, "altLabel"))
					.getString());
			}
		}else{
			txtfield_EditialtLabel.setText("");
		}
		Property aprop = model.getProperty(dctNS + "description");
		if(selectedIndividual.getPropertyValue(aprop) != null){
			txtarea_EditDescription.setText(selectedIndividual.getPropertyValue(aprop).asLiteral().getString());
		}else{
			txtarea_EditDescription.setText("");
		}
		
		if(selectedIndividual.getPropertyValue(dprop) != null)
		{
			String url = selectedIndividual.getPropertyValue(dprop).asLiteral().getString();
			txtfield_EditimageURL.setText(url);
			if(!url.isEmpty()){
				imageEditIndividual.setImage(new Image(url));
			}else{
				imageEditIndividual.setImage(defaultimage);
			}
		}else{
			txtfield_EditimageURL.setText("");
			imageEditIndividual.setImage(defaultimage);
			
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
	 * All skos and skosxl OntologyClasses will be shown in the TreeView by this
	 * method
	 * 
	 */
	public void showOntClassInTree() {
		classes.clear();
		liste_classes.clear();
		selectedOntClass = null;

		ExtendedIterator<OntClass> oclasslist = model.listClasses();
		while (oclasslist.hasNext()) {

			OntClass oclass = (OntClass) oclasslist.next();
			if (oclass.toString().contains(skosNS)
					|| oclass.toString().contains(skosxlNS)) {
				liste_classes.add(oclass);
				classes.add(oclass.getLocalName());
			}

		}
		log.info("class added to List");

	}

	/**
	 * 
	 * Method of Button btn_addprop. Set a object property between selected
	 * Individual and selected individual of a choicebox.
	 * 
	 * @param event
	 */
	@FXML
	private void showSubIndividualinListView(MouseEvent event) {
		setFilteredItems();
	}
	@FXML
	private void updateEditCollectionView(){
		liste_selectedindis.clear();
		selectedCollectionLabel.setText(localizedBundle.getString("labelEditCollectionSelectedCollection"));
		TextFieldLabelEditCollection.clear();
		
		//if the selected item is an collection then update view else set to no-collection
		if(selectedOntClass.getLocalName().equals(COLLECTION)){
			//get the selected Collection as long as one is selected
			if(selectedIndividual != null){
				log.info(selectedIndividual.getURI());
				selectedCollectionLabel.setText(selectedIndividual.getLocalName());
				TextFieldLabelEditCollection.setText(getLabelForCollection(selectedIndividual).getObject().asLiteral().toString());
				prefilEditCollectionSelectedList(selectedIndividual);
				setChoiseboxItems();
				setFilteredItemsEditCollection();
			}
			
		}
	}
	
	private void prefilEditCollectionSelectedList(Individual collection){
		StmtIterator iter = collection.listProperties();
		while(iter.hasNext()){
			Statement state = iter.next();
			System.out.println(state);
			if(state.getPredicate().getLocalName().equals("member")){
				liste_selectedindis.add(model.getIndividual(state.getObject().asResource().getURI()));
			}
		}
	}
	
	private Statement getLabelForCollection(Individual collection){
		StmtIterator statements = collection.listProperties();
		while(statements.hasNext()){
			Statement statement = statements.next();
			if(statement != null){
				if(statement.getPredicate().getLocalName().equals("prefLabel")){
					StmtIterator labelIter = model.getIndividual(statement.getObject().asResource().getURI()).listProperties();
					while(labelIter.hasNext()){
						statement = labelIter.next();
						System.out.println(statement);
						if(statement.getPredicate().getLocalName().equals("literalForm")){
							return statement;
						}
					}
				}
			}
		}
		return null;
	}
	@FXML
	private void cancelEditCollection(){
		//Custom button text
		Frame frame = new Frame();
		Object[] options = {localizedBundle.getString("btnOk"),
		                    localizedBundle.getString("btnCancel")};
		int n = JOptionPane.showOptionDialog(frame,
		    localizedBundle.getString("msgCancel"),
		    localizedBundle.getString("msgCancelTop"),
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.WARNING_MESSAGE,
		    null,
		    options,
		    options[1]);
		if(n == 0)
			updateEditCollectionView();
	}
	@FXML
	private void deleteEditCollection(){
		if(selectedOntClass.getLocalName().equals(COLLECTION)){
			//get the selected Collection as long as one is selected
			if(selectedIndividual != null){
				Frame frame = new Frame();
				Object[] options = {localizedBundle.getString("btnOk"),
				                    localizedBundle.getString("btnCancel")};
				int n = JOptionPane.showOptionDialog(frame,
				    localizedBundle.getString("msgRemove"),
				    localizedBundle.getString("msgCancelTop"),
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.WARNING_MESSAGE,
				    null,
				    options,
				    options[1]);
				if(n == 0){	
					model.remove(selectedIndividual.listProperties());
					updateEditCollectionView();
				}
			}
		}
	}
	@FXML
	private void saveEditCollection(){
		if(selectedOntClass.getLocalName().equals(COLLECTION)){
			//get the selected Collection as long as one is selected
			if(selectedIndividual != null){
				Frame frame = new Frame();
				Object[] options = {localizedBundle.getString("btnOk"),
				                    localizedBundle.getString("btnCancel")};
				int n = JOptionPane.showOptionDialog(frame,
				    localizedBundle.getString("msgSave"),
				    localizedBundle.getString("msgCancelTop"),
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[1]);
				if(n == 0){	
					//implement here
					Statement label = getLabelForCollection(selectedIndividual);
					if(label != null){
//						Resource subject = label.getSubject();
//						Property predicate = label.getPredicate();
						String collectionLabelString = baseNS + "LabelForCollection";
						model.remove(label);
						createLabelRecipe("prefLabel", collectionLabelString, TextFieldLabelEditCollection.getText(), "de", selectedIndividual);
					}
					clearCollection(selectedIndividual);
					insertElemsToCollectionRecipe(selectedIndividual, listviewCollectionSelected);
					
				}
			}
		}
	}
	private void clearCollection(Individual collection){
		collection.removeAll(model.getProperty(skosNS + "member"));
	}
	
}
