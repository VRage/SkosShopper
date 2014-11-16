package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

public class SKOSOntology {
	/* SKOS Classes */
    private OWLClass collection;
    private OWLClass concept;
    private OWLClass concept_scheme;
    private OWLClass label;
    private OWLClass ordered_collection;
    
    /* OBJECT PROPERTIES */
    // SKOS-XL Label Properties
    private OWLObjectProperty alt_label_skosxl;
    private OWLObjectProperty pref_label_skosxl;
    private OWLObjectProperty hidden_label_skosxl;
    private OWLObjectProperty label_realtion_skosxl;
    
    // SKOS Relation Properties
    private OWLObjectProperty semantic_relation;
    private OWLObjectProperty broader_transitive;
    private OWLObjectProperty broader;
    private OWLObjectProperty broad_match;
    private OWLObjectProperty narrower_transitive;
    private OWLObjectProperty narrower;
    private OWLObjectProperty narrow_match;
    private OWLObjectProperty related;
    private OWLObjectProperty related_match;
    private OWLObjectProperty mapping_realtion;
    private OWLObjectProperty close_match;
    private OWLObjectProperty exact_match;
    
    // SKOS Collection Properties
    private OWLObjectProperty member;
    private OWLObjectProperty member_list;
    
    // SKOS Concept Scheme Properties
    private OWLObjectProperty has_topconcept;
    private OWLObjectProperty in_scheme;
    private OWLObjectProperty top_conceptof;
    
    /* DATA PROPERTIES */
    // SKOS-XL Data Properties
    private OWLDataProperty literal_form;
    
    // Notation
    private OWLDataProperty notation;

    /* ANNOTATION PROPERTIES */
    private OWLAnnotationProperty note;
    private OWLAnnotationProperty change_note;
    private OWLAnnotationProperty definition;
    private OWLAnnotationProperty editorial_note;
    private OWLAnnotationProperty example;
    private OWLAnnotationProperty history_note;
    private OWLAnnotationProperty scope_note;
    
    /* ONTOLOGY HEADER */
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private IRI ontologyIRI, sk, sk_xl;
    private PrefixManager skos, skos_xl, ont;
    private PrefixOWLOntologyFormat pf;
    private File file;
	
    /* REASONER */
    private OWLReasonerFactory reasonerFactory;
    
    //public ArrayList<String> con;
    public ObservableList<String> con;
    
    /********************/
    /** 1. CONSTRUCTOR **/
    /********************/
    
	public SKOSOntology() {
		// Prefixes that are constantly needed for creating individuals
		sk = IRI.create("http://www.w3.org/2004/02/skos/core#");
		sk_xl = IRI.create("http://www.w3.org/2008/05/skos-xl#");
		skos = new DefaultPrefixManager(sk.toString());
		skos_xl = new DefaultPrefixManager(sk_xl.toString());
		reasonerFactory = new StructuralReasonerFactory();
//        skos = new DefaultPrefixManager(sk.toString());
//        skos_xl = new DefaultPrefixManager(sk_xl.toString());
        
//        // Individuals
//        OWLNamedIndividual cam = factory.getOWLNamedIndividual(":Camera", pm);
//        OWLNamedIndividual cam_label = factory.getOWLNamedIndividual(":cam", pm);
//        OWLDatatype literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
//        OWLLiteral literal = factory.getOWLLiteral("cam", literalDatatype);
//        OWL
//        OWLAxiom ax1 = factory.getOWLDataPropertyAssertionAxiom(literal_form, cam, literal);
//        OWLClassAssertionAxiom  classAssertion1 = factory.getOWLClassAssertionAxiom(concept, cam);
//        OWLClassAssertionAxiom  classAssertion2 = factory.getOWLClassAssertionAxiom(label, cam_label);
	}
	
	public OWLOntologyManager getManager() {
		return manager;
	}
	
	public OWLDataFactory getDataFactory() {
		return factory;
	}
	
	public OWLOntology getOntology() {
		return ontology;
	}
	
	public IRI getOntologyIRI() {
		return ontologyIRI;
	}
	
	public IRI getIRI(String individual) {
		return factory.getOWLNamedIndividual(individual, ont).getIRI();
	}
	
	public PrefixManager getPrefixManager() {
		return ont;
		
	}
	
