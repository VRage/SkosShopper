/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

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
 * Date: Feb 5, 2008<br><br>
 */
public class SameAsDoclet extends AbstractOWLElementsDoclet<OWLNamedIndividual, OWLIndividual> {

    public SameAsDoclet(OWLHTMLKit kit) {
        super("Same As", Format.list, kit);
    }

    protected Collection<OWLIndividual> getElements(Set<OWLOntology> onts) {
        Set<OWLIndividual> sameAs = new HashSet<OWLIndividual>();
        for (OWLOntology ont : onts){
            sameAs.addAll(getUserObject().getSameIndividuals(ont));
        }
        return sameAs;
    }
}
