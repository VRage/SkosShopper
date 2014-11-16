package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.Set;

import model.FusekiModel;
import model.ProductFactory;
import model.ModelFacade;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.core.DatasetGraph;

import exceptions.fuseki_exceptions.NoDatasetAccessorException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.fuseki.server.ServerConfig;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ProductCategorySettingsController implements Initializable{
	
	
	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = DEFINITION OF VARIABLES AND OBJECTS
	
	// = = = = = = = = = = FXML vars
	
	
	// FXML Elements of the left Pane
	@FXML	ScrollPane selectionPaneScrollPane;
	@FXML	AnchorPane selectionPaneAnchorPane;
	
	
	// FXML Elements of product categories
	@FXML	Pane productCategoriesPane;
	@FXML	Pane productCategoriesBtn;
	@FXML	TreeView productCategoriesTreeView;
	
	
	// FXML Elements of product propeties and values
	@FXML	Pane productPropertiesPane;
	@FXML	Pane productPropertiesBtn;
	@FXML	ListView productPropertiesListView;
	
	
	// FXML Elements of product and category relations
	@FXML	Pane productRelationsPane;
	@FXML	Pane productRelationsBtn;
	@FXML	ListView productRelationsListView;
	

	// FXML other Elements
	@FXML	Button btnCategoryCreate;
	@FXML	ScrollPane categoryProperties;
	
	
	
	// = = = = = = = = = = other vars
	
	public static final Logger log = Logger.getLogger(ProductCategorySettingsController.class);
	private boolean initialized;
	private EventHandler<MouseEvent> mouseEventHandler;
	
	
	
	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = DE-/CONSTRUCTOR AND INITIALIZATION METHODS
		
	
	// when tab is opened, this function is called
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		System.out.println("initialize()");
		
		// resize and position panes
		setStartSizes();
		//setStartPositions();
		setStartVisibilities();

		
		
		loadCategories();
		loadProperties();
		loadRelations();
		
	}
	
	
	private void setStartSizes()
	{
		double PaneHeight = 50.0;
		
		productCategoriesPane.setPrefHeight(PaneHeight);
		productCategoriesPane.setMinHeight(PaneHeight);
		productCategoriesPane.setMaxHeight(PaneHeight);
		
		productPropertiesPane.setPrefHeight(PaneHeight);
		productPropertiesPane.setMinHeight(PaneHeight);
		productPropertiesPane.setMaxHeight(PaneHeight);
		
		productRelationsPane.setPrefHeight(PaneHeight);
		productRelationsPane.setMinHeight(PaneHeight);
		productRelationsPane.setMaxHeight(PaneHeight);
	}
	
	
	private void setStartPositions()
	{
		productCategoriesPane.requestLayout();
		
		log.info("setStartPositions()");
		
		productCategoriesPane.setLayoutY(0.0);
		productPropertiesPane.setLayoutY(50);
		productRelationsPane.setLayoutY(100);

	}
	
	
	private void setStartVisibilities()
	{
		log.info("setStartVisibilities()");
		productCategoriesTreeView.setVisible(false);
		productPropertiesListView.setVisible(false);
		productRelationsListView.setVisible(false);
	}
	
	
	
	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = SIMPLE GETTER & SETTER
	
	// = = = = = = = = = = getter
	
	// = = = = = = = = = = setter
	
	
	
	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = COMPLEX GETTER & SETTER
	
	
	private void setSelectionPaneAnchorPaneHeight(double newHeight, int fromPane)
	{
		int height = 0;
		
		height += newHeight;
		
		switch(fromPane)
		{
		case 1:
			height += productPropertiesPane.getHeight(); height += productRelationsPane.getHeight();
			break;
		case 2:
			height += productCategoriesPane.getHeight(); height += productRelationsPane.getHeight();
			break;
		case 3:
			height += productPropertiesPane.getHeight(); height += productCategoriesPane.getHeight();
		}
		
		selectionPaneAnchorPane.setPrefHeight(height);
		selectionPaneAnchorPane.setMinHeight(height);
		selectionPaneAnchorPane.setMaxHeight(height);
	}
	
	
	
	
	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = OTHER METHODS
	
	
	private void treeClicked(MouseEvent e){
		System.out.println(e);
	}
	
	
	// loads the name of the categories and puts them into the TreeView of the product categories
	public void loadCategories()
	{
		log.info("loadCategories()");
		
		// initializing variables
		TreeView tv = productCategoriesTreeView;
		
		
		// Adding the Names of the ConceptSchemes as roots of the TreeView
		String[] conceptSchemeNames = ProductFactory.getConceptSchemeAsStringArray();
		
		for(int i = 0; i < conceptSchemeNames.length; i++)
		{
			System.out.println("#"+i+" ConceptSchemeLabel: "+conceptSchemeNames[i]);
			
			TreeItem root = new TreeItem();
			root.setValue(conceptSchemeNames[i]);
			root.setExpanded(true);
			tv.setRoot(root);
		}
		
		
		// Adding the top Concepts to the root
		/*String[] topConcepts = ProductFactory.getConceptURIsOfConceptScheme(tv.getRoot().getValue().toString());
		
		for(int i = 0; i < topConcepts.length; i++)
		{
			TreeItem item = new TreeItem();
			item.setValue(topConcepts[i]);
			//item.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
			tv.getRoot().getChildren().add(item);
			log.info("Added skos:Concept "+item.getValue().toString()+" to Product Category List");
		}
		*/
		
		
		
		// Iterate through all the root children and just print if they have narrower
		//String[] conArr = ProductFactory.getConceptsOfConceptScheme(tv.getRoot().getValue().toString());
		String[] conArr = ProductFactory.getConceptURIsOfConceptScheme(tv.getRoot().getValue().toString());
		List<String> conList = Arrays.asList(conArr);
		ListIterator<String> conIte = conList.listIterator();
		
		
		//
		//		VERSION 1
		//
		/*while(conIte.hasNext())
		{
			String uri = conIte.next();
			boolean hasNarrower = ModelFacade.hasNarrower(uri);
			TreeItem ti = new TreeItem();
			ti.setValue(uri);
			
			log.info("> skos:narrower "+uri);
			log.info("> "+hasNarrower);
			
			if(hasNarrower)
			{
				tv.getRoot().getChildren().add(addChilds(ti));
			} else {
				tv.getRoot().getChildren().add(ti);
			}
		}*/
		
		
		
		//
		//		VERSION 2
		//
		while(conIte.hasNext())
		{
			String uri = conIte.next();
			boolean hasNarrower = ModelFacade.hasNarrower(uri);
			TreeItem ti = new TreeItem();
			ti.setValue(uri);
			
			log.info("> skos:narrower "+uri);
			log.info("> "+hasNarrower);
			
			if(hasNarrower)
			{
				//tv.getRoot().getChildren().add(addChilds(tv.getRoot(), ti, ""));
				addChilds(ti, "");
			} else {
				ti.setValue(ModelFacade.getLiteralByConcept(ti.getValue().toString()));
			}
			
			tv.getRoot().getChildren().add(ti);
			
		}
		

		
		
		// Iterating through all root childs and add new childs if necessary
		//ListIterator<TreeItem> treeIte = tv.getRoot().getChildren().listIterator();
		
		
		StmtIterator stmti = ModelFacade.getNarrowerModel("http://rdf.getting-started.net/ontology/Camera").listStatements();
		/*
		while(treeIte.hasNext())
		{
			TreeItem treeCurr = treeIte.next();
			treeCurr.getChildren().add(addChilds(treeCurr));
		}
		*/
		

	}
	
	
	
	
	//
	//		VERSION 2
	//
	public static void addChilds(TreeItem root, String pre) {
		
		String rootURI = root.getValue().toString();
		
		log.info(pre+"> I am "+root);
		
		if(ModelFacade.hasNarrower(rootURI))
		{
			log.info(pre+"> > I have children");
			
			Model model = ModelFacade.getNarrowerModel(rootURI);
			
			StmtIterator childs = model.listStatements();
			ModelFacade.printModel(model, "-----------> ");
			
			while(childs.hasNext())
			{
				Statement childChild = childs.nextStatement();
				TreeItem child = new TreeItem();
				child.setValue(childChild.getObject().toString());
				
				log.info(pre+"> > > One Child is "+child);
				
				root.getChildren().add(child);
				
				log.info(pre+"> > > I added it to me: "+root);
				
				log.info(pre+"> > > I check him");
				
				addChilds(child, pre+"> > > ");
			}
			
			log.info(pre+"> I have no more childs");
			
			ListIterator<TreeItem> it;
			it = root.getChildren().listIterator();
			
			log.info(pre+"> My Childs are now:");
			
			while(it.hasNext())
			{
				TreeItem ti = it.next();
				log.info(pre+"> "+ti);
			}
			
		} else {
			log.info(pre+"> I have no childs");
		}
		
		root.setValue(ModelFacade.getLiteralByConcept(root.getValue().toString()));
		
		//root.setValue(ModelFacade.getLiteralByConcept(childURI));
		//return root;
		//child.setValue(ModelFacade.getLiteralByConcept(childURI));
		//root.getChildren().add(child);
		
		
		//log.info(pre+"> I add myslef to my parent "+child);
		//root.getChildren().add(child);

	}
	

	
	//
	//		VERSION 1
	//
	/*public static TreeItem addChilds(TreeItem currItem) {
		
		String uri = currItem.getValue().toString();
		
		if(ModelFacade.hasNarrower(uri))
		{
			Model model = ModelFacade.getNarrowerModel(uri);
			
			StmtIterator stmti = ModelFacade.getNarrowerModel(uri).listStatements();
			
			while(stmti.hasNext())
			{
				Statement stmt = stmti.nextStatement();
				TreeItem ti = new TreeItem();
				ti.setValue(stmt.getObject().toString());
				
				currItem.getChildren().add(addChilds(ti));
				
				
				// puts subtrees everywhere
				// currItem.getChildren().add(addChilds(ti));
				
				// this returns only the most child items of a subtree
				// currItem.getChildren().add(ti);
				// return addChilds(ti);
			}
	
		} 
		
		currItem.setValue(ModelFacade.getLiteralByConcept(uri));
		return currItem;

	}*/
	
		
	
	
	private void loadProperties()
	{
		
	}
	
	
	private void loadRelations()
	{
		
	}
	
	
	
	public void createCategory()
	{
		log.info("createCategories()");
		

	}
	
	
	// when you click on product categories, this method is called
	public void openProductCategories(MouseEvent me)
	{
		double newHeight = calculateHeight(1);

		if(!productCategoriesTreeView.isVisible())
		{
			productCategoriesTreeView.setVisible(true);
		} else {
			newHeight = 40.0;
			productCategoriesTreeView.setVisible(false);
		}
		
		productCategoriesPane.setPrefHeight(newHeight);
		productCategoriesPane.setMinHeight(newHeight);
		productCategoriesPane.setMaxHeight(newHeight);
		
		productPropertiesPane.setLayoutY(newHeight);
		
		productRelationsPane.setLayoutY(newHeight+productPropertiesPane.getHeight());
		
		printYAndHeight();
		setSelectionPaneAnchorPaneHeight(newHeight, 1);
	}
	
	
	// when you click on product properties, this method is called
	public void openProductProperties(MouseEvent me)
	{
		double newHeight = calculateHeight(2);
		
		if(!productPropertiesListView.isVisible())
		{
			productPropertiesListView.setVisible(true);
		} else {
			newHeight = 40.0;
			productPropertiesListView.setVisible(false);
		}
			
		productPropertiesPane.setPrefHeight(newHeight);
		productPropertiesPane.setMinHeight(newHeight);
		productPropertiesPane.setMaxHeight(newHeight);
			
		productRelationsPane.setLayoutY(productCategoriesPane.getHeight()+newHeight);
		
		printYAndHeight();
		setSelectionPaneAnchorPaneHeight(newHeight, 2);
	}
	
	
	// when you click on product relations, this method is called
	public void openProductRelations(MouseEvent me)
	{
		double newHeight = calculateHeight(3);
		
		if(!productRelationsListView.isVisible())
		{
			productRelationsListView.setVisible(true);
		} else {
			newHeight = 40.0;
			productRelationsListView.setVisible(false);
		}
			
		productRelationsPane.setPrefHeight(newHeight);
		productRelationsPane.setMinHeight(newHeight);
		productRelationsPane.setMaxHeight(newHeight);
		
		printYAndHeight();
		
		setSelectionPaneAnchorPaneHeight(newHeight, 3);
	}


	private double calculateHeight(int num)
	{
		switch(num)
		{
		case 1:
			return calculateCategoriesHeight();
		case 2:
			return calculatePropertiesHeight();
		case 3:
			return calculateRelationsHeight();
		}
		
		return 0.0;

	}
	
	private void printYAndHeight()
	{
		System.out.println("Categories: Y = "+productCategoriesPane.getLayoutY()+", H = "+productCategoriesPane.getHeight());
		System.out.println("Properties: Y = "+productPropertiesPane.getLayoutY()+", H = "+productPropertiesPane.getHeight());
		System.out.println("Relations:  Y = "+productRelationsPane.getLayoutY()+", H = "+productRelationsPane.getHeight()+"\n");
	}
	
	
	private double calculateCategoriesHeight()
	{
		
		
		return 300.0;
	}
	
	
	private double calculatePropertiesHeight()
	{
		return 200.0;
	}
	
	
	private double calculateRelationsHeight()
	{
		return 150.0;
	}

}
