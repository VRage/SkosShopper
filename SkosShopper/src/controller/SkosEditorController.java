package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class SkosEditorController implements Initializable {

	@FXML	private Button btn_addIndi;
	@FXML	private Button btn_addLabel;
	@FXML	private Label label_uri;
	@FXML	private Label label_uri2;
	@FXML	private TreeView tree_Classes;
	@FXML	private ListView<String> listview_indi;
	@FXML   private Button btn_show;
	
	
	public static final Logger log = Logger.getLogger(SkosEditorController.class);
	final TreeItem<String> root = new TreeItem<String>("Classes");
	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	private ObservableList<String> items =FXCollections.observableArrayList();
	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();
	private OntModel model = ModelFactory.createOntologyModel();
	private static OntClass selectedOntClass;
	private Individual selectedIndividual;
	private String NS = "";
	
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		btn_addLabel.setDisable(true);
		label_uri2.setText("");
		label_uri.setText(NS);
		tree_Classes.setRoot(root);
		root.setExpanded(true);
		loadOntologi();
		tree_Classes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                TreeItem treeItem = (TreeItem)newValue;
                if(!treeItem.getValue().toString().equals("Classes")){
        		int index = tree_Classes.getSelectionModel().getSelectedIndex()-1;
        		label_uri.setText(liste_classes.get(index).getURI());
        		NS = liste_classes.get(index).getNameSpace();
//        		System.out.println(treeItem.toString());
                String s = tree_Classes.getSelectionModel().getSelectedItem().toString().substring(18, tree_Classes.getSelectionModel().getSelectedItem().toString().length()-2);
                selectedOntClass = model.getOntClass(NS+s);
                listIndi(s);
                
                }
                
                
            }
		});
		fillTree();
		listview_indi.setItems(items);;
	}

	public void loadOntologi() {
		try{
		Path input = Paths.get("C:\\Users\\VRage\\workspace\\Skos\\", "test.owl");
		
		model.read(input.toUri().toString(), "RDF/XML");
		
//		model.read("./test1.rdf");
		
		ExtendedIterator classes = model.listClasses();
		while(classes.hasNext()){
			OntClass thisClass = (OntClass) classes.next();
			String s = thisClass.toString();
			if(s.contains("http://")){
				liste_classes.add(thisClass);
			}
			ExtendedIterator indi_list = thisClass.listInstances();
			while(indi_list.hasNext()){
				Individual indi = (Individual) indi_list.next();
				liste_indi.add(indi);
			}
			
		}

		
		
		log.info("Ontologie loaded");
		}catch(Exception e){
			log.error(e);
		}
	}
	
	public void fillTree(){
		for (OntClass ontclass : liste_classes) {
            TreeItem<String> empLeaf = new TreeItem<String>(ontclass.getLocalName());
//                while(ontclass.listInstances().hasNext()){
//                	Individual indi = (Individual) ontclass.listInstances().next();
//                	TreeItem<String> childLeaf = new TreeItem<String>(indi.getLocalName());
//                	empLeaf.getChildren().add(childLeaf);
//                }
                root.getChildren().add(empLeaf);
            }
		log.info("class added to List");
        
		
	}

	@FXML private void addIndi(ActionEvent event){
        Object antwort = JOptionPane.showInputDialog(null, "Enter name of Individual", "Eingabefenster",
               JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if(antwort!=null){
        String s = tree_Classes.getSelectionModel().getSelectedItem().toString().substring(18, tree_Classes.getSelectionModel().getSelectedItem().toString().length()-2);
        model.getOntClass(NS + s).createIndividual( NS +  ((String) antwort));;

        listIndi(s);
        }
	}
	
	public void listIndi(String ontclass)
	{
		
		items.clear();
		liste_indi.clear();
		selectedIndividual = null;
		OntClass indiClass = model.getOntClass(NS + ontclass);
		ExtendedIterator indilist = indiClass.listInstances();
		while(indilist.hasNext()){
			Individual indivi = (Individual) indilist.next();
			liste_indi.add(indivi);
			items.add(indivi.getLocalName());
		}
		
		
	}

	@FXML private void printToConsole(ActionEvent event){
		try {
		File file = new File("./test.owl");
			FileOutputStream out = new FileOutputStream(file);
			model.write(out, "RDF/XML");
			log.info("label hinzufügen");
			

				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML private void addLabel(ActionEvent event){
		if((selectedOntClass.getLocalName().equals("Concept"))&&(selectedIndividual != null)){
			
        Object antwort = JOptionPane.showInputDialog(null, "Enter name of Label", "Eingabefenster",
                JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if(antwort!=null){
        String name = (String) antwort;
        model.getOntClass("http://www.w3.org/2008/05/skos-xl#Label").createIndividual("http://www.w3.org/2008/05/skos-xl#LabelFor"+name);
		Individual indi = model.getIndividual("http://www.w3.org/2008/05/skos-xl#LabelFor"+name);
		DatatypeProperty dprop = model.getDatatypeProperty("http://www.w3.org/2008/05/skos-xl#literalForm");
			log.info("datatypeProp"+dprop.getLocalName());
			indi.addProperty(dprop, model.createLiteral( name, "de" ) );
			ObjectProperty Oprop = model.getObjectProperty("http://www.w3.org/2008/05/skos-xl#prefLabel");
			model.add(selectedIndividual, Oprop, indi);
		}
		}
        
	}
 
	@FXML private void handleMouseClicked(MouseEvent event){
		selectedIndividual = model.getIndividual(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI());
		label_uri2.setText(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI());
		btn_addLabel.setDisable(false);
	}
	
}
