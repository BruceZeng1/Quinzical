package application;

import java.util.ArrayList;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Category;
import utilities.BashCommand;
import utilities.GUIValues;

/**
 * Class for adding new questions to the existing game categories
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class AddQuestion {

	private Button _backButton;
	private Button _submit;
	private TextField _qField;
	private TextField _aField;
	private TextField _pField;
	private Scene addQuestionScene;
	private ComboBox<Category> _categorySelect;
	private Label _menuLabel;
	private Label _qLabel;
	private Label _aLabel;
	private Label _pLabel;
	private Label _catLabel;
	private ArrayList<Category> _questionBank;
	private String fieldStyle = "-fx-base: "+GUIValues.primaryButtonColour+"; -fx-font-size: 14px; -fx-background-radius: 10;";
	private String fxFontSize = "-fx-font-size: 14px;";

	public AddQuestion(ArrayList<Category> questionBank) {
		_questionBank = questionBank;
		initialiseFields();

		//on submit button press check if all inputs are valid
		//if all valid then add question to file and change scene back to main
		_submit.setOnAction(e -> {
			if (checkInputs()) {
				writeQuestiontoFile();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Question Added");
				alert.getDialogPane().getStylesheets().add(getClass().getResource("popup.css").toExternalForm());
				alert.getDialogPane().applyCss();
				alert.setHeaderText("Success!");
				alert.setContentText("Question was added, please\nrestart game to see new question.");
				alert.showAndWait();
				clearInputs();
				Main.setPrimaryScene();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("popup.css").toExternalForm());
				alert.getDialogPane().applyCss();
				alert.setResizable(true);
				alert.setHeaderText("Error!");
				alert.setContentText("Please select a category and enter\na prefix, question, and answer.\n"
						+ "(Do not use \"|\" in your inputs)");
				
				alert.showAndWait();
			}
		});

		_backButton.setOnAction(e -> {
			clearInputs();
			Main.setPrimaryScene();
		});

		_menuLabel.setTextAlignment(TextAlignment.CENTER);
		_menuLabel.setAlignment(Pos.TOP_CENTER);
		//Gridpane for all labels and textFields
		GridPane addQuestions_grid = new GridPane();
		addQuestions_grid.setVgap(5);
		addQuestions_grid.setPadding(new Insets(15));
		addQuestions_grid.add(_categorySelect, 2, 1);
		addQuestions_grid.add(_catLabel, 1, 1);
		addQuestions_grid.add(_qField, 2, 2);
		addQuestions_grid.add(_aField, 2, 3);
		addQuestions_grid.add(_pField, 2, 4);
		addQuestions_grid.add(_qLabel, 1, 2);
		addQuestions_grid.add(_aLabel, 1, 3);
		addQuestions_grid.add(_pLabel, 1, 4);
		addQuestions_grid.setAlignment(Pos.CENTER);

		// Vbox for combobox and label at top of scene
		VBox addQuestion_VBox = new VBox(20);
		_menuLabel.setFont(new Font("Dekko", 34));
		addQuestion_VBox.getChildren().add(_menuLabel);
		addQuestion_VBox.getChildren().add(addQuestions_grid);
		addQuestion_VBox.getChildren().add(_submit);

		addQuestion_VBox.setAlignment(Pos.CENTER);

		// Buttons at bottom of scene
		HBox addQuestion_HBox = new HBox(5);
		_backButton.setStyle("-fx-base: "+GUIValues.backButtonColour+"");
		addQuestion_HBox.getChildren().add(_backButton);
		addQuestion_HBox.setAlignment(Pos.BASELINE_LEFT);

		//BorderPane for add questions scene
		BorderPane addQBPane = new BorderPane();
		addQBPane.setPadding(new Insets(20));
		addQBPane.setStyle(GUIValues.backgroundStyle);

		addQBPane.setCenter(addQuestion_VBox);
		addQBPane.setBottom(addQuestion_HBox);
		addQuestionScene = new Scene(addQBPane, GUIValues.screenWidth, GUIValues.screenHeight);

	}

	/**
	 * initialize all fields required to make AddQuestion Gui
	 */
	private void initialiseFields() {
		_menuLabel = new Label("Add a new question");
		_menuLabel.setTextFill(Color.web(GUIValues.textColor));
		_catLabel = new Label("Category: ");
		_catLabel.setTextFill(Color.web(GUIValues.textColor));
		_qField = new TextField();
		_aField = new TextField();
		_pField = new TextField();
		_qLabel = new Label("Question: ");
		_aLabel = new Label("Answer: ");
		_pLabel = new Label("Prefix: ");
		_submit = new Button("Add question");
		_submit.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-font-size: 16px; -fx-background-radius: 20;");
		_backButton = new Button("Cancel");
		_categorySelect = new ComboBox<Category>();
		_categorySelect.getItems().addAll(_questionBank);
		_categorySelect.setPromptText("Select a category");
		_categorySelect.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-font-size: 14px; -fx-background-radius: 20;");
		_categorySelect.setPrefWidth(200);
		_pField.setStyle(fieldStyle);
		_aField.setStyle(fieldStyle);
		_qField.setStyle(fieldStyle);
		_catLabel.setStyle(fxFontSize);
		_qLabel.setStyle(fxFontSize);
		_qLabel.setTextFill(Color.web(GUIValues.textColor));
		_aLabel.setStyle(fxFontSize);
		_aLabel.setTextFill(Color.web(GUIValues.textColor));
		_pLabel.setStyle(fxFontSize);
		_pLabel.setTextFill(Color.web(GUIValues.textColor));
		
	}

	/**
	 * checks if inputs in text fields are valid
	 * @return boolean true if all inputs are valid
	 */
	public boolean checkInputs() {
		if (!(_categorySelect.getSelectionModel().getSelectedItem() instanceof Category)) {
			return false;
		} else if (_qField.getText().equals("") || _qField.getText().contains("|")) {
			return false;
		} else if (_aField.getText().equals("")|| _aField.getText().contains("|")) {
			return false;
		} else if (_pField.getText().equals("")|| _pField.getText().contains("|")) {
			return false;
		} else {
			return true;
		}

	}
	
	/**
	 * clear all input text fields and resets combo box
	 */
	public void clearInputs() {
		_qField.clear();
		_aField.clear();
		_pField.clear();
		_categorySelect.getSelectionModel().clearSelection();
		_categorySelect.promptTextProperty();
		_categorySelect.setPromptText("Select a category");
	}
	
	
	/**
	 * takes inputs from text fields and writes to correct category
	 * file in correct format
	 */
	public void writeQuestiontoFile() {
		String output = _qField.getText() +"|" + _pField.getText() + "|" + _aField.getText();
		String filename = _categorySelect.getSelectionModel().getSelectedItem().getName();
		BashCommand command = new BashCommand("echo \""+output+"\" >> \"Questions/"+filename+"\"");
		command.endProcess();

	}

	/**
	 * change scene to AddQuestion Gui
	 */
	public void setScene() {
		Main.getStage().setScene(addQuestionScene);
	}
}
