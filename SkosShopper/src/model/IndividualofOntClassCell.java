package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import main.Main;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import controller.MainController;

public class IndividualofOntClassCell extends TreeCell<Individual> {
	
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button();
    Individual lastItem;
    private MainController mcon =  Main.loader.getController();
    private TreeView<Individual> parentTree;
    public OntModel model;
//    private static final DataFormat IndividualDataFormat = new DataFormat("IndiWrapper");
    
    
    public IndividualofOntClassCell(final TreeView<Individual> parentTree,final OntModel model) {
        super();
        this.parentTree = parentTree;
        this.model = model;
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	lastItem.remove();
                mcon.skoseditorliteController.showIndividualsOfOntClass(mcon.skoseditorliteController.selectedOntClass);
            }
        });
        label.setFont(new Font("System", 12));
        button.setPrefHeight(18.0);
        button.setPrefWidth(18.0);
        button.setId("btndeltxtfields");
        
        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Drag detected on " + lastItem);
                if (lastItem == null) {
                    return;
                }
                Dragboard dragBoard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(DataFormat.PLAIN_TEXT, lastItem.getURI());
                dragBoard.setContent(content);
                event.consume();
            }
        });
        setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                System.out.println("Drag done on " + lastItem);

                dragEvent.consume();
            }
        });
        // ON TARGET NODE.
//        setOnDragEntered(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent dragEvent) {
//                System.out.println("Drag entered on " + item);
//                dragEvent.consume();
//            }
//        });        
        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                System.out.println("Drag over on " + lastItem);
                if (dragEvent.getDragboard().hasString()) {
                	System.out.println("try to cast");
                	String valueToMove = dragEvent.getDragboard().getString();
                	System.out.println(valueToMove);
                    if (!valueToMove.equals(lastItem.getURI()) ){
                        // We accept the transfer!!!!!
                    	System.out.println("transfer accepted");
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }else{
                    	System.out.println("isgleich");
                    }
                }
                dragEvent.consume();
            }
        });
//        setOnDragExited(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent dragEvent) {
//                System.out.println("Drag exited on " + item);
//                dragEvent.consume();
//            }
//        });        
        setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                System.out.println("Drag dropped on " + lastItem);
            	String uri = dragEvent.getDragboard().getString();
            	System.out.println(uri);
            	Individual valueToMove = model.getIndividual(uri);
            	chancheHirarchyOfModel(valueToMove, lastItem);
                mcon.skoseditorliteController.showIndividualsOfOntClass(mcon.skoseditorliteController.selectedOntClass);
            }
        });
    }
    
    
        
    @Override
    protected void updateItem(Individual item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            label.setText(item!=null ? item.getLocalName() : "<null>");
            setGraphic(hbox);
        }
        

    }
    
    public void chancheHirarchyOfModel(Individual individualToMove, Individual individualDestination){
    	//First of all we are only able to move individuals of ontclass Concept
    	if(mcon.skoseditorliteController.selectedOntClass.getLocalName().equals("Concept")){
    		//We will need the narrower Property alot in this method so we generate it
    		Property narrower = model.createObjectProperty(mcon.skoseditorliteController.skosNS + "narrower");
    		System.out.println("Property created: " + narrower);
    		//Generate a TreeView to work better with the Items
    		//TreeItem<Individual> root = new TreeItem<Individual>();
    		/**root = mcon.skoseditorliteController.showIndividualsOfOntClassRecursive(
    				mcon.skoseditorliteController.selectedOntClass, 
    				root);*/
    		TreeItem<Individual> root = mcon.skoseditorliteController.root;
    		System.out.println("Tree successfully created");
    		//search the two Items in the view to be able to relocate one of them
    		TreeItem<Individual> itemToMove      = searchTreeItem(individualToMove, root);
    		System.out.println("itemToMove created with: " + itemToMove.getValue().getLocalName());
    		TreeItem<Individual> itemDestination = searchTreeItem(individualDestination, root);
    		System.out.println("itemDestination created with: " + itemDestination.getValue().getLocalName());
    		
    		//Step1: if my item to move has children, i have to link them to the parent of my item to move 
    		// except the parent is root. Also we need to delelte the link between children and itemToMove
    		if(!itemToMove.getParent().equals(root) && !itemToMove.getChildren().isEmpty()){
    			System.out.println("Item has Parent:" + itemToMove.getParent().getValue().getLocalName() + "\n and Children!");
    			Iterator<TreeItem<Individual>> children = itemToMove.getChildren().iterator();
    			while(children.hasNext()){
    				TreeItem<Individual> child = children.next();
    				System.out.println("Child: " + child.getValue().getLocalName());
    				itemToMove.getParent().getValue().addProperty(narrower, child.getValue());
    			}
    		}
    		//if item to move has root as parent but has children we need to just delete the parent/child relation
    		if(!itemToMove.getChildren().isEmpty()){
    			System.out.println("ItemtoMove has Children to be removed");
    			Iterator<TreeItem<Individual>> children = itemToMove.getChildren().iterator();
    			while(children.hasNext()){
    				TreeItem<Individual> child = children.next();
    				System.out.println("Child: " + child.getValue().getLocalName());
    				individualToMove.removeProperty(narrower, child.getValue());
    			}
    		}
    		//after that we are able to move the itemToMove to its new Parent and delete the old relation
    		individualDestination.addProperty(narrower, individualToMove);
    		System.out.println("New Relation betwee ItemToMove and itemDestination created");
    		if(!itemToMove.getParent().equals(root)){
    		itemToMove.getParent().getValue().removeProperty(narrower, individualToMove);
    		System.out.println("Old Relation betwee itemToMove and its Parent removed");
    		}
    	}
    }
    
    private TreeItem<Individual> searchTreeItem(Individual individual, TreeItem<Individual> item){
    	//if its not the root element, compare it with the individual
    	System.out.println("searchTreeItem entered!");
    	if(item.getValue() != null){
    		System.out.println("ItemValue = " + item.getValue().getLocalName());
	    	if(item.getValue().equals(individual)){
	    		System.out.println("Individual found: " + item.getValue().getLocalName());
	    		return item;
	    	}
	    	//otherwise continue with a recursion as long as there are children
    	}
    		
    	if(!item.getChildren().isEmpty()){
			Iterator<TreeItem<Individual>> children = item.getChildren().iterator();
			while(children.hasNext()){
				TreeItem<Individual> child = children.next();
				System.out.println("Child Found in searchTreeItem: " + child.getValue().getLocalName());
				TreeItem<Individual> result = searchTreeItem(individual, child);
				System.out.println("result " + result);
				if(result != null){
					return result;
				}
	    	
	    	}
    	}
    	System.out.println("Item: " + item.getValue().getLocalName() + " is Leaf and not a match!");
    	return null;
    }
}

