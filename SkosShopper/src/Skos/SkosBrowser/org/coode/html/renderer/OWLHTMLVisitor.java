/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.renderer;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLUnaryPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLFacet;

import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLConstants;
import Skos.SkosBrowser.org.coode.html.url.OWLObjectURLRenderer;
import Skos.SkosBrowser.org.coode.html.util.URLUtils;
import Skos.SkosBrowser.org.coode.owl.util.ModelUtil;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 12, 2008<br><br>
 */
public class OWLHTMLVisitor implements OWLObjectVisitor {

    private Logger logger = Logger.getLogger(getClass().getName());

    // These should match the css class names
    private static final String CSS_DEPRECATED = "deprecated";
    private static final String CSS_ACTIVE_ENTITY = "active-entity";
    private static final String CSS_KEYWORD = "keyword";
    private static final String CSS_ONTOLOGY_URI = "ontology-uri";
    private static final String CSS_ACTIVE_ONTOLOGY_URI = "active-ontology-uri";
    private static final String CSS_SOME = "some";
    private static final String CSS_ONLY = "only";
    private static final String CSS_VALUE = "value";
    private static final String CSS_LITERAL = "literal";
    private static final String CSS_ANNOTATION_URI = "annotation-uri";


    private Writer out;

    private URL pageURL = null;

    private OWLObjectURLRenderer urlRenderer;

    private ShortFormProvider sfProvider;

    private OntologyIRIShortFormProvider ontologyIriSFProvider;

    private Set<OWLOntology> ontologies = new HashSet<OWLOntology>();

    private OWLOntology activeOntology = null;

    private int indent = 0;

    private OWLHTMLConstants.LinkTarget targetWindow = null;


    public OWLHTMLVisitor(OWLObjectURLRenderer urlRenderer,
                          ShortFormProvider sfProvider,
                          Writer out) {
        this.urlRenderer = urlRenderer;
        this.sfProvider = sfProvider;
        this.ontologyIriSFProvider = new OntologyIRIShortFormProvider();
        this.out = out;
    }

    /**
     * Sets the current page URL. All links will be rendered relative to this page
     * @param pageURL
     */
    public void setPageURL(URL pageURL){
        this.pageURL = pageURL;
    }

    public void setOntologies(Set<OWLOntology> ontologies){
        this.ontologies = ontologies;
    }

    public void setActiveOntology(OWLOntology activeOnt){
        this.activeOntology = activeOnt;
    }

    /**
     * Sets the indentation level of subsequent lines used when wrapping some expressions
     * @param indent
     */
    public void setIndentation(int indent){
        this.indent = indent;
    }

    /**
     * Sets the target window of any links - in case frames or popups are required
     * @param targetWindow
     */
    public void setContentTargetWindow(OWLHTMLConstants.LinkTarget targetWindow){
        this.targetWindow = targetWindow;
    }

    private void write(String s) {
        try {
            out.write(s);
        }
        catch (IOException e) {
            logger.error(e);
        }
    }

    ////////// Ontology
    public void visit(OWLOntology ontology) {
        final URL urlForOntology = urlRenderer.getURLForOWLObject(ontology);
        String link = urlForOntology.toString();
        String cssClass = CSS_ONTOLOGY_URI;
        if (activeOntology != null && ontology.equals(activeOntology)){
            cssClass = CSS_ACTIVE_ONTOLOGY_URI;
        }

        boolean writeLink = false;

        if (pageURL == null){
            writeLink = true;
        }
        else{
            if (!pageURL.equals(urlForOntology)){
                link = URLUtils.createRelativeURL(pageURL, urlForOntology);
                writeLink = true;
            }
            else{
                write("<span class='" + cssClass + "'>");
                write(ontologyIriSFProvider.getShortForm(ontology));
                write("</span>");
            }
        }

        if (writeLink){
            write("<a class='" + cssClass + "'");
            write(" href=\"" + link + "\" title='" + ontology.getOntologyID() + "'");
            if (targetWindow != null){
                write(" target=\"" + targetWindow + "\"");
            }
            write(">");
            write(ontologyIriSFProvider.getShortForm(ontology));
            write("</a>");
        }

        write(" <span style='color:gray;'>(" +
              "c:" + ontology.getClassesInSignature().size() +
              ", op:" + ontology.getObjectPropertiesInSignature().size() +
              ", dp:" + ontology.getDataPropertiesInSignature().size() +
              ", i:" + ontology.getIndividualsInSignature().size() +
              ")</span>");
    }


