package model;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ImportManager implements Initializable{


	private static Model model;
	private static DatasetAccessor ds;
	private static OntDocumentManager mgr;
	private OntModelSpec spec;
	String serviceURI;
	String port = "3030";
	String initURL ="http://localhost:"; 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		model = ModelFactory.createDefaultModel();
		serviceURI = initURL+port;
		ds = DatasetAccessorFactory.createHTTP(serviceURI);
	}
	public static DatasetAccessor getDataAccesor() {
		return ds;
		// TODO Auto-generated method stub
		
	}
		private void setURI(String serviceURI) {
			// TODO Auto-generated method stub
			serviceURI = serviceURI;
		}
	
	public void importServer() throws Exception{
		// Here we can insert the uri of a named graph (needs setup of fuseki configuration file)
		model = ds.getModel();
		spec = new OntModelSpec( OntModelSpec.OWL_DL_MEM );
		spec.setDocumentManager(mgr);
//		ontmodel = ModelFactory.createOntologyModel(spec);
//		ontmodel.add(model);
	}
	

	
	// Use this method when you want to write back and update a model to server
	public void updateModelOfServer() {
		try {
			model = ModelFacadeTEST.ontModel;
			ds.add(model);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Use this method when you want to write back and replace a model to server
	public static void replaceModelOfServer() {
		try {
			model = ModelFacadeTEST.ontModel;
			ds.putModel(model);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Use this method to enter an alternative url location of a file
	public static void addAltEntry(String destination, String alternativePath) {
		mgr.addAltEntry(destination, alternativePath);
	}

	
}
