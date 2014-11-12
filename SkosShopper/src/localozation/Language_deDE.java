package localozation;

public class Language_deDE extends Language{
	public Language_deDE(){
		initMenuItems();
		initOverviewItems();
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
	}
}
