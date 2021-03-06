/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.IRI;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 5, 2008<br><br>
 */
public class OntologyImportsDoclet extends AbstractOWLElementsDoclet<OWLOntology, IRI> {

    public OntologyImportsDoclet(OWLHTMLKit kit) {
        super("Imports", Format.list, kit);
    }

    protected Collection<IRI> getElements(Set<OWLOntology> onts) {
        Set<IRI> iris = new HashSet<IRI>();
        for (OWLImportsDeclaration decl : getUserObject().getImportsDeclarations()){
            iris.add(decl.getIRI());
        }
        return iris;
    }
}
