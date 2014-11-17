package localozation;

public class Language_enEN extends Language{
	public Language_enEN(){
		initMenuItems();
		initOverviewItems();
		initMaintems();
		initClassesCreateItems();
	}
	
	private void initMenuItems(){
		menuFile="File";
		menuItemClose="Exit";
		menuItemDelete="Delete";
	
		menuLanguage="Select Language";
		menuItemEN="English";
		menuItemGER="German";
	
		menuHelp="Help";
		menuItemAbout="About";
	
		settingsItem="Settings";
		importOnthologyFileItem="Import Ontology";
		onStartItem="On Start Settings";
	}
	
	private void initOverviewItems(){
		labelFusekiServer="Fuseki Server";
		labelStatusFuseki="Status:";
		labelVersion="Version:";
		labelStartedFuseki="Started:";

		//Buttons with corrupt label
		startStopFusekiStarted="Start";
		startStopFusekiStoped="Stop";
		checkServer="Check Server";
	}
	
	private void initMaintems(){
		tabOverview="Overview";
		tabTriplesShow="Triples";
		tabSPARQLQuery="SparQL Query";
		tabProductCategoriesSettings="Product Categories";
		tabProductsShow="Products";
		tabProductCreate="Create Product";
		tabSKOSEditorLite="SKOS Editor Lite";
		tabSKOSOntology="SKOS Editor";
	}
	
	private void initClassesCreateItems(){
		checkboxClassesCreateFunctional="Functional";
		checkboxClassesCreateSymetric="Symetric";
		checkboxClassesCreateReflexive="Reflexive";
		checkboxClassesCreateTransitive="Transitive";
		checkboxClassesCreateInverseFunctional="Inverse Functional";
		checkboxClassesCreateAsymetric="Asymetric";
		checkboxClassesCreateIrreflexive="Irreflexive";
		buttonClassesCreateResetFields="Reset Fields";
		buttonClassesCreateSave="Save";
	}
}
