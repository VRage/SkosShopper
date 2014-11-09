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
	
	
	public static Model getObjectByPredicateAndSubject(String predicate, String subject, Model fromModel)
	{
		Model resultTemp = ModelFactory.createDefaultModel();
		Model result = ModelFactory.createDefaultModel();
		
		StmtIterator modelStartIter = fromModel.listStatements();
		
		while(modelStartIter.hasNext())
		{
			Statement s1 = modelStartIter.nextStatement();
			
			if(s1.getPredicate().equals(predicate))
			{
				resultTemp.add(s1);
			}
		}
		
		StmtIterator modelTempIter = resultTemp.listStatements();
		
		while(modelTempIter.hasNext())
		{
			Statement s2 = modelTempIter.nextStatement();
			
			if(s2.getSubject().equals(subject))
			{
				result.add(s2);
			}
		}
		
		return result;
	}
	
	
	//public static Model getMod abc
	
	
	public static Model getModelByPredicateAndOther(String pre, String other, Model fromModel, int mode)
	{
		Model resultTemp = ModelFactory.createDefaultModel();
		Model result = ModelFactory.createDefaultModel();
		
		StmtIterator modelStartIter = fromModel.listStatements();
		
		while(modelStartIter.hasNext())
		{
			Statement s1 = modelStartIter.nextStatement();
			
			if(mode == 0)
			{
				if(s1.getPredicate().equals(pre))
				{
					resultTemp.add(s1);
				}
				
			} else if(mode == 1){
				
				if(s1.getPredicate().toString().contains(pre));
				{
					resultTemp.add(s1);
					System.out.println("(1)FIND-PRE > "+s1.getPredicate().toString()+" ---CONTAINS STRING--> "+pre);
				}
			} else if(mode == 2) {
				if(s1.getPredicate().toString().endsWith(pre));
				{
					resultTemp.add(s1);
					System.out.println("(1)FIND-PRE > "+s1.getPredicate().toString()+" ---ENDS WITH--> "+pre);
				}
			}
		}
		
		StmtIterator modelTempIter = resultTemp.listStatements();
		
		while(modelTempIter.hasNext())
		{
			Statement s2 = modelTempIter.nextStatement();
			
			if(mode == 0)
			{
				
				if(s2.getObject().equals(other) || s2.getSubject().equals(other))
				{
					result.add(s2);
				}
				
			} else if(mode == 1){
				
				if(s2.getSubject().toString().contains(other) || s2.getObject().toString().contains(other))
				{
					result.add(s2);
					System.out.println("(1)FIND-SUB > "+s2.getSubject().toString()+" ---CONTAINS STRING--> "+other);
				}
				
			} else if(mode == 2) {
				
				if(s2.getSubject().toString().endsWith(other))
				{
					result.add(s2);
					System.out.println("(1)FIND-SUB > "+s2.getSubject().toString()+" ---ENDS WITH--> "+other);
				}
				
				if(s2.getObject().toString().endsWith(other))
				{
					result.add(s2);
					System.out.println("(1)FIND-OBJ > "+s2.getObject().toString()+" ---ENDS WITH--> "+other);
				}
				
			}

		}
		
		System.out.println("----testoutput for fond getModelByPredicateAndOther("+pre+", "+other+")");
		
		StmtIterator finalIt = result.listStatements();
		
		while(finalIt.hasNext())
		{
			Statement stmt = finalIt.nextStatement();
			System.out.println("Statement: "+stmt);
		}
			
		
		
		return result;
	}


}
