package model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import localozation.Language;
import localozation.Language_deDE;
import localozation.Language_enEN;
import main.Main;

public class LanguageModel {
	/** English by default **/
	private static Language selectedLanguage = new Language_enEN();
	
	
	/** 
	 * This method sets up a new prefered language 
	 *	that can be used to change the interface
	 *
	 * 	The language-code can be achieved by the Language class itself
	 * 	it is stored in an final String! For example:
	 * 	Language.ENGLISH_enEN for the standart english enEN
	 *	
	 *	It automatically updates the view!
	 ***/	
	public static void setLanguage(int lang){
		switch(lang){
		case Language.ENGLISH_enEN:
			selectedLanguage = new Language_enEN();
		break;
		case Language.GERMAN_deDE:
			selectedLanguage = new Language_deDE();
		break;
		}
		updateView();
	}
	
	/** 
	 * Programms can get the currently selected
	 *  Language without setting a new one with this method
	 */
	public static Language getSelectedLanguage(){
		return selectedLanguage;
	}
	
	private static void updateView(){
		updateOverviewItems();
	}
	
	private static void updateOverviewItems(){
		// scene.lookup("#id")
		//Button test =(Button) Main.scene.lookup("#btn_addIndi");
		Label label = (Label) Main.scene.lookup("#labelFusekiServer");
		label.setText(LanguageModel.getSelectedLanguage().labelFusekiServer);
		
		label = (Label) Main.scene.lookup("#labelVersion");
		label.setText(LanguageModel.getSelectedLanguage().labelVersion);
		
		label = (Label) Main.scene.lookup("#labelStartedFuseki");
		label.setLayoutX(label.getLayoutX() - computeRightPosition(LanguageModel.getSelectedLanguage().labelStartedFuseki, label.getText()));
		label.setText(LanguageModel.getSelectedLanguage().labelStartedFuseki);

		Button button = (Button) Main.scene.lookup("#checkServer");
		button.setLayoutX(button.getLayoutX() - computeRightPosition(LanguageModel.getSelectedLanguage().checkServer,button.getText()));
		button.setText(LanguageModel.getSelectedLanguage().checkServer);
	}
	
	/** This method calculates a new x-position for a button or label when it has to be fixed on the right side and grow
	 * to the left when the new text is longer than the old one
	 */
	private static double computeRightPosition(String newString, String oldString){
		if(newString.length() >= oldString.length()){
			return 5.5* (newString.length() - oldString.length());
		}
		return -5.5*(oldString.length() - newString.length());
	}
	
}
