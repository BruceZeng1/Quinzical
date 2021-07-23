package application;


import java.util.ArrayList;

import guiTools.MacronButtons;
import guiTools.TTSManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Category;
import model.Question;
import utilities.GUIValues;
import utilities.GameStateLoader;

/**
 * This class manages the creation and running of the practice module of Quinzical
 * @author Bruce Zeng and Adam Wiener
 *
 */
public class PracticeModule {

	private Scene practiceScene;
	private Scene practiceAnswerScene;
	private Scene practiceResultScene;
	private int attempts;
	private Question chosenQuestion;
	private ArrayList<Category> _questionBank;
	private Label remainingAttempts;
	private TextField practiceAnswerTextField;
	private Label prefixLabel;
	private TTSManager ttsManager;
	private Label result;
	private Label answerLabel;
	private Label resultQuestionLabel;
	private Button _internationalButton;
	private Category _internationalQuestions;
	private HBox ttsHBox;
	private Button practiceBack;
	private GridPane catBox;
	private Label questionLabel;
	private Label hintLabel;

	/**
	 * Practice object creates the GUI for the all of the practice module
	 * Includes all necessary logic for practice module.
	 * @param questionBank takes a list of Categories as parameter
	 */
	PracticeModule (ArrayList<Category> questionBank) {
		_questionBank = questionBank;
		initialiseFieldsAndGUI();
		_internationalQuestions = GameStateLoader.loadInternationalQuestions();

		// HBox for buttons for the Practice Pane
		HBox practice_HBbuttons = new HBox(15);
		Button ps_buttonBack = new Button("Back");
		ps_buttonBack.setStyle("-fx-base: "+GUIValues.backButtonColour+"");
		
		// Build Practice SCENE
		BorderPane practicePane = new BorderPane();
		practicePane.setStyle(GUIValues.backgroundStyle);
		Label practiceLabel = new Label("Practice Module");
		practiceLabel.setTextFill(Color.web(GUIValues.textColor));
		practiceLabel.setFont(new Font("Arial", 30));
		practiceLabel.setAlignment(Pos.TOP_CENTER);
		practiceLabel.setTextAlignment(TextAlignment.CENTER);
		practicePane.setPadding(new Insets(20));
		practicePane.setBottom(practice_HBbuttons);
		practiceScene = new Scene(practicePane,GUIValues.screenWidth,GUIValues.screenHeight);

		// VBox for Practice Answer pane
		VBox practiceAnsVBox = new VBox(10);
		Button practiceSubmit = new Button("Submit");
		practiceSubmit.setMinWidth(100);
		practiceSubmit.setMinHeight(40);
		practiceSubmit.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 30;");
		MacronButtons macrons = new MacronButtons(practiceAnswerTextField);
		practiceAnsVBox.getChildren().addAll(questionLabel, prefixLabel, practiceAnswerTextField, macrons.getButtonHBox(), practiceSubmit, remainingAttempts, hintLabel);
		practiceAnsVBox.setAlignment(Pos.CENTER);

		// Practice Answer Pane
		BorderPane practiceAnswerPane = new BorderPane();
		practiceAnswerPane.setStyle(GUIValues.backgroundStyle);
		practiceAnswerPane.setCenter(practiceAnsVBox);
		practiceAnswerPane.setBottom(ttsHBox);
		practiceAnswerPane.setPadding(new Insets(20));
		practiceAnswerScene = new Scene(practiceAnswerPane,GUIValues.screenWidth,GUIValues.screenHeight);

		// Grid for all categories
		catBox = new GridPane();
		buildCatButtons();
		
		// VBox for categories and Label
		VBox practiceVbox = new VBox(20);
		practiceVbox.getChildren().addAll(practiceLabel, catBox, _internationalButton);
		practiceVbox.setAlignment(Pos.CENTER);
		practicePane.setCenter(practiceVbox);

		// Screen for after giving answer
		BorderPane practiceResult = new BorderPane();
		practiceResult.setPadding(new Insets(20));
		practiceResult.setStyle(GUIValues.backgroundStyle);
		VBox result_Vbox = new VBox(10);
		Button resultBackButton = new Button("Back");
		result_Vbox.getChildren().addAll(result, resultQuestionLabel, answerLabel, resultBackButton);
		result_Vbox.setAlignment(Pos.CENTER);
		resultBackButton.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-background-radius: 20;");
		resultBackButton.setMinHeight(40);
		resultBackButton.setMinWidth(200);
		practice_HBbuttons.getChildren().add(ps_buttonBack);
		practiceResult.setCenter(result_Vbox);
		practiceResultScene = new Scene(practiceResult,GUIValues.screenWidth,GUIValues.screenHeight);
		
		//setup set on action behavior
		resultBackButton.setOnAction(e -> {
			ttsManager.stopSpeaking();
			setScene();
		});
		
		ps_buttonBack.setOnAction(e -> {
			ttsManager.stopSpeaking();
			Main.setPrimaryScene();
		});
		
		practiceSubmit.setOnAction(e -> {
			answerQuestion();
		});
		
		practiceAnswerTextField.setOnAction(e -> {
			answerQuestion();
		});
		
		practiceBack.setOnAction(e -> {
			hintLabel.setVisible(false);
			stopSpeaking();
			attempts = 3;
			Main.getStage().setScene(practiceScene);
		});
		
		_internationalButton.setOnAction(e -> {
			chosenQuestion = _internationalQuestions.getRandomQuestion();
			ttsManager.setQuestion(chosenQuestion);
			remainingAttempts.setText("Remaining attempts: " + attempts);
			questionLabel.setText(chosenQuestion.getQuestion()+".");
			prefixLabel.setText(chosenQuestion.getPrefix().toUpperCase()+": ");
			ttsManager.speak(chosenQuestion.getQuestion());
			Main.getStage().setScene(practiceAnswerScene);
		});
	}

