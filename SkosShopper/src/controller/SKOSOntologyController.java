package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import model.SKOSOntology;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class SKOSOntologyController implements Initializable{
	
	@FXML	private Label lab1, lab2, labA, labB, labLang;
	@FXML	private Button btn_loadSKOS;
	@FXML	private Button add;
	@FXML	private Button add1;
	@FXML	private Button clear;
	@FXML	private TextField trash;
	@FXML	private Button print;
	@FXML	private ComboBox<String> box_source;
	@FXML	private ComboBox<String> skosClass;
	@FXML	private TextField dragBox;
	@FXML	private TextField tf_skosURI;
	@FXML	private TextField dragBox2;
	@FXML	private TextField skosfield;
	@FXML	private TextField language;
	@FXML	private TextArea assertion;
	@FXML	private ComboBox<String> box1;
	@FXML	private ComboBox<String> box2;
	@FXML	private ComboBox<String> box3;
	// Main SKOS Classes
	@FXML	private ListView<String> conceptList;
	@FXML	private ListView<String> conceptSchemeList;
	@FXML	private ListView<String> collectionList;
	@FXML	private ListView<String> orderedCollectionList;
	@FXML	private ListView<String> labelList;

	
	
	
	private ObservableList<String> boxSource;
	private SKOSOntology skos;
	private String baseURI;
	private ObservableList<String> concepts;
	private ObservableList<String> conceptSchemes;
	private ObservableList<String> collections;
	private ObservableList<String> orderedCollections;
	private ObservableList<String> labels;
	private ObservableList<String> object_properties;
	private ObservableList<String> skos_box;
	private ObservableList<String> annot;
	private ObservableList<String> datatypes;
	private ObservableList<String> userchoice;

	public void initialize(URL location, ResourceBundle resources) {
		skos = new SKOSOntology();
		createBoxSource();
		
		conceptList.setOnDragDetected(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				Dragboard dragBoard = conceptList.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(conceptList.getSelectionModel().getSelectedItem());
				dragBoard.setContent(content);
				System.out.println(dragBoard.getString());
				event.consume();
			}
		});
		
		dragBox.setOnDragOver(new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    dragBox.setText(event.getDragboard().getString());
                }
                
                event.consume();
			}
		});
		
		trash.setOnDragOver(new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    String delete = event.getDragboard().getString();
                    trash.setText(event.getDragboard().getString());
                    /* "Do you really want to delete the individual?\n"*/
                    int eingabe = JOptionPane.showConfirmDialog(null,
                    		"NOT IMPLMENTED YET\n"
                    		+ "\" " + delete + "\" ", "deleted", JOptionPane.YES_NO_OPTION);
                }
                trash.clear();
			}
		});
	}
	
	@FXML public void loadSKOS(ActionEvent event) throws OWLOntologyCreationException, IOException {
		if(box_source.getValue() == boxSource.get(0)) {
			
		} else if(box_source.getValue() == boxSource.get(1)) {
			// User enters URL of SKOS-File
			baseURI = (String)JOptionPane.showInputDialog(
	                null,
	                "Enter an URL for your SKOS-File:\n"
	                + "Example: http://www.w3.org/2008/05/skos-xl",
	                "SKOS-URL",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null, "http://");
			skos.getSKOSOntologyFromServer(baseURI);
			tf_skosURI.setText(baseURI);
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File file = fileChooser.showOpenDialog(null);
			if(fileChooser != null) {
				skos.getSKOSOntologyFromLocal(file.getPath());
			}
		}
		fillTable();
	}
	
	@FXML public void printSKOS(ActionEvent event) throws OWLOntologyStorageException {
		skos.getManager().saveOntology(skos.getOntology(), System.out);
	}
	
	@FXML public void add_individual() {
		if(skos.getOntology() != null) {
			if(skosfield.getText() != null && skosClass.getValue() != null) {
				if(skosClass.getValue() == skos_box.get(0)) {
					OWLClassAssertionAxiom ax = skos.createConcept(skosfield.getText());
					assertion.setText(ax.toString());
					concepts.add(skosfield.getText());
				} else if(skosClass.getValue() == skos_box.get(1)) {
					skos.createConceptScheme(skosfield.getText());
					conceptSchemes.add(skosfield.getText());
				} else if(skosClass.getValue() == skos_box.get(2)) {
					skos.createCollection(skosfield.getText());
					collections.add(skosfield.getText());
				} else if(skosClass.getValue() == skos_box.get(3)) {
					skos.createLabel(skosfield.getText());
					labels.add(skosfield.getText());
					labels.add(skosfield.getText());
				} else {
					skos.createOrderedCollection(skosfield.getText());
					orderedCollections.add(skosfield.getText());
				}
			}
			//individuals.add(skosfield.getText());
		}
		skosfield.setText(null);
		skosClass.setValue(null);
	}
	
	@FXML public void add_item(ActionEvent event) {
		if(skos.getOntology() != null) {
			if(box1.getValue() == userchoice.get(0)) {
				if(dragBox.getText() != null && dragBox2.getText() != null && box2.getValue() != null) {
					if(box2.getValue() == object_properties.get(0)) {
						skos.hasPrefLabel(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(1)) {
						skos.hasAltLabel(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(2)) {
						skos.hasHiddenLabel(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(3)) {
						skos.isInSemanticRelationWith(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(4)) {
						skos.hasBroaderTransitive(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(5)) {
						skos.hasBroader(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(6)) {
						skos.hasBroaderMatch(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(7)) {
						skos.hasNarrowerTransitive(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(8)) {
						skos.hasNarrower(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(9)) {
						skos.hasNarrowMatch(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(10)) {
						skos.hasRelated(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(11)) {
						skos.hasRelatedMatch(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(12)){
						skos.isInMappingRelationWith(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(10)) {
						skos.hasCloseMatch(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(11)) {
						skos.hasExactMatch(dragBox.getText(), dragBox2.getText());
					} else if(box2.getValue() == object_properties.get(12)){
						skos.hasRelatedLabel(dragBox.getText(), dragBox2.getText());
					}
				}
			} else if(box1.getValue() == userchoice.get(1)) {
				if(skos.getOntology() != null) {
					if(box2.getValue() == null) {
						if(dragBox.getText() != null && dragBox2.getText() != null && language.getText() != null) {
							skos.hasLiteralForm(dragBox.getText(), dragBox2.getText(), language.getText(), -1);
						}
					} else {
						int i = 0;
						for (i = 0; i < datatypes.size(); i++) {
							if(datatypes.get(i) == box2.getValue()) {
								break;
							}
						}
						skos.hasLiteralForm(dragBox.getText(), dragBox2.getText(), null, i);
					}
				}
			} else if(box1.getValue() == userchoice.get(2)) {
				if(language == null) {
					if(dragBox.getText() != null && dragBox2.getText() != null && box2.getValue() != null && box3.getValue() != null) {
						int i = 0;
						int j = 0;
						for (i = 0; i < annot.size(); i++) {
							if(box2.getValue() == annot.get(i)) {
								break;
							}
						}
						for(j = 0; j < datatypes.size(); j++) {
							if(box3.getValue() == datatypes.get(j)) {
								break;
							}
						}
						skos.hasNote(dragBox.getText(), dragBox2.getText(), i, null, j);
					}
				} else if(dragBox.getText() != null && dragBox2.getText() != null && box2.getValue() != null && box3.getValue() == null){
					int i = 0;
					for (i = 0; i < annot.size(); i++) {
						if(box2.getValue() == annot.get(i)) {
							break;
						}
					}
					skos.hasNote(dragBox.getText(), dragBox2.getText(), i, language.getText(), -1);
				}
			}
		}
	}
//	
//	@FXML public void add_individual(ActionEvent event) {
//		if(skos.getOntology() != null) {
//			if(skosfield.getText() != null && skosClass.getValue() != null) {
//				if(skosClass.getValue() == skos_box.get(0)) {
//					OWLClassAssertionAxiom ax = skos.createConcept(skosfield.getText());
//					assertion.setText(ax.toString());
//					concepts.add(skosfield.getText());
//				} else if(skosClass.getValue() == skos_box.get(1)) {
//					skos.createConceptScheme(skosfield.getText());
//					conceptSchemes.add(skosfield.getText());
//				} else if(skosClass.getValue() == skos_box.get(2)) {
//					skos.createCollection(skosfield.getText());
//					collections.add(skosfield.getText());
//				} else if(skosClass.getValue() == skos_box.get(3)) {
//					skos.createLabel(skosfield.getText());
//					labels.add(skosfield.getText());
//					orderedCollections.add(skosfield.getText());
//				} else {
//					skos.createOrderedCollection(skosfield.getText());
//					labels.add(skosfield.getText());
//				}
//			}
//		}
//		skosfield.clear();
//		skosClass.setValue(null);
//	}
	
	@FXML public void clear(ActionEvent event) {
		
	}
	
	@FXML public void option_event(ActionEvent event) {
		if(box1.getValue() == userchoice.get(0)) {
			if(language.isVisible()) {
				language.setVisible(false);
			}
			if(labLang.isVisible()) {
				labLang.setVisible(false);
			}

			labA.setText("Individual A");
			labB.setText("Individual B");
			lab1.setText("Object Properties");
			//box2.getItems().clear();
			box2.setItems(object_properties);
			if(!box2.isVisible()) {
				box2.setVisible(true);
			}
			if(!lab1.isVisible()) {
				lab1.setVisible(true);
			}
		} else if(box1.getValue() == userchoice.get(1)) {
			labA.setText("Individual Label");
			labB.setText("Literal");
			lab1.setText("Datatype");
			//box2.getItems().clear();
			box2.setItems(datatypes);
			if(!labLang.isVisible()) {
				labLang.setVisible(true);
			}
			if(!language.isVisible()) {
				language.setVisible(true);
			}
		} else if(box1.getValue() == userchoice.get(2)) {
			labA.setText("Individual");
			lab1.setText("Annotation Form");
			labB.setText("Annotation");
			box2.setItems(annot);
			if(!language.isVisible()) {
				language.setVisible(true);
			}
			if(!labLang.isVisible()) {
				labLang.setVisible(true);
			}
		}
	}
	
	
	public void createBoxSource() {
		// General Settings
		boxSource = FXCollections.observableArrayList(
				"Load data from Fuseki-Server",
				"Load data from http",
				"Load data from physical storage system");
		box_source.getItems().clear();
		box_source.setItems(boxSource);
		
		// SKOS CLASS
		skos_box = FXCollections.observableArrayList(
				"Concept", "Concept Scheme", "Collection", "Ordered Collection", "Label");
		skosClass.getItems().clear();
		skosClass.setItems(skos_box);
		
		// USER CHOICE
		userchoice = FXCollections.observableArrayList(
				"Object Properties",
				"Data Properties",
				"Annotation Properties");
		box1.getItems().clear();
		box1.setItems(userchoice);
		
		// Object Properties
		// SEMANTIC object_properties BOX
		object_properties = FXCollections.observableArrayList(
				"has preferred label",
				"has alternative label",
				"has hidden label",
				"is in semantic relation with",
				"has broader transitive",
				"has broader",
				"has braod match",
				"has narrower transitive",
				"has narrower",
				"has narrow match",
				"has related",
				"has related match",
				"is in mapping relation with",
				"has close match",
				"has exact match",
				"has related label"
				);
		
		// Data Properties
		// DATATYPE BOX
		datatypes = FXCollections.observableArrayList( "OWL_RATIONAL", "OWL_REAL", "RDF_PLAIN_LITERAL", "RDF_XML_LITERAL", "RDFS_LITERAL",
			"XSD_ANY_URI", "XSD_BASE_64_BINARY", "XSD_BOOLEAN", "XSD_BYTE", "XSD_DATE_TIME",
			"XSD_DATE_TIME_STAMP", "XSD_DECIMAL", "XSD_DOUBLE", "XSD_FLOAT", "XSD_HEX_BINARY",
			"XSD_INT", "XSD_INTEGER", "XSD_LANGUAGE", "XSD_LONG", "XSD_NAME", "XSD_NCNAME",
			"XSD_NEGATIVE_INTEGER", "XSD_NMTOKEN", "XSD_NON_NEGATIVE_INTEGER", "XSD_NON_POSITIVE_INTEGER",
			"XSD_NORMALIZED_STRING", "XSD_POSITIVE_INTEGER", "XSD_SHORT", "XSD_STRING", "XSD_TOKEN",
			"XSD_UNSIGNED_BYTE", "XSD_UNSIGNED_INT", "XSD_UNSIGNED_LONG", "XSD_UNSIGNED_SHORT");
		
		
		// ANNOTATION BOX
		annot = FXCollections.observableArrayList(
				"has note",
				"has change note",
				"has definition",
				"has editorial note",
				"has example",
				"has history note",
				"has scope note");
	}
	
	public void addSKOSClasses() {
		if(skos.getOntology() != null) {
			
		}
	}
	
	public void fillTable() {
		concepts = FXCollections.observableArrayList();
		conceptSchemes = FXCollections.observableArrayList();
		collections = FXCollections.observableArrayList();
		orderedCollections = FXCollections.observableArrayList();
		labels = FXCollections.observableArrayList();
		concepts = skos.getConcepts(concepts);
		conceptSchemes = skos.getConceptSchemes(conceptSchemes);
		collections = skos.getCollections(collections);
		orderedCollections = skos.getOrderedCollections(orderedCollections);
		labels = skos.getLabels(labels);
		
		conceptList.setItems(concepts);
		//conceptList.getSelectionModel().getSelectedItem().
		conceptSchemeList.setItems(conceptSchemes);
		collectionList.setItems(collections);
		orderedCollectionList.setItems(orderedCollections);
		labelList.setItems(labels);
	}
}
