package localozation;

public class Language_deDE extends Language{
	public Language_deDE(){
		initMenuItems();
		initOverviewItems();
		initMaintems();
	}
	
	private void initMenuItems(){
		menuFile="Datei";
		menuItemClose="Schließen";
		menuItemDelete="Löschen";
	
		menuLanguage="Sprachauswahl";
		menuItemEN="Englisch";
		menuItemGER="Deutsch";
	
		menuHelp="Hilfe";
		menuItemAbout="Über";
	
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
		checkServer="Überprüfe Server";
	}
	
	private void initMaintems(){
		tabOverview="Übersicht";
		tabTriplesShow="Tripel";
		tabSPARQLQuery="SparQL Abfrage";
		tabProductCategoriesSettings="Produkt Kategorien";
		tabProductsShow="Produkte";
		tabProductCreate="Produkte Erstellen";
		tabSKOSEditorLite="SKOS Editor Lite";
		tabSKOSOntology="SKOS Editor";
	}
}
