package iut.prj2024.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    private Stage stage;

    @FXML
    private RadioButton dimension3x3RadioButton;
    @FXML
    private RadioButton dimension5x5RadioButton;
    @FXML
    private RadioButton dimension6x4RadioButton;
    @FXML
    private RadioButton dimension7x7RadioButton;
    @FXML
    private RadioButton dimension10x10RadioButton;
    @FXML
    private RadioButton dimension14x14RadioButton;

    @FXML
    private RadioButton easyRadioButton;
    @FXML
    private RadioButton hardRadioButton;
    @FXML
    private RadioButton trickyRadioButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void startButtonAction() {
        if (dimension3x3RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 3, 3, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 3, 3, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 3, 3, 3);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else if (dimension5x5RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 5, 5, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 5, 5, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 5, 5, 3);
            } else {
                //new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else if (dimension6x4RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 6, 4, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 6, 4, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 6, 4, 3);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else if (dimension7x7RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 7, 7, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 7, 7, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 7, 7, 3);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else if (dimension10x10RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 10, 10, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 10, 10, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 10, 10, 3);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else if (dimension14x14RadioButton.isSelected()) {
            if (easyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 14, 14, 1);
            } else if (hardRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 14, 14, 2);
            } else if (trickyRadioButton.isSelected()) {
                //new GamePageController().startGame(stage, 14, 14, 3);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please select a difficulty level").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a dimension").show();
        }
    }

    @FXML
    public void quitButtonAction() {
        stage.close();
    }

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