	/**
	 * Initializes fields for practice module and applies styling to GUI elements.
	 */
	private void initialiseFieldsAndGUI() {
		attempts = 3;
		remainingAttempts = new Label();
		remainingAttempts.setTextAlignment(TextAlignment.CENTER);
		remainingAttempts.setAlignment(Pos.CENTER);
		practiceAnswerTextField = new TextField();
		result = new Label();
		answerLabel = new Label();
		answerLabel.setTextFill(Color.web(GUIValues.textColor));
		resultQuestionLabel = new Label();
		result.setTextFill(Color.web(GUIValues.textColor));
		result.setFont(new Font ("Arial", 20));
		answerLabel.setFont(new Font ("Arial", 16));
		resultQuestionLabel.setFont(new Font ("Arial", 16));
		resultQuestionLabel.setTextFill(Color.web(GUIValues.textColor));
		resultQuestionLabel.setMaxWidth(400);
		resultQuestionLabel.setWrapText(true);
		resultQuestionLabel.setTextAlignment(TextAlignment.CENTER);
		resultQuestionLabel.setAlignment(Pos.CENTER);
		prefixLabel = new Label();
		prefixLabel.setTextFill(Color.web(GUIValues.textColor));
		practiceAnswerTextField.setMaxWidth(300);
		practiceAnswerTextField.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-font-size: 14px; -fx-background-radius: 10;");
		_internationalButton = new Button("International");
		_internationalButton.setDisable(true);
		_internationalButton.setStyle("-fx-base: "+GUIValues.backButtonColour+"; -fx-background-radius: 15; -fx-font-size: 16px");
		_internationalButton.setMinWidth(160);
		_internationalButton.setMinHeight(35);
		remainingAttempts = new Label("Remaining attempts: " + attempts);
		remainingAttempts.setTextFill(Color.web(GUIValues.textColor));
		questionLabel = new Label();
		questionLabel.setTextFill(Color.web(GUIValues.textColor));
		questionLabel.setFont(new Font("Arial", 18));
		questionLabel.setMaxWidth(500);
		questionLabel.setWrapText(true);
		questionLabel.setTextAlignment(TextAlignment.CENTER);
		questionLabel.setAlignment(Pos.CENTER);
		practiceBack = new Button("Back");
		practiceBack.setStyle("-fx-base: "+GUIValues.backButtonColour+"");
		ttsHBox = new HBox(15);
		ttsHBox.getChildren().add(practiceBack);
		ttsHBox.setAlignment(Pos.CENTER);
		ttsManager = new TTSManager(ttsHBox);
		hintLabel = new Label();
		hintLabel.setTextFill(Color.web(GUIValues.textColor));
	}
	
