package Skos.SkosBuilder;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SKOSStatements {
	private final SimpleStringProperty subject;
    private final SimpleStringProperty predicate;
    private final SimpleStringProperty object;
    private final Statement stmt;
    private final boolean isLiteral;
	
    public SKOSStatements(Statement s) {
    	this.stmt = s;
    	this.subject = new SimpleStringProperty(s.getSubject().getLocalName());
        this.predicate = new SimpleStringProperty(s.getPredicate().getLocalName());
	    if (s.getObject() instanceof Resource) {
	    	this.object = new SimpleStringProperty(((Resource) s.getObject()).getLocalName());
	    	isLiteral = false;
	    } else {
	    	this.object = new SimpleStringProperty(" \"" + s.getObject().toString() + "\"");
	    	isLiteral = true;
	    }
    }
    
    public Statement getStmt() {
    	return stmt;
    }
    
    public boolean isLiteral() {
    	return isLiteral;
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
