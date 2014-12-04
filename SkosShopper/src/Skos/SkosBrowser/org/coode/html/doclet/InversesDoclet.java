/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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
public class InversesDoclet extends AbstractOWLElementsDoclet<OWLObjectProperty, OWLObjectPropertyExpression> {

    public InversesDoclet(OWLHTMLKit kit) {
        super("Inverses", Format.list, kit);
    }

    protected Collection<OWLObjectPropertyExpression> getElements(Set<OWLOntology> onts) {
        return getUserObject().getInverses(onts);
    }
}