	/**
	 * Builds the category buttons for the module
	 */
	private void buildCatButtons() {
		for (int i = 0; i < _questionBank.size(); i++) {
			Category category = _questionBank.get(i);
			Button catButton = new Button(category.getName());
			catBox.add(catButton, i%3, i/3);
			catButton.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 25; -fx-font-size: 14px");
			catButton.setMinWidth(130);
			catButton.setMaxWidth(130);
			catButton.setMinHeight(40);
			catButton.setOnAction(e -> {
				chosenQuestion = category.getRandomQuestion();
				ttsManager.setQuestion(chosenQuestion); //speak question and assign data
				remainingAttempts.setText("Remaining attempts: " + attempts);
				questionLabel.setText(chosenQuestion.getQuestion()+".");
				prefixLabel.setText(chosenQuestion.getPrefix().toUpperCase()+": ");
				ttsManager.speak(chosenQuestion.getQuestion());
				Main.getStage().setScene(practiceAnswerScene);
			});
		}
		catBox.setVgap(10);
		catBox.setHgap(10);
		catBox.setAlignment(Pos.CENTER);
	}

	/**
	 * Sets the scene to the practice scene
	 */
	public void setScene() {
		Main.getStage().setScene(practiceScene);
	}
	
	/**
	 * Checks the answer in the text field and compares it to the correct answer
	 * if correct then return to the practice scene if incorrect reduce remaining attemps by 1.
	 * When attempts == 1 A hint it shown. When attempts == 0 the incorrect answer scene is shown
	 * with the correct answer.
	 */
	public void answerQuestion() {
		if (chosenQuestion.answerQuestion(practiceAnswerTextField.getText())) {
			ttsManager.speak("Correct");
			resultQuestionLabel.setText("");
			answerLabel.setText("Answer: " +chosenQuestion.getAnswer());
			result.setText("Correct");
			Main.getStage().setScene(practiceResultScene);
			attempts = 3;
			hintLabel.setVisible(false);
		} else {
			attempts -= 1;
			if (attempts != 0) {
				ttsManager.speak("incorrect");
			}
			if (attempts == 1) {
				remainingAttempts.setText("Remaining Attempts: 1");
				hintLabel.setText("HINT: The first letter is "+chosenQuestion.getAnswer().charAt(0));
				hintLabel.setVisible(true);
			} else if (attempts == 0) {
				ttsManager.speak("incorrect. The correct answer was "+ chosenQuestion.getAnswer().replaceAll("/", " or "));
				resultQuestionLabel.setText(chosenQuestion.getQuestion());
				answerLabel.setText("Answer: " +chosenQuestion.getAnswer());
				result.setText("Incorrect");
				Main.getStage().setScene(practiceResultScene);
				attempts = 3;
				hintLabel.setVisible(false);
			} else {
				remainingAttempts.setText("Remaining attempts: " + attempts);
			}
		}
		practiceAnswerTextField.clear();
	}

	/**
	 * Unlocks the International section
	 */
	public void unlockInternational() {
		_internationalButton.setDisable(false);
		_internationalButton.setStyle("-fx-base: "+GUIValues.alternateButtonColour+" ; -fx-background-radius: 25; -fx-font-size: 16px");

	}

	/**
	 * locks the international section
	 */
	public void lockInternational() {
		_internationalButton.setDisable(true);
		_internationalButton.setStyle("-fx-base: "+GUIValues.backButtonColour+"; -fx-background-radius: 25; -fx-font-size: 16px");
	}

	public void stopSpeaking() {
		ttsManager.stopSpeaking();
	}
}