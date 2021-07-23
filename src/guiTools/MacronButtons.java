package guiTools;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utilities.GUIValues;

/**
 * Class for building and controlling use the use macron buttons.
 * @author Adam Wiener
 *
 */
public class MacronButtons {
	HBox buttonHBox;
	
	/**
	 * 
	 * @param textField the text field to which the buttons will append
	 */
	public MacronButtons(TextField textField) {
		buttonHBox = new HBox(5);
		getButtonHBox().setAlignment(Pos.CENTER);
		String[] macrons = {"ā", "ē", "ī", "ō", "ū"};
		for (String macron: macrons) {
			Button button = new Button(macron);
			button.setStyle("-fx-base: "+GUIValues.primaryButtonColour+"; -fx-background-radius: 10; -fx-font-size: 14px;");
			button.setMinWidth(40);
			getButtonHBox().getChildren().add(button);
			button.setOnAction(e -> {
				textField.appendText(macron);
			});
		}
	}
	
	/**
	 * 
	 * @return the HBox containing the macron buttons
	 */
	public HBox getHbox() {
		return getButtonHBox();
	}

	public HBox getButtonHBox() {
		return buttonHBox;
	}
}
