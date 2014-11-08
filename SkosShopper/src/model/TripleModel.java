package model;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.fuseki.migrate.GraphLoadUtils;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import javafx.beans.property.SimpleStringProperty;

import com.hp.hpl.jena.rdf.model.Statement;

import exceptions.fuseki_exceptions.NoDatasetAccessorException;

public class TripleModel {
	
	
	public static Model getAllTriples()
	{
		System.out.println("IN FusekiModel.getAllTriples()");
		String s;
		
		GraphLoadUtils glu = new GraphLoadUtils();
		Model model;
		
		try {
			model = FusekiModel.getDatasetAccessor().getModel();
			Graph graph = model.getGraph();
			return model;
		} catch (NoDatasetAccessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
				
		
		//System.out.println(s);
		
		
	}
	
	
	public static Model getTriplesBySkosConcept(Model model)
	{
		//final Set<Statement> foundTriples = new HashSet<Statement>();
		Model result = ModelFactory.createDefaultModel();
		
		
		StmtIterator stmti = model.listStatements();

		// get all Triples (Statements) which are a skos:concept
		while(stmti.hasNext())
		{
			Statement stmt = stmti.nextStatement();
			
			RDFNode o = stmt.getObject();

			if(o.toString().contains("skos/core#Concept"))
			{
				if(stmt.getPredicate().toString().contains("#type"))
				{
					//objects.add(stmt.getSubject().toString().substring(stmt.getSubject().toString().lastIndexOf("#")+1));
					result.add(stmt);
					System.out.println("found skos#concept: "+stmt.getSubject().toString());
				}
			}

		}
		
		return result;
	}
	
	
	public static Model getLabelsByConcept(Model modelAllTriples, Model modelSkosConcepts)
	{
		Model result = ModelFactory.createDefaultModel();
		
		StmtIterator allIt = modelAllTriples.listStatements();
		
		while(allIt.hasNext())
		{
			StmtIterator concIt = modelSkosConcepts.listStatements();
			
			Statement allItem = allIt.next();
			
			while(concIt.hasNext())
			{
				Statement concItem = concIt.next();
				
				if(allItem.getSubject().equals(concItem.getSubject()) && allItem.getPredicate().toString().contains("#prefLabel"))
				{
					result.add(allItem);
					System.out.println("found skos-xl#prefLabel: "+allItem.getObject().toString());
				}
			}
		}
		
		return result;
	}
	
	// Triple is Statement
	// Object is RDFNode
	// Predicate is Property
	// Subject is Resource
	
	/* from concept to label

	 	skos/core#Concept | rdf-syntax-ns#type | skos/core#Bag
	 	
	 	skos/core#BagLabel | un#prefLabel | skos/core#Bag
	 	
	 	Bag^^http...XMLSchema#string | un#literalForm | core#BagLabel
	 */
	
	
	
	public static Model getLiteralOfPrefLabel(Model modelConceptLabels, Model modelAllTriples)
	{
		Model result = ModelFactory.createDefaultModel();
		
		StmtIterator allIt = modelAllTriples.listStatements();
		
		while(allIt.hasNext())
		{
			StmtIterator prefIt = modelConceptLabels.listStatements();
			
			Statement allItem = allIt.next();
			
			while(prefIt.hasNext())
			{
				Statement prefItem = prefIt.next();
				
				if(allItem.getSubject().equals(prefItem.getObject()) && allItem.getPredicate().toString().contains("#literalForm"))
				{
					result.add(allItem);
					System.out.println("found skos-xl#prefLabel: "+allItem.getObject().toString());
				}
			}
		}
		
		return result;
	}


}