	// Get all concept individual
	public ObservableList<String> getConcepts(ObservableList<String> concept) {
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner.getInstances(this.concept, true);
		Set<OWLNamedIndividual> individuals = conceptNodeSet.getFlattened();
		
		// Put all concept individual in list and return
		for(OWLNamedIndividual s: individuals) {
			String dp="";
			Set<OWLEntity> signatures = s.getSignature();
			for(OWLEntity sig : signatures) {
				dp = sig.getIRI().getFragment().toString();
				System.out.println(dp);
				concept.add(dp);
			}
		}
		return concept;
	}
	
	// Get all concept scheme individual
	public ObservableList<String> getConceptSchemes(ObservableList<String> conceptSchemes) {
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner.getInstances(this.concept_scheme, true);
		Set<OWLNamedIndividual> individuals = conceptNodeSet.getFlattened();
		
		// Put all concept scheme individual in list and return
		for(OWLNamedIndividual s: individuals) {
			String dp="";
			Set<OWLEntity> signatures = s.getSignature();
			for(OWLEntity sig : signatures) {
				dp = sig.getIRI().getFragment().toString();
				conceptSchemes.add(dp);
			}
		}
		return conceptSchemes;
	}
	
	// Get all collection individual
	public ObservableList<String> getCollections(ObservableList<String> collections) {
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner.getInstances(this.collection, true);
		Set<OWLNamedIndividual> individuals = conceptNodeSet.getFlattened();
		
		// Put all collection individual in list and return
		for(OWLNamedIndividual s: individuals) {
			String dp="";
			Set<OWLEntity> signatures = s.getSignature();
			for(OWLEntity sig : signatures) {
				dp = sig.getIRI().getFragment().toString();
				collections.add(dp);
			}
		}
		return collections;
	}
	
	// Get all ordered collection individual
	public ObservableList<String> getOrderedCollections(ObservableList<String> ordCollections) {
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner.getInstances(this.ordered_collection, true);
		Set<OWLNamedIndividual> individuals = conceptNodeSet.getFlattened();
		
		// Put all ordered collection individual in list and return
		for(OWLNamedIndividual s: individuals) {
			String dp="";
			Set<OWLEntity> signatures = s.getSignature();
			for(OWLEntity sig : signatures) {
				dp = sig.getIRI().getFragment().toString();
				ordCollections.add(dp);
			}
		}
		return ordCollections;
	}
	
