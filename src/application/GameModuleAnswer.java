package application;

import java.util.Timer;
import java.util.TimerTask;

import guiTools.MacronButtons;
import guiTools.TTSManager;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.Question;
import utilities.GUIValues;

/**
 * Class to handle answering of selected Questions from GameModule
 * Also handles TTS and timer organization and final screen.
 * @author se206-2020
 *
 */
public class GameModuleAnswer {
	private Label _valueLabel;
	private TextField gameAnswerTextBox;
	private Label prefixLabel;
	private Label correctLabel;
	private HBox gamesAnsHBoxAForTTS;
	private TTSManager ttsManager;
	private Scene gameAnswerScene;
	private Scene postAnswerScene;
	private Labeled _timeleft;
	private ImageView image;
	private Label scoreLabel;
	private Scene scoreScene;
	private GameModule gameModule;
	private Question _currentQuestion;
	private Timer timer;
	private TimerTask timerTask;
	private int _questionValue;

	public GameModuleAnswer(GameModule gameModule) {
		this.gameModule = gameModule;
		_valueLabel = new Label();
		_valueLabel.setTextFill(Color.web(GUIValues.textColor));
		_valueLabel.setMaxWidth(500);
		_valueLabel.setWrapText(true);
		_valueLabel.setTextAlignment(TextAlignment.CENTER);
		_valueLabel.setAlignment(Pos.CENTER);
		_valueLabel.setFont(new Font("Arial", 18));
		gameAnswerTextBox = new TextField();
		prefixLabel = new Label();
		prefixLabel.setTextFill(Color.web(GUIValues.textColor));
		correctLabel = new Label();
		correctLabel.setTextFill(Color.web(GUIValues.textColor));
		correctLabel.setStyle("-fx-font-size: 34px");
		gamesAnsHBoxAForTTS = new HBox(15);
		gamesAnsHBoxAForTTS.setAlignment(Pos.BASELINE_CENTER);
		ttsManager = new TTSManager(gamesAnsHBoxAForTTS);
		_timeleft = new Label("");	
		_timeleft.setTextFill(Color.web(GUIValues.textColor));
		scoreLabel = new Label("Your final score is: " + gameModule.getWinnings().getWinnings());
		scoreLabel.setTextFill(Color.web(GUIValues.textColor));
		scoreLabel.setTextAlignment(TextAlignment.CENTER);
		scoreLabel.setAlignment(Pos.TOP_CENTER);

		// Setup GUI elements in VBox for Game Answer pane where question is entered and submitted
		VBox gamesAns = new VBox(10);
		Button submit = new Button("Submit");
		submit.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 30;");
		submit.setMinWidth(100);
		submit.setMinHeight(40);
		gameAnswerTextBox.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-font-size: 14px; -fx-background-radius: 10;");
		gameAnswerTextBox.setMaxWidth(300);
		MacronButtons macrons = new MacronButtons(gameAnswerTextBox);
		gamesAns.getChildren().addAll(_valueLabel, prefixLabel, gameAnswerTextBox, macrons.getHbox(), submit, _timeleft);
		gamesAns.setAlignment(Pos.CENTER);

		// Create game Answer Pane
		BorderPane gameAnswerPane = new BorderPane();
		gameAnswerPane.setStyle(GUIValues.backgroundStyle);
		gameAnswerPane.setCenter(gamesAns);
		gameAnswerPane.setBottom(gamesAnsHBoxAForTTS);
		gameAnswerPane.setPadding(new Insets(20));
		gameAnswerScene = new Scene(gameAnswerPane,GUIValues.screenWidth,GUIValues.screenHeight);

		//builds post answer scene that says correct or incorrect
		Button okayButton = new Button("ok");
		okayButton.setMinWidth(100);
		okayButton.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 30; -fx-font-size: 20px;");
		okayButton.setAlignment(Pos.CENTER);
		VBox postAnswerVbox = new VBox(5);
		postAnswerVbox.setAlignment(Pos.CENTER);
		postAnswerVbox.getChildren().addAll(correctLabel, okayButton);
		BorderPane postAnswerPane = new BorderPane();
		postAnswerPane.setCenter(postAnswerVbox);
		postAnswerPane.setStyle(GUIValues.backgroundStyle);
		postAnswerPane.setPadding(new Insets(20));
		postAnswerScene = new Scene(postAnswerPane,GUIValues.screenWidth,GUIValues.screenHeight);

		//create score screen pane
		BorderPane scorePane = new BorderPane();
		scorePane.setPadding(new Insets(20));
		scorePane.setStyle(GUIValues.backgroundStyle);
		VBox playAgain_VBox = new VBox(15);
		scoreLabel.setFont(new Font("Arial", 20));
		Label congrat = new Label("Congratulations!");
		congrat.setFont(new Font("Arial", 40));
		congrat.setTextFill(Color.web(GUIValues.textColor));
		image = new ImageView(new Image(getClass().getResourceAsStream("trophy.png")));
		image.setFitHeight(200);
		image.setFitWidth(200);
		Button scorePlayAgain = new Button("Play Again");
		scorePlayAgain.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-font-size: 20px ;-fx-background-radius: 30");
		playAgain_VBox.getChildren().addAll(congrat, scoreLabel, scorePlayAgain,image);
		playAgain_VBox.setAlignment(Pos.CENTER);
		scorePane.setCenter(playAgain_VBox);
		scoreScene = new Scene(scorePane,GUIValues.screenWidth,GUIValues.screenHeight);

		scorePlayAgain.setOnAction(e -> {
			gameModule.reset();
			Main.setPrimaryScene();
		});

		okayButton.setOnAction(e -> {
			ttsManager.stopSpeaking();
			gameModule.setScene();
		});

		submit.setOnAction(e -> {
			ttsManager.stopSpeaking();
			answerQuestion();
		});

		gameAnswerTextBox.setOnAction(e -> {
			ttsManager.stopSpeaking();
			answerQuestion();
		});
	}

