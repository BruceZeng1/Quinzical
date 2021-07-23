package guiTools;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Question;
import utilities.GUIValues;
import utilities.SpeakBackgroundThread;

/**
 * The TTS manager class creates GUI for changing the speed and replaying tts.
 * It also manages making tts system calls and stopping tts overlap.
 * @author Adam Wiener
 *
 */
public class TTSManager {
	private double _ttsSpeedValue;
	private SpeakBackgroundThread _speakThread;
	private Question _currentQuestion;
	
	/**
	 * 
	 * @param currentQuestion The question which will have its value repeated
	 * @param HBoxAForTTS the HBox where the GUI components will be placed.
	 */
	public TTSManager(HBox HBoxAForTTS) {
		_ttsSpeedValue = 1;
		_currentQuestion = new Question("Error|e|e");
		Label speedLabel = new Label("TTS Speed:");
		speedLabel.setTextFill(Color.web(GUIValues.textColor));
		DecimalFormat df = new DecimalFormat("0.0");
		Button playAgain = new Button("Repeat Question");
		playAgain.setOnAction(e -> {
			stopSpeaking();
			_speakThread = SpeakBackgroundThread.speak(_currentQuestion.getQuestion(), _ttsSpeedValue);
		});
		playAgain.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-background-radius: 30;");
		Slider ttsSpeedSlider = new Slider(0.5, 1.5, 1);
		Label currentSpeed = new Label(Double.toString(ttsSpeedSlider.getValue()));
		currentSpeed.setTextFill(Color.web(GUIValues.textColor));
		ttsSpeedSlider.setMajorTickUnit(0.5);
		ttsSpeedSlider.setShowTickLabels(true);
		ttsSpeedSlider.setShowTickMarks(true);
		ttsSpeedSlider.setOnMouseReleased(e -> {
			_ttsSpeedValue = ttsSpeedSlider.getValue();
		});
		ttsSpeedSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->
		{
			String i = df.format(newvalue.doubleValue());
			currentSpeed.setText(i);
		});
		HBoxAForTTS.getChildren().addAll(speedLabel, currentSpeed, ttsSpeedSlider, playAgain);
	}
	
	/**
	 * Stops the current TTS speaking
	 */
	public void stopSpeaking() {
		if (_speakThread != null) {
			_speakThread.stopSpeaking();
		}
	}
	
	public void askQuestion (String string, Timer timer, TimerTask task) {
		stopSpeaking();
		_speakThread = SpeakBackgroundThread.speakQuestion(string, _ttsSpeedValue, timer, task);
	}
	
	/**
	 * This method will speak the inputed string at the speed governed by the tts bar in GUI
	 * @param string
	 */
	public void speak(String string) {
		stopSpeaking();
		_speakThread = SpeakBackgroundThread.speak(string, _ttsSpeedValue);
	}
	
	/**
	 * set the question for its question to be read question must be set before the replay button
	 * can be used.
	 * @param question
	 */
	public void setQuestion(Question question) {
		_currentQuestion = question;
	}

}
