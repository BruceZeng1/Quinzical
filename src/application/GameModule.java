package application;


import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Category;
import model.Question;
import model.Winnings;
import utilities.BashCommand;
import utilities.GUIValues;
import utilities.GameStateLoader;

/**
 * The GameModu class creates the GUI for the game module and manages the necessary logic and objects
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class GameModule {

	private Scene gamesScene; 
	private ArrayList<Category> _questionBank;
	private ArrayList<Category> _boardCatList;
	private Winnings _winnings;
	private boolean intUnlocked;
	private Label _currentScore;
	private VBox vBoxForCategories;
	private Boolean stateLoaded;
	private static final int NUMQUESTIONS = 5;
	private GameModuleAnswer gameModuleAnswer;

	/**
	 * The GameModu class creates the GUI for the game module and manages the necessary logic and objects
	 * @param questionBank the list of all category objects
	 */
	GameModule(ArrayList<Category> questionBank){
		_winnings = new Winnings(Winnings.loadWinnings());
		_currentScore = new Label("Score: "+_winnings.getWinnings());
		_currentScore.setTextFill(Color.web(GUIValues.textColor));
		_boardCatList = new ArrayList<Category>();
		vBoxForCategories = new VBox(18);
		_questionBank = questionBank;
		gameModuleAnswer = new GameModuleAnswer(this);
		stateLoaded = GameStateLoader.LoadGameState(_boardCatList);
		intUnlocked=false;
		
		if (check2FullyAnswered()) {
			Main.unlockInternational();
			intUnlocked=true;
		}	
		
		// create Bottom Buttons - Question Scene
		Pane filler = new Pane();
		HBox games_HBbuttons = new HBox();
		Button gamesMod_buttonBack = new Button("Back");	
		gamesMod_buttonBack.setStyle("-fx-base: "+GUIValues.backButtonColour+"");
		games_HBbuttons.getChildren().add(gamesMod_buttonBack);
		games_HBbuttons.getChildren().add(filler);
		HBox.setHgrow(filler, Priority.ALWAYS);
		games_HBbuttons.getChildren().add(_currentScore);
		games_HBbuttons.setAlignment(Pos.CENTER_LEFT);

		// game category setup
		Label info = new Label("Select a Category");
		info.setTextFill(Color.web(GUIValues.textColor));
		info.setFont(new Font("Arial", 24));
		vBoxForCategories.getChildren().add(info);
		if (stateLoaded) {
			buildCatButtons();
		}

		// Build GameModule SCENE
		BorderPane gamesPane = new BorderPane();
		gamesPane.setStyle(GUIValues.backgroundStyle);
		gamesPane.setPadding(new Insets(20));
		gamesPane.setBottom(games_HBbuttons);
		gamesPane.setCenter(vBoxForCategories);
		gamesScene = new Scene(gamesPane,GUIValues.screenWidth,GUIValues.screenHeight);

		//setup button behaviors
		gamesMod_buttonBack.setOnAction(e -> {
			stopSpeaking();
			Main.setPrimaryScene();
		});
	}

	/**
	 * Builds category buttons for game module
	 */
	protected void buildCatButtons() {
		//Create all menu buttons to ask questions
		for (Category currentCat: _boardCatList) {
			VBox currentVBox = new VBox(2);
			Button currentCategoryButton = new Button(currentCat.getName()+" "+(currentCat.getNumAnswered()+1)*100);
			Label currentCatInfo = new Label("Answered: " + currentCat.getNumAnswered()+"/"+NUMQUESTIONS);
			currentCatInfo.setTextFill(Color.web(GUIValues.textColor));
			currentCatInfo.setAlignment(Pos.CENTER);
			currentCatInfo.setTextAlignment(TextAlignment.CENTER);
			currentCategoryButton.setStyle("-fx-base: "+GUIValues.alternateButtonColour+" ; -fx-background-radius: 25; -fx-font-size: 15px;");
			//Disable buttons if all the questions are already answered.
			if (currentCat.getNumAnswered()>=NUMQUESTIONS) {
				currentCategoryButton.setText(currentCat.getName());
				currentCatInfo.setText("Complete!");
				currentCategoryButton.setDisable(true);
			}
			//Add button to button VBox and set GUI properties.
			currentCategoryButton.setMinWidth(164);
			currentCategoryButton.setMinHeight(35);
			currentVBox.getChildren().addAll(currentCategoryButton, currentCatInfo);
			currentVBox.setAlignment(Pos.CENTER);
			vBoxForCategories.getChildren().add(currentVBox);
			vBoxForCategories.setAlignment(Pos.CENTER);
			addCatButtonLogic(currentCategoryButton, currentCatInfo, currentCat);
		}
	}

	/**
	 * Add category button logic to the given button
	 * @param currentCategoryButton the category button
	 * @param currentCatInfo the label containing category information
	 * @param currentCat the category with which the button is associated
	 */
	private void addCatButtonLogic(Button currentCategoryButton, Label currentCatInfo, Category currentCat) {
		currentCategoryButton.setOnAction(e -> {
			if (currentCat.getNumAnswered() < NUMQUESTIONS) { // if questions are not all answered 
				Question _currentQuestion = currentCat.getQuestion(currentCat.getNumAnswered());
				int _questionValue = ((currentCat.getNumAnswered() + 1) * 100);
				gameModuleAnswer.selectQuesiton(_currentQuestion, _questionValue);
				currentCat.setNumAnswered(currentCat.getNumAnswered() + 1); //update info on and under button
				currentCatInfo.setText("Answered: " + currentCat.getNumAnswered()+"/"+NUMQUESTIONS);
				currentCategoryButton.setText(currentCat.getName()+" "+(currentCat.getNumAnswered()+1)*100);
			} 
			if (currentCat.getNumAnswered() >= NUMQUESTIONS) { //if questions are all anwered then disable button
				currentCategoryButton.setText(currentCat.getName());
				currentCategoryButton.setDisable(true);
				currentCatInfo.setText("Complete!");
			}
		});
	}
	
	/**
	 * Updates score label in GameModule
	 */
	protected void updateScorelabel() {
		_currentScore.setText("Score: "+_winnings.toString());
	}

	/**
	 * reset the game module data, lock international.
	 */
	protected void reset() {
		Main.lockInternational();
		BashCommand delete = new BashCommand("rm -r tmp/categories");
		delete.endProcess();
		_winnings.clearWinnings();
		Main.resetGameModule();
	}

	/**
	 * set the scene to the game module menu if there are questions to load in, otherwise go to question
	 * selection menu
	 */
	public void setScene() {
		if (stateLoaded) {
			Main.getStage().setScene(gamesScene);
			checkAllAnswered();
		} else {
			stateLoaded = true;
			@SuppressWarnings("unused")
			CategorySelection _catSelect = new CategorySelection(_questionBank, this);
		}
	}

	/**
	 * returns true if all questions are answered in the games module.
	 * @return boolean indicating if all quest
	 */
	boolean allAnswered() {
		boolean allAnswered = true;
		for (Category cat: _boardCatList) {
			if (cat.getNumAnswered()<5) {
				allAnswered = false;
			}
		}
		return allAnswered;
	}

	/**
	 * check if international section needs to be unlocked and unlock if it does
	 */
	protected void checkForUnlockInternational() {
		if (!intUnlocked && check2FullyAnswered()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.getDialogPane().getStylesheets().add(getClass().getResource("popup.css").toExternalForm());
			alert.getDialogPane().applyCss();
			alert.setContentText("International section in Practice module unlocked");
			alert.setHeaderText("Congratulations!");
			alert.setTitle("Two categories completed");
			alert.showAndWait();
			intUnlocked=true;
			Main.unlockInternational();
		}
	}

	/**
	 * Check if all questions are answered and go to final screen if true
	 */
	public void checkAllAnswered() {
		if (allAnswered()) {
			gameModuleAnswer.setSceneFinalScreen();
		}
	}

	/**
	 * Check if at least two categories are fully answered or not.
	 * @return boolean true if two or more categories are fully answered, and false otherwise.
	 */
	public boolean check2FullyAnswered() {
		int count = 0;
		for (Category cat: _boardCatList) {
			if (cat.getNumAnswered() >= 5) {
				count++;
			}
		}
		if (count >= 2) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * save game state to tmp
	 */
	public void saveState() {
		_winnings.saveWinnings();
		GameStateLoader.saveGameState(_boardCatList);
	}

	/**
	 * @return winnings object from game module.
	 */
	protected Winnings getWinnings() {
		return _winnings;
	}
	
	/**
	 * assign to board category list to the one provided
	 * @param catList the categories for the game module.
	 */
	protected void setBoardCatList(ArrayList<Category> catList) {
		_boardCatList = catList;
	}

	public void stopSpeaking() {
		gameModuleAnswer.stopSpeaking();
	}
}
