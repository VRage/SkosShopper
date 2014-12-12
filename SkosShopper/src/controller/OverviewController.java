package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;




import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Callback;




import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;




import model.ModelFacadeTEST;
import model.ServerImporter;




import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.riot.RiotException;
import org.apache.log4j.Logger;




import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;




import exceptions.fuseki_exceptions.NoDatasetAccessorException;

public class OverviewController implements Initializable {

	/* JAVAFX COMPONENTS */
	private RadioMenuItem btn_server_import, btn_file_import, btn_web_import;
	@FXML
	private ComboBox<String> cb_save_graph;
	@FXML
	private MenuButton btn_source;
	@FXML
	private TextArea ta_log_field;
	@FXML
	private TextField tf_dest_url, tf_curr_loaded_graph, tf_alt_url;
	@FXML
	private Button btn_add_entry, btn_save_graph;
	@FXML
	private TableView<String> tv_graph_uri;
	@FXML
	private TableView<AltEntriesManager> tv_alt_entries;
	@FXML private ListView<String> tv_models;
	@FXML private ToggleButton OverViewToggleButton;
	private TableColumn<OntModel, String> model_graph;
	@FXML
	private TableColumn<String, String> col_graph_uri;
	@FXML
	private TableColumn<AltEntriesManager, String> col_dest_url;
	@FXML
	private TableColumn<AltEntriesManager, String> col_alt_url;
	private ObservableList<AltEntriesManager> altEntryList = FXCollections
			.observableArrayList();
	private ObservableList<String> graphURIs = FXCollections
			.observableArrayList();
	private ObservableList<String> modelsLoaded = FXCollections
			.observableArrayList();
	private final ObservableList<String> saveModelTo = FXCollections
			.observableArrayList();
	@FXML
	private Button startStopFuseki;
	@FXML
	Button btnHome;
	@FXML
	private Label fusekiStatus;
	@FXML
	Label lblIndividuals;
	@FXML
	Label lblObjektProperties;
	@FXML
	Label lblDataProperties;
	@FXML
	Label lblClasses;
	@FXML
	Label OverviewlblState;
	@FXML
	WebView webView;
	@FXML
	TextField txtFieldURL;
	@FXML
	TextField OverviewtxtField;
	@FXML
	Button OverviewbtnLoadFromStorage;
	File localFile = null;
	public static boolean modelLoaded = false;
	ToggleGroup group = new ToggleGroup();

	WebEngine webEngine;
	WebHistory webHistory;

	public static final Logger log = Logger
			.getLogger(SkosEditorController.class);
	private ArrayList<Entry> browserHistory = new ArrayList<WebHistory.Entry>();
	public static OntDocumentManager mgr;

