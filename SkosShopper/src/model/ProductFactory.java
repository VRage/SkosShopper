package model;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import controller.TriplesShowController.TripleOwn;

public class ProductFactory {
	
	private static boolean firstLoaded;
	
	public static String[] getCreatableProductsAsString()
	{
		final Set<String> objects = new HashSet<String>();
		
		Model model = FusekiModel.getAllTriples();
		StmtIterator stmti = model.listStatements();
		
		while(stmti.hasNext())
		{
			Statement stmt = stmti.nextStatement();
			
			RDFNode o = stmt.getObject();

			if(o.toString().contains("skos/core#Concept"))
			{
				objects.add(stmt.getSubject().toString().substring(stmt.getSubject().toString().lastIndexOf("#")+1));	
			}

		}
		
		String[] objectsString = objects.toArray(new String[objects.size()]);
		
		return objectsString;
	}

}
