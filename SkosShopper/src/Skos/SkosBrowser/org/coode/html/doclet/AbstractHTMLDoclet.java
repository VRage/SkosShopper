/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.doclet;

import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLConstants;
import Skos.SkosBrowser.org.coode.html.util.URLUtils;

import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jan 24, 2008<br><br>
 *
 * Default handling for doclet nesting, including handing the user object down through the hierarchy.
 * Content will be surrounded by whatever is created in renderHeader and renderFooter
 */
public abstract class AbstractHTMLDoclet<O> implements NestedHTMLDoclet<O> {

    private boolean pinned = false;

    private List<HTMLDoclet<O>> doclets = new ArrayList<HTMLDoclet<O>>();

    private O userObject;

    protected abstract void renderHeader(URL pageURL, PrintWriter out);
    protected abstract void renderFooter(URL pageURL, PrintWriter out);

    public void renderContent(URL pageURL, PrintWriter out) {
        for (HTMLDoclet doclet : doclets){
            doclet.renderAll(pageURL, out);
        }
    }

    public final void renderAll(URL pageURL, PrintWriter out) {
        renderHeader(pageURL, out);
        renderContent(pageURL, out);
        renderFooter(pageURL, out);
    }

    public final void addDoclet(HTMLDoclet<O> doclet){
        if (!doclet.isPinned()){
            doclet.setUserObject(userObject);
        }
        doclets.add(doclet);
    }

    public final void addDoclet(HTMLDoclet<O> doclet, int index){
        if (!doclet.isPinned()){
            doclet.setUserObject(userObject);
        }
        doclets.add(index, doclet);
    }

    public final void removeDoclet(HTMLDoclet doclet) {
        doclets.remove(doclet);
    }

    public final HTMLDoclet getDoclet(String id) {
        for (HTMLDoclet doclet : doclets){
            if (doclet.getID().equals(id)){
                return doclet;
            }
        }
        return null;
    }

    public int getSubDocletCount() {
        return doclets.size();
    }

    protected List<HTMLDoclet<O>> getDoclets(){
        return Collections.unmodifiableList(doclets);
    }

    public void clear() {
        doclets.clear();
    }

    public final int indexOf(HTMLDoclet doclet) {
        return doclets.indexOf(doclet);
    }

    public void setUserObject(O object){
        this.userObject = object;
        for (HTMLDoclet<O> doclet : doclets){
            if (!doclet.isPinned()){
                doclet.setUserObject(object);
            }
        }
    }

    public final O getUserObject(){
        return this.userObject;
    }

    public void setPinned(boolean pinned){
        this.pinned = pinned;
    }

    public boolean isPinned(){
        return pinned;
    }

    public Set<URL> getRequiredCSS() {
        Set<URL> css = new HashSet<URL>();
        for (HTMLDoclet doclet : doclets){
            css.addAll(doclet.getRequiredCSS());
        }
        return css;
    }

    public Set<URL> getRequiredJS() {
        Set<URL> js = new HashSet<URL>();
        for (HTMLDoclet doclet : doclets){
            js.addAll(doclet.getRequiredJS());
        }
        return js;
    }

    /**
     * Makes sure the link does not target a frame when single frame navigation is on (to prevent popups in other windows).
     *
     * @param name readable link text
     * @param href page to link to
     * @param target may be null. If set, this is only added if in multiframe mode
     * @param cssClass may be null. The CSS class applied to the anchor element
     * @param singleFrame - if true, the target should be ignored
     * @param pageURL the current page URL, so that links can be made relative
     * @param out printwriter to write to
     */
    protected final void renderLink(String name, URL href, OWLHTMLConstants.LinkTarget target, String cssClass, boolean singleFrame, URL pageURL, PrintWriter out) {
        out.print("<a href='" + URLUtils.createRelativeURL(pageURL, href) + "'");

        if (cssClass != null){
            out.print(" class='" + cssClass + "'");
        }

        // if the linktarget is another window or we are in a frames view add the target
        if (target != null && (target == OWLHTMLConstants.LinkTarget._blank || !singleFrame)){
            out.print(" target='" + target + "'");
        }

        out.println(" >" + name + "</a>");
    }

    protected final void renderBoxStart(String name, PrintWriter out) {
        out.println("<h2>" + name + "</h2>");
        out.println("<div class='codebox' id='" + name.toLowerCase().replace(" ", "_") + "'>");
    }

    protected final void renderBoxStart(String name, String id, PrintWriter out) {
        out.println("<h2>" + name + "</h2>");
        out.println("<div class='codebox' id='" + id + "'>");
    }

    protected final void renderBoxEnd(String id, PrintWriter out) {
        out.println("</div><!-- " + id.toLowerCase() + " -->");
    }
}
