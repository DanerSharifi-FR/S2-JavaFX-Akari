package iut.prj2024.view;

import iut.prj2024.AkariApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    private Stage stage;

    @FXML
    private ToggleGroup dimensions;

    @FXML
    private ToggleGroup difficulty;
    @FXML
    private RadioButton easyRadioButton;
    @FXML
    private RadioButton hardRadioButton;
    @FXML
    private RadioButton trickyRadioButton;

    private AkariApp akariApp;

    public void setAkariApp(AkariApp akariApp) {
        this.akariApp = akariApp;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void startButtonAction() {
        if (getDifficulty().isEmpty()) {
            System.out.println("No dimension selected");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a valid difficulty");
            alert.showAndWait();
        } else {
            String[] dimensions = getDimension().split("x");
            int dimensionWidth = Integer.parseInt(dimensions[0]);
            int dimensionHeight = Integer.parseInt(dimensions[1]);
            String difficulty = getDifficulty().toLowerCase();

            if (this.akariApp != null) {
                this.akariApp.loadGame(dimensionWidth, dimensionHeight, difficulty);
            }
        }
    }

    @FXML
    public void checkDifficultyAvailable() {
        if (dimensions.getSelectedToggle() != null) {
            String dimension = getDimension();

            String[] levels = availableLevels(dimension);
            System.out.println(Arrays.toString(levels));
            this.easyRadioButton.setDisable(levels[0] == null);
            if (this.easyRadioButton.isSelected() && levels[0] == null) {
                this.easyRadioButton.setSelected(false);
            }
            this.hardRadioButton.setDisable(levels[1] == null);
            if (this.hardRadioButton.isSelected() && levels[1] == null) {
                this.hardRadioButton.setSelected(false);
            }
            this.trickyRadioButton.setDisable(levels[2] == null);
            if (this.trickyRadioButton.isSelected() && levels[2] == null) {
                this.trickyRadioButton.setSelected(false);
            }
        }
    }

    private String[] availableLevels(String dimension) {
        return switch (dimension) {
            case "3x3", "6x4" -> new String[]{"easy", null, null};
            case "5x5", "7x7" -> new String[]{"easy", "hard", "tricky"};
            case "10x10" -> new String[]{"easy", "hard", null};
            case "14x14" -> new String[]{"easy", null, "tricky"};
            default -> new String[]{null, null, null};
        };
    }

    private String getDimension() {
        return dimensions.getSelectedToggle().toString().substring(dimensions.getSelectedToggle().toString().indexOf("'") + 1, dimensions.getSelectedToggle().toString().lastIndexOf("'"));
    }

    private String getDifficulty() {
        if (difficulty.getSelectedToggle() == null) return "";
        return difficulty.getSelectedToggle().toString().substring(difficulty.getSelectedToggle().toString().indexOf("'") + 1, difficulty.getSelectedToggle().toString().lastIndexOf("'"));
    }

    @FXML
    public void quitButtonAction() {
        stage.close();
    }

    /**
     * Initializes the controller class.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