	/**
	 * Select question to answer/play with
	 * @param currentQuestion Question object to use
	 * @param value value of question
	 */
	public void selectQuesiton (Question currentQuestion, int value) {
		_currentQuestion = currentQuestion;
		_questionValue = value;
		ttsManager.setQuestion(_currentQuestion);
		_valueLabel.setText("Question value: " + value);
		if (Main.accessability()) { //Show question if accessibility mode is active
			_valueLabel.setText(_currentQuestion.getQuestion());
		}
		_timeleft.setText("Time left: 30");
		buildTimer(); 
		ttsManager.askQuestion(_currentQuestion.getQuestion(), timer, timerTask); //speak question and set scene to answer scene
		Main.getStage().setScene(gameAnswerScene);
		prefixLabel.setText(_currentQuestion.getPrefix().toUpperCase());
	}

	/**
	 * call to answer question, cancels the timer and stops previous speech
	 */
	private void answerQuestion() {
		timer.cancel();
		_timeleft.setStyle("-fx-text-fill:"+GUIValues.textColor+"");
		if (_currentQuestion.answerQuestion(gameAnswerTextBox.getText())) { //if answer correct add value to winnings and announce correct
			gameModule.getWinnings().addToWinnings(_questionValue);
			correctLabel.setText("Correct!");
			ttsManager.speak("correct. "+_questionValue+ " points added to score");
		} else {
			correctLabel.setText("Incorrect!");
			ttsManager.speak("incorrect, The correct answer was " + _currentQuestion.getAnswer().replaceAll("/", " or "));
		}
		if (!gameModule.allAnswered()) { //check questions are all answered if they arent go to post answer screen
			Main.getStage().setScene(postAnswerScene);
		} else {
			scoreLabel.setText("Your final score is: " + gameModule.getWinnings().getWinnings()); //If game finished go to final screen
			Main.getStage().setScene(scoreScene);
			animateTrophy();
		}
		gameAnswerTextBox.clear();
		gameModule.updateScorelabel();
		gameModule.checkForUnlockInternational();
	}

	/**
	 * Animates trophy for final animation
	 */
	private void animateTrophy() {
		ScaleTransition st = new ScaleTransition(Duration.seconds(0.2));
		st.setFromX(0.5);
		st.setFromY(0.5);
		st.setToX(1);
		st.setToY(1);
		st.setNode(image);
		st.playFromStart();
	}

	/**
	 * Sets up timer for answering a question
	 */
	private void buildTimer() {
		timer = new Timer();
		timerTask = new TimerTask() {
			int counter = 30;
			public void run() {
				Platform.runLater(() -> _timeleft.setText("Time left: "+counter));
				counter--;
				if (counter == -1) {
					Platform.runLater(() -> answerQuestion());
				} else if (counter == 10) {
					Platform.runLater(() -> _timeleft.setStyle("-fx-text-fill: red"));
				}
			};
		};
	}

	/**
	 * Sets screen to final score screen
	 */
	public void setSceneFinalScreen() {
		Main.getStage().setScene(scoreScene);
	}

	public void stopSpeaking() {
		ttsManager.stopSpeaking();
	}
}
