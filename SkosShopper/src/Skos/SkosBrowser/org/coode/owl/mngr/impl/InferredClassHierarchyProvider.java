package Skos.SkosBrowser.org.coode.owl.mngr.impl;

import org.semanticweb.owlapi.reasoner.OWLReasoner;

import Skos.SkosBrowser.org.coode.owl.mngr.OWLServer;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 6, 2009<br><br>
 */
public class InferredClassHierarchyProvider extends ClassHierarchyProvider{


    public InferredClassHierarchyProvider(OWLServer server) {
        super(server);
    }


    protected OWLReasoner getReasoner() {
        return getServer().getOWLReasoner();
    }
}