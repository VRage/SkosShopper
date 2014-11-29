package Skos.SkosBuilder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SKOSStatements {
	private final SimpleStringProperty subject;
    private final SimpleStringProperty predicate;
    private final SimpleStringProperty object;
	
    public SKOSStatements(String subject, String predicate, String object) {
    	this.subject = new SimpleStringProperty(subject);
        this.predicate = new SimpleStringProperty(predicate);
        this.object = new SimpleStringProperty(object);
    }
    
    public String getSubject() {
    	return subject.get();
    }
    
    public void setSubject(String subject) {
    	this.subject.set(subject);
    }
    
    public StringProperty subjectProperty() {
    	return subject;
    }
    
    public String getPredicate() {
        return predicate.get();
    }
    public void setPredicate(String predicate) {
    	this.predicate.set(predicate);
    }
    
    public StringProperty predicateProperty() {
    	return predicate;
    }
        
    public String getObject() {
        return object.get();
    }
    public void setLastName(String object) {
    	this.object.set(object);
    }
    
    public StringProperty objectProperty() {
    	return object;
    }
}
