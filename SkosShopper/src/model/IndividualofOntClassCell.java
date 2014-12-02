package model;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import com.hp.hpl.jena.ontology.Individual;

public class IndividualofOntClassCell extends TreeCell<Individual> {
	
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button();
    Individual lastItem;
    private TreeView<Individual> parentTree;
    
    public IndividualofOntClassCell(final TreeView<Individual> parentTree) {
        super();
        this.parentTree = parentTree;
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
        label.setFont(new Font("System", 12));
        button.setPrefHeight(18.0);
        button.setPrefWidth(18.0);
        button.setId("btndeltxtfields");
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
    

    
}
