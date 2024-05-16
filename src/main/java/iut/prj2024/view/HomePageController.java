package iut.prj2024.view;

import iut.prj2024.AkariApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    private Stage stage;

    @FXML
    private ToggleGroup dimensions;
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
    private ToggleGroup difficulty;
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

        } else if (dimension5x5RadioButton.isSelected()) {

        } else if (dimension6x4RadioButton.isSelected()) {

        } else if (dimension7x7RadioButton.isSelected()) {

        } else if (dimension10x10RadioButton.isSelected()) {

        } else if (dimension14x14RadioButton.isSelected()) {

        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a dimension").show();
        }
    }

    @FXML
    public void checkDifficultyAvailable() {
        if (dimensions.getSelectedToggle() != null) {
            String dimension = dimensions.getSelectedToggle().toString();
            dimension = dimension.substring(dimension.indexOf("'") + 1);
            dimension = dimension.substring(0, dimension.indexOf("'"));

            String[] levels = availableLevels(dimension, false);
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


    private String[] availableLevels(String dimension, boolean showAllFiles) {
        File folder = new File(pathToDimension(dimension));
        File[] files = folder.listFiles((dir, name) -> name.startsWith("easy") || name.startsWith("hard") || name.startsWith("tricky"));
        if (files == null) {
            System.out.println("No levels found");
            System.out.println(folder.getAbsolutePath());
            return new String[0];
        }
        String[] levels = new String[files.length];
        if (showAllFiles) {
            for (int i = 0; i < files.length; i++) {
                levels[i] = files[i].getName();
            }
        } else {
            levels = new String[3];
            if (Arrays.stream(files).anyMatch(file -> file.getName().startsWith("easy"))) {
                levels[0] = "easy";
            }
            if (Arrays.stream(files).anyMatch(file -> file.getName().startsWith("hard"))) {
                levels[1] = "hard";
            }
            if (Arrays.stream(files).anyMatch(file -> file.getName().startsWith("tricky"))) {
                levels[2] = "tricky";
            }
        }
        return levels;
    }

    private String pathToDimension(String dimension) {
        return "src/main/resources/iut/prj2024/jeu/dataset/" + dimension;
    }

    private String pathToLevel(String dimension, String difficulty) {
        return pathToDimension(dimension) + "/" + difficulty;
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
