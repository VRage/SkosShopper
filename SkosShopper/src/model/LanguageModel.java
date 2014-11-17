package model;

import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
		updateMainviewItems();
	}
	
	private static void updateOverviewItems(){
		// scene.lookup("#id")
		//Button test =(Button) Main.scene.lookup("#btn_addIndi");
		setLabelText("#labelFusekiServer", selectedLanguage.labelFusekiServer);
		setLabelText("#labelVersion", selectedLanguage.labelVersion);
		setLabelTextWithRightAllignment("#labelStartedFuseki", selectedLanguage.labelStartedFuseki);

		setButtonTextWithRightAllignment("#checkServer", selectedLanguage.checkServer);
	}
	
	private static void updateMainviewItems(){
		TabPane pane = (TabPane) Main.scene.lookup("#mainTabPane");
		ObservableList<Tab> tabList = pane.getTabs();
		Iterator i = tabList.iterator();
		while(i.hasNext()){
			Tab tab = (Tab) i.next();
			String id = tab.getId();
			
			if(id != null){
			
				if(id.equals("tabOverview"))
					tab.setText(selectedLanguage.tabOverview);
				
				if(id.equals("tabTriplesShow"))
					tab.setText(selectedLanguage.tabTriplesShow);
				
				if(id.equals("tabSPARQLQuery"))
					tab.setText(selectedLanguage.tabSPARQLQuery);
				
				if(id.equals("tabProductCategoriesSettings"))
					tab.setText(selectedLanguage.tabProductCategoriesSettings);
				
				if(id.equals("tabProductsShow"))
					tab.setText(selectedLanguage.tabProductsShow);
				
				if(id.equals("tabProductCreate"))
					tab.setText(selectedLanguage.tabProductCreate);
				
				if(id.equals("tabSKOSEditorLite"))
					tab.setText(selectedLanguage.tabSKOSEditorLite);
				
				if(id.equals("tabSKOSOntology"))
					tab.setText(selectedLanguage.tabSKOSOntology);
			}
		}
		
	}

	private static void setButtonText(String id, String text){
		Button button = (Button) Main.scene.lookup(id);
		if(button != null){
			button.setText(text);
		}
		else
			System.out.println("Button: '" + id + "' not found");
	}
	
	private static void setButtonTextWithRightAllignment(String id, String text){
		Button button = (Button) Main.scene.lookup(id);
		if(button != null){
			button.setLayoutX(button.getLayoutX() - computeRightPosition(text,button.getText()));
			setButtonText(id, text);
		}
		else
			System.out.println("Button: '" + id + "' not found");
	}
	
	private static void setLabelText(String id, String text){
		Label label = (Label) Main.scene.lookup(id);
		if(label != null){
			label.setText(text);
		}
		else
			System.out.println("Label: '" + id + "' not found");
	}
	
	private static void setLabelTextWithRightAllignment(String id, String text){
		Label label = (Label) Main.scene.lookup(id);
		if(label != null){
			label.setLayoutX(label.getLayoutX() - computeRightPosition(text,label.getText()));
			setLabelText(id, text);
		}
		else
			System.out.println("Button: '" + id + "' not found");
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
