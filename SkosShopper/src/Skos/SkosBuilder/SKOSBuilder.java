package Skos.SkosBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.ObservableList;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class SKOSBuilder {
	
	// SKOS Classes
	public final static String concept = "http://www.w3.org/2004/02/skos/core#Concept";
	public final static String conceptScheme = "http://www.w3.org/2004/02/skos/core#ConceptScheme";
	public final static String collection = "http://www.w3.org/2004/02/skos/core#Collection";
	public final static String ordCollection = "http://www.w3.org/2004/02/skos/core#OrderedCollection";
	public final static String label = "http://www.w3.org/2008/05/skos-xl#Label";
	public final static String list = "http://www.w3.org/1999/02/22-rdf-syntax-ns#List";
	
	public static Map<String, String> clsMap = new HashMap<String, String>();
	public static Map<String, String> objPropMap = new HashMap<String, String>();
	public static Map<String, String> datPropMap = new HashMap<String, String>();
	public static Map<String, String> annotPropMap = new HashMap<String, String>();
	public static Map<String, String> datatypeMap = new HashMap<String, String>();
	
	// Gets members of skos super classes, super properties (object properties and data properties)
	public static void getSuperClassMembers(OntModel model) {
		ExtendedIterator<OntClass> it = model.listClasses();
		while(it.hasNext()) {
			OntClass tmpCls = it.next();
			try {
				if(tmpCls.getURI().contains("skos") || tmpCls.getURI().contains("rdf") && tmpCls.getURI() != null) {
					putInMap(tmpCls.getURI(), clsMap);
				}
			} catch(Exception e) {
			}
		}
	}
	
	public static void getSuperObjPropMembers(OntModel model) {
		ExtendedIterator<? extends ObjectProperty> it = model.listObjectProperties();
		while(it.hasNext()) {
			ObjectProperty op = it.next();
			if(op.getURI().contains("skos"))
				putInMap(op.getURI(), objPropMap);
		}
	}
	
	public static void getSuperDatPropMembers(OntModel model) {
		ExtendedIterator<? extends OntProperty> props = model.listDatatypeProperties();
		while(props.hasNext()) {
			OntProperty datProp = props.next();
			if(datProp.getURI().contains("skos")) {
				putInMap(datProp.getURI(), datPropMap);
			}	
		}
	}
	
	public static void getSuperAnnotPropMembers(OntModel model) {
		ExtendedIterator<? extends AnnotationProperty> it = model.listAnnotationProperties();
		while(it.hasNext()) {
			AnnotationProperty ap = it.next();
			if(ap.getURI().contains("skos"))
				putInMap(ap.getURI(), annotPropMap);
		}
	}
	
	public static void setupDataTypes() {
		TypeMapper tm = new TypeMapper();
		XSDDatatype.loadXSDSimpleTypes(tm);
		Iterator<RDFDatatype> it = tm.listTypes();
		
		while(it.hasNext()) {
			RDFDatatype dt = it.next();
			putInMap(dt.getURI(), datatypeMap);
		}
	}
	
	public static void putInMap(String uri, Map<String, String> map) {
		String[] s = uri.split("#");
		if(!map.containsKey(s[1]))
			map.put(s[1], uri);
	}
	
	public static void fillList(Map<String, String> map, ObservableList<String> list) {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
			list.add(pairs.getKey());
		}
	}
	
	
}
