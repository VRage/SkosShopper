package model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import controller.SKOSOntologyController;
import controller.SkosEditorController;
import exceptions.fuseki_exceptions.NoDatasetAccessorException;



public class ModelFacadeTEST implements Initializable
{
	public enum ModelState {Server, WEB, LOCAL};
	static public ModelState aktState = ModelState.Server;
	//static Model model = ModelFactory.createDefaultModel();
	static OntModel ontModel = ModelFactory.createOntologyModel();
	public static OntDocumentManager mgr = new OntDocumentManager();
	static OntModelSpec ontSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
	ObservableList <OntModel>modelList = FXCollections.observableArrayList();
	//static OntModelSpec ontSpec = new OntModelSpec(PelletReasonerFactory.THE_SPEC);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ontSpec.setDocumentManager(mgr);
	}
	
	public static final Logger log = Logger.getLogger(ModelFacadeTEST.class);
	
	public static String[] getStates(){
		String [] result = new String[ModelState.values().length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ModelState.values()[i].toString();
			System.out.println(result[i]);
		}
		return result;
	}
	public static void setState(ModelState ms){
		// TODO Auto-generated method stub
		aktState = ms;
	}
	public  static void loadModelFromLocal(File file) throws IOException {
		// TODO Auto-generated method stub
		Path input = Paths.get(file.getAbsolutePath());
		ontModel = ModelFactory.createOntologyModel(ontSpec);

		if(file.getAbsolutePath().contains(".rdf")){

			ontModel.read(input.toUri().toString(),"RDF/XML");
			log.info("RDF File loaded");
		}
		else if(file.getAbsolutePath().contains(".ttl")){
			ontModel.read(input.toUri().toString(),"TURTLE");
		}
		else {
//			ontModel.read(filePath,"TURTLE");
			ontModel.read(input.toUri().toString());
			log.info("TURTLE File loaded");
		}
		setState(ModelState.LOCAL);
		notifyAllControllerSart();
		ExtendedIterator<OntClass> oclasslist = ontModel.listClasses();
		while (oclasslist.hasNext()) {

				OntClass oclass = (OntClass) oclasslist.next();
			
		}
		
	}
	public  static void loadModelFromWeb(String filePath) {
		// TODO Auto-generated method stub
		System.out.println(filePath);
		ontModel = ModelFactory.createOntologyModel();
		ontModel.read(filePath);
		
		//ontModel.write(System.out);


	}
	
	public static void notifyAllControllerEnd() {
		ontModel = SkosEditorController.endSKOSController();
	}

	public static void notifyAllControllerSart() {
		// All controller will be notified so they can start to get this model and work with it
		SkosEditorController.startSKOSController(ontModel);
		SKOSOntologyController.startSKOSOntologyController(ontModel);
	}
	
	public static void loadModelFromServer(String graphURI) throws NoDatasetAccessorException{
		setState(ModelState.Server);
		ontModel = ModelFactory.createOntologyModel(ServerImporter.spec, ServerImporter.model);
		ontModel.setNsPrefixes(ServerImporter.uriMap);
		notifyAllControllerSart();
		
		
		ExtendedIterator<OntClass> oclasslist = ontModel.listClasses();
		while (oclasslist.hasNext()) {

				OntClass oclass = (OntClass) oclasslist.next();
			
		}
	}
	
	public static OntModel getOntModel (){
		return ontModel;
	}
	public static void  setModel(Model m) {
		// TODO Auto-generated method stub
		ontModel = ModelFactory.createOntologyModel();
		ontModel.add(m);
	}
	public static String modelToString(){
//		String result="";
//		model.read(result);
//		return result;
		OutputStream output = new OutputStream()
	    {
	        private StringBuilder string = new StringBuilder();
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }

	        //Netbeans IDE automatically overrides this toString()
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    ontModel.write(output);
	    System.out.println("############################################"+output.toString());
		return output.toString();
	}
	public static ObservableList Strins() {
		// TODO Auto-generated method stub
			Map <String,String> mapoo= ontModel.getNsPrefixMap();
			ObservableList<Entry> result= FXCollections.observableArrayList();
			result.addAll(mapoo.entrySet());
			return result;
			
	}
}
