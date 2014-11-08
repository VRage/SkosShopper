package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import controller.TriplesShowController.TripleOwn;

public class ProductFactory {
	
	private static boolean firstLoaded;
	
	public static String[] getCreatableProductsAsString()
	{
		// Triple is Statement
		// Object is RDFNode
		// Predicate is Property
		// Subject is Resource
		
		/* from concept to label

		 	skos/core#Concept | rdf-syntax-ns#type | skos/core#Bag
		 	
		 	skos/core#BagLabel | un#prefLabel | skos/core#Bag
		 	
		 	Bag^^http...XMLSchema#string | un#literalForm | core#BagLabel
		 */
		
		Model modelAllTriples = TripleModel.getAllTriples();
		
		Model modelSkosConcepts = TripleModel.getTriplesBySkosConcept(modelAllTriples);
		
		Model modelConceptLabels = TripleModel.getLabelsByConcept(modelAllTriples, modelSkosConcepts);
		
		Model modelLabelLiterals = TripleModel.getLiteralOfPrefLabel(modelConceptLabels, modelAllTriples);
		
		StmtIterator litIt = modelLabelLiterals.listStatements();
		
		final Set<String> productNames = new HashSet<String>();
		
		while(litIt.hasNext())
		{
			Statement productName = litIt.nextStatement();
			productNames.add(StringUtils.substringBefore(productName.getObject().toString(), "^^"));
		}
		
		String[] result = productNames.toArray(new String[productNames.size()]);
		Arrays.sort(result);
		
		return result;

	}

}
