/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLObject;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLProperty;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 5, 2008<br><br>
 *
 * Handy version for getting access to the server
 */
public abstract class AbstractOWLDocDoclet<O extends OWLObject> extends AbstractHTMLDoclet<O>{

    private OWLHTMLKit kit;


    public AbstractOWLDocDoclet(OWLHTMLKit kit) {
        this.kit = kit;
    }

    protected final OWLHTMLKit getHTMLGenerator(){
        return kit;
    }

    protected boolean isSingleFrameNavigation() {
        return getHTMLGenerator().getHTMLProperties().get(OWLHTMLProperty.optionContentWindow) == null;
    }
}
