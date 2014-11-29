package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.zookeeper.proto.SetWatches;

import Skos.SkosBuilder.SKOSNameSpaces;
import Skos.SkosBuilder.SKOSStatements;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class SKOSOntologyController implements Initializable{
	
	// Javafx Components
	@FXML private AnchorPane ap_create_indiv = new AnchorPane();
	@FXML private AnchorPane ap_edit_stmt = new AnchorPane();
	@FXML private ListView<String> lv_concepts = new ListView<String>();
	@FXML private ListView<String> lv_conceptSchemes = new ListView<String>();
	@FXML private ListView<String> lv_collections = new ListView<String>();
	@FXML private ListView<String> lv_ordCollections = new ListView<String>();
	@FXML private ListView<String> lv_labels = new ListView<String>();
	@FXML private ListView<String> lv_lists = new ListView<String>();
	
	@FXML private TableView<SKOSStatements> tv_statements = new TableView<SKOSStatements>();
	@FXML private TableColumn<SKOSStatements, String> tc_subject = new TableColumn<SKOSStatements, String>();
	@FXML private TableColumn<SKOSStatements, String> tc_predicate = new TableColumn<SKOSStatements, String>();
	@FXML private TableColumn<SKOSStatements, String> tc_object = new TableColumn<SKOSStatements, String>();
	@FXML private TableColumn<SKOSStatements, Boolean> tc_remove = new TableColumn<SKOSStatements, Boolean>();
	@FXML private TableColumn<SKOSStatements, Boolean> tc_edit = new TableColumn<SKOSStatements, Boolean>();
	
	@FXML private Accordion accordeon = new Accordion();
	
	// Observable Lists
	public static ObservableList<String> concepts = FXCollections.observableArrayList();
	public static ObservableList<String> conceptSchemes = FXCollections.observableArrayList();
	public static ObservableList<String> collections = FXCollections.observableArrayList();
	public static ObservableList<String> ordCollections = FXCollections.observableArrayList();
	public static ObservableList<String> labels = FXCollections.observableArrayList();
	public static ObservableList<String> lists = FXCollections.observableArrayList();
	
	private ObservableList<SKOSStatements> statementList = FXCollections.observableArrayList();
	
	private static OntModel model;
	private static HashMap<String, String> indMap = new HashMap<String, String>();

	public void initialize(URL location, ResourceBundle resources) {
		
		lv_concepts.setItems(concepts);
		lv_conceptSchemes.setItems(conceptSchemes);
		lv_collections.setItems(collections);
		lv_ordCollections.setItems(ordCollections);
		lv_labels.setItems(labels);
		lv_lists.setItems(lists);
		// Setup ListViews
		setupListViews(lv_concepts);
		setupListViews(lv_conceptSchemes);
		setupListViews(lv_collections);
		setupListViews(lv_ordCollections);
		setupListViews(lv_labels);
		setupListViews(lv_lists);
		
		tc_subject.prefWidthProperty().bind(tv_statements.widthProperty().multiply(0.283f));
		tc_object.prefWidthProperty().bind(tv_statements.widthProperty().multiply(0.283f));
		tc_predicate.prefWidthProperty().bind(tv_statements.widthProperty().multiply(0.283f));
		tc_remove.prefWidthProperty().bind(tv_statements.widthProperty().multiply(0.08f));
		tc_edit.prefWidthProperty().bind(tv_statements.widthProperty().multiply(0.07f));
		
		tc_subject.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
		tc_predicate.setCellValueFactory(cellData -> cellData.getValue().predicateProperty());
		tc_object.setCellValueFactory(cellData -> cellData.getValue().objectProperty());
		tc_remove.setSortable(false);
		tc_edit.setSortable(false);
		
		tc_remove.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SKOSStatements, Boolean>, ObservableValue<Boolean>>() {
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<SKOSStatements, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
 
		tc_remove.setCellFactory(new Callback<TableColumn<SKOSStatements, Boolean>, TableCell<SKOSStatements, Boolean>>() {
            public TableCell<SKOSStatements, Boolean> call(TableColumn<SKOSStatements, Boolean> p) {
                return new removeBtn(tv_statements);
            }
        });
		
		tc_edit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SKOSStatements, Boolean>, ObservableValue<Boolean>>() {
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<SKOSStatements, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
 
		tc_edit.setCellFactory(new Callback<TableColumn<SKOSStatements, Boolean>, TableCell<SKOSStatements, Boolean>>() {
            public TableCell<SKOSStatements, Boolean> call(TableColumn<SKOSStatements, Boolean> p) {
                return new editBtn(tv_statements);
            }
        });
		
		tv_statements.setItems(statementList);
		
		
		
	}
	
	private class removeBtn extends TableCell<SKOSStatements, Boolean> {
        Button cellButton = new Button("Delete");
        
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty && t){
                setGraphic(cellButton);
                this.setAlignment(Pos.CENTER);
            }else {
				setText(null);
				setGraphic(null);
			}
        }
         
        removeBtn(final TableView<SKOSStatements> tblView){
             
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent t) {
                    int selectdIndex = getTableRow().getIndex();
                    statementList.remove(selectdIndex);
                }
            });
        }

    }
	
	private class editBtn extends TableCell<SKOSStatements, Boolean> {
        Button cellButton = new Button("Edit");
        
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty && t){
                setGraphic(cellButton);
                this.setAlignment(Pos.CENTER);
            }else {
				setText(null);
				setGraphic(null);
			}
        }
         
        editBtn(final TableView<SKOSStatements> tblView){
             
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent t) {
                	if(!ap_edit_stmt.isVisible()) {
                		ap_edit_stmt.setVisible(true);
                	}
                	
                	if(ap_create_indiv.isVisible()) {
                		ap_create_indiv.setVisible(false);
                	}
                }
            });
        }

    }
	
	// Getting OntModel (if available) automatically, when user switch to this tab
	public static void startSKOSOntologyController(OntModel ontModel) {
		model = ontModel;
		sortIndividuals();
	}
	
	public static void sortIndividuals() {
		ExtendedIterator<OntClass> allClasses = model.listClasses();
		while(allClasses.hasNext()) {
			try {
				OntClass cls = allClasses.next();
				ExtendedIterator<? extends OntResource> ind = cls.listInstances();
				while(ind.hasNext()) {
					Individual i = (Individual) ind.next();
					indMap.put(i.getLocalName(), i.getURI());
					switch (cls.toString()) {
					case SKOSNameSpaces.concept:
						concepts.add(i.getLocalName());
						break;
					case SKOSNameSpaces.conceptScheme:
						conceptSchemes.add(i.getLocalName());
						break;
					case SKOSNameSpaces.collection:
						collections.add(i.getLocalName());
						break;
					case SKOSNameSpaces.ordCollection:
						ordCollections.add(i.getLocalName());
						break;
					case SKOSNameSpaces.label:
						labels.add(i.getLocalName());
						break;
					case SKOSNameSpaces.list:
						lists.add(i.getLocalName());
						break;
					default:
						break;
					}
				}
			} catch(Exception e) {
				System.out.println("Not a valid class or individual");
			}
		}
	}
	
	public void setupListViews(ListView<String> listView) {
		listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				final ListCell<String> listCell = new ListCell<String>() {
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty && item != null) {
							setText(item);
						} else {
							setText(null);
							setGraphic(null);
						}
					}
				};
				
				listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton().equals(MouseButton.PRIMARY)) {
							if(listCell.getText() != null) {
								statementList.clear();
								Individual i = model.getIndividual(indMap.get(listCell.getText()));
								StmtIterator iter = i.listProperties();
								while(iter.hasNext()) {
								    Statement stmt      = iter.nextStatement();  // get next statement
								    Resource  subject   = stmt.getSubject();     // get the subject
								    Property  predicate = stmt.getPredicate();   // get the predicate
								    RDFNode   object    = stmt.getObject();      // get the object

								    if (object instanceof Resource) {
								       statementList.add(new SKOSStatements(subject.toString(), predicate.toString(), object.toString()));
								    } else {
								        statementList.add(new SKOSStatements(subject.toString(), predicate.toString(), " \"" + object.toString() + "\""));
								    }
								}
							}
						}
					}
				});
				return listCell;
			}
		});
	}
}
