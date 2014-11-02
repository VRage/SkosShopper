package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ClassManagerController implements Initializable{

	@FXML TableView<OntClass> tableView = new TableView<OntClass>();
	@FXML TableColumn<OntClass, String> tableView_class;
	@FXML TableColumn<OntClass, String> tableView_desc;
	
	//defines date which will be shown in the table
	final ObservableList<OntClass> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//customized CellValueFactory for OntClass on Class Column
		tableView_class.setCellValueFactory(new Callback<CellDataFeatures<OntClass, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<OntClass, String> p) {
		    	 StringProperty classname = new SimpleStringProperty();
		    	 classname.set(p.getValue().getLocalName());
//		    	 StringProperty s = new SimpleStringProperty(this, classname);
		         return  classname;
		     }
		  });
		//customized CellValueFactory for OntClass on Description Column

		tableView_desc.setCellValueFactory(new Callback<CellDataFeatures<OntClass, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<OntClass, String> p) {
		    	 SimpleStringProperty s = new SimpleStringProperty();
		    	 s.set(p.getValue().getLabel(null));
		         return  s;
		     }
		  });
	
		tableView.setItems(data);
		getClassName();
	}
	
	public void getClassName(){
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read("./camera.owl");
		
		
		
	    ExtendedIterator classes = model.listClasses();
	    while(classes.hasNext()){
	        OntClass thisClass = (OntClass) classes.next();
	        data.add(thisClass);
	    	
	    }
	}
	
}