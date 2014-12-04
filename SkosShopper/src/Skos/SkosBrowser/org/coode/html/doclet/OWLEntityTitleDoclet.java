/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLEntity;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.owl.mngr.NamedObjectType;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 7, 2008<br><br>
 */
public class OWLEntityTitleDoclet<O extends OWLEntity> extends AbstractTitleDoclet<O> {


    public OWLEntityTitleDoclet(OWLHTMLKit kit) {
        super(kit);
    }

    public String getTitle() {
        final O object = getUserObject();
        return NamedObjectType.getType(object).getSingularRendering() + ": " +
               getHTMLGenerator().getOWLServer().getShortFormProvider().getShortForm(object);
    }


    public String getSubtitle() {
        return getUserObject().getIRI().toString();
    }
}
