/*
* Copyright (C) 2007, University of Manchester
*/
package Skos.SkosBrowser.org.coode.html.url;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import Skos.SkosBrowser.org.coode.html.OWLHTMLKit;
import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLConstants;
import Skos.SkosBrowser.org.coode.html.impl.OWLHTMLParam;
import Skos.SkosBrowser.org.coode.html.util.URLUtils;
import Skos.SkosBrowser.org.coode.owl.mngr.NamedObjectType;
import Skos.SkosBrowser.org.coode.owl.util.ModelUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jun 11, 2007<br><br>
 * <p/>
 * code made available under Mozilla Public License (http://www.mozilla.org/MPL/MPL-1.1.html)<br>
 * copyright 2006, The University of Manchester<br>
 *
 * URL scheme for dynamic server-side resolution
 *
 * This will ONLY work if the hashCode() method for URIs is 1-1 mapping and completely deterministic
 * (for each vm the hash should be the same).
 * We may need to implement our own hash function if this is not the case.
 * It appears to work on a mac, but this will need to be double checked.
 *
 * Creates URLs of the form:
 * serverbase/<type>/<hash-of-object-uri>/
 *
 */
public class RestURLScheme extends AbstractURLScheme {

    private static final Logger logger = Logger.getLogger(RestURLScheme.class.getName());

    private String additionalLinkArguments;

    public RestURLScheme(OWLHTMLKit kit) {
        super(kit);
    }


    public URL getURLForOWLObject(OWLObject owlObject) {
        try {

            String type;
            int code;

            if (owlObject instanceof OWLEntity){
                type = NamedObjectType.getType(owlObject).toString();
                code = ((OWLEntity)owlObject).getIRI().hashCode();
            }
            else if (owlObject instanceof OWLOntology){
                type = NamedObjectType.getType(owlObject).toString();
                code = ((OWLOntology)owlObject).getOntologyID().hashCode();
            }
            else{
                type = owlObject.getClass().getSimpleName();
                code = owlObject.hashCode();
            }

            StringBuilder sb = new StringBuilder(type);
            sb.append(OWLHTMLConstants.SLASH);
            sb.append(code);
            sb.append(OWLHTMLConstants.SLASH);

            if (additionalLinkArguments != null){
                sb.append(additionalLinkArguments);
            }

            return new URL(getBaseURL(), sb.toString());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public OWLObject getOWLObjectForURL(URL url) {
        try {
            NamedObjectType type = getType(url);
            int hashCode = getID(url);
            if (type.equals(NamedObjectType.ontologies)){
                for (OWLOntology ont : kit.getOWLServer().getActiveOntologies()){
                    if (ont.getOntologyID().hashCode() == hashCode){
                        return ont;
                    }
                }
            }
            else{
                Set<OWLEntity> objs = new HashSet<OWLEntity>();
                for (OWLOntology ont : kit.getOWLServer().getActiveOntologies()){
                    objs.addAll(ModelUtil.getOWLEntitiesFromOntology(type, ont));
                }
                for (OWLEntity obj : objs){
                    if (obj.getIRI().hashCode() == hashCode){
                        return obj;
                    }
                }
            }
        }
        catch (Exception e) {
            // do nothing - there is no object specified
        }
        return null;
    }


    public URL getURLForOntologyIndex(OWLOntology ont, NamedObjectType type) {
        try {
            String encodedURI = URLEncoder.encode(ont.getOntologyID().getOntologyIRI().toString(), OWLHTMLConstants.DEFAULT_ENCODING);

            StringBuilder sb = new StringBuilder(type.toString());
            sb.append(OWLHTMLConstants.SLASH);
            sb.append(OWLHTMLConstants.START_QUERY);
            sb.append(OWLHTMLParam.ontology);
            sb.append(OWLHTMLConstants.EQUALS);
            sb.append(encodedURI);

            return new URL(getBaseURL(), sb.toString());
        }
        catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public void setAdditionalLinkArguments(String s) {
        additionalLinkArguments = s;
    }

    public void clearAdditionalLinkArguments() {
        additionalLinkArguments = null;
    }

    private int getID(URL url) throws MalformedURLException {
        String relativeURL = URLUtils.createRelativeURL(kit.getBaseURL(), url);
        String[] path = relativeURL.split(OWLHTMLConstants.SLASH);
        if (path.length >= 2){
            try{
                return Integer.parseInt(path[1]); // always the second element
            }
            catch (NumberFormatException e){
                throw new MalformedURLException("Cannot find a valid ID: " + path[1]);
            }
        }
        throw new MalformedURLException("The URL specified does not contain an object identifier: " + url);
    }
}
