package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.SKOSOntology;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SKOSOntologyController implements Initializable{
	
	@FXML	private Label lab1, lab2, labA, labB, labLang;
	@FXML	private Button send;
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
	@FXML	private TableView<String> conceptList = new TableView<String>();
	@FXML	private TableColumn<String, String> conceptListColumn = new TableColumn<String, String>();
	@FXML	private TableView<String> labelList = new TableView<String>();
	@FXML	private TableColumn<String, String> labelListColumn = new TableColumn<String, String>();
	@FXML	private TreeTableView<String> collectionList = new TreeTableView<String>();
	@FXML	private TreeTableView<String> orderedCollectionList = new TreeTableView<String>();
	@FXML	private TreeTableColumn<String, String> orderedCollectionListColumns = new TreeTableColumn<String, String>();
	@FXML	private TableView<String> conceptSchemeList = new TableView<String>();
	@FXML	private TreeTableColumn<String, String> collectionListColumn = new TreeTableColumn<String, String>();
	@FXML	private TableColumn<String, String> conceptSchemeColumn = new TableColumn<String, String>();

	
	
	
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
	private Model model = ModelFactory.createDefaultModel();
	private DatasetAccessor ds;

	public void initialize(URL location, ResourceBundle resources) {
		createBoxSource();
	
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
	
	/** DRAG AND DROP **/
	
	// Drag Drop Concept Table
	@FXML public void conceptListDrag(Event event) {
		Dragboard dragBoard = conceptList.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(conceptList.getSelectionModel().getSelectedItem());
		dragBoard.setContent(content);
		System.out.println(dragBoard.getString());   
         event.consume();
	}
	
	@FXML public void conceptListDragDonde(Event event) {
		conceptList.setOnDragDone(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		        event.consume();
		    }
		});
	}
	
	// Drag Drop Concept Scheme Table
	@FXML public void conceptSchemeDrag(Event event) {
		Dragboard dragBoard = conceptSchemeList.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(conceptSchemeList.getSelectionModel().getSelectedItem());
		dragBoard.setContent(content);
		System.out.println(dragBoard.getString());   
         event.consume();
	}
	
	// Drag Drop Concept Label Table
	@FXML public void labelDrag(Event event) {
		Dragboard dragBoard = labelList.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(labelList.getSelectionModel().getSelectedItem());
		dragBoard.setContent(content);
		System.out.println(dragBoard.getString());   
         event.consume();
	}
	
	@FXML public void ordCollectionDrag(Event event) {
		Dragboard dragBoard = orderedCollectionList.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(orderedCollectionList.getSelectionModel().getSelectedItem().getValue());
		dragBoard.setContent(content);
		System.out.println(dragBoard.getString());   
         event.consume();
	}
	
	@FXML public void collectionDrag(Event event) {
		Dragboard dragBoard = collectionList.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(collectionList.getSelectionModel().getSelectedItem().getValue());
		dragBoard.setContent(content);
		System.out.println(dragBoard.getString());   
         event.consume();
	}
	
	// Drag Drop Box A
	@FXML public void overBoxA(DragEvent event) {
        if (event.getGestureSource() != dragBox &&
                event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
	}
	
	// Drag and Drop Box A
	@FXML public void droppedOnBoxA(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
        	dragBox.setText(db.getString());
           success = true;
        }
        event.setDropCompleted(success);
        
        event.consume();
	}
	
	// Drag and Drop Box B
	@FXML public void droppedOnBoxB(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
        	dragBox2.setText(db.getString());
           success = true;
        }
        event.setDropCompleted(success);
        
        event.consume();
	}
	
	@FXML public void overBoxB(DragEvent event) {
        if (event.getGestureSource() != dragBox2 &&
                event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
	}
	
	@FXML public void droppedOnCollectionBox(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
        	TreeItem<String> node = new TreeItem<String>(db.getString());
        	TreeItem<String> tmp = new TreeItem<String>(collectionList.getSelectionModel().getSelectedItem().getValue());
        	tmp.getChildren().add(node);
           success = true;
        }
        event.setDropCompleted(success);
        
        event.consume();
	}
	
	@FXML public void overCollectionTable(DragEvent event) {
        if (event.getGestureSource() != collectionList &&
                event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
	}
	
	@FXML public void loadSKOS(ActionEvent event) {
		// Get SKOS Ontology from Fuseki-Server
		if(box_source.getValue() == boxSource.get(0)) {
			skos = new SKOSOntology();
			skos.getSKOSFromFuseki();
			// Get SKSO Ontology from Http
		} else if(box_source.getValue() == boxSource.get(1)) {
			skos = new SKOSOntology();
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
			
			// Get SKOS Ontology from local storage system
		} else if(box_source.getValue() == boxSource.get(2)){
			skos = new SKOSOntology();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File file = fileChooser.showOpenDialog(null);
			if(fileChooser != null) {
				String path = file.getAbsolutePath();
				skos.getSKOSOntologyFromLocal(path);
			}
			baseURI = skos.getModel().getNsPrefixURI("");
			tf_skosURI.setText(baseURI);
			
			// Create SKOS Ontology from scratch
		} else {
			skos = new SKOSOntology();
			baseURI = (String)JOptionPane.showInputDialog(
	                null,
	                "Enter an URL for your new SKOS-File:\n"
	                + "Example: http://www.example.org/ont",
	                "Create New SKOS",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null, "http://");
			skos.createNewOntology(baseURI, 0);
			tf_skosURI.setText(baseURI);
		}
		fillTable();
	}
	
	@FXML public void printSKOS(ActionEvent event) {
		skos.getModel().write(System.out);
	}
	
	@FXML public void add_individual() {
		
		if(skosfield.getText() != null && skosClass.getValue() != null) {
			if(skosClass.getValue() == skos_box.get(0)) {
				skos.createConcept(skosfield.getText());
				concepts.add(skosfield.getText());

			} else if(skosClass.getValue() == skos_box.get(1)) {
				skos.createConceptScheme(skosfield.getText());
				conceptSchemes.add(skosfield.getText());
			} else if(skosClass.getValue() == skos_box.get(2)) {
				skos.createCollection(skosfield.getText());
				collections.add(skosfield.getText());
				
				TreeItem<String> node = new TreeItem<String>(skosfield.getText());
				collectionList.getRoot().getChildren().add(node);
			} else if(skosClass.getValue() == skos_box.get(3)) {
				skos.createOrderedCollection(skosfield.getText());

				TreeItem<String> node = new TreeItem<String>(skosfield.getText());
				orderedCollectionList.getRoot().getChildren().add(node);
			} else {
				skos.createLabel(skosfield.getText());
				labels.add(skosfield.getText());
			}
		}
		skosfield.setText(null);
		skosClass.setValue(null);
	}
	
	@FXML public void add_item(ActionEvent event) {
		if(box1.getValue() == userchoice.get(0)) {
			if(dragBox.getText() != null && dragBox2.getText() != null && box2.getValue() != null) {
				if(box2.getValue() == object_properties.get(0)) {
					skos.hasPrefLabel(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(1)) {
					skos.hasAltLabel(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(2)) {
					skos.hasHiddenLabel(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(3)) {
					skos.hasTopConcept(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(4)) {
					skos.isInScheme(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(5)) {
					skos.isTopConceptOf(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(6)) {
					skos.isInSemanticRelationWith(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(7)) {
					skos.hasBroaderTransitive(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(8)) {
					skos.hasBroader(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(9)) {
					skos.hasBroaderMatch(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(10)) {
					skos.hasNarrowerTransitive(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(11)) {
					skos.hasNarrower(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(12)) {
					skos.hasNarrowMatch(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(13)) {
					skos.hasRelated(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(14)) {
					skos.hasRelatedMatch(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(15)){
					skos.isInMappingRelationWith(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(16)) {
					skos.hasCloseMatch(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(17)) {
					skos.hasExactMatch(dragBox.getText(), dragBox2.getText());
				} else if(box2.getValue() == object_properties.get(18)){
					skos.hasRelatedLabel(dragBox.getText(), dragBox2.getText());
				}
			}
		} else if(box1.getValue() == userchoice.get(1)) {
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
	
	@FXML public void clear(ActionEvent event) {
		
	}
	
	@FXML public void send_to_fuseki(ActionEvent event) {
		String serviceURI = "http://localhost:3030/ds/data";
		ds = DatasetAccessorFactory.createHTTP(serviceURI);
		model = ds.getModel();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		skos.getModel().write(out);
		model.write(out);
	}
	
	@FXML public void getFromFuseki(ActionEvent event) {
		String serviceURI = "http://localhost:3030/ds/data";
		ds = DatasetAccessorFactory.createHTTP(serviceURI);
		model = ds.getModel();
		model.write(System.out);
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
			if(!box2.isVisible()) {
				box2.setVisible(true);
			}
			if(!labLang.isVisible()) {
				labLang.setVisible(true);
			}
			if(!language.isVisible()) {
				language.setVisible(true);
			}
			if(!lab1.isVisible()) {
				lab1.setVisible(true);
			}
		} else if(box1.getValue() == userchoice.get(2)) {
			labA.setText("Individual");
			lab1.setText("Annotation Form");
			labB.setText("Annotation");
			box2.setItems(annot);
			
			if(!box2.isVisible()) {
				box2.setVisible(true);
			}
			if(!language.isVisible()) {
				language.setVisible(true);
			}
			if(!labLang.isVisible()) {
				labLang.setVisible(true);
			}
			if(!lab1.isVisible()) {
				lab1.setVisible(true);
			}
		}
	}
	
	
	public void createBoxSource() {
		// General Settings
		boxSource = FXCollections.observableArrayList(
				"Load data from Fuseki-Server",
				"Load data from http",
				"Load data from physical storage system",
				"Create SKOS from Scratch");
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
				"has top concept",
				"is top concept of",
				"is in scheme with",
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
		datatypes = FXCollections.observableArrayList("XSDanyURI", "XSDbase64Binary", "XSDboolean", "XSDbyte", "XSDdate",
				"XSDdateTime", "XSDdecimal", "XSDdouble", "XSDduration", "XSDENTITY", "XSDfloat", "XSDgDay", "XSDgMonth",
				"XSDgMonthDay", "XSDgYear", "XSDgYearMonth", "XSDhexBinary", "XSDID", "XSDIDREF" , "XSDint", "XSDinteger",
				"XSDlanguage", "XSDlong", "XSDName", "XSDNCName", "XSDnegativeInteger", "XSDNMTOKEN", "XSDnonNegativeInteger",
				"XSDnonPositiveInteger", "XSDnormalizedString", "XSDNOTATION", "XSDpositiveInteger", "XSDQName", "XSDshort",
				"XSDstring", "XSDtime", "XSDtoken", "XSDunsignedByte", "XSDunsignedInt", "XSDunsignedLong", "XSDunsignedShort");
		
		
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

		collectionListColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<String, String> p) {
		         return (new ReadOnlyStringWrapper(p.getValue().getValue()));
		     }
		  });
		
		TreeItem<String> root1 = new TreeItem<String>("Collections");
		for (int i = 0; i < collections.size(); i++) {
			TreeItem<String> node = new TreeItem<String>(collections.get(i));
			root1.getChildren().add(node);
		}
		collectionList.setShowRoot(false);
		collectionList.setRoot(root1);
		
		orderedCollectionListColumns.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<String, String> p) {
		         return (new ReadOnlyStringWrapper(p.getValue().getValue()));
		     }
		  });
		
		TreeItem<String> root2 = new TreeItem<String>("Ordered Collections");
		for (int i = 0; i < orderedCollections.size(); i++) {
			TreeItem<String> node = new TreeItem<String>(orderedCollections.get(i));
			root2.getChildren().add(node);
		}
		orderedCollectionList.setShowRoot(false);
		orderedCollectionList.setRoot(root2);
		
		conceptListColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {

			public ObservableValue<String> call(CellDataFeatures<String, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue());
			}
		});
		
		labelListColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {

			public ObservableValue<String> call(CellDataFeatures<String, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue());
			}
		});
		
		conceptSchemeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {

			public ObservableValue<String> call(CellDataFeatures<String, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue());
			}
		});

		conceptList.setItems(concepts);
		conceptSchemeList.setItems(conceptSchemes);
		labelList.setItems(labels);
	}
}
