package localozation;

public class Language_deDE extends Language{
	public Language_deDE(){
		initMenuItems();
		initOverviewItems();
		initMaintems();
		initClassesCreateItems();
	}
	
	private void initMenuItems(){
		menuFile="Datei";
		menuItemClose="Schlie�en";
		menuItemDelete="L�schen";
	
		menuLanguage="Sprachauswahl";
		menuItemEN="Englisch";
		menuItemGER="Deutsch";
	
		menuHelp="Hilfe";
		menuItemAbout="�ber";
	
		settingsItem="Einstellungen";
		importOnthologyFileItem="Ontologie importieren";
		onStartItem="Programmstart Einstellungen";
	}
	
	private void initOverviewItems(){
		labelFusekiServer="Fuseki Server";
		labelStatusFuseki="Status:";
		labelVersion="Version:";
		labelStartedFuseki="Gestartet:";

		//Buttons with corrupt label
		startStopFusekiStarted="Start";
		startStopFusekiStoped="Stop";
		checkServer="�berpr�fe Server";
		
		labelOverviewServerDataInfo="Serverdaten Info";
		labelOverviewAmountCustomClasses="Anzahl eigener Klassen";
		labelOverviewAmountIndividuals="Anzahl Individuen";
		btnHome="Fuseki Startseite";
	}
	
	private void initMaintems(){
		tabOverview="�bersicht";
		tabTriplesShow="Tripel";
		tabSPARQLQuery="SparQL Abfrage";
		tabProductCategoriesSettings="Produkt Kategorien";
		tabProductsShow="Produkte";
		tabProductCreate="Produkte Erstellen";
		tabSKOSEditorLite="SKOS Editor Lite";
		tabSKOSOntology="SKOS Editor";
	}
	
	private void initClassesCreateItems(){
		checkboxClassesCreateFunctional="Funktional";
		checkboxClassesCreateSymetric="Symmetrisch";
		checkboxClassesCreateReflexive="Reflexiv";
		checkboxClassesCreateTransitive="Transitiv";
		checkboxClassesCreateInverseFunctional="Invers Funktional";
		checkboxClassesCreateAsymetric="Asymmetrisch";
		checkboxClassesCreateIrreflexive="Irreflexiv";
		buttonClassesCreateResetFields="Felder zur�cksetzen";
		buttonClassesCreateSave="Speichern";
	}
}