	// Get all label individual
	public ObservableList<String> getLabels(ObservableList<String> labels) {
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner.getInstances(this.label, true);
		Set<OWLNamedIndividual> individuals = conceptNodeSet.getFlattened();
		
		// Put all label individual in list and return
		for(OWLNamedIndividual s: individuals) {
			String dp="";
			Set<OWLEntity> signatures = s.getSignature();
			for(OWLEntity sig : signatures) {
				dp = sig.getIRI().getFragment().toString();
				labels.add(dp);
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
    public void createNewOntology(String ont_iri) throws OWLOntologyCreationException {
    	ontologyIRI = IRI.create(ont_iri);
    	manager = OWLManager.createOWLOntologyManager();
    	ontology = manager.createOntology(ontologyIRI);
    	factory = manager.getOWLDataFactory();
    	ont = new DefaultPrefixManager(ontologyIRI.toString());
    	createAll();
    }
    
    // 2.2 Load a SKOS Ontology from local storage system
    public void getSKOSOntologyFromLocal(String path_name) throws OWLOntologyCreationException {
    	file = new File(path_name);
    	SimpleIRIMapper iriMapper =  new SimpleIRIMapper(IRI.create("url/Base.owl"),IRI.create(file));
    	ontologyIRI = IRI.create(file);
    	manager = OWLManager.createOWLOntologyManager();
    	manager.addIRIMapper(iriMapper);
    	ontology = manager.loadOntology(ontologyIRI);
    	factory = manager.getOWLDataFactory();
    	ont = new DefaultPrefixManager(ontologyIRI.toString()+ "#");
    	createAll();
    }
    
    // 2.3 Load a SKOS Ontology from Web
    public void getSKOSOntologyFromServer(String iri) throws OWLOntologyCreationException {
    	ontologyIRI = IRI.create(iri);
    	manager = OWLManager.createOWLOntologyManager();
    	ontology = manager.loadOntology(ontologyIRI);
    	factory = manager.getOWLDataFactory();
    	ont = new DefaultPrefixManager(ontologyIRI.toString()+ "#");
    	createAll();
    }
    
    // 2.4 Save an Ontology to local storage system
    public void saveOntologyLocal(String path_name, OWLOntologyFormat format) throws FileNotFoundException, OWLOntologyStorageException {
        File f = new File(path_name);
        FileOutputStream out = new FileOutputStream(f);
        //* NOTE: FORMAT NEEDS CHECK
        manager.saveOntology(ontology, out);
    }
    
    /***********************************************************************************************/
    /** 3. SKOS CLASSES, SKOS OBJECT PROPERTIES, SKOS DATA PROPERTIES, SKOS ANNOTATION PROPERTIES **/
    /** Will be initialized once - Instances of these represents an individual					  **/
    /***********************************************************************************************/
    
    private void createSKOSClasses() {
        collection = factory.getOWLClass(":Collection", skos);
        concept = factory.getOWLClass(":Concept", skos);
        concept_scheme = factory.getOWLClass(":ConceptScheme", skos);
        label = factory.getOWLClass(":Label", skos_xl);
        ordered_collection = factory.getOWLClass(":OrderedCollection", skos);
    }
    
    private void createSKOSObjectProperties() {
        // SKOS-XL Label Properties
        alt_label_skosxl = factory.getOWLObjectProperty("altLabel", skos_xl);
        pref_label_skosxl = factory.getOWLObjectProperty("prefLabel", skos_xl);
        hidden_label_skosxl = factory.getOWLObjectProperty("hiddenLabel", skos_xl);
        label_realtion_skosxl = factory.getOWLObjectProperty("labelRelation", skos_xl);
        
        // SKOS Relation Properties
        semantic_relation = factory.getOWLObjectProperty("semanticRelation", skos);
        broader_transitive = factory.getOWLObjectProperty("broaderTransitive",  skos);
        broader = factory.getOWLObjectProperty("broader", skos);
        broad_match = factory.getOWLObjectProperty("broadMatch", skos);
        narrower_transitive = factory.getOWLObjectProperty("narrowerTransitive", skos);
        narrower = factory.getOWLObjectProperty("narrower", skos);
        narrow_match = factory.getOWLObjectProperty("narrowMatch", skos);
        related = factory.getOWLObjectProperty("related", skos);
        related_match = factory.getOWLObjectProperty("relatedMatch", skos);
        mapping_realtion = factory.getOWLObjectProperty("mappingRelation", skos);
        close_match = factory.getOWLObjectProperty("closeMatch", skos);
        exact_match = factory.getOWLObjectProperty("exactMatch", skos);
        
        // SKOS Collection Properties
        member = factory.getOWLObjectProperty("member", skos);
        member_list = factory.getOWLObjectProperty("memberList", skos);
        
        // SKOS Concept Scheme Properties
        has_topconcept = factory.getOWLObjectProperty("hasTopConcept", skos);
        in_scheme = factory.getOWLObjectProperty("inScheme", skos);
        top_conceptof = factory.getOWLObjectProperty("topConceptOf", skos);
    }
    
    private void createSKOSDataProperties() {
        // SKOS-XL Data Properties
        literal_form = factory.getOWLDataProperty("literalForm", skos_xl);
        
        // Notation
        notation = factory.getOWLDataProperty("notation", skos);
    }
    
    private void createSKOSAnnotationProperties() {
        /* SKOS ANNOTATION PROPERTIES */
        note = factory.getOWLAnnotationProperty("note", skos);
        change_note = factory.getOWLAnnotationProperty("changeNote", skos);
        definition = factory.getOWLAnnotationProperty("definition", skos);
        editorial_note = factory.getOWLAnnotationProperty("editorial_note", skos);
        example = factory.getOWLAnnotationProperty("example", skos);
        history_note = factory.getOWLAnnotationProperty("historyNote", skos);
        scope_note = factory.getOWLAnnotationProperty("scopeNote", skos);
    }
    
    private void createAll() {
    	createSKOSClasses();
    	createSKOSObjectProperties();
    	createSKOSDataProperties();
    	createSKOSAnnotationProperties();
    }
    
    /**********************************************************************/
    /** 4. Create instances of #3 in order to represent SKOS individuals **/
    /**********************************************************************/
    
    /***********************************/
    /** 4.1 INSTANCES OF SKOS CLASSES **/
    /***********************************/
    
	// skos:Concept Individual
	public OWLClassAssertionAxiom createConcept(String conceptName) {
		// 1. Create an Individiual
		OWLNamedIndividual conceptIndividual = factory.getOWLNamedIndividual(conceptName, ont);
		// 2. Assert a type to individual
		OWLClassAssertionAxiom  classAssertion = factory.getOWLClassAssertionAxiom(concept, conceptIndividual);
		// 3. Assert individual to ontology
		manager.addAxiom(ontology, classAssertion);
		return classAssertion;
	}
	
	// skos:Collection Individual
	public void createCollection(String collectionName) {
		// 1. Create an Individiual
		OWLNamedIndividual coll = factory.getOWLNamedIndividual(collectionName, ont);
		// 2. Assert a type to individual
		OWLClassAssertionAxiom  classAssertion = factory.getOWLClassAssertionAxiom(collection, coll);
		// 3. Assert individual to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:ConceptScheme Individual
	public void createConceptScheme(String conceptSchemeName) {
		// 1. Create an Individiual
		OWLNamedIndividual conceptScheme = factory.getOWLNamedIndividual(conceptSchemeName, ont);
		// 2. Assert a type to individual
		OWLClassAssertionAxiom  classAssertion = factory.getOWLClassAssertionAxiom(concept_scheme, conceptScheme);
		// 3. Assert individual to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:OrderedCollection Individual
	public void createOrderedCollection(String orderedCollection) {
		// 1. Create an Individiual
		OWLNamedIndividual ordCollection = factory.getOWLNamedIndividual(orderedCollection, ont);
		// 2. Assert a type to individual
		OWLClassAssertionAxiom  classAssertion = factory.getOWLClassAssertionAxiom(ordered_collection, ordCollection);
		// 3. Assert individual to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos-xl:Label Individual
	public void createLabel(String label) {
		// 1. Create an Individiual
		OWLNamedIndividual lbl = factory.getOWLNamedIndividual(label, ont);
		// 2. Assert a type to individual
		OWLClassAssertionAxiom  classAssertion = factory.getOWLClassAssertionAxiom(this.label, lbl);
		// 3. Assert individual to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	

    /*********************************************/
    /** 4.2 INSTANCES OF SKOS OBJECT PROPERTIES **/
    /*********************************************/
	
	// skos-xl:altLabel Individual
	public void hasAltLabel(String individual, String altLabel) {
		// 1. Create an Individiual
		OWLNamedIndividual altlbl = factory.getOWLNamedIndividual(altLabel, ont);
		// 2. Assert alternative label to class skos:Label
		OWLClassAssertionAxiom classAssertion1 = factory.getOWLClassAssertionAxiom(label, altlbl);
		// 3. Assert alternative label to any other individual
		OWLObjectPropertyAssertionAxiom  classAssertion2 = factory.
				getOWLObjectPropertyAssertionAxiom(alt_label_skosxl, factory.
						getOWLNamedIndividual(individual, ont), altlbl);
		// 4. Assert to ontology
		manager.addAxiom(ontology, classAssertion1);
		manager.addAxiom(ontology, classAssertion2);
	}
	
	// skos-xl:prefLabel Individual
	public void hasPrefLabel(String individual, String prefLabel) {
		// 1. Create an Individiual
		OWLNamedIndividual preflbl = factory.getOWLNamedIndividual(prefLabel, ont);
		// 2. Assert prefered label to class skos:Label
		OWLClassAssertionAxiom classAssertion1 = factory.getOWLClassAssertionAxiom(label, preflbl);
		// 3. Assert prefered label to any other individual
		OWLObjectPropertyAssertionAxiom  classAssertion2 = factory.
				getOWLObjectPropertyAssertionAxiom(pref_label_skosxl, factory.
						getOWLNamedIndividual(individual, ont), preflbl);
		// 4. Assert to ontology
		manager.addAxiom(ontology, classAssertion1);
		manager.addAxiom(ontology, classAssertion2);
	}
	
	// skos-xl:hiddenLabel Individual
	public void hasHiddenLabel(String individual, String hiddenLabel) {
		// 1. Create an Individiual
		OWLNamedIndividual hidlbl = factory.getOWLNamedIndividual(hiddenLabel, ont);
		// 2. Assert hidden label to class skos:Label
		OWLClassAssertionAxiom classAssertion1 = factory.getOWLClassAssertionAxiom(label, hidlbl);
		// 3. Assert hidden label to any other individual
		OWLObjectPropertyAssertionAxiom  classAssertion2 = factory.
				getOWLObjectPropertyAssertionAxiom(hidden_label_skosxl, factory.
						getOWLNamedIndividual(individual, ont), hidlbl);
		// 4. Assert to ontology
		manager.addAxiom(ontology, classAssertion1);
		manager.addAxiom(ontology, classAssertion2);
	}
	
    /*********************************************************/
    /** 4.2.1 INSTANCES OF SKOS OBJECT PROPERTIES - RELATION**/
    /*********************************************************/
	
	// skos-xl:labelRelation
	public void hasRelatedLabel(String label1, String label2) {
		// 1. Assert label1 related to label2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(label_realtion_skosxl, factory.
						getOWLNamedIndividual(label1, ont), factory.getOWLNamedIndividual(label2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:semanticRelation
	public void isInSemanticRelationWith(String concept1, String concept2) {
		// 1. Assert concept1 is in semantic relation to concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(semantic_relation, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:broaderTransitive
	public void hasBroaderTransitive(String concept1, String concept2) {
		// 1. Assert that concept1 has broader transitive concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(broader_transitive, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:broader
	public void hasBroader(String concept1, String concept2) {
		// 1. Assert that concept1 has broader concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(broader, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:broadMatch
	public void hasBroaderMatch(String concept1, String concept2) {
		// 1. Assert that concept1 has broader match concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(broad_match, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:narrowerTransitive
	public void hasNarrowerTransitive(String concept1, String concept2) {
		// 1. Assert that concept1 has narrower transitive concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(narrower_transitive, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:narrower
	public void hasNarrower(String concept1, String concept2) {
		// 1. Assert that concept1 has narrower concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(narrower, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:narrowMatch
	public void hasNarrowMatch(String concept1, String concept2) {
		// 1. Assert that concept1 has narrower match concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(narrow_match, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:related
	public void hasRelated(String concept1, String concept2) {
		// 1. Assert that concept1 has related concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(related, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:relatedMatch
	public void hasRelatedMatch(String concept1, String concept2) {
		// 1. Assert that concept1 has related match concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(related_match, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:mappingRelation
	public void isInMappingRelationWith(String concept1, String concept2) {
		// 1. Assert that concept1 is in mapping relation with concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(mapping_realtion, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:closeMatch
	public void hasCloseMatch(String concept1, String concept2) {
		// 1. Assert that concept1 has close match concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(close_match, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:exactMatch
	public void hasExactMatch(String concept1, String concept2) {
		// 1. Assert that concept1 has exact match concept2
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(exact_match, factory.
						getOWLNamedIndividual(concept1, ont), factory.getOWLNamedIndividual(concept2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
    /************************************************************/
    /** 4.2.2 INSTANCES OF SKOS OBJECT PROPERTIES - COLLECTION **/
    /************************************************************/
	
	// skos:member
	public void hasMember(String individual1, String individual2) {
		// 1. Assert that collection has a member
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(member, factory.
						getOWLNamedIndividual(individual1, ont), factory.getOWLNamedIndividual(individual2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:memberList
	public void hasMemberList(String individual1, String individual2) {
		// 1. No idea what it does?!
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(member_list, factory.
						getOWLNamedIndividual(individual1, ont), factory.getOWLNamedIndividual(individual2, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
    /*****************************************************************/
    /** 4.2.3 INSTANCES OF SKOS OBJECT PROPERTIES - Concept Schemes **/
    /*****************************************************************/
	
	// skos:hasTopConcept
	public void hasTopConcept(String conceptScheme, String concept) {
		// 1. Assert that concept scheme has top concept
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(has_topconcept, factory.
						getOWLNamedIndividual(conceptScheme, ont), factory.getOWLNamedIndividual(concept, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:inScheme
	public void isInScheme(String concept, String conceptScheme) {
		// 1. Assert that concept is in scheme with concept scheme
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(in_scheme, factory.
						getOWLNamedIndividual(concept, ont), factory.getOWLNamedIndividual(conceptScheme, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
	// skos:topConceptOf 
	public void isTopConceptOf(String concept, String conceptScheme) {
		// 1. Assert that concept is top concept of concept scheme
		OWLObjectPropertyAssertionAxiom  classAssertion = factory.
				getOWLObjectPropertyAssertionAxiom(top_conceptof, factory.
						getOWLNamedIndividual(concept, ont), factory.getOWLNamedIndividual(conceptScheme, ont));
		// 2. Assert to ontology
		manager.addAxiom(ontology, classAssertion);
	}
	
    /*****************************************/
    /** 5 INSTANCES OF SKOS DATA PROPERTIES **/
    /*****************************************/
	
	// skos-xl:literalForm
	public void hasLiteralForm(String label, String literal, String lang, int choice) {
		 OWLDatatype literalDatatype = null;
		 
		 if(choice < 0) {
			 OWLLiteral lit = factory.getOWLLiteral(literal, lang);
			 OWLAxiom ax1 = factory.getOWLDataPropertyAssertionAxiom(literal_form, 
					 factory.getOWLNamedIndividual(label, ont), lit);
			 manager.addAxiom(ontology, ax1);
		 } else {
			 switch(choice) {
			 	case 0:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.OWL_RATIONAL.getIRI());
			 		break;
			 	case 1:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.OWL_REAL.getIRI());
			 		break;
			 	case 2:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDF_PLAIN_LITERAL.getIRI());
			 		break;
			 	case 3:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDF_XML_LITERAL.getIRI());
			 		break;
			 	case 4:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
			 		break;
			 	case 5:
			 		// Will be used for image of products
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_ANY_URI.getIRI());
			 		break;
			 	case 6:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BASE_64_BINARY.getIRI());
			 		break;
			 	case 7:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
			 		break;
			 	case 8:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BYTE.getIRI());
			 		break;
			 	case 9:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
			 		break;
			 	case 10:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
			 		break;
			 	case 11:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
			 		break;
			 	case 12:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DOUBLE.getIRI());
			 		break;
			 	case 13:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
			 		break;
			 	case 14:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_HEX_BINARY.getIRI());
			 		break;
			 	case 15:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
			 		break;
			 	case 16:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
			 		break;
			 	case 17:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_LANGUAGE.getIRI());
			 		break;
			 	case 18:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_LONG.getIRI());
			 		break;
			 	case 19:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NAME.getIRI());
			 		break;
			 	case 20:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NCNAME.getIRI());
			 		break;
			 	case 21:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NEGATIVE_INTEGER.getIRI());
			 		break;
			 	case 22:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NMTOKEN.getIRI());
			 		break;
			 	case 23:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI());
			 		break;
			 	case 24:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI());
			 		break;
			 	case 25:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NORMALIZED_STRING.getIRI());
			 		break;
			 	case 26:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
			 		break;
			 	case 27:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_SHORT.getIRI());
			 		break;
			 	case 28:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
			 		break;
			 	case 29:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_TOKEN.getIRI());
			 		break;
			 	case 30:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_BYTE.getIRI());
			 		break;
			 	case 31:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
			 		break;
			 	case 32:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_LONG.getIRI());
			 		break;
			 	case 33:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_SHORT.getIRI());
			 		break;
			 	default:
			 		break;
			 }
			 
			 if(literalDatatype != null) {
			 		OWLLiteral lit = factory.getOWLLiteral(literal, literalDatatype);
			 		OWLAxiom ax1 = factory.getOWLDataPropertyAssertionAxiom(literal_form, factory.
			 				getOWLNamedIndividual(label, ont), lit);
			 		manager.addAxiom(ontology, ax1);
			 }
		 }
	}
	
    /***********************************************/
    /** 5 INSTANCES OF SKOS ANNOTATION PROPERTIES **/
    /***********************************************/
	
	public void hasNote(String individual, String annotation, int annotation_form, String lang, int choice) {
		OWLDatatype literalDatatype = null;
		 if(choice < 0) {
			 OWLAnnotation anno = null;
			 OWLLiteral lit = factory.getOWLLiteral(annotation, lang);
			 
			 switch(annotation_form) {
			 	case 0:
			 		anno = factory.getOWLAnnotation(note, lit);
			 		break;
			 	case 1:
			 		anno = factory.getOWLAnnotation(change_note, lit);
			 		break;
			 	case 2:
			 		anno = factory.getOWLAnnotation(definition, lit);
			 		break;
			 	case 3:
			 		anno = factory.getOWLAnnotation(editorial_note, lit);
			 		break;
			 	case 4:
			 		anno = factory.getOWLAnnotation(example, lit);
			 		break;
			 	case 5:
			 		anno = factory.getOWLAnnotation(history_note, lit);
			 		break;
			 	case 6:
			 		anno = factory.getOWLAnnotation(scope_note, lit);
			 		break;
			 	default:
			 		break;
			 }
			 OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(factory.
		        		getOWLNamedIndividual(individual, ont).getIRI(),anno);
			 manager.addAxiom(ontology, ax);
		 } else {
			 switch(choice) {
			 	case 0:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.OWL_RATIONAL.getIRI());
			 		break;
			 	case 1:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.OWL_REAL.getIRI());
			 		break;
			 	case 2:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDF_PLAIN_LITERAL.getIRI());
			 		break;
			 	case 3:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDF_XML_LITERAL.getIRI());
			 		break;
			 	case 4:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
			 		break;
			 	case 5:
			 		// Will be used for image of products
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_ANY_URI.getIRI());
			 		break;
			 	case 6:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BASE_64_BINARY.getIRI());
			 		break;
			 	case 7:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
			 		break;
			 	case 8:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_BYTE.getIRI());
			 		break;
			 	case 9:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
			 		break;
			 	case 10:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
			 		break;
			 	case 11:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
			 		break;
			 	case 12:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DOUBLE.getIRI());
			 		break;
			 	case 13:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
			 		break;
			 	case 14:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_HEX_BINARY.getIRI());
			 		break;
			 	case 15:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
			 		break;
			 	case 16:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
			 		break;
			 	case 17:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_LANGUAGE.getIRI());
			 		break;
			 	case 18:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_LONG.getIRI());
			 		break;
			 	case 19:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NAME.getIRI());
			 		break;
			 	case 20:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NCNAME.getIRI());
			 		break;
			 	case 21:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NEGATIVE_INTEGER.getIRI());
			 		break;
			 	case 22:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NMTOKEN.getIRI());
			 		break;
			 	case 23:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI());
			 		break;
			 	case 24:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI());
			 		break;
			 	case 25:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_NORMALIZED_STRING.getIRI());
			 		break;
			 	case 26:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
			 		break;
			 	case 27:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_SHORT.getIRI());
			 		break;
			 	case 28:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
			 		break;
			 	case 29:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_TOKEN.getIRI());
			 		break;
			 	case 30:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_BYTE.getIRI());
			 		break;
			 	case 31:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
			 		break;
			 	case 32:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_LONG.getIRI());
			 		break;
			 	case 33:
			 		literalDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_SHORT.getIRI());
			 		break;
			 	default:
			 		break;
			 }
			 
			 if(literalDatatype != null) {
				 OWLAnnotation anno = null;
			 		OWLLiteral lit = factory.getOWLLiteral(annotation, literalDatatype);
			 		
					 switch(annotation_form) {
					 	case 0:
					 		anno = factory.getOWLAnnotation(note, lit);
					 		break;
					 	case 1:
					 		anno = factory.getOWLAnnotation(change_note, lit);
					 		break;
					 	case 2:
					 		anno = factory.getOWLAnnotation(definition, lit);
					 		break;
					 	case 3:
					 		anno = factory.getOWLAnnotation(editorial_note, lit);
					 		break;
					 	case 4:
					 		anno = factory.getOWLAnnotation(example, lit);
					 		break;
					 	case 5:
					 		anno = factory.getOWLAnnotation(history_note, lit);
					 		break;
					 	case 6:
					 		anno = factory.getOWLAnnotation(scope_note, lit);
					 		break;
					 	default:
					 		break;
					 }
					 
					 OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(factory.
				        		getOWLNamedIndividual(individual, ont).getIRI(),anno);
					 manager.addAxiom(ontology, ax);
			 }
		 }
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		SKOSOntology skos = new SKOSOntology();
		skos.getSKOSOntologyFromLocal("C:/Users/rd/Desktop/skosss.owl");
		ObservableList<String> concept = FXCollections.observableArrayList();
		skos.getConcepts(concept);
	}
}
