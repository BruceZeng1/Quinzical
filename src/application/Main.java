package application;


import java.io.File;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Category;
import utilities.GUIValues;
import utilities.GameStateLoader;

/**
 * Main class for Quinzical application, also handles main menu
 * @author Adam Wiener and Bruce Zeng
 *
 */

public class Main extends Application {

	private static ArrayList<Category> _questionBank;
	private static Scene scene_main;
	private static Stage _stage;
	private static PracticeModule _practice;
	private static GameModule _gameModu;
	private static Boolean accessability;
	private AddQuestion _addQuestion;


	public void start(Stage primaryStage) {
		_stage = primaryStage;
		_practice = new PracticeModule(_questionBank);
		_gameModu = new GameModule(_questionBank);
		_addQuestion = new AddQuestion(_questionBank);
		accessability = false;

		if (_questionBank.size() < 5) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("You must have at least 5 categories present in the Questions folder");
			alert.setContentText("Please add more and delete tmp/ if you encounter errors");
			alert.showAndWait();
		}
		
		String invalidCats = _questionBank.stream()
				.filter(c -> c.numQuestions()<5)
				.map(c -> c.getName())
				.collect(Collectors.joining("\n"));
		
		if(!invalidCats.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("The categories:\n"+ invalidCats + "\nhave fewer than 5 questions");
			alert.setContentText("Please add more and delete tmp/ if you\nencounter errors");
			alert.showAndWait();
		}
		
		// create center Buttons and Labels
		VBox menu_centerVbox = new VBox(5);

		Label labelWelcome = new Label("Quinzical");
		labelWelcome.setFont(new Font("Dekko", 60));
		labelWelcome.setStyle("-fx-font-weight: BOLD;");
		labelWelcome.setTextFill(Color.web(GUIValues.textColor));
		labelWelcome.setAlignment(Pos.TOP_CENTER);

		Label newZealandEdition = new Label("New Zealand");
		newZealandEdition.setFont(new Font("Dekko", 18));
		newZealandEdition.setTextFill(Color.web(GUIValues.textColor));
		newZealandEdition.setAlignment(Pos.TOP_CENTER);

		Button button_Game = new Button("Game Module");
		Button button_Practice = new Button("Practice");
		button_Game.setStyle(GUIValues.buttonStyle);
		button_Game.setMinWidth(200);
		button_Practice.setStyle(GUIValues.buttonStyle);
		button_Practice.setMinWidth(200);
		menu_centerVbox.getChildren().addAll(labelWelcome,newZealandEdition, button_Game, button_Practice);
		menu_centerVbox.setAlignment(Pos.CENTER);

		// create Bottom Buttons - Menu Scene
		HBox menu_HBbuttons = new HBox(5);
		Button menu_buttonExit = new Button("Exit");
		Pane filler = new Pane();
		Button showQuestion = new Button("Accessibility");
		menu_buttonExit.setStyle("-fx-base: "+GUIValues.backButtonColour+"");
		showQuestion.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"");
		
		Button button_add_question = new Button("Add Question");
		button_add_question.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"");
		Button help = new Button("Help");
		help.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"");
		HBox.setHgrow(filler, Priority.ALWAYS);
		menu_HBbuttons.getChildren().addAll(menu_buttonExit, filler, showQuestion, button_add_question, help);
		menu_HBbuttons.setAlignment(Pos.CENTER_LEFT);

		// Create MENU SCREEN
		BorderPane menu = new BorderPane();
		menu.setPadding(new Insets(20));
		menu.setCenter(menu_centerVbox);
		menu.setBottom(menu_HBbuttons);
		menu.setStyle(GUIValues.backgroundStyle);

		scene_main = new Scene(menu, GUIValues.screenWidth, GUIValues.screenHeight);
		primaryStage.setTitle("Quinzical"); 
		primaryStage.setScene(scene_main);
		primaryStage.show();
		
		
		//add button actions and logic
		primaryStage.setOnCloseRequest(e -> {
			_gameModu.stopSpeaking();
			_practice.stopSpeaking();
			_gameModu.saveState();
		});
		
		button_Practice.setOnAction(e -> {
			_practice.setScene();
		});
		
		button_Game.setOnAction(e -> {
			_gameModu.setScene();
		});

		button_add_question.setOnAction(e -> {
			_addQuestion.setScene();
		});
		
		help.setOnAction(e -> {
			//open help file
			File myFile = new File(System.getProperty("user.dir")+ "//quinzical_user_manual.pdf");
			HostServices hs = getHostServices();
			hs.showDocument(myFile.getAbsolutePath());
		});
		
		showQuestion.setOnAction(e -> {
			if (!accessability) {
				showQuestion.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"");
				accessability = true;

			} else {
				showQuestion.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"");
				accessability = false;
			}

		});
		
		menu_buttonExit.setOnAction(e -> {
			_gameModu.saveState();
			primaryStage.close();
		});
	}

	public static boolean accessability() {
		return accessability;

	}

	public static void main(String[] args) {
		_questionBank = GameStateLoader.loadQuestionBank();
		launch(args);
	}

	/**
	 * sets the stage to display the main menu
	 */
	public static void setPrimaryScene() {
		_stage.setScene(scene_main);
	}

	/**
	 * @return main stage of the GUI
	 */
	public static Stage getStage() {
		return _stage;
	}

	/**
	 * resets the game module by creating a new GameModu object
	 */
	public static void resetGameModule() {
		_gameModu = new GameModule(_questionBank);
	}

	/**
	 * Unlocks the international section in the practice game
	 */
	public static void unlockInternational() {
		_practice.unlockInternational();
	}

	/**
	 * locks the international section in the practice game
	 */
	public static void lockInternational() {
		_practice.lockInternational();

	}
}