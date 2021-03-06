/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.url;

import org.apache.log4j.Logger;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLConstants;
import Skos.SkosBrowser.org.coode.html.util.URLUtils;
import Skos.SkosBrowser.org.coode.owl.mngr.NamedObjectType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jan 12, 2008<br><br>
 */
public abstract class AbstractURLScheme implements URLScheme {

    private static final Logger logger = Logger.getLogger(AbstractURLScheme.class);

    protected final OWLHTMLKit kit;


    public AbstractURLScheme(OWLHTMLKit kit) {
        this.kit = kit;
    }

    public NamedObjectType getType(URL url) {
        String relativeURL = URLUtils.createRelativeURL(kit.getBaseURL(), url);
        String[] path = relativeURL.split(OWLHTMLConstants.SLASH);
        String typeName = path[0]; // always the first element
        try{
            return NamedObjectType.valueOf(typeName);
        }
        catch(IllegalArgumentException e){
            // do nothing
        }
        return null;
    }

    public URL getURLForIndex(NamedObjectType type) {
        try {
            return new URL(kit.getBaseURL(), type.toString() + OWLHTMLConstants.SLASH);
        }
        catch (MalformedURLException e) {
            logger.error("Could not create URL for index: " + type, e);
        }
        return null;
    }

    public URL getURLForRelativePage(String pageRelativeToBase) {
        try {
            return new URL(getBaseURL() + pageRelativeToBase);
        }
        catch (MalformedURLException e) {
            logger.error("Could not create URL for page: " + pageRelativeToBase, e);
        }
        return null;
    }


    public URL getURLForAbsolutePage(URL pageURL) {
        return pageURL;
    }

    public URL getBaseURL(){
        return kit.getBaseURL();
    }

    protected OWLHTMLKit getOWLHTMLKit(){
        return kit;
    }
}
