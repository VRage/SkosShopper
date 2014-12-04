package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import Skos.SkosBrowser.org.coode.html.OntologyExporter;
import Skos.SkosBuilder.SKOSBuilder;
import Skos.SkosBuilder.SKOSStatements;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class SKOSOntologyController implements Initializable{
	
	@FXML private Tab tab_skos_browser = new Tab();
	@FXML private WebView skos_browser = new WebView();
	final WebEngine webEngine = skos_browser.getEngine();
	
	// change indiv stmt
	@FXML private TextField tf_indiv_a = new TextField();
	@FXML private ComboBox<String> cb_objprop = new ComboBox<String>();
	@FXML private ComboBox<String> cb_indiv_b = new ComboBox<String>();
	@FXML private Button btn_submit_change = new Button();
	@FXML private TextField tf_inf_report = new TextField();
	

	// change literal stmt
	@FXML private TextField tf_subject = new TextField();
	@FXML private TextField tf_text = new TextField();
	@FXML private TextField tf_inference_report = new TextField();
	@FXML private ComboBox<String> cb_data_prop = new ComboBox<String>();
	@FXML private ComboBox<String> cb_datatype = new ComboBox<String>();
	@FXML private ComboBox<String> cb_lang_tag = new ComboBox<String>();
	@FXML private Button btn_sub_change = new Button();
	
	private static ObservableList<String> dataAndAnnotationProps = FXCollections.observableArrayList();
	private static ObservableList<String> datatype = FXCollections.observableArrayList();
	private static ObservableList<String> langTag = FXCollections.observableArrayList();
	private static ObservableList<String> objProps = FXCollections.observableArrayList();
	
	// Javafx Components
	@FXML private AnchorPane ap_edit_indiv_stmt = new AnchorPane();
	@FXML private AnchorPane ap_edit_literal_stmt = new AnchorPane();
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
	
	// Observable Lists
	public static ObservableList<String> concepts = FXCollections.observableArrayList();
	public static ObservableList<String> conceptSchemes = FXCollections.observableArrayList();
	public static ObservableList<String> collections = FXCollections.observableArrayList();
	public static ObservableList<String> ordCollections = FXCollections.observableArrayList();
	public static ObservableList<String> labels = FXCollections.observableArrayList();
	public static ObservableList<String> lists = FXCollections.observableArrayList();
	private static ObservableList<SKOSStatements> statementList = FXCollections.observableArrayList();
	
	private static OntModel model;
	private static HashMap<String, String> indMap = new HashMap<String, String>();
	
	private static int selected = -1;

	public void initialize(URL location, ResourceBundle resources) {
		
		tab_skos_browser.setOnSelectionChanged(new EventHandler<Event>() {
			public void handle(Event event) {
				OntologyExporter.createOWLDOC(model);
				File file = new File("./skos_browser_data/outputhtml");
				
				try {
					skos_browser.getEngine().load("file:///" + file.getCanonicalPath() + "/" + "index.html");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		cb_data_prop.setItems(dataAndAnnotationProps);
		cb_datatype.setItems(datatype);
		cb_lang_tag.setItems(langTag);
		tf_text.setAlignment(Pos.TOP_LEFT);
		
		cb_objprop.setItems(objProps);
		
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
		
		setupTableColumn(tc_subject);
		setupTableColumn(tc_predicate);
		setupTableColumn(tc_object);
		
		setupEditRemoveBtn(tc_remove);
		setupEditRemoveBtn(tc_edit);
		tv_statements.setItems(statementList);
	}
	
	public void setupTableColumn(TableColumn<SKOSStatements, String> tc) {
		tc.setCellFactory(new Callback<TableColumn<SKOSStatements,String>, TableCell<SKOSStatements,String>>() {
			public TableCell<SKOSStatements, String> call(TableColumn<SKOSStatements, String> param) {
				final TableCell<SKOSStatements, String> tableCell = new TableCell<SKOSStatements, String>() {
	                protected void updateItem(String item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (!empty && item != null) {
	                        setText(item);
	                        if(tc == tc_subject) {
	                        	Tooltip tooltip = new Tooltip(statementList.get(getIndex()).getStmt().getSubject().getURI());
	                        	setTooltip(tooltip);
	                        } else if(tc == tc_predicate) {
	                        	Tooltip tooltip = new Tooltip(statementList.get(getIndex()).getStmt().getPredicate().getURI());
	                        	setTooltip(tooltip);
	                        } else if(tc == tc_object) {
	                        	RDFNode r = statementList.get(getIndex()).getStmt().getObject();
	                        	if(!r.isLiteral()) {
	                        		Tooltip tooltip = new Tooltip(r.asResource().getURI());
		                        	setTooltip(tooltip);
	                        	} else {
	                        		Tooltip tooltip = new Tooltip(" \"" + r.toString() + "\"");
		                        	setTooltip(tooltip);
	                        	}
	                        }
	                        
	                    }else{
	                        setText(null);
	                    }
	                }
				};
				return tableCell;
			}
		});
	}
	
	public void setupEditRemoveBtn(TableColumn<SKOSStatements, Boolean> tc) {
		tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SKOSStatements, Boolean>, ObservableValue<Boolean>>() {
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<SKOSStatements, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
 
		tc.setCellFactory(new Callback<TableColumn<SKOSStatements, Boolean>, TableCell<SKOSStatements, Boolean>>() {
            public TableCell<SKOSStatements, Boolean> call(TableColumn<SKOSStatements, Boolean> p) {
            	if(tc == tc_remove) {
            		return new removeBtn(tc_remove);
            	} else {
            		return new editBtn(tc_edit);
            	}
            }
        });
	}
	
	// Method is called to clear textfields and comboboxes
	public void clearStmtFields() {
		tf_subject.clear();
		tf_text.clear();
		cb_datatype.getSelectionModel().select(null);
		cb_lang_tag.getSelectionModel().clearSelection();
	}
	
	private class removeBtn extends TableCell<SKOSStatements, Boolean> {
		Button cellButton = new Button("Delete");

		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
            if(!empty && t) {
            	if(model.getBaseModel().contains(statementList.get(getIndex()).getStmt())) {
                    setGraphic(cellButton);
                    setText(null);
                    setAlignment(Pos.CENTER);
            	} else {
            		setGraphic(null);
    				setAlignment(Pos.CENTER);
    				setTextFill(Color.GREEN);
    				setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
    				setText("Inferred");
            	}
			} else {
				setText(null);
				setGraphic(null);
			}
		}
         
        removeBtn(final TableColumn<SKOSStatements, Boolean> tc_remove){
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent t) {
                    int selectdIndex = getTableRow().getIndex();
                    Statement stmt = statementList.get(selectdIndex).getStmt();
                    statementList.remove(selectdIndex);
                    model.remove(stmt);
                }
            });
        }
    }
	
	private class editBtn extends TableCell<SKOSStatements, Boolean> {
        Button cellButton = new Button("Edit");
        
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty && t) {
            	if(model.getBaseModel().contains(statementList.get(getIndex()).getStmt())) {
                    setGraphic(cellButton);
                    setText(null);
                    setAlignment(Pos.CENTER);
            	} else {
            		setGraphic(null);
    				setAlignment(Pos.CENTER);
    				setTextFill(Color.GREEN);
    				setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
    				setText("Inferred");
            	}
			} else {
				setText(null);
				setGraphic(null);
			}
        }
         
        editBtn(final TableColumn<SKOSStatements, Boolean> tc_edit){ 
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent t) {
                	fillStmtEditor(getIndex());
                	tv_statements.getSelectionModel().select(getIndex());
                }
            });
        }

    }
	
	public void fillStmtEditor(int index) {
		clearStmtFields();
		// check if object is instance of class/object property/data property
		selected = index;
		Statement stmt = statementList.get(selected).getStmt();
		if(stmt.getObject().isLiteral()) {
			if(!ap_edit_literal_stmt.isVisible()) {
				ap_edit_literal_stmt.setVisible(true);
				ap_edit_indiv_stmt.setVisible(false);
			}
			try {
				tf_subject.setText(stmt.getSubject().getLocalName());
				tf_text.setText(stmt.getObject().asLiteral().getLexicalForm());
				cb_data_prop.getSelectionModel().select(stmt.getPredicate().getLocalName());
				String l = stmt.getObject().asLiteral().getLanguage();
				if(l.length() > 0) {
					if(!langTag.contains(l))
						langTag.add(l);
					cb_lang_tag.getSelectionModel().select(l);
				}
				DatatypeProperty tmpURI = model.createDatatypeProperty(stmt.getObject().asLiteral().getDatatypeURI());
				if(tmpURI != null) {
					cb_datatype.getSelectionModel().select(tmpURI.getLocalName());
				}
			} catch(Exception e) {
			}
		} else if(!stmt.getObject().isLiteral()) {
			try {
				tf_indiv_a.setText(stmt.getSubject().getLocalName());
				if(!ap_edit_indiv_stmt.isVisible()) {
					ap_edit_indiv_stmt.setVisible(true);
					ap_edit_literal_stmt.setVisible(false);
				}
			} catch(Exception e) {
			}
		}
	}
	
	@FXML private void submit_changes(ActionEvent event) {

	}
	
	// Getting OntModel (if available) automatically, when user switch to this tab
	public static void startSKOSOntologyController(OntModel ontModel) {
		model = ontModel;
		SKOSBuilder.setupDataTypes();
		SKOSBuilder.getSuperClassMembers(model);
		SKOSBuilder.getSuperObjPropMembers(model);
		SKOSBuilder.getSuperDatPropMembers(model);
		SKOSBuilder.getSuperAnnotPropMembers(model);
		SKOSBuilder.fillList(SKOSBuilder.datatypeMap, datatype);
		sortIndividuals();
		
		SKOSBuilder.fillList(SKOSBuilder.objPropMap, objProps);
		SKOSBuilder.fillList(SKOSBuilder.datPropMap, dataAndAnnotationProps);
		SKOSBuilder.fillList(SKOSBuilder.annotPropMap, dataAndAnnotationProps);
		
		// Sorting lists
		FXCollections.sort(datatype);
		FXCollections.sort(objProps);
		FXCollections.sort(dataAndAnnotationProps);
		langTag.addAll("en", "de", "fr", "it", "es");
	}
	
	public static void sortIndividuals() {
		Iterator<Entry<String, String>> it = SKOSBuilder.clsMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
			OntClass tmp = model.createClass(pairs.getValue());
			System.out.println(tmp.getURI());
			ExtendedIterator<? extends OntResource> ind = tmp.listInstances();
			while(ind.hasNext()) {
				Individual i = (Individual) ind.next();
				indMap.put(i.getLocalName(), i.getURI());
				switch (tmp.toString()) {
				case SKOSBuilder.concept:
					concepts.add(i.getLocalName());
					break;
				case SKOSBuilder.conceptScheme:
					conceptSchemes.add(i.getLocalName());
					break;
				case SKOSBuilder.collection:
					collections.add(i.getLocalName());
					break;
				case SKOSBuilder.ordCollection:
					ordCollections.add(i.getLocalName());
					break;
				case SKOSBuilder.label:
					labels.add(i.getLocalName());
					break;
				case SKOSBuilder.list:
					lists.add(i.getLocalName());
					break;
				default:
					break;
				}
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
								tv_statements.getItems().clear();
								statementList.clear();
								tv_statements.setItems(statementList);
								
								Individual i = model.getIndividual(indMap.get(listCell.getText()));
								StmtIterator iter = i.listProperties();
								while(iter.hasNext()) {
								    Statement stmt = iter.nextStatement();  // get next statement
								    statementList.add(new SKOSStatements(stmt));
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
