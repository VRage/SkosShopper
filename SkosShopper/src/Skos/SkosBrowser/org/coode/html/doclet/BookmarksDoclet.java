/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.html.bookmarks.OntologyBookmarks;
import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 11, 2008<br><br>
 */
public class BookmarksDoclet extends AbstractOWLElementsDoclet<OWLOntology, OWLEntity>{

    public BookmarksDoclet(String name, Format format, OWLHTMLKit kit) {
        super(name, format, kit);
        setTarget(OWLHTMLConstants.LinkTarget.content);
    }

    protected Collection<OWLEntity> getElements(Set<OWLOntology> onts) {
        Set<OWLEntity> bookmarks = new HashSet<OWLEntity>();
        for (OWLOntology ont : onts){
            bookmarks.addAll(new OntologyBookmarks(getHTMLGenerator().getOWLServer().getOWLOntologyManager(), ont).getBookmarks());
        }
        return bookmarks;
    }

    public void setUserObject(OWLOntology ontology) {
        super.setUserObject(ontology);

        if (ontology != null){
        setOntologies(Collections.singleton(ontology));
        }
        else{
              setOntologies(null);
        }
    }
}
