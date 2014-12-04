package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
    
    public void chancheHirarchyOfModel(Individual draged, Individual droped){
    	
    }
  
}

