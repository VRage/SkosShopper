package localozation;

public class Language_enEN extends Language{
	public Language_enEN(){
		initMenuItems();
		initOverviewItems();
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
}
