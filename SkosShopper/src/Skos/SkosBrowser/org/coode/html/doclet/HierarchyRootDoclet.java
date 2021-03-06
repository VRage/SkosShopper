/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import org.semanticweb.owlapi.model.OWLEntity;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.html.hierarchy.TreeFragment;

import java.io.PrintWriter;
import java.net.URL;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Feb 7, 2008<br><br>
 */
public class HierarchyRootDoclet<O extends OWLEntity> extends AbstractHierarchyNodeDoclet<O>{

    public HierarchyRootDoclet(OWLHTMLKit kit, TreeFragment<O> model) {
        super(kit, model);
        setPinned(false);
    }

    protected void renderHeader(URL pageURL, PrintWriter out) {
        renderBoxStart(getModel().getTitle(), out);
        out.println("<ul class='minihierarchy'>");
    }

    protected void renderFooter(URL pageURL, PrintWriter out) {
        out.println("</ul>");
        renderBoxEnd(getModel().getTitle(), out);
    }

    /**
     * This will be the <em>focus</em> object, not the root
     * @param object
     */
    public void setUserObject(O object) {
        clear();
        getModel().setFocus(object);
        if (object != null){
            HierarchyNodeDoclet<O> lastPath = null;
            HierarchyNodeDoclet<O> lastPathContainingFocusedNode = null;
            for (O root : getModel().getRoots()){
                lastPath = new HierarchyNodeDoclet<O>(getHTMLGenerator(), getModel());
                lastPath.setAutoExpandEnabled(isAutoExpandSubs());
                lastPath.setUserObject(root);
                addDoclet(lastPath);
                if (getModel().pathContainsNode(root, getModel().getFocus())){
                    lastPathContainingFocusedNode = lastPath;
                }
            }
            if(lastPathContainingFocusedNode != null){ // only show subs for the last branch
                lastPathContainingFocusedNode.setShowSubs(isRenderSubsEnabled());
            }
            else if (lastPath != null) { // the node only appears in the hierarchy due to an equivalence
                lastPath.setShowSubs(isRenderSubsEnabled());
            }
            else{
                throw new RuntimeException("Root: cannot find a path containing the node: " + object);
            }
        }
        super.setUserObject(object);
    }
}
