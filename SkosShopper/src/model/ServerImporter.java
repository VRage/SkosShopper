package model;

import java.util.ArrayList;
import java.util.Map;




import org.apache.jena.riot.RDFDataMgr;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class ServerImporter{


	public static Model model;
	public static DatasetAccessor ds;
	public static OntDocumentManager mgr;
	public static OntModelSpec spec;
	public static  String serviceURI = null;
	public static Map<String, String> uriMap;
	public static ArrayList<String> graphList;
	public static String graphURI = null;
	
	public ServerImporter() {
		model = ModelFactory.createDefaultModel();
		mgr = new OntDocumentManager();
		spec = new OntModelSpec(PelletReasonerFactory.THE_SPEC);
		spec.setDocumentManager(mgr);
	}
	
	public static boolean importNamedGraph(String grphURI) {
		try {
			graphURI = grphURI;
			model = ds.getModel(graphURI);
			uriMap = model.getNsPrefixMap();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void importDefaultGraph() {
		try {
			model = ds.getModel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void setServiceURI(String uri) {
		serviceURI = uri;
		// sesame server requires additional information since it is working with repositories
		if(serviceURI.contains("repositories")) {
			ds = DatasetAccessorFactory.createHTTP(serviceURI + "/rdf-graphs/service");
			model = ds.getModel("http://hs-ulm.de/rd_skos_test");
		} else {
			ds = DatasetAccessorFactory.createHTTP(serviceURI);
			model = ds.getModel("http://hs-ulm.de/rd_skos_test");
		}
	}
	
	// Use this method when you want to write back and update a model to server
	public static boolean updateModelOfServer() {
		try {
			model = ModelFacadeTEST.ontModel.getBaseModel();
			ds.add(graphURI, model);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Use this method when you want to write back and replace a model to server
	public static boolean replaceModelOfServer() {
		try {
			model = ModelFacadeTEST.ontModel.getBaseModel();
			ds.putModel(graphURI, model);
			return true;
		} catch(Exception e) {
		}
		return false;
	}
	
	// Use this method to enter an alternative url location of a file
	public void addAltEntry(String destination, String alternativePath) {
		mgr.addAltEntry(destination, alternativePath);
	}
	public static String sendQuery(String query) throws Exception{
		
			if(serviceURI != ""){
			Query graphQuery = QueryFactory.create(query);
			QueryEngineHTTP qeHttp = QueryExecutionFactory.createServiceRequest(serviceURI, graphQuery);
			ResultSet results = qeHttp.execSelect();
			return results.toString();
			}
			else
				throw new Exception("No Service URL was set");

		
		
	}
	public boolean queryServerGraphs() {
		try {
			// Tricky query because of different server
			String servURI = serviceURI;
			Query graphQuery = null;
			graphList = new ArrayList<String>();
			if(servURI.toLowerCase().contains("data")) {
				servURI = servURI.replaceAll("data", "sparql");
				graphQuery = QueryFactory.create("SELECT DISTINCT ?g WHERE { GRAPH ?g { } }");
			} else {
				graphQuery = QueryFactory.create("SELECT DISTINCT ?g WHERE { GRAPH ?g { ?x ?y ?z } }");
			}
			QueryEngineHTTP qeHttp = QueryExecutionFactory.createServiceRequest(servURI, graphQuery);
			ResultSet results = qeHttp.execSelect();
			
			while (results.hasNext()) {
				Resource individual = results.next().getResource("?g");
				if (individual.isURIResource()) {
					graphList.add(individual.toString());
					System.out.println(individual.toString());
				}
			}
			return true;
		} catch(Exception e) {
		}
		return false;
	}
}