    ////////// Entities

    public void visit(OWLClass desc) {
        writeOWLEntity(desc);
    }

    public void visit(OWLDataProperty property) {
        writeOWLEntity(property);
    }

    public void visit(OWLObjectProperty property) {
        writeOWLEntity(property);
    }

    public void visit(OWLAnnotationProperty property) {
        writeOWLEntity(property);
    }

    public void visit(OWLNamedIndividual individual) {
        writeOWLEntity(individual);
    }

    public void visit(OWLDatatype datatype) {
        writeOWLEntity(datatype);
    }

    public void visit(IRI iri) {
        writeIRIWithBoldFragment(iri, iri.getFragment());
    }

    public void visit(OWLAnonymousIndividual individual) {
        write(individual.getID().toString());
    }

    ///////// Anonymous classes

    public void visit(OWLObjectSomeValuesFrom desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.SOME.toString(), CSS_SOME);
        write(" ");
        writeOp(desc.getFiller());
    }

    public void visit(OWLObjectAllValuesFrom desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.ONLY.toString(), CSS_ONLY);
        write(" ");
        writeOp(desc.getFiller());
    }

    public void visit(OWLObjectHasValue desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.VALUE.toString(), CSS_VALUE);
        write(" ");
        writeOp(desc.getValue());
    }

    public void visit(OWLObjectMinCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.MIN.toString());
    }

    public void visit(OWLObjectExactCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.EXACTLY.toString());
    }

    public void visit(OWLObjectMaxCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.MAX.toString());
    }

    public void visit(OWLObjectComplementOf desc) {
        writeKeyword(ManchesterOWLSyntax.NOT.toString());
        write(" ");
        writeOp(desc.getOperand());
    }

    public void visit(OWLObjectHasSelf desc) {
        writeKeyword(ManchesterOWLSyntax.SELF.toString());
    }

    public void visit(OWLObjectIntersectionOf desc) {
        writeKeywordOpList(orderOps(desc.getOperands()), ManchesterOWLSyntax.AND.toString(), true);
    }

    public void visit(OWLObjectUnionOf desc) {
        writeKeywordOpList(orderOps(desc.getOperands()), ManchesterOWLSyntax.OR.toString(), false);
    }

    public void visit(OWLObjectOneOf desc) {
        write("{");
        writeOpList(desc.getIndividuals(), ", ", false);
        write("}");
    }

    public void visit(OWLDataOneOf desc) {
        write("{");
        writeOpList(desc.getValues(), ", ", false);
        write("}");
    }


    public void visit(OWLDataSomeValuesFrom desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.SOME.toString(), CSS_SOME);
        write(" ");
        writeOp(desc.getFiller());
    }

    public void visit(OWLDataAllValuesFrom desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.ONLY.toString(), CSS_ONLY);
        write(" ");
        writeOp(desc.getFiller());
    }

    public void visit(OWLDataHasValue desc) {
        desc.getProperty().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.VALUE.toString(), CSS_VALUE);
        write(" ");
        writeOp(desc.getValue());
    }

    public void visit(OWLDataMinCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.MIN.toString());
    }

    public void visit(OWLDataExactCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.EXACTLY.toString());
    }

    public void visit(OWLDataMaxCardinality desc) {
        writeCardinality(desc, ManchesterOWLSyntax.MAX.toString());
    }

    public void visit(OWLDatatypeRestriction node) {
        node.getDatatype().accept(this);
        write(" [");
        writeOpList(node.getFacetRestrictions(), ", ", false);
        write("]");
    }


    public void visit(OWLFacetRestriction node) {
        writeKeyword(writeFacet(node.getFacet()));
        node.getFacetValue().accept(this);
    }

    public void visit(OWLDataComplementOf node) {
        writeKeyword(ManchesterOWLSyntax.NOT.toString());
        write(" ");
        writeOp(node.getDataRange());
    }


    public void visit(OWLDataIntersectionOf owlDataIntersectionOf) {
        writeKeywordOpList(owlDataIntersectionOf.getOperands(), ManchesterOWLSyntax.AND.toString(), true);
    }


    public void visit(OWLDataUnionOf owlDataUnionOf) {
        writeKeywordOpList(owlDataUnionOf.getOperands(), ManchesterOWLSyntax.OR.toString(), false);
    }

    ////////// Properties

    public void visit(OWLObjectInverseOf property) {
        writeKeyword(ManchesterOWLSyntax.INVERSE_OF.toString());
        write(" ");
        writeOp(property.getInverse());
    }

    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.FUNCTIONAL.toString());
    }

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.INVERSE_FUNCTIONAL.toString());
    }

    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.SYMMETRIC.toString());
    }

    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.TRANSITIVE.toString());
    }

    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.ASYMMETRIC.toString());
    }

    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.REFLEXIVE.toString());
    }

    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.IRREFLEXIVE.toString());
    }

    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.DOMAIN.toString());
        write(" ");
        writeOp(axiom.getDomain());
        writeAnnotations(axiom);
    }

    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.RANGE.toString());
        write(" ");
        writeOp(axiom.getRange());
        writeAnnotations(axiom);
    }

    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        writeOp(axiom.getFirstProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.INVERSE.toString());
        write(" ");
        writeOp(axiom.getSecondProperty());
        writeAnnotations(axiom);
    }


    public void visit(OWLHasKeyAxiom axiom) {
        writeOp(axiom.getClassExpression());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.HAS_KEY.toString());
        write(" ");
        write("(");
        writeOpList(axiom.getPropertyExpressions(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }


    public void visit(OWLDatatypeDefinitionAxiom axiom) {
        axiom.getDatatype().accept(this);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.EQUIVALENT_TO.toString());
        write(" ");
        axiom.getDataRange().accept(this);
        writeAnnotations(axiom);
    }


    public void visit(SWRLRule swrlRule) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLClassAtom swrlClassAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLDataRangeAtom swrlDataRangeAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLObjectPropertyAtom swrlObjectPropertyAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLDataPropertyAtom swrlDataPropertyAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLBuiltInAtom swrlBuiltInAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLVariable swrlVariable) {
        // @@TODO SWRL Support
    }


    public void visit(SWRLIndividualArgument swrlIndividualArgument) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLLiteralArgument swrlLiteralArgument) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLSameIndividualAtom swrlSameIndividualAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(SWRLDifferentIndividualsAtom swrlDifferentIndividualsAtom) {
        // @@TODO SWRL SUpport
    }


    public void visit(OWLSubPropertyChainOfAxiom axiom) {
        writeKeywordOpList(axiom.getPropertyChain(), "o", false);
        write(" ");
        writeKeyword(ManchesterOWLSyntax.SUB_PROPERTY_OF.toString());
        write(" ");
        writeOp(axiom.getSuperProperty());
        writeAnnotations(axiom);
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.DOMAIN.toString());
        write(" ");
        writeOp(axiom.getDomain());
        writeAnnotations(axiom);
    }

    public void visit(OWLDataPropertyRangeAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.RANGE.toString());
        write(" ");
        writeOp(axiom.getRange());
        writeAnnotations(axiom);
    }

    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writeUnaryPropertyAxiom(axiom, ManchesterOWLSyntax.FUNCTIONAL.toString());
        writeAnnotations(axiom);
    }

    ////////// Annotations

    public void visit(OWLAnnotationAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        writeKeyword(" /*&nbsp;", "comment");
        axiom.getAnnotation().accept(this);
        writeKeyword("&nbsp;*/", "comment");
        writeAnnotations(axiom); // in theory, you could annotate the annotation axioms !!
    }


    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        writeOp(axiom.getSubProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.SUB_PROPERTY_OF.toString());
        write(" ");
        writeOp(axiom.getSuperProperty());
        writeAnnotations(axiom);
    }


    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.RANGE.toString());
        write(" ");
        writeOp(axiom.getDomain());
        writeAnnotations(axiom);
    }


    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
        writeOp(axiom.getProperty());
        write(" ");
        writeKeyword(ManchesterOWLSyntax.RANGE.toString());
        write(" ");
        writeOp(axiom.getRange());
        writeAnnotations(axiom);
    }


    public void visit(OWLAnnotation annotation) {
        annotation.getProperty().accept(this);
        write(" ");
        annotation.getValue().accept(this);
    }
    
    public void visit(OWLLiteral node) {
        write("<span class='" + CSS_LITERAL + "'>\"");
        writeLiteralContents(node.getLiteral());
        if (node.isRDFPlainLiteral()) {
            write("\"");
            final String lang = node.getLang();
            if (lang != null){
                write(" <span style='color: black;'>(" + lang + ")</span>");
            }
            write("</span>");
        }
        else {
            write("\"</span> (");
            node.getDatatype().accept(this);
            write(")");
        }
    }


    /////////// Axioms

    public void visit(OWLEquivalentClassesAxiom axiom) {
        writeKeywordOpList(orderOps(axiom.getClassExpressions()), OWLHTMLConstants.EQUIV_CHAR, false);
        writeAnnotations(axiom);
    }

    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        writeKeywordOpList(axiom.getProperties(), OWLHTMLConstants.EQUIV_CHAR, false);
        writeAnnotations(axiom);
    }

    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        writeKeywordOpList(axiom.getProperties(), OWLHTMLConstants.EQUIV_CHAR, false);
        writeAnnotations(axiom);
    }

    public void visit(OWLSameIndividualAxiom axiom) {
        writeKeywordOpList(axiom.getIndividuals(), OWLHTMLConstants.EQUIV_CHAR, false);
        writeAnnotations(axiom);
    }

    public void visit(OWLSubClassOfAxiom axiom) {
        axiom.getSubClass().accept(this);
        write(" ");
        writeKeyword(OWLHTMLConstants.SUBCLASS_CHAR);
        write(" ");
        axiom.getSuperClass().accept(this);
        writeAnnotations(axiom);
    }

    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        axiom.getSubProperty().accept(this);
        write(" ");
        writeKeyword(OWLHTMLConstants.SUBCLASS_CHAR);
        write(" ");
        axiom.getSuperProperty().accept(this);
        writeAnnotations(axiom);
    }

    public void visit(OWLSubDataPropertyOfAxiom axiom) {
        axiom.getSubProperty().accept(this);
        write(" ");
        writeKeyword(OWLHTMLConstants.SUBCLASS_CHAR);
        write(" ");
        axiom.getSuperProperty().accept(this);
        writeAnnotations(axiom);
    }

    public void visit(OWLDisjointClassesAxiom axiom) {
        writeKeyword(ManchesterOWLSyntax.DISJOINT_CLASSES.toString());
        write("(");
        writeOpList(axiom.getClassExpressions(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }

    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        writeKeyword(ManchesterOWLSyntax.DISJOINT_PROPERTIES.toString());
        write("(");
        writeOpList(axiom.getProperties(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }

    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        writeKeyword(ManchesterOWLSyntax.DISJOINT_PROPERTIES.toString());
        write("(");
        writeOpList(axiom.getProperties(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }

    public void visit(OWLDifferentIndividualsAxiom axiom) {
        writeKeyword(ManchesterOWLSyntax.DIFFERENT_INDIVIDUALS.toString());
        write("(");
        writeOpList(axiom.getIndividuals(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }

    public void visit(OWLDisjointUnionAxiom axiom) {
        writeKeyword(ManchesterOWLSyntax.DISJOINT_UNION_OF.toString());
        write("(");
        writeOpList(axiom.getClassExpressions(), ", ", false);
        write(")");
        writeAnnotations(axiom);
    }

    public void visit(OWLDeclarationAxiom axiom) {
        final OWLEntity entity = axiom.getEntity();
        if (entity instanceof OWLClass){
            writeKeyword(ManchesterOWLSyntax.CLASS.toString());
            write(": ");
        }
        else if (entity instanceof OWLObjectProperty){
            writeKeyword(ManchesterOWLSyntax.OBJECT_PROPERTY.toString());
            write(": ");
        }
        else if (entity instanceof OWLDataProperty){
            writeKeyword(ManchesterOWLSyntax.DATA_PROPERTY.toString());
            write(": ");
        }
        else if (entity instanceof OWLAnnotationProperty){
            writeKeyword(ManchesterOWLSyntax.ANNOTATION_PROPERTY.toString());
            write(": ");
        }
        else if (entity instanceof OWLNamedIndividual){
            writeKeyword(ManchesterOWLSyntax.INDIVIDUAL.toString());
            write(": ");
        }
        else if (entity instanceof OWLDatatype){
            writeKeyword("Datatype");
            write(": ");
        }
        entity.accept(this);
        writeAnnotations(axiom);
    }

    /////// OWLIndividual assertions

    public void visit(OWLClassAssertionAxiom axiom) {
        axiom.getIndividual().accept(this);
        write(": ");
        axiom.getClassExpression().accept(this);
        writeAnnotations(axiom);
    }

    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        writeAssertionAxiom(axiom);
    }

    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        writeAssertionAxiom(axiom);
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        writeAssertionAxiom(axiom);
    }

    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        writeAssertionAxiom(axiom);
    }


    public void writeImportsDeclaration(OWLImportsDeclaration axiom) {
        writeKeyword("imports: ");
        IRI iri = axiom.getIRI();
        OWLOntology loadedOnt = null;
        for (OWLOntology ont : ontologies){
            if (ont.getOntologyID().getDefaultDocumentIRI().equals(iri)){
                loadedOnt = ont;
                break;
            }
        }
        if (loadedOnt != null){
            loadedOnt.accept(this);
        }
        else{
            write(iri.toString());//writeOntologyURIWithBoldFragment(uri);
        }
    }

    private String getName(OWLEntity entity){
        return sfProvider.getShortForm(entity);
    }

    private String getBase(URI uri){
        String uriStr = uri.toString();
        String fragment = uri.getFragment();
        if (fragment != null){
            return uriStr.substring(0, uriStr.length()-uri.getFragment().length()-1);
        }
        else{
            return uri.toString();
        }
    }

    // just make sure a named class is first if there is one
    private List<OWLClassExpression> orderOps(Set<OWLClassExpression> ops) {
        List<OWLClassExpression> orderedOps = new ArrayList<OWLClassExpression>(ops);
        Collections.sort(orderedOps, new Comparator<OWLClassExpression>(){
            public int compare(OWLClassExpression d1, OWLClassExpression d2) {
                if (d1 instanceof OWLClass){
                    return -1;
                }
                else if (d2 instanceof OWLClass){
                    return 1;
                }
                return 0;
            }
        });
        return orderedOps;
    }

    private void writeOntologyIRI(OWLOntology ont) {
        writeIRIWithBoldFragment(ont.getOntologyID().getOntologyIRI(), ontologyIriSFProvider.getShortForm(ont));
    }


    private void writeIRIWithBoldFragment(IRI iri, String shortForm) {
        final String fullURI = iri.toString();
        int index = 0;
        if (shortForm != null) {
        	index = fullURI.lastIndexOf(shortForm);
        }
        if (index == 0){
            write(fullURI);
        }
        else{
            write(fullURI.substring(0, index));
            write("<b>");
            write(shortForm);
            write("</b>");
            write(fullURI.substring(index+shortForm.length()));
        }
    }

    // add a span to allow for css highlighting
    private void writeKeyword(String keyword) {
        writeKeyword(keyword, CSS_KEYWORD);
    }

    // add a span to allow for css highlighting
    private void writeKeyword(String keyword, String cssClass) {
        write("<span class='" + cssClass + "'>" + keyword + "</span>");
    }


    // useful to add brackets around the anonymous operators of unions and intersections and the fillers of restrictions
    private void writeOp(OWLObject op) {
        if (op instanceof OWLEntity ||
            op instanceof OWLObjectOneOf ||
            op instanceof OWLDataOneOf ||
            op instanceof OWLDatatypeRestriction){
            op.accept(this);
        }
        else{ // provide brackets for clarity
            write("(");
            op.accept(this);
            write(")");
        }
    }

    private void writeIndent() {
        for (int i=0; i<indent; i++){
            write("&nbsp;");
        }
    }


    private void writeOWLEntity(OWLEntity entity) {
        final URI uri = entity.getIRI().toURI();

        String name = StringEscapeUtils.escapeHtml4(getName(entity));

        Set<String> cssClasses = new HashSet<String>();
        if (ModelUtil.isDeprecated(entity, ontologies)){
            cssClasses.add(CSS_DEPRECATED);
        }

        if (pageURL == null){
            final URL urlForTarget = urlRenderer.getURLForOWLObject(entity);
            write("<a href=\"" + urlForTarget + "\"");
            if (targetWindow != null){
                write(" target=\"" + targetWindow + "\"");
            }
            writeCSSClasses(cssClasses);
            write(" title=\"" + uri + "\">" + name + "</a>");
        }
        else{
            OWLObject currentTarget = urlRenderer.getOWLObjectForURL(pageURL);
            if (currentTarget != null && currentTarget.equals(entity)){
                cssClasses.add(CSS_ACTIVE_ENTITY);
                write("<span");
                writeCSSClasses(cssClasses);
                write(">" + name + "</span>");
            }
            else{
                final URL urlForTarget = urlRenderer.getURLForOWLObject(entity);
                write("<a href=\"" + URLUtils.createRelativeURL(pageURL, urlForTarget) + "\"");
                if (targetWindow != null){
                    write(" target=\"" + targetWindow + "\"");
                }
                writeCSSClasses(cssClasses);
                write(" title=\"" + uri + "\">" +name + "</a>");
            }
        }
    }


    private void writeCardinality(OWLCardinalityRestriction desc, String cardinalityType) {
        desc.getProperty().accept(this);
        writeKeyword(" " + cardinalityType + " ", cardinalityType);
        write(desc.getCardinality() + " ");
        if (desc.getFiller() != null){
            writeOp(desc.getFiller());
        }
    }

    private void writeUnaryPropertyAxiom(OWLUnaryPropertyAxiom axiom, String keyword) {
        writeKeyword(keyword);
        write(" (");
        writeOp(axiom.getProperty());
        write(")");
        writeAnnotations(axiom);
    }

    private String writeFacet(OWLFacet facet) {
        // need to make ranges HTML safe
        if (facet.equals(OWLFacet.MIN_INCLUSIVE)) return "&gt;=";
        else if (facet.equals(OWLFacet.MIN_EXCLUSIVE)) return "&gt;";
        else if (facet.equals(OWLFacet.MAX_INCLUSIVE)) return "&lt;=";
        else if (facet.equals(OWLFacet.MAX_EXCLUSIVE)) return "&lt;";
        return facet.getSymbolicForm();
    }

    // @@TODO literal should use <pre> to make sure that fomatting inside the string doesn't disrupt the html
    // but it appears java ignores this tag so for now just disable tags completely
    private void writeLiteralContents(String literal) {
        boolean writtenExternalRef = false;
        try {
            URI uri = new URI(literal);
            if (uri.isAbsolute()){
                write("<a href='" + uri + "' target='ext_ref'>" + uri + "</a>");
                writtenExternalRef = true;
            }
        }
        catch (URISyntaxException e) {
            // do nothing
        }
        finally{
            if (!writtenExternalRef){
                literal = literal.replace("<", "&lt;");
                literal = literal.replace(">", "&gt;");
                write(literal);
            }
        }
    }


    private void writeAnnotations(OWLAxiom axiom) {
        for (OWLAnnotation annot : axiom.getAnnotations()){
            annot.accept(this);
        }
    }

    private void writeAssertionAxiom(OWLPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        write(" ");
        axiom.getProperty().accept(this);
        write(" ");
        axiom.getObject().accept(this);
        writeAnnotations(axiom);
    }


    private <O extends OWLObject> void writeOpList(Iterable<O> args, String separator, boolean wrap) {
        for (Iterator<O> i = args.iterator(); i.hasNext();) {
            i.next().accept(this);
            if (i.hasNext()){
                write(separator);
                if (wrap && indent > 0){
                    write("<br>"); // cannot use <br /> in java browser
                    writeIndent();
                }
            }
        }
    }

    private <O extends OWLObject> void writeKeywordOpList(Iterable<O> args, String keyword, boolean wrap) {
        for (Iterator<O> i = args.iterator(); i.hasNext();) {
            i.next().accept(this);
            if (i.hasNext()){
                write(" ");
                writeKeyword(keyword);
                write(" ");
                if (wrap && indent > 0){
                    write("<br>"); // cannot use <br /> in java browser
                    writeIndent();
                }
            }
        }
    }
    private void writeCSSClasses(Set<String> cssClasses) {
        if (!cssClasses.isEmpty()){
            boolean started = false;
            write(" class='");
            for (String cls : cssClasses){
                if (started){
                    write(" ");
                }
                write(cls);
                started = true;
            }
            write("'");
        }
    }
}
