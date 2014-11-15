package model;

import java.util.Map;

import javafx.collections.ObservableList;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class SKOSOntology {
	/* SKOS Classes */
	private OntClass collection;
	private OntClass concept;
	private OntClass concept_scheme;
	private OntClass label;
	private OntClass ordered_collection;

	/* OBJECT PROPERTIES */
	// SKOS-XL Label Properties
	private ObjectProperty alt_label_skosxl;
	private ObjectProperty pref_label_skosxl;
	private ObjectProperty hidden_label_skosxl;
	private ObjectProperty label_realtion_skosxl;

	// SKOS Relation Properties
	private ObjectProperty semantic_relation;
	private ObjectProperty broader_transitive;
	private ObjectProperty broader;
	private ObjectProperty broad_match;
	private ObjectProperty narrower_transitive;
	private ObjectProperty narrower;
	private ObjectProperty narrow_match;
	private ObjectProperty related;
	private ObjectProperty related_match;
	private ObjectProperty mapping_realtion;
	private ObjectProperty close_match;
	private ObjectProperty exact_match;

	// SKOS Collection Properties
	private ObjectProperty member;
	private ObjectProperty member_list;

	// SKOS Concept Scheme Properties
	private ObjectProperty has_topconcept;
	private ObjectProperty in_scheme;
	private ObjectProperty top_conceptof;

	/* DATA PROPERTIES */
	// SKOS-XL Data Properties
	private DatatypeProperty literal_form;

	// Notation
	private DatatypeProperty notation;

	/* ANNOTATION PROPERTIES */
	private AnnotationProperty note;
	private AnnotationProperty change_note;
	private AnnotationProperty definition;
	private AnnotationProperty editorial_note;
	private AnnotationProperty example;
	private AnnotationProperty history_note;
	private AnnotationProperty scope_note;

	/* ONTOLOGY HEADER */
	private OntModel model;
	private Ontology ont;
	private String baseURI;
	private OntModelSpec spec;
	private Map<String, String> prefixMap;

	// basic prefixes
	final private String sk = "http://www.w3.org/2004/02/skos/core";
	final private String skxl = "http://www.w3.org/2008/05/skos-xl";
	final private String skos = "http://www.w3.org/2004/02/skos/core#";
	final private String skos_xl = "http://www.w3.org/2008/05/skos-xl#";

	/* REASONER */

	// public ArrayList<String> con;
	public ObservableList<String> con;

	/********************/
	/** 1. CONSTRUCTOR **/
	/********************/

	public SKOSOntology() {
		// Prefixes that are constantly needed for creating individuals
	}

	// Get all concept individual
	public ObservableList<String> getConcepts(ObservableList<String> concept) {
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			if(this.concept.getURI().toString().equals(i.getRDFType().toString())) {
				concept.add(i.getLocalName());
			}
		}
		return concept;
	}

	// Get all concept scheme individual
	public ObservableList<String> getConceptSchemes(ObservableList<String> conceptSchemes) {
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			if(this.concept_scheme.getURI().equals(i.getRDFType().toString())) {
				conceptSchemes.add(i.getLocalName());
			}
		}
		return conceptSchemes;
	}

	// Get all collection individual
	public ObservableList<String> getCollections(ObservableList<String> collections) {
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			if(this.collection.getURI().equals(i.getRDFType().toString())) {
				collections.add(i.getLocalName());
			}
		}
		return collections;
	}

	// Get all ordered collection individual
	public ObservableList<String> getOrderedCollections(ObservableList<String> ordCollections) {
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			if(this.ordered_collection.getURI().equals(i.getRDFType().toString())) {
				ordCollections.add(i.getLocalName());
			}
		}
		return ordCollections;
	}

	// Get all label individual
	public ObservableList<String> getLabels(ObservableList<String> labels) {
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			if(this.label.getURI().equals(i.getRDFType().toString())) {
				labels.add(i.getLocalName());
			}
		}
		return labels;
	}

	public void deleteIndividual(String individual) {
		// NOT IMPLEMENTED YET
	}

	/**********************************************************************/
	/** 2. LOADING/CREATING A SKOS ONTOLOGY, CONFIGURING HEADER, IMPORTS **/
	/**********************************************************************/

	// 2.1 Create a SKOS Ontology from scratch
	public void createNewOntology(String ont_uri, int ont_spec) {
		// set the base uri
		baseURI = ont_uri + "#";

		switch (ont_spec) {
		case 0:
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
			break;
		case 1:
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
			break;
		case 2:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
			break;
		case 3:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
			break;
		case 4:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
			break;
		case 5:
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM);
			break;
		case 6:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_LITE_MEM_RDFS_INF);
			break;
		case 7:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_LITE_MEM_RULES_INF);
			break;
		case 8:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_LITE_MEM_TRANS_INF);
			break;
		case 9:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
			break;
		case 10:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_MINI_RULE_INF);
			break;
		case 11:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
			break;
		case 12:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
			break;
		case 13:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);
			break;
		case 14:
			model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
			break;
		case 15:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
			break;
		case 16:
			model = ModelFactory
					.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);
			break;
		default:
			break;
		}

		// Add base uri to ontology-header
		ont = model.createOntology(baseURI);

		// Add skos-xl Import to ontology-header
		ont.addImport(model.createResource(skxl));

		// Add skos and skos-xl prefixes to ontology-header
		model.setNsPrefix("skos", skos);
		model.setNsPrefix("skos-xl", skos_xl);

		// Add prefixes to prefix-map
		prefixMap = model.getNsPrefixMap();
		
		// Add base uri to ontology-header
		ont = model.createOntology(baseURI);

		createAll();
	}

	// 2.2 Load a SKOS Ontology from local storage system
	public void getSKOSOntologyFromLocal(String path_name) {
		Model m = FileManager.get().loadModel(path_name);
		model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, m);

		baseURI = model.getNsPrefixURI("");
		
		// Add skos-xl Import to ontology-header
		ont = model.createOntology(baseURI);
		ont.addImport(model.createResource(skxl));
		model.setNsPrefix("skos", skos);
		// check if Online File contains prefix for skos and skos-xl
		if(model.getNsPrefixURI("skos") == null) {
			model.setNsPrefix("skos", skos);
		}
		if(model.getNsPrefixURI("skos-xl") == null) {
			model.setNsPrefix("skos-xl", skos_xl);
		}
		
		
		createAll();
	}

	// 2.3 Load a SKOS Ontology from Web
	public void getSKOSOntologyFromServer(String uri) {
		// Set base uri
		baseURI = uri;
		
		// read Online SKOS into model
		model = ModelFactory.createOntologyModel();
		model.read(baseURI);

		// Add skos-xl Import to ontology-header
		ont = model.createOntology(baseURI);
		ont.addImport(model.createResource(skxl));

		// check if Online File contains prefix for skos and skos-xl
		if(model.getNsPrefixURI("skos") == null) {
			model.setNsPrefix("skos", skos);
		}
		if(model.getNsPrefixURI("skos-xl") == null) {
			model.setNsPrefix("skos-xl", skos_xl);
		}

		// Add prefixes to prefix-map
		prefixMap = model.getNsPrefixMap();
		createAll();
	}
	
	// 2.4 Get SKOS Ontology from fuseki
	public void getSKOSFromFuseki() {
		String serviceURI = "http://localhost:3030/ds/data";
		DatasetAccessor ds = DatasetAccessorFactory.createHTTP(serviceURI);
		Model m = ModelFactory.createDefaultModel();
		m = ds.getModel();
		
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, m);
		baseURI = model.getNsPrefixURI("");
		
		// Add skos-xl Import to ontology-header
		ont = model.createOntology(baseURI);
		ont.addImport(model.createResource(skxl));
		
		// check if Online File contains prefix for skos and skos-xl
		if(model.getNsPrefixURI("skos") == null) {
			model.setNsPrefix("skos", skos);
		}
		if(model.getNsPrefixURI("skos-xl") == null) {
			model.setNsPrefix("skos-xl", skos_xl);
		}
		System.out.println(baseURI);
		createAll();
	}

	// 2.5 Save an Ontology to local storage system
	public void saveOntologyLocal() {

	}
	
	public OntModel getModel() {
		return model;
	}
	
	public void setModel(OntModel model) {
		this.model = model;
	}

	/***********************************************************************************************/
	/**
	 * 3. SKOS CLASSES, SKOS OBJECT PROPERTIES, SKOS DATA PROPERTIES, SKOS
	 * ANNOTATION PROPERTIES
	 **/
	/** Will be initialized once - Instances of these represents an individual **/
	/***********************************************************************************************/

	private void createSKOSClasses() {
		concept = model.createClass(skos + "Concept");
		concept_scheme = model.createClass(skos + "ConceptScheme");
		collection = model.createClass(skos + "Collection");
		ordered_collection = model.createClass(skos + "OrderedCollection");
		label = model.createClass(skos_xl + "Label");
	}

	private void createSKOSObjectProperties() {
		// SKOS-XL Label Properties
		alt_label_skosxl = model.createObjectProperty(skos_xl + "altLabel");
		pref_label_skosxl = model.createObjectProperty(skos_xl + "prefLabel");
		hidden_label_skosxl = model.createObjectProperty(skos_xl
				+ "hiddenLabel");
		label_realtion_skosxl = model.createObjectProperty(skos_xl
				+ "labelRelation");

		// SKOS Relation Properties
		semantic_relation = model.createObjectProperty(skos
				+ "semanticRelation");
		broader_transitive = model.createObjectProperty(skos
				+ "broaderTransitive");
		broader = model.createObjectProperty(skos + "broader");
		broad_match = model.createObjectProperty(skos + "broadMatch");
		narrower_transitive = model.createObjectProperty(skos
				+ "narrowerTransitive");
		narrower = model.createObjectProperty(skos + "narrower");
		narrow_match = model.createObjectProperty(skos + "narrowMatch");
		related = model.createObjectProperty(skos + "related");
		related_match = model.createObjectProperty(skos + "relatedMatch");
		mapping_realtion = model.createObjectProperty(skos + "mappingRelation");
		close_match = model.createObjectProperty(skos + "closeMatch");
		exact_match = model.createObjectProperty(skos + "exactMatch");

		// SKOS Collection Properties
		member = model.createObjectProperty(skos + "member");
		member_list = model.createObjectProperty(skos + "memberList");

		// SKOS Concept Scheme Properties
		has_topconcept = model.createObjectProperty(skos + "hasTopConcept");
		in_scheme = model.createObjectProperty(skos + "inScheme");
		top_conceptof = model.createObjectProperty(skos + "topConceptOf");
	}

	private void createSKOSDataProperties() {
		// SKOS-XL Data Properties
		literal_form = model.createDatatypeProperty(skos_xl + "literalForm");

		// Notation
		notation = model.createDatatypeProperty(skos + "notation");
	}

	private void createSKOSAnnotationProperties() {
		/* SKOS ANNOTATION PROPERTIES */
		note = model.createAnnotationProperty(skos + "note");
		change_note = model.createAnnotationProperty(skos + "changeNote");
		definition = model.createAnnotationProperty(skos + "definition");
		editorial_note = model
				.createAnnotationProperty(skos + "editorial_note");
		example = model.createAnnotationProperty(skos + "example");
		history_note = model.createAnnotationProperty(skos + "historyNote");
		scope_note = model.createAnnotationProperty(skos + "scopeNote");
	}

	private void createAll() {
		createSKOSClasses();
		createSKOSObjectProperties();
		createSKOSDataProperties();
		createSKOSAnnotationProperties();
	}

	// Returns true if Ontology contains an individual, else it returns false
	public boolean contains(String individual) {
		return (model.getIndividual(baseURI + individual) != null) ? true
				: false;
	}

	/**********************************************************************/
	/** 4. Create instances of #3 in order to represent SKOS individuals **/
	/**********************************************************************/

	/***********************************/
	/** 4.1 INSTANCES OF SKOS CLASSES **/
	/***********************************/

	// skos:Concept Individual
	public void createConcept(String conceptName) {
		// Create an Individiual (instance of skos:Concept) and assert to
		// Ontology
		concept.createIndividual(baseURI + conceptName);
	}

	// skos:Collection Individual
	public void createCollection(String collectionName) {
		// Create an Individiual (instance of skos:Collection) and assert to
		// Ontology
		collection.createIndividual(baseURI + collectionName);
	}

	// skos:ConceptScheme Individual
	public void createConceptScheme(String conceptSchemeName) {
		// Create an Individiual (instance of skos:ConceptScheme) and assert to
		// Ontology
		concept_scheme.createIndividual(baseURI + conceptSchemeName);
	}

	// skos:OrderedCollection Individual
	public void createOrderedCollection(String orderedCollection) {
		// Create an Individiual (instance of skos:OrderedCollection) and assert
		// to Ontology
		ordered_collection.createIndividual(baseURI + orderedCollection);
	}

	// skos-xl:Label Individual
	public void createLabel(String label) {
		// Create an Individiual (instance of skos-xl:Label) and assert to
		// Ontology
		this.label.createIndividual(baseURI + label);
	}

	/*********************************************/
	/** 4.2 INSTANCES OF SKOS OBJECT PROPERTIES **/
	/*********************************************/

	// skos-xl:altLabel Individual
	public void hasAltLabel(String individual, String altLabel) {
		if (model.getIndividual(baseURI + altLabel) == null) {
			label.createIndividual(baseURI + altLabel);
		}
		model.getIndividual(baseURI + individual).addProperty(alt_label_skosxl,
				model.getIndividual(baseURI + altLabel));
	}

	// skos-xl:prefLabel Individual
	public void hasPrefLabel(String individual, String prefLabel) {
		if (model.getIndividual(baseURI + prefLabel) == null) {
			label.createIndividual(baseURI + prefLabel);
		}
		model.getIndividual(baseURI + individual).addProperty(
				pref_label_skosxl, model.getIndividual(baseURI + prefLabel));
	}

	// skos-xl:hiddenLabel Individual
	public void hasHiddenLabel(String individual, String hiddenLabel) {
		if (model.getIndividual(baseURI + hiddenLabel) == null) {
			label.createIndividual(baseURI + hiddenLabel);
		}
		model.getIndividual(baseURI + individual)
				.addProperty(hidden_label_skosxl,
						model.getIndividual(baseURI + hiddenLabel));
	}

	/*********************************************************/
	/** 4.2.1 INSTANCES OF SKOS OBJECT PROPERTIES - RELATION **/
	/*********************************************************/

	// skos-xl:labelRelation
	public void hasRelatedLabel(String label1, String label2) {
		if (!contains(label1)) {
			concept.createIndividual(baseURI + label1);
		}
		if (!contains(label2)) {
			concept.createIndividual(baseURI + label2);
		}
		model.getIndividual(baseURI + label1).addProperty(
				label_realtion_skosxl, model.getIndividual(baseURI + label2));
	}

	// skos:semanticRelation
	public void isInSemanticRelationWith(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(semantic_relation,
				model.getIndividual(baseURI + concept2));
	}

	// skos:broaderTransitive
	public void hasBroaderTransitive(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(broader_transitive,
				model.getIndividual(baseURI + concept2));
	}

	// skos:broader
	public void hasBroader(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(broader,
				model.getIndividual(baseURI + concept2));
	}

	// skos:broadMatch
	public void hasBroaderMatch(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(broad_match,
				model.getIndividual(baseURI + concept2));
	}

	// skos:narrowerTransitive
	public void hasNarrowerTransitive(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(
				narrower_transitive, model.getIndividual(baseURI + concept2));
	}

	// skos:narrower
	public void hasNarrower(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(narrower,
				model.getIndividual(baseURI + concept2));
	}

	// skos:narrowMatch
	public void hasNarrowMatch(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(narrow_match,
				model.getIndividual(baseURI + concept2));
	}

	// skos:related
	public void hasRelated(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(related,
				model.getIndividual(baseURI + concept2));
	}

	// skos:relatedMatch
	public void hasRelatedMatch(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(related_match,
				model.getIndividual(baseURI + concept2));
	}

	// skos:mappingRelation
	public void isInMappingRelationWith(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(mapping_realtion,
				model.getIndividual(baseURI + concept2));
	}

	// skos:closeMatch
	public void hasCloseMatch(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(close_match,
				model.getIndividual(baseURI + concept2));
	}

	// skos:exactMatch
	public void hasExactMatch(String concept1, String concept2) {
		if (!contains(concept1)) {
			concept.createIndividual(baseURI + concept1);
		}
		if (!contains(concept2)) {
			concept.createIndividual(baseURI + concept2);
		}
		model.getIndividual(baseURI + concept1).addProperty(exact_match,
				model.getIndividual(baseURI + concept2));
	}

	/************************************************************/
	/** 4.2.2 INSTANCES OF SKOS OBJECT PROPERTIES - COLLECTION **/
	/************************************************************/

	// skos:member
	public void hasMember(String collection, String member) {
		if (!contains(collection)) {
			this.collection.createIndividual(baseURI + collection);
		}
		if (!contains(member)) {
			concept.createIndividual(baseURI + member);
		}
		model.getIndividual(baseURI + collection).addProperty(this.member,
				model.getIndividual(baseURI + member));
	}

	// skos:memberList
	public void hasMemberList(String individual1, String individual2) {
		// not implemented yet
	}

	/*****************************************************************/
	/** 4.2.3 INSTANCES OF SKOS OBJECT PROPERTIES - Concept Schemes **/
	/*****************************************************************/

	// skos:hasTopConcept
	public void hasTopConcept(String conceptScheme, String concept) {
		if (!contains(conceptScheme)) {
			concept_scheme.createIndividual(baseURI + conceptScheme);
		}
		if (!contains(concept)) {
			this.concept.createIndividual(baseURI + concept);
		}
		model.getIndividual(baseURI + conceptScheme).addProperty(
				has_topconcept, model.getIndividual(baseURI + concept));
	}

	// skos:inScheme
	public void isInScheme(String concept, String conceptScheme) {
		if (!contains(concept)) {
			this.concept.createIndividual(baseURI + concept);
		}
		if (!contains(conceptScheme)) {
			concept_scheme.createIndividual(baseURI + conceptScheme);
		}
		model.getIndividual(baseURI + concept).addProperty(in_scheme,
				model.getIndividual(baseURI + conceptScheme));
	}

	// skos:topConceptOf
	public void isTopConceptOf(String concept, String conceptScheme) {
		if (!contains(concept)) {
			this.concept.createIndividual(baseURI + concept);
		}
		if (!contains(conceptScheme)) {
			concept_scheme.createIndividual(baseURI + conceptScheme);
		}
		model.getIndividual(baseURI + concept).addProperty(top_conceptof,
				model.getIndividual(baseURI + conceptScheme));
	}

	/*****************************************/
	/** 5 INSTANCES OF SKOS DATA PROPERTIES **/
	/*****************************************/

	// skos-xl:literalForm
	public void hasLiteralForm(String label, String literal, String lang,
			int choice) {
		Literal litt = null;

		if (!contains(label)) {
			this.label.createIndividual(baseURI + label);
		}

		if (choice < 0) {
			Literal lit = ResourceFactory.createLangLiteral(literal, lang);
			model.getIndividual(baseURI + label).addLiteral(literal_form, lit);
		} else {

			switch (choice) {
			case 0:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDanyURI);
				break;
			case 1:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDbase64Binary);
				break;
			case 2:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDboolean);
				break;
			case 3:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDbyte);
				break;
			case 4:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDdate);
				break;
			case 5:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDdateTime);
				break;
			case 6:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDdecimal);
				break;
			case 7:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDdouble);
				break;
			case 8:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDduration);
				break;
			case 9:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDENTITY);
				break;
			case 10:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDfloat);
				break;
			case 11:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDgDay);
				break;
			case 12:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDgMonth);
				break;
			case 13:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDgMonthDay);
				break;
			case 14:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDgYear);
				break;
			case 15:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDgYearMonth);
				break;
			case 16:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDhexBinary);
				break;
			case 17:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDID);
				break;
			case 18:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDIDREF);
				break;
			case 19:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDint);
				break;
			case 20:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDinteger);
				break;
			case 21:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDlanguage);
				break;
			case 22:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDlong);
				break;
			case 23:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDName);
				break;
			case 24:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDNCName);
				break;
			case 25:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDnegativeInteger);
				break;
			case 26:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDNMTOKEN);
				break;
			case 27:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDnonNegativeInteger);
				break;
			case 28:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDnonPositiveInteger);
				break;
			case 29:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDnormalizedString);
				break;
			case 30:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDNOTATION);
				break;
			case 31:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDpositiveInteger);
				break;
			case 32:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDQName);
				break;
			case 33:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDshort);
				break;
			case 34:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDstring);
				break;
			case 35:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDtime);
				break;
			case 36:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDtoken);
				break;
			case 37:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDunsignedByte);
				break;
			case 38:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDunsignedInt);
				break;
			case 39:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDunsignedLong);
				break;
			case 40:
				litt = ResourceFactory.createTypedLiteral(literal,
						XSDDatatype.XSDunsignedShort);
				break;
			default:
				break;
			}
			model.getIndividual(baseURI + label).addLiteral(literal_form, litt);
		}
	}

	/***********************************************/
	/** 5 INSTANCES OF SKOS ANNOTATION PROPERTIES **/
	/***********************************************/

	public void hasNote(String individual, String annotation,
			int annotation_form, String lang, int choice) {
		Literal anno2 = null;

		if (!contains(individual)) {
			this.label.createIndividual(baseURI + individual);
		}

		if (choice < 0) {
			Literal anno1 = ResourceFactory.createLangLiteral(annotation, lang);

			switch (annotation_form) {
			case 0:
				model.getIndividual(baseURI + label).addLiteral(note, anno1);
				break;
			case 1:
				model.getIndividual(baseURI + label).addLiteral(change_note,
						anno1);
				break;
			case 2:
				model.getIndividual(baseURI + label).addLiteral(definition,
						anno1);
				break;
			case 3:
				model.getIndividual(baseURI + label).addLiteral(editorial_note,
						anno1);
				break;
			case 4:
				model.getIndividual(baseURI + label).addLiteral(example, anno1);
				break;
			case 5:
				model.getIndividual(baseURI + label).addLiteral(history_note,
						anno1);
				break;
			case 6:
				model.getIndividual(baseURI + label).addLiteral(scope_note,
						anno1);
				break;
			default:
				break;
			}

		} else {
			switch (choice) {
			case 0:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDanyURI);
				break;
			case 1:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDbase64Binary);
				break;
			case 2:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDboolean);
				break;
			case 3:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDbyte);
				break;
			case 4:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDdate);
				break;
			case 5:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDdateTime);
				break;
			case 6:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDdecimal);
				break;
			case 7:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDdouble);
				break;
			case 8:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDduration);
				break;
			case 9:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDENTITY);
				break;
			case 10:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDfloat);
				break;
			case 11:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDgDay);
				break;
			case 12:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDgMonth);
				break;
			case 13:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDgMonthDay);
				break;
			case 14:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDgYear);
				break;
			case 15:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDgYearMonth);
				break;
			case 16:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDhexBinary);
				break;
			case 17:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDID);
				break;
			case 18:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDIDREF);
				break;
			case 19:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDint);
				break;
			case 20:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDinteger);
				break;
			case 21:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDlanguage);
				break;
			case 22:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDlong);
				break;
			case 23:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDName);
				break;
			case 24:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDNCName);
				break;
			case 25:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDnegativeInteger);
				break;
			case 26:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDNMTOKEN);
				break;
			case 27:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDnonNegativeInteger);
				break;
			case 28:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDnonPositiveInteger);
				break;
			case 29:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDnormalizedString);
				break;
			case 30:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDNOTATION);
				break;
			case 31:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDpositiveInteger);
				break;
			case 32:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDQName);
				break;
			case 33:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDshort);
				break;
			case 34:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDstring);
				break;
			case 35:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDtime);
				break;
			case 36:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDtoken);
				break;
			case 37:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDunsignedByte);
				break;
			case 38:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDunsignedInt);
				break;
			case 39:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDunsignedLong);
				break;
			case 40:
				anno2 = ResourceFactory.createTypedLiteral(annotation,
						XSDDatatype.XSDunsignedShort);
				break;
			default:
				break;
			}
			switch (annotation_form) {
			case 0:
				model.getIndividual(baseURI + label).addLiteral(note, anno2);
				break;
			case 1:
				model.getIndividual(baseURI + label).addLiteral(change_note,
						anno2);
				break;
			case 2:
				model.getIndividual(baseURI + label).addLiteral(definition,
						anno2);
				break;
			case 3:
				model.getIndividual(baseURI + label).addLiteral(editorial_note,
						anno2);
				break;
			case 4:
				model.getIndividual(baseURI + label).addLiteral(example, anno2);
				break;
			case 5:
				model.getIndividual(baseURI + label).addLiteral(history_note,
						anno2);
				break;
			case 6:
				model.getIndividual(baseURI + label).addLiteral(scope_note,
						anno2);
				break;
			default:
				break;
			}
		}
	}

	public static void main(String[] args) {

	}
}
