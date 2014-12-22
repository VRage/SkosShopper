package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AltEntriesManager {

	
    private final SimpleStringProperty destURL;
    private final SimpleStringProperty altURL;
    private final SimpleBooleanProperty useEntry;
	
    public AltEntriesManager(boolean useEntry, String dest, String alt) {
    	this.useEntry = new SimpleBooleanProperty(useEntry);
        destURL = new SimpleStringProperty(dest);
        altURL = new SimpleStringProperty(alt);
    }
    public AltEntriesManager() {
    	useEntry = new SimpleBooleanProperty(false);
    	destURL = new SimpleStringProperty("");
        altURL = new SimpleStringProperty("");
	}
    
    public String getDestUrl() {
        return destURL.get();
    }
    public void setDestUrl(String dest) {
    	destURL.set(dest);
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
    
    public StringProperty destURLProperty() {
    	return destURL;
    }
        
    public BooleanProperty useEntryProperty(){
    	return useEntry;
    }
}

