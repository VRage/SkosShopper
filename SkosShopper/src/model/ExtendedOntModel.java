package model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.hp.hpl.jena.ontology.OntModel;

public class ExtendedOntModel {
	private String path;
	private OntModel model;
	private String shortPath;
	public ExtendedOntModel(String path, OntModel model) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.model = model;
		if(path.length()>30)
			shortPath =path.substring(0, 10)+"  ...  " + path.substring(path.length()-20, path.length());
		else 
			shortPath = path;
	}
	public String getShortPath(){
		return shortPath;
	}
	public OntModel getOnModel(){
		return model;
	}
	public int hashCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(path);
		return builder.toString().hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return shortPath;
	}
}