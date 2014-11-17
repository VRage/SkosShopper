package localozation;

public abstract class Language {
	public static final int ENGLISH_enEN = 1;
	public static final int GERMAN_deDE   = 2;
	
	
	/** Menu.fxml Items **/
	public static String menuFile;
	public static String menuItemClose;
	public static String menuItemDelete;

	public static String menuLanguage;
	public static String menuItemEN;
	public static String menuItemGER;

	public static String menuHelp;
	public static String menuItemAbout;

	public static String settingsItem;
	public static String importOnthologyFileItem;
	public static String onStartItem;
	
	/** Overview.fxml Items **/
	public static String labelFusekiServer;
	public static String labelStatusFuseki;
	public static String labelVersion;
	public static String labelStartedFuseki;
	
	public static String labelOverviewServerDataInfo;
	public static String labelOverviewAmountCustomClasses;
	public static String labelOverviewAmountIndividuals;
	public static String btnHome;

	//Buttons with corrupt label
	public static String startStopFusekiStarted;
	public static String startStopFusekiStoped;
	public static String checkServer;
	
	/** Main.fxml Items **/
	public static String tabOverview;
	public static String tabTriplesShow;
	public static String tabSPARQLQuery;
	public static String tabProductCategoriesSettings;
	public static String tabProductsShow;
	public static String tabProductCreate;
	public static String tabSKOSEditorLite;
	public static String tabSKOSOntology;
	
	/**ClasssesCreate.fxml Items **/
	public static String checkboxClassesCreateFunctional;
	public static String checkboxClassesCreateSymetric;
	public static String checkboxClassesCreateReflexive;
	public static String checkboxClassesCreateTransitive;
	public static String checkboxClassesCreateInverseFunctional;
	public static String checkboxClassesCreateAsymetric;
	public static String checkboxClassesCreateIrreflexive;
	public static String buttonClassesCreateResetFields;
	public static String buttonClassesCreateSave;
	
	/** TriplesShow.fxml Items **/
	public static String loadTriplesFromServerBtn;
	public static String object;
	public static String predicate;
	public static String subject;
	//tableTriples

}