	public void initialize(URL fxmlPath, ResourceBundle resources) {
		// Initialize overview components
		OverViewToggleButton.setOnAction((event->
		{
			if(OverViewToggleButton.isSelected())
				OverViewToggleButton.setText("Method: Add to current");
			else 
				OverViewToggleButton.setText("Method: Override");
		}));
		col_alt_url.prefWidthProperty().bind(
				tv_alt_entries.widthProperty().multiply(0.5f));
		col_dest_url.prefWidthProperty().bind(
				tv_alt_entries.widthProperty().multiply(0.5f));
		col_alt_url.setCellValueFactory(cellData -> cellData.getValue()
				.altURLProperty());
		col_dest_url.setCellValueFactory(cellData -> cellData.getValue()
				.destURLProperty());

		tv_alt_entries.setItems(altEntryList);
		tv_graph_uri.setItems(graphURIs);
		
		ta_log_field.setEditable(false);
		tf_curr_loaded_graph.setStyle("-fx-text-inner-color: green;");
		tf_curr_loaded_graph.setEditable(false);
		saveModelTo.addAll("Add/Update Model from Server",
				"Replace Model from Server", "Save Model to File",
				"Discard Model");
		cb_save_graph.setItems(saveModelTo);

		// Helpful for copy pasterino url at beginning
		ta_log_field
				.setText("For copy pasterino:\nFuseki:\nhttp://i-ti-01.informatik.hs-ulm.de:3030/ds/data\nor sesame server:\nhttp://i-ti-01.informatik.hs-ulm.de:8080/openrdf-sesame/repositories/skos");

		setGraphTable();
		setMenuButtons();

		webEngine = webView.getEngine();
		webHistory = webEngine.getHistory();
		webHistory.setMaxSize(3);
		try {
			altEntryList.addAll(new DataSaver().loadEntries());
			tv_alt_entries.setItems(altEntryList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MenuItem mdel = new MenuItem("delete");
		mdel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				tv_alt_entries.getItems().remove(
				tv_alt_entries.getSelectionModel().getSelectedIndex());
			}
		});
		tv_graph_uri.setContextMenu(new ContextMenu(mdel));
		

	}

	@FXML
	public void adding_entry(ActionEvent event) {
		ta_log_field.clear();
		try {
			new URL(tf_dest_url.getText());
			altEntryList.add(new AltEntriesManager(tf_dest_url.getText(),
					tf_alt_url.getText()));
			ModelFacadeTEST.mgr.addAltEntry(tf_dest_url.getText(),
					tf_alt_url.getText());
		} catch (HttpException | MalformedURLException | RiotException e) {
			ta_log_field.appendText(e.getMessage());
		}
	}

	@FXML
	public void saveModelToBtnAction(ActionEvent event) {
		if (modelLoaded) {
			ta_log_field.clear();
			// send model back to server (add/update)
			if (cb_save_graph.getValue().equals(saveModelTo.get(0))) {
				// check if server is reachable
				if (checkServerConnection()) {
					ta_log_field.appendText("1. Trying to reach \""
							+ txtFieldURL.getText() + "\"\t... OK\n");
					// try to add/update model from server
					if (ServerImporter.updateModelOfServer()) {
						ta_log_field
								.appendText("2. Trying to add/update model: \""
										+ tf_curr_loaded_graph.getText()
										+ "\"\t... OK\n");
						ta_log_field.appendText("3. Transaction successful");
						// add/update was ok
						modelLoaded = false;
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"Unable to add/update model of server\nYou should store your model locally or retry",
										null, JOptionPane.WARNING_MESSAGE);
					}
				} else {
					ta_log_field.appendText("1. Trying to reach \""
							+ txtFieldURL.getText() + "\"\t... FAILED\n");
				}
				// send model back to server (replace)
			} else if (cb_save_graph.getValue().equals(saveModelTo.get(1))) {
				// check if server is reachable
				if (checkServerConnection()) {
					JOptionPane
							.showMessageDialog(
									null,
									"This will replace the model which is stored in server\nCannot be undone!",
									null, JOptionPane.WARNING_MESSAGE);
					ta_log_field.appendText("1. Trying to reach \""
							+ txtFieldURL.getText() + "\"\t... OK\n");
					// try to add/update model from server
					if (ServerImporter.replaceModelOfServer()) {
						ta_log_field
								.appendText("2. Trying to replace model: \""
										+ tf_curr_loaded_graph.getText()
										+ "\"\t... OK\n");
						ta_log_field.appendText("3. Transaction successful");
						// add/update was ok
						modelLoaded = false;
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"Unable to replace model of server\nYou should store your model locally or retry",
										null, JOptionPane.WARNING_MESSAGE);
					}
				} else {
					ta_log_field.appendText("1. Trying to reach \""
							+ txtFieldURL.getText() + "\"\t... FAILED\n");
				}
				// save model to file
			} else if (cb_save_graph.getValue().equals(saveModelTo.get(2))) {
				FileChooser  fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RDF file (*.rdf)", "*.rdf");
	              fileChooser.getExtensionFilters().add(extFilter);
				try {
					   File savedFile = fileChooser.showSaveDialog(null);
					   FileOutputStream fout = new FileOutputStream(savedFile);
						ModelFacadeTEST.getOntModel().write(fout);
						log.info("saving to file "+ savedFile.getAbsolutePath());
						modelLoaded = false;
				} catch (Exception e) {
					// TODO: handle exception
					log.error("FilePath is null !!");
				}
	           
				
				// Discard model
			} else if (cb_save_graph.getValue().equals(saveModelTo.get(3))) {
				System.out.println("Model is getting deleted");
				int result = JOptionPane.showConfirmDialog(null,
						"Changes will be lost, cannot be undone!",
						"Warning: Discard Model", JOptionPane.OK_CANCEL_OPTION);
				if(result==0)
				modelLoaded= false;
			}
		} else {
			// Warn the user that there is no model
			JOptionPane.showMessageDialog(null, "No model available to save",
					null, JOptionPane.WARNING_MESSAGE);
		}
	}

	@FXML
	private void OverviewbtnReloadDatasetOnAction(ActionEvent event)
			throws Exception {
		graphURIs.clear();
		if (!modelLoaded) {
			ta_log_field.clear();
			if (btn_server_import.isSelected()) {
				try {
					// check if URL is not malformed
					new URL(txtFieldURL.getText());
					ServerImporter imp = new ServerImporter();
					imp.setServiceURI(txtFieldURL.getText());

					// check if server is reachable
					if (checkServerConnection()) {
						ta_log_field.appendText("1. Trying to reach \""
								+ txtFieldURL.getText() + "\"\t... OK\n");
						// Query server for graphs
						if (imp.queryServerGraphs()) {
							ta_log_field
									.appendText("2. Querrying Server Data to to retrieve named graphs\t... OK\n");
							ta_log_field
									.appendText("3. Listing all founded named graphs\n");

							// add models to list of graphs
							for (int i = 0; i < ServerImporter.graphList.size(); i++) {
								if (!graphURIs
										.contains(ServerImporter.graphList
												.get(i))) {
									graphURIs.add(ServerImporter.graphList
											.get(i));
								}
							}
							// Transaction ok, load server page to web engine
							ta_log_field.appendText("4. Transaction done");
							URL url = new URL(ServerImporter.serviceURI);
							webEngine.load("http://" + url.getHost() + ":"
									+ url.getPort());

						} else {
							ta_log_field
									.appendText("2. Querrying Server Data to to retrieve named graphs\t... FAILED\n");
						}
					} else {
						ta_log_field.appendText("1. Trying to reach \""
								+ txtFieldURL.getText() + "\"\t... FAILED\n");
					}
				} catch (Exception e) {
					// not implemented yet
				}
			}
			if (btn_file_import.isSelected()) {
				
				FileChooser fileChooser = new FileChooser();

				fileChooser.setTitle("Open Resource File");
				localFile = fileChooser.showOpenDialog(null);
				ModelFacadeTEST.loadModelFromLocal(localFile);
				ta_log_field.setText(ModelFacadeTEST.modelToString());
				modelLoaded =true;

				//tv_models.getColumns().add(new TableColumn<OntModel,String>(ModelFacadeTEST.getOntModel().getGraph().toString()));
			}
			if (btn_web_import.isSelected()) {
				ModelFacadeTEST.loadModelFromWeb(txtFieldURL.getText());
				modelLoaded = true;
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"There is already a model in process!", null,
					JOptionPane.WARNING_MESSAGE);
		}
		OntModel searchModel = ModelFacadeTEST.getOntModel();
		Map <String,String>prefixMap=searchModel.getNsPrefixMap();
		prefixMap.forEach((Uri,Prefix)->{
			tv_models.getItems().clear();
			
			if(!tv_models.getItems().contains(Uri+"   "+Prefix))
			{
				tv_models.getItems().add(Uri+"   "+Prefix);
			}
		
		});

	
		
	}

	public void setGraphTable() {		
		col_graph_uri
				.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(
							CellDataFeatures<String, String> param) {
						return new ReadOnlyObjectWrapper<String>(param
								.getValue());
					}
				});

		col_graph_uri
				.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
					public TableCell<String, String> call(
							TableColumn<String, String> param) {
						final TableCell<String, String> tablecell = new TableCell<String, String>() {
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

						// Listener for table of graphs, double clicking a cell
						// will import a model from server
						tablecell
								.setOnMouseClicked(new EventHandler<MouseEvent>() {
									public void handle(MouseEvent event) {
										if (event.getButton().equals(
												MouseButton.PRIMARY)) {
											if (event.getClickCount() == 2
													&& tablecell.getText() != null) {
												if (!modelLoaded) {
													ta_log_field.clear();
													if (ServerImporter
															.importNamedGraph(tablecell
																	.getText())) {
														ta_log_field.appendText("1. Trying to load named graph: \""
																+ tablecell
																		.getText()
																+ "\"\t... OK");
														tf_curr_loaded_graph
																.setText(tablecell
																		.getText());
														try {
															ModelFacadeTEST
																	.loadModelFromServer(tablecell
																			.getText());
														} catch (NoDatasetAccessorException e) {
															// TODO
															// Auto-generated
															// catch block
														}
														modelLoaded = true;
													} else {
														ta_log_field.appendText("1. Trying to load named graph: \""
																+ tablecell
																		.getText()
																+ "\"\t... FAILED");
													}
												} else {
													warnModelLoaded();
												}
											}
										}
									}
								});
						return tablecell;
					}
				});
	}

	public void warnModelLoaded() {
		JOptionPane
				.showMessageDialog(
						null,
						"Another Model is currently in process! Please save the model or update the Model from Server",
						null, JOptionPane.WARNING_MESSAGE);
	}
	
	public boolean checkServerConnection() {
		try {
			URL url = new URL(txtFieldURL.getText());
			Socket s = new Socket(url.getHost(), url.getPort());
			System.out.println("IS ONLINE");
			s.close();
			return true;
		} catch (IOException ex) {
		}
		return false;
	}

	public void setMenuButtons() {
		group = new ToggleGroup();
		btn_server_import = new RadioMenuItem("Server Import");
		btn_file_import = new RadioMenuItem("File Import");
		btn_web_import = new RadioMenuItem("Web Import");
		btn_server_import.setToggleGroup(group);
		btn_file_import.setToggleGroup(group);
		btn_web_import.setToggleGroup(group);
		btn_source.getItems().add(btn_server_import);
		btn_source.getItems().add(btn_file_import);
		btn_source.getItems().add(btn_web_import);
	}

	@FXML
	void saveListbtnOnAction(ActionEvent event) throws JAXBException,
			IOException {
		try {
			DataSaver ds = new DataSaver();
			ds.SaveEntries(altEntryList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void tvGraphOnMouseClicked(MouseEvent event) {
		if (event.getButton().toString()=="SECONDARY") {
			System.out.println("left");
		tv_graph_uri.getContextMenu().show(tv_graph_uri,event.getScreenX(),event.getScreenY());	
		}
	}

}
