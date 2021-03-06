/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;

import java.util.Collection;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jan 25, 2008<br><br>
 */
public class AssertedEquivalentsDoclet extends AbstractOWLElementsDoclet<OWLClass, OWLClassExpression> {

    public AssertedEquivalentsDoclet(OWLHTMLKit kit) {
        super("Equivalents", Format.list, kit);
    }

    protected Collection<OWLClassExpression> getElements(Set<OWLOntology> onts) {
        return getUserObject().getEquivalentClasses(onts);
    }
}
