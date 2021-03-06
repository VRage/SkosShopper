/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

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
public class AssertedSuperpropertiesDoclet<O extends OWLProperty> extends AbstractOWLElementsDoclet<O, O> {

    public AssertedSuperpropertiesDoclet(OWLHTMLKit kit) {
        super("Superproperties", Format.list, kit);
    }

    protected Collection<O> getElements(Set<OWLOntology> onts) {
        return getUserObject().getSuperProperties(onts);
    }
}
