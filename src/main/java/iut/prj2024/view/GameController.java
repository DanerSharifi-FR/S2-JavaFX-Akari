package iut.prj2024.view;

import iut.prj2024.AkariApp;
import iut.prj2024.jeu.JeuAraki;
import iut.prj2024.jeu.TypeCellule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private int width, height;
    private String difficulty;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label scoreLabel;
    @FXML
    private Label dimensionLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button resetButton;
    @FXML
    private Button newGameButton;

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

    public void initGame() {
        String fileName = this.chooseRandomIfManyFiles(this.width + "x" + this.height, this.difficulty);
        String pathToFile = "jeu/dataset/" + this.width + "x" + this.height + "/" + fileName;

        RowConstraints rc = new RowConstraints();
        rc.setFillHeight(true);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);

        this.gridPane.getColumnConstraints().set(0, cc);
        this.gridPane.getRowConstraints().set(0, rc);

        JeuAraki jeu = new JeuAraki(this.width, this.height);

        jeu.chargerGrilleFromStream(AkariApp.class.getResourceAsStream(pathToFile));

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Button button = new Button();
                String type = jeu.getCellule(i, j).getType().name();
                button.setText(" ");

                switch (type) {
                    case "MUR":
                        button.getStyleClass().add("mur-btn");
                        if (jeu.getCellule(i, j).getNombreAmpoulesNecessaires() > 0) {
                            button.setText(String.valueOf(jeu.getCellule(i, j).getNombreAmpoulesNecessaires()));
                        }
                        break;
                    case "LUMIERE":
                        button.getStyleClass().add("lumiere-btn");
                        break;
                    case "VIDE":
                        button.getStyleClass().add("vide-btn");
                        break;
                    case "AMP":
                        button.getStyleClass().add("amps-btn");
                        break;
                }

                button.setPrefWidth(1000);
                button.setPrefHeight(1000);
                this.gridPane.add(button, i, j);
            }
        }
    }

    private String chooseRandomIfManyFiles(String dimension, String level) {
        File folder = new File((new HomePageController()).pathToDimension(dimension));
        File[] files = folder.listFiles((dir, name) -> name.startsWith(level));
        if (files == null) {
            return "";
        }
        int random = (int) (Math.random() * files.length);
        return files[random].getName();
    }
}
