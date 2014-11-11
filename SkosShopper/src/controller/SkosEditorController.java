package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.swing.JOptionPane;

import model.TripleModel;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class SkosEditorController implements Initializable {

	@FXML	private Button btn_addIndi;
	@FXML	private Button btn_addLabel;
	@FXML	private Label label_uri;
	@FXML	private Label label_uri2;
	@FXML	private TreeView tree_Classes;
	@FXML	private ListView<String> listview_indi;
	@FXML	private ListView<String> listview_objprop;
	@FXML	private ListView<String> listview_dataprop;
	@FXML   private Button btn_show;
	@FXML 	private Button btn_addProp;
	@FXML 	private Group grp_addProp;
	@FXML	private ChoiceBox choicebox_properties;
	@FXML	private ChoiceBox choicebox_indi;
	
	
	public static final Logger log = Logger.getLogger(SkosEditorController.class);
	final TreeItem<String> root = new TreeItem<String>("Classes");
	private ArrayList<OntClass> liste_classes = new ArrayList<OntClass>();
	private ObservableList<String> items =FXCollections.observableArrayList();
	private ObservableList<String> props =FXCollections.observableArrayList();
	private ArrayList<String> propNS = new ArrayList<String>();
	private ArrayList<String> indiNS = new ArrayList<String>();
	private ObservableList<String> indis =FXCollections.observableArrayList();
	private ArrayList<Individual> liste_indi = new ArrayList<Individual>();
	private OntModel model = ModelFactory.createOntologyModel();
	private static OntClass selectedOntClass;
	private Individual selectedIndividual;
	private String NS = "";
	private String baseNS ="";
	private String skosNS ="";
	private String skosxlNS ="";
	
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		
		btn_addLabel.setDisable(true);
		grp_addProp.setDisable(true);
		label_uri2.setText("");
		label_uri.setText(NS);
		loadOntologi();
		listview_indi.setCursor(Cursor.HAND);
		tree_Classes.cursorProperty().set(Cursor.HAND);
		tree_Classes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                TreeItem treeItem = (TreeItem)newValue;
                if(treeItem != null){
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
                
            }
		});
		tree_Classes.setRoot(root);
		
		choicebox_indi.setItems(indis);
		choicebox_properties.setItems(props);
		listview_indi.setItems(items);
		
	}

	public void loadOntologi() {
		if(model.isEmpty()){
		try{
//		Path input = Paths.get("C:\\Users\\VRage\\Documents\\SpiderOak Hive\\studium\\5_Semester\\projekt\\", "test1.rdf");
//		
//		model.read(input.toUri().toString(), "RDF/XML");
		
		model.read("./fuseki/Data/test1.rdf");
			//model = TripleModel.getAllTriples();
//			Model m = FusekiModel.getDatasetAccessor().getModel();
//		model.add(m);
		
		baseNS = model.getNsPrefixURI("");
		log.info("Base NS set to: "+baseNS);
		skosNS = model.getNsPrefixURI("skos");
		log.info("Skos NS set to: "+skosNS);
		skosxlNS = model.getNsPrefixURI("skos-xl");
		log.info("Skosxl NS set to: "+skosxlNS);

		
		
		
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
		listProps();
		listIndis();
		
		fillTree();

		
		log.info("Ontologie loaded");
		}catch(Exception e){
			log.error(e,e );
		}
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
        model.getOntClass(NS + s).createIndividual( baseNS +  ((String) antwort));
        int length = baseNS.length();
        String indinamespace =  model.getIndividual( baseNS +  ((String) antwort)).getNameSpace();
        if(!(length == indinamespace.length())){
        	String superindi = indinamespace.substring(length, indinamespace.length()-1);
        	log.info(superindi);
        	String[] stringarray = superindi.split("/");
        	String newindi = baseNS;
        	for(String ss : stringarray){
        		Individual tempindi = model.getIndividual(newindi+ss);
        		if(tempindi==null)
        		{
        			model.getOntClass(NS + s).createIndividual(newindi+ss);
        			newindi = newindi+ss+"/";
        			log.info("new individual added: "+ ss);
        			
        		}else{
        			newindi = tempindi.getURI()+"/";
        		}
        		log.info(ss);
        	}
        	newindi = baseNS;
        	if(stringarray.length>0){
	        	for(int i =0;i<stringarray.length;i++){
	        		Individual tempindi = model.getIndividual(newindi+stringarray[i]);
	        		ObjectProperty oProp = model.getObjectProperty(skosNS+"narrower");
	        		if(i <stringarray.length-1){
	        			Individual nextindi = model.getIndividual(newindi+stringarray[i]+"/"+stringarray[i+1]);
	        			model.add(tempindi, oProp, nextindi);
	        		}else{
	        			Individual nextindi = model.getIndividual(baseNS +  ((String) antwort));
	        			model.add(tempindi, oProp, nextindi);
	        		}
	        		newindi = newindi+stringarray[i]+"/";
	        	}
        	}
        }
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
		File file = new File("./fuseki/Data/outputfromProgramm.owl");
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
        model.getOntClass(skosxlNS+"Label").createIndividual(baseNS+"LabelFor"+name);
		Individual indi = model.getIndividual(baseNS+"LabelFor"+name);
		DatatypeProperty dprop = model.getDatatypeProperty(skosxlNS+"literalForm");
			log.info("datatypeProp"+dprop.getLocalName());
			indi.addProperty(dprop, model.createLiteral( name, "de" ) );
			ObjectProperty Oprop = model.getObjectProperty(skosxlNS+"prefLabel");
			model.add(selectedIndividual, Oprop, indi);
		}
		}
        
	}
 
	@FXML private void handleMouseClicked(MouseEvent event){
		if(!listview_indi.getSelectionModel().isEmpty()){
			selectedIndividual = model.getIndividual(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI());
			label_uri2.setText(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI());
			btn_addLabel.setDisable(false);
			grp_addProp.setDisable(false);
			showObjectProperties(selectedIndividual);
			showDataProperties(selectedIndividual);
			
			if (event.getClickCount() == 2) {
				String selected = model.getIndividual(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI()).getLocalName();
				int delete = JOptionPane.showConfirmDialog(null, "Do you really want to Delete: "+selected, "Delete Indivdual?",
						JOptionPane.YES_NO_OPTION);
				if(delete == JOptionPane.YES_OPTION){
					model.getIndividual(liste_indi.get(listview_indi.getSelectionModel().getSelectedIndex()).getURI()).remove();
					listIndi(selectedOntClass.getLocalName());
					showObjectProperties(selectedIndividual);
					showDataProperties(selectedIndividual);
				}
			}
		}
	}
	
	@FXML private void addProp(ActionEvent event){
		if(!choicebox_properties.getSelectionModel().isEmpty() &&!choicebox_indi.getSelectionModel().isEmpty()){
			int index_prop  =choicebox_properties.getSelectionModel().getSelectedIndex();
			String NSofprop = propNS.get(index_prop);
			ObjectProperty oProp = model.getObjectProperty(NSofprop);
			log.info("Objectpropertie selected: "+ oProp.getNameSpace());
			int index_indi  =choicebox_indi.getSelectionModel().getSelectedIndex();
			String NSofindi = indiNS.get(index_indi);		
			Individual individual = model.getIndividual(NSofindi);
			log.info("Individual selected: "+ individual.getLocalName());
			model.add(selectedIndividual, oProp, individual);
		}
		
	}
	
	private void listProps(){
		ExtendedIterator list_prop = model.listObjectProperties();
		while(list_prop.hasNext()){
			ObjectProperty prop = (ObjectProperty) list_prop.next();
			props.add(prop.getLabel("en"));			
			propNS.add(prop.getURI());
			log.info("property added: "+prop.getLocalName());
		}
	}
	private void listIndis(){
		ExtendedIterator list_indis = model.listIndividuals();
		while(list_indis.hasNext()){
			Individual indi = (Individual) list_indis.next();
			indis.add(indi.getLocalName());
			log.info("Individual added: "+indi.getLocalName());
			indiNS.add(indi.getURI());
		}
	}
	private void showObjectProperties(Individual selectedIndividual){
		// Property Window ID: listview_dataprop
		listview_dataprop.setItems(null);
		if(selectedIndividual!=null){
			StmtIterator iterProperties = selectedIndividual.listProperties();
			ObservableList<String> items =FXCollections.observableArrayList();
			String predicate = "";
			String object = "";
			while(iterProperties.hasNext()){
				Statement nextProperty = iterProperties.next();
				if(nextProperty.getPredicate().getNameSpace().equals(skosNS) || nextProperty.getPredicate().getNameSpace().equals(skosxlNS)){
					try {
						
						if (nextProperty.getObject().isResource()){
							predicate = model.getObjectProperty(nextProperty.getPredicate().getURI()).getLabel("en");
							object = nextProperty.getObject().asResource().getLocalName();
							items.add("'"+predicate+"'" + "  " + object + "\n\n");
						}
							
					} catch (ResourceRequiredException e) {
						log.error(e, e);
					}		
				}
				if(!items.isEmpty()){
					listview_dataprop.setItems(items);
				}
	//			System.out.println(nextProperty);
	//			System.out.println(nextProperty.getPredicate().getLocalName());
	//			System.out.println(nextProperty.getObject().asResource().getLocalName());
	//			System.out.println(skosNS);
		}
		}
	}
	
	private void showDataProperties(Individual selectedIndividual){
		// Property Window ID: listview_objprop
		listview_objprop.setItems(null);
		if(selectedIndividual!=null){
			StmtIterator iterProperties = selectedIndividual.listProperties();
			ObservableList<String> items =FXCollections.observableArrayList();
			String predicate = "";
			String object = "";
			while(iterProperties.hasNext()){
				Statement nextProperty = iterProperties.next();
				if(nextProperty.getPredicate().getNameSpace().equals(skosNS) || nextProperty.getPredicate().getNameSpace().equals(skosxlNS)){
					try {
						
						if (nextProperty.getObject().isLiteral()){
							predicate = nextProperty.getPredicate().getLocalName();
							object = nextProperty.getObject().asLiteral().toString();
							items.add("'"+predicate +"'"+ "  " + object + "\n\n");
						}
								
					} catch (ResourceRequiredException e) {
						log.error(e, e);
					}		
				}
				if(!items.isEmpty()){
					listview_objprop.setItems(items);
				}
			}
		}
	}

	
	
}
