package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import model.ProductFactory;
import model.TripleModel;

import com.hp.hpl.jena.rdf.model.Model;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class ProductCategoryController implements Initializable{
	
	@FXML	TreeView categoriesTreeView;
	@FXML	Button btnCategoryCreate;
	@FXML	ScrollPane categoryProperties;
	
	public static final Logger log = Logger.getLogger(ProductCategoryController.class);
	private boolean initialized;
	private EventHandler<MouseEvent> mouseEventHandler;
	
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		System.out.println("init productCategoryController");
		
	
		initialized = false;
		TreeView tv = categoriesTreeView;
		
		TreeItem root = new TreeItem();
		root.setValue("Product Categories");
		
		tv.setRoot(root);
		
		//String[] proCat = ProductFactory.getCreatableProductsAsURI();
		String[] proCat = ProductFactory.getCreatableProductsAsString();		
		
		for(int i = 0; i < proCat.length; i++)
		{
			TreeItem item = new TreeItem();
			item.setValue(proCat[i]);
			log.info("category found, URI:"+item.getValue().toString());
		}
		
		Set<TreeItem> cats = new HashSet<TreeItem>();
		
		for(int i = 0; i < proCat.length; i++)
		{
			TreeItem item = new TreeItem();
			item.setValue(proCat[i]);
			//item.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
			cats.add(item);
			log.info("Added skos:Concept "+item.getValue().toString()+" to Product Category List");
		}

		tv.getRoot().getChildren().addAll(cats);
	}
	
	
	private void treeClicked(MouseEvent e){
		System.out.println(e);
	}
	
	
	
	public void loadCategories()
	{
		log.info("loadCategories()");
	}
	
	
	
	public void createCategory()
	{
		log.info("createCategories()");
	}

}
