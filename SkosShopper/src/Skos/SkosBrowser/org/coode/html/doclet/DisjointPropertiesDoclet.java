/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

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
public class DisjointPropertiesDoclet<O extends OWLProperty> extends AbstractOWLElementsDoclet<O, OWLPropertyExpression> {

    public DisjointPropertiesDoclet(OWLHTMLKit kit) {
        super("Disjoint Properties", Format.csv, kit);
    }

    protected Collection<OWLPropertyExpression> getElements(Set<OWLOntology> onts) {
        return getUserObject().getDisjointProperties(onts);
    }
}
