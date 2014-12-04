/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jan 25, 2008<br><br>
 */
public class UsageDoclet<O extends OWLEntity> extends AbstractOWLElementsDoclet<O, OWLAxiom>{

    private UsageVisibilityVisitor usageVisibilityVisitor = new UsageVisibilityVisitor();

    public UsageDoclet(OWLHTMLKit kit) {
        super("Usage", ElementsDoclet.Format.list, kit);
    }

    protected Collection<OWLAxiom> getElements(Set<OWLOntology> onts) {
        OWLEntity entity = getUserObject();
        Collection<OWLAxiom> usage = new HashSet<OWLAxiom>();
        for (OWLOntology ont : onts){
            for (OWLAxiom ax : ont.getReferencingAxioms(entity)){
                if (usageVisibilityVisitor.getShowUsage(ax, entity)){
                    usage.add(ax);
                }
            }

        }
        return usage;
    }


    private class UsageVisibilityVisitor extends OWLAxiomVisitorAdapter {

        private boolean showUsage;
        private OWLEntity currentEntity;

        public void visit(OWLAnnotationAssertionAxiom axiom) {
            if (axiom.getSubject() instanceof IRI && axiom.getSubject().equals(currentEntity.getIRI())){
                showUsage = false;
            }
        }

        public void visit(OWLSubClassOfAxiom ax) {
            if (ax.getSubClass() instanceof OWLClass){
                if (ax.getSubClass().equals(currentEntity)){
                    showUsage = false; // we'll already be showing it as superclasses
                }
                else if (ax.getSuperClass().equals(currentEntity)){
                    showUsage = false; // we'll already be showing it as subclasses
                }
            }
        }

        public void visit(OWLDisjointClassesAxiom ax) {
            for (OWLClassExpression d : ax.getClassExpressions()){
                if (!(d instanceof OWLClass)){
                    return;
                }
            }
            showUsage = false;
        }


        public void visit(OWLEquivalentClassesAxiom ax) {
            for (OWLClassExpression d : ax.getClassExpressions()){
                if (d.equals(currentEntity)){
                    showUsage = false;
                    return;
                }
            }
        }

        public void visit(OWLClassAssertionAxiom ax) {
            if (ax.getIndividual().equals(currentEntity)){
                showUsage = false; // we'll already be showing it as type
                return;
            }
        }


        public void visit(OWLDifferentIndividualsAxiom ax) {
            visitNaryIndAxiom(ax);
        }


        public void visit(OWLSameIndividualAxiom ax) {
            visitNaryIndAxiom(ax);
        }

        public void visitNaryIndAxiom(OWLNaryIndividualAxiom ax) {
            for (OWLIndividual d : ax.getIndividuals()){
                if (d.equals(currentEntity)){
                    showUsage = false; // we'll already be showing it as same or different
                    return;
                }
            }
        }

        public boolean getShowUsage(OWLAxiom ax, OWLEntity entity) {
            this.currentEntity = entity;
            showUsage = true;
            if (ax instanceof OWLUnaryPropertyAxiom){ // too expensive to do each by hand
                if (((OWLUnaryPropertyAxiom)ax).getProperty().equals(currentEntity)){
                    showUsage = false;
                }
            }
            else{
                ax.accept(this);
            }
            return showUsage;
        }
    }
}
