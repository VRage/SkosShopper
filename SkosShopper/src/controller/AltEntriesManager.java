package controller;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AltEntriesManager {

	
    private final SimpleStringProperty destURL;
    private final SimpleStringProperty altURL;
	
    public AltEntriesManager(String dest, String alt) {
        destURL = new SimpleStringProperty(dest);
        altURL = new SimpleStringProperty(alt);
    }
    public AltEntriesManager() {
    	destURL = new SimpleStringProperty("");
        altURL = new SimpleStringProperty("");
	}
    
    public String getDestUrl() {
        return destURL.get();
    }
    public void setDestUrl(String dest) {
    	destURL.set(dest);
    }
    
    public StringProperty destURLProperty() {
    	return destURL;
    }
        
    public String getAltUrl() {
        return altURL.get();
    }
    public void setAltUrl(String alt) {
    	altURL.set(alt);
    }
    
    public StringProperty altURLProperty() {
    	return altURL;
    }
}

