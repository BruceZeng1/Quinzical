package application;

import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Category;
import utilities.GUIValues;

/**
 * The CategorySelection class creates the GUI for the selection of 5 categories
 * which are required for the game module and manages the necessary logic and objects
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class CategorySelection {
	private Scene selectionScene;
	private ArrayList<Category> _questionBank;
	private ArrayList<Category> _catList;
	private int selected;
	private GameModule _gamemodu;

	CategorySelection(ArrayList<Category> questionBank, GameModule gamemodu){
		_questionBank = questionBank;
		_catList = new ArrayList<Category>();
		selected = 0;
		_gamemodu = gamemodu;
		
		//Initialize header label and continue button and sets correct styling
		Label selectionLabel = new Label("Select 5 Categories");
		selectionLabel.setTextFill(Color.web(GUIValues.textColor));
		Button selectionContinue = new Button("Continue");
		selectionLabel.setFont(new Font("Arial", 34));
		selectionLabel.setAlignment(Pos.BOTTOM_CENTER);
		selectionLabel.setTextAlignment(TextAlignment.CENTER);
		selectionContinue.setDisable(true);
		selectionContinue.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 25; -fx-font-size: 16px");
		selectionContinue.setMinWidth(160);
		selectionContinue.setMinHeight(35);

		
		//sets on action for continue button to load the selected categories 
		//into _boardCatList Changes scene to the game module scene
		
		selectionContinue.setOnAction(e -> {
			ArrayList<Category> _boardCatList = new ArrayList<Category>();
			for (Category cat: _catList) {
				_boardCatList.add(new Category(cat.getName(), cat.Get5Random(), 0));
			}
			Collections.sort(_boardCatList);
			_gamemodu.setBoardCatList(_boardCatList);
			_gamemodu.setScene();
			_gamemodu.buildCatButtons();
		});

		//build selection GUI and apply selection logic
		GridPane catBox = new GridPane();
		for (int i = 0; i < _questionBank.size(); i++) {
			Category category = _questionBank.get(i);
			Button catButton = new Button(category.getName());
			catBox.add(catButton, i%3, i/3);
			//set styling on the category buttons
			catButton.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-background-radius: 30; -fx-font-size: 14px");
			catButton.setMinWidth(130);
			catButton.setMaxWidth(130);
			catButton.setMinHeight(40);
			if (category.numQuestions() < 5) {
				catButton.setDisable(true);;
			}
			catButton.setOnAction(e -> {
				// when button is toggled un-toggle, otherwise toggle.
				// when toggled change color to orange
				if (!_catList.contains(category) && selected < 5) {
					catButton.setStyle("-fx-base: "+GUIValues.alternateButtonColour+"; -fx-background-radius: 30; -fx-font-size: 14px");
					_catList.add(category);
					selected += 1;
				} else if (_catList.contains(category)){
					catButton.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-background-radius: 30; -fx-font-size: 14px");
					_catList.remove(category);
					selected -= 1;
				}
				//once 5 are selected enbable continue button
				if (selected == 5) {
					selectionContinue.setDisable(false);
				} else {
					selectionContinue.setDisable(true);
				}

			});

		}
		catBox.setVgap(10);
		catBox.setHgap(10);
		catBox.setAlignment(Pos.CENTER);

		// Gui for scene
		VBox selectionVBox = new VBox(20);
		selectionVBox.getChildren().addAll(selectionLabel, catBox, selectionContinue);
		selectionVBox.setAlignment(Pos.CENTER);

		BorderPane selectionPane = new BorderPane();
		selectionPane.setStyle(GUIValues.backgroundStyle);
		selectionPane.setPadding(new Insets(20));
		selectionPane.setCenter(selectionVBox);

		selectionScene = new Scene(selectionPane,GUIValues.screenWidth,GUIValues.screenHeight);
		Main.getStage().setScene(selectionScene);
	}
}
