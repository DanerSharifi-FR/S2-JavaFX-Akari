package iut.prj2024.view;

import iut.prj2024.AkariApp;
import iut.prj2024.jeu.JeuAraki;
import iut.prj2024.jeu.ReponsePlacement;
import iut.prj2024.jeu.TypeCellule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private Stage stage;
    private AkariApp AkariApp;
    private int width, height;
    private String difficulty;
    private JeuAraki jeuAraki;
    private Image lighBulbImage;
    private HashMap<Integer, ArrayList<Button>> buttons = new HashMap<>();
    private int[] time;

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
    private Label errorLabel;

    @FXML
    public void resetButtonAction() {
        this.stage.close();
        Platform.runLater(() -> this.AkariApp.loadGame(this.width, this.height, this.difficulty));
    }

    @FXML
    public void newGameButtonAction() {
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAkariApp(AkariApp akariApp) {
        this.AkariApp = akariApp;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setJeuAraki(int width, int height) {
        this.jeuAraki = new JeuAraki(width, height);
    }

    /**
     * Initializes the controller class.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lighBulbImage = new Image(AkariApp.class.getResource("images/bulb.png").toExternalForm());
    }

    public void initGame() {
        this.dimensionLabel.setText(this.width + "x" + this.height);
        this.difficultyLabel.setText(this.difficulty);
        this.scoreLabel.setText("0");
        this.timeLabel.setText("00:00");
        this.setStopWatch();
        this.errorLabel.setPrefWidth(Integer.MAX_VALUE);

        System.out.println("Width: " + this.width + " Height: " + this.height + " Difficulty: " + this.difficulty);
        String fileName = this.difficulty + ".txt";
        if (this.width == 6 && this.height == 4) {
            int randomEasy = (int) (Math.random() * 3) + 1;
            fileName = "easy" + randomEasy + ".txt";
        }
        String pathToFile = "jeu/dataset/" + this.width + "x" + this.height + "/" + fileName;

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setFillHeight(true);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);

        this.gridPane.getColumnConstraints().set(0, columnConstraints);
        this.gridPane.getRowConstraints().set(0, rowConstraints);

        this.jeuAraki.chargerGrilleFromStream(AkariApp.class.getResourceAsStream(pathToFile));

        this.setGameGrid(false);
    }

    private void setStopWatch() {
        this.time = new int[]{0, 0};
        Thread thread = new Thread(() -> {
            while (!this.jeuAraki.verifierVictoire()) {
                try {
                    Thread.sleep(1000);
                    time[1]++;
                    if (time[1] == 60) {
                        time[0]++;
                        time[1] = 0;
                    }
                    String minutes = time[0] < 10 ? "0" + time[0] : String.valueOf(time[0]);
                    String seconds = time[1] < 10 ? "0" + time[1] : String.valueOf(time[1]);
                    Platform.runLater(() -> this.timeLabel.setText(minutes + ":" + seconds));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void setBulbButtonAction(Button button, int i, int j) {
        TypeCellule type = this.jeuAraki.getCellule(i, j).getType();
        ReponsePlacement response = this.jeuAraki.placerAmpoule(i, j);

        switch (type) {
            case VIDE:
                this.addAndRemoveClasses(button, new String[]{"lumiere-btn", "amp-btn"}, new String[]{"vide-btn"});
                break;

            case AMPOULE:
                this.removeBulb(button, i, j);
                break;
        }
        System.out.println("Erreur type : " + response);
        switch (response) {
            case AJOUTE_AMPOULE:
                this.addBulb(button);
                this.errorLabel.setText("");
                this.addAndRemoveClassesLabel(this.errorLabel, new String[]{"alert", "alert-danger"}, new String[]{""});
                break;

            case NON_CONFORME_NOMBRE:
                this.errorLabel.setText("Erreur : Nombre d'ampoules incorrect");
                this.addAndRemoveClassesLabel(this.errorLabel, new String[]{""}, new String[]{"alert", "alert-danger"});
                return;

            case SUR_MUR:
                this.errorLabel.setText("Erreur : Vous ne pouvez pas placer d'ampoule sur un mur");
                this.addAndRemoveClassesLabel(this.errorLabel, new String[]{""}, new String[]{"alert", "alert-danger"});
                return;

            case DEJA_ECLAIREE:
                this.errorLabel.setText("Erreur : Vous ne pouvez pas placer d'ampoule sur une case déjà éclairée");
                this.addAndRemoveClassesLabel(this.errorLabel, new String[]{""}, new String[]{"alert", "alert-danger"});
                return;

            default:
                System.out.println("Erreur lors du placement : " + response);
                this.errorLabel.setText("");
                this.addAndRemoveClassesLabel(this.errorLabel, new String[]{"alert", "alert-danger"}, new String[]{""});
                break;
        }

        int score = Integer.parseInt(this.scoreLabel.getText()) + 1;
        this.scoreLabel.setText(String.valueOf(score));
        this.setGameGrid(true);
    }

    private void addBulb(Button button) {
        this.addAndRemoveClasses(button, new String[]{"vide-btn", "lumiere-btn"}, new String[]{"amp-btn"});
        ImageView lightBulb = new ImageView(this.lighBulbImage);
        lightBulb.setFitHeight(this.imageDimensions());
        lightBulb.setFitWidth(this.imageDimensions());
        button.setGraphic(lightBulb);
    }

    private int imageDimensions() {
        return switch (this.width) {
            case 3 -> 96;
            case 5, 6 -> 48;
            case 7 -> 32;
            case 10 -> 24;
            default -> 10;
        };
    }

    private int wallFontSize() {
        return switch (this.width) {
            case 3 -> 64;
            case 5 -> 28;
            case 6 -> 40;
            case 7 -> 18;
            case 10 -> 24;
            default -> 16;
        };
    }

    private void winGame() {
        System.out.println("Félicitations ! Vous avez gagné !");
        Alert alert = getAlert();
        ButtonType buttonTypeOne = new ButtonType("YES");
        ButtonType buttonTypeTwo = new ButtonType("NO");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        alert.showAndWait().ifPresent(typeButton -> {
            if (typeButton == buttonTypeOne) this.resetButtonAction();
            else this.stage.close();
        });
    }

    private Alert getAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText("You have won this game!");
        String contentText = "You have completed the game in ";
        if (time[0] > 0) contentText = contentText + time[0] + " minutes and ";
        contentText = contentText + time[1] + " second";
        if (time[1] > 1) contentText = contentText + "s";
        contentText = contentText + ".\nWith a score of " + this.scoreLabel.getText() + ".";
        contentText = contentText + "\nDo you want to play again?";
        alert.setContentText(contentText);
        return alert;
    }

    private void removeBulb(Button button, int i, int j) {
        this.addAndRemoveClasses(button, new String[]{"amp-btn", "lumiere-btn"}, new String[]{"vide-btn"});
        this.jeuAraki.getCellule(i, j).setType(TypeCellule.VIDE);
        button.setGraphic(null);
    }

    private void setGameGrid(boolean refresh) {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Button button = new Button();
                if (refresh) {
                    button = this.buttons.get(i).get(j);
                }
                TypeCellule type = this.jeuAraki.getCellule(i, j).getType();
                button.setText(" ");

                final int finalI = i;
                final int finalJ = j;
                Button finalButton = button;
                switch (type) {
                    case MUR:
                        button.getStyleClass().add("mur-btn");
                        button.setStyle("-fx-font-size: " + this.wallFontSize() + "px;");
                        if (this.jeuAraki.getCellule(i, j).getNombreAmpoulesNecessaires() >= 0) {
                            button.setText(String.valueOf(this.jeuAraki.getCellule(i, j).getNombreAmpoulesNecessaires()));
                        }
                        break;
                    case ILLUMINEE:
                        this.addAndRemoveClasses(button, new String[]{"amp-btn", "vide-btn"}, new String[]{"lumiere-btn"});
                        break;
                    case VIDE:
                        this.addAndRemoveClasses(button, new String[]{"lumiere-btn", "amp-btn"}, new String[]{"vide-btn"});
                        break;
                    case AMPOULE:
                        this.addAndRemoveClasses(button, new String[]{"lumiere-btn", "vide-btn"}, new String[]{"amp-btn"});
                        break;
                }
                button.setOnAction(event -> setBulbButtonAction(finalButton, finalI, finalJ));
                if (!refresh) {

                    button.setPrefWidth(Integer.MAX_VALUE);
                    button.setPrefHeight(Integer.MAX_VALUE);
                    this.gridPane.add(button, i, j);

                    if (this.buttons.containsKey(i)) this.buttons.get(i).add(button);
                    else {
                        ArrayList<Button> buttonList = new ArrayList<>();
                        buttonList.add(button);
                        this.buttons.put(i, buttonList);
                    }
                }
            }
        }
        if (this.jeuAraki.verifierVictoire()) this.winGame();
    }

    private void addAndRemoveClasses(Button button, String[] classesToRemove, String[] classesToAdd) {
        for (String classToRemove : classesToRemove) button.getStyleClass().remove(classToRemove);
        for (String classToAdd : classesToAdd) {
            if (!button.getStyleClass().contains(classToAdd)) {
                button.getStyleClass().add(classToAdd);
            }
        }
    }

    private void addAndRemoveClassesLabel(Label label, String[] classesToRemove, String[] classesToAdd) {
        for (String classToRemove : classesToRemove) label.getStyleClass().remove(classToRemove);
        for (String classToAdd : classesToAdd) {
            if (!label.getStyleClass().contains(classToAdd)) {
                label.getStyleClass().add(classToAdd);
            }
        }
    }
}
