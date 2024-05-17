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
import javafx.scene.input.MouseButton;
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

    /**
     * Game initialization
     * <BR>
     * First we set the labels with the width, height and difficulty of the game
     * Then we set the stopwatch
     * We load the game grid from the dataset
     * Finally we set the game grid
     */
    public void initGame() {
        // -------------------------------------- //
        // Set the labels with the width, height and difficulty of the game
        // -------------------------------------- //
        this.dimensionLabel.setText(this.width + "x" + this.height);
        this.difficultyLabel.setText(this.difficulty);
        this.scoreLabel.setText("0");
        this.timeLabel.setText("00:00");
        this.setStopWatch();
        this.errorLabel.setPrefWidth(Integer.MAX_VALUE);


        System.out.println("Width: " + this.width + " Height: " + this.height + " Difficulty: " + this.difficulty);

        // -------------------------------------- //
        // Generate the path to the dataset file
        // -------------------------------------- //
        String fileName = this.difficulty + ".txt";

        // If the game is easy and the dimensions are 6x4, we choose a random easy level
        if (this.width == 6 && this.height == 4) {
            int randomEasy = (int) (Math.random() * 3) + 1;
            fileName = "easy" + randomEasy + ".txt";
        }
        String pathToFile = "jeu/dataset/" + this.width + "x" + this.height + "/" + fileName;

        // -------------------------------------- //
        // Define the grid row and column constraints
        // -------------------------------------- //
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setFillHeight(true);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);

        this.gridPane.getColumnConstraints().set(0, columnConstraints);
        this.gridPane.getRowConstraints().set(0, rowConstraints);

        // -------------------------------------- //
        // Load the game grid from the dataset
        // -------------------------------------- //
        this.jeuAraki.chargerGrilleFromStream(AkariApp.class.getResourceAsStream(pathToFile));

        // -------------------------------------- //
        // Set the game grid with refresh = false to initialize the game
        // -------------------------------------- //
        this.setGameGrid(false);
    }

    /**
     * Set the stopwatch
     * <BR>
     * The stopwatch is a thread that will run in the background and update the time label every second
     */
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

    /**
     * Set the action of the bulb button
     *
     * @param button the button to set the action on
     * @param i the x position of the button
     * @param j the y position of the button
     */
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

    /**
     * Add a bulb to a button
     *
     * @param button the button to add the bulb to
     */
    private void addBulb(Button button) {
        this.addAndRemoveClasses(button, new String[]{"vide-btn", "lumiere-btn"}, new String[]{"amp-btn"});
        ImageView lightBulb = new ImageView(this.lighBulbImage);
        lightBulb.setFitHeight(this.imageDimensions());
        lightBulb.setFitWidth(this.imageDimensions());
        button.setGraphic(lightBulb);
    }

    /**
     * Get the dimensions of the image based on the width of the game
     *
     * @return the dimensions of the image
     */
    private int imageDimensions() {
        return switch (this.width) {
            case 3 -> 96;
            case 5, 6 -> 48;
            case 7 -> 32;
            case 10 -> 24;
            default -> 10;
        };
    }

    /**
     * Get the font size of the wall based on the width of the game
     *
     * @return the font size of the wall
     */
    private int wallFontSize() {
        return switch (this.width) {
            case 3 -> 64;
            case 5 -> 28;
            case 6 -> 40;
            case 7, 10 -> 18;
            default -> 16;
        };
    }

    /**
     * Shows the victory message and asks the user if they want to play again
     */
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

    /**
     * Get the victory alert
     * @return the victory alert
     */
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

    /**
     * Remove a bulb from a button
     *
     * @param button the button to remove the bulb from
     * @param i the x position of the button
     * @param j the y position of the button
     */
    private void removeBulb(Button button, int i, int j) {
        this.addAndRemoveClasses(button, new String[]{"amp-btn", "lumiere-btn"}, new String[]{"vide-btn"});
        this.jeuAraki.getCellule(i, j).setType(TypeCellule.VIDE);
        button.setGraphic(null);
    }

    /**
     * Set the game grid
     *
     * @param refresh if the grid should be refreshed, this is used when the game is initialized
     *                and user asks to play again or to reset the game
     */
    private void setGameGrid(boolean refresh) {
        // -------------------------------------- //
        // Loop through the grid and set the buttons
        // -------------------------------------- //
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {

                // Create a new button if the grid is not being refreshed
                Button button = new Button();

                // Get the button from the buttons list if the grid is being refreshed
                if (refresh) button = this.buttons.get(i).get(j);

                // Get the type of the cell : MUR, ILLUMINEE, VIDE, AMPOULE
                TypeCellule type = this.jeuAraki.getCellule(i, j).getType();
                button.setText(" ");

                // Set final variables for the lambda expression
                final int finalI = i;
                final int finalJ = j;
                Button finalButton = button;

                // Set the button style based on the type of the cell
                switch (type) {
                    // If the cell is a wall, set the button style to mur-btn
                    case MUR:
                        button.getStyleClass().add("mur-btn");
                        button.setStyle("-fx-font-size: " + this.wallFontSize() + "px;");
                        if (this.jeuAraki.getCellule(i, j).getNombreAmpoulesNecessaires() >= 0) {
                            button.setText(String.valueOf(this.jeuAraki.getCellule(i, j).getNombreAmpoulesNecessaires()));
                        }
                        break;

                    // If the cell is illuminated, set the button style to lumiere-btn
                    case ILLUMINEE:
                        this.addAndRemoveClasses(button, new String[]{"amp-btn", "vide-btn"}, new String[]{"lumiere-btn"});
                        break;

                    // If the cell is empty, set the button style to vide-btn
                    case VIDE:
                        this.addAndRemoveClasses(button, new String[]{"lumiere-btn", "amp-btn"}, new String[]{"vide-btn"});
                        break;

                    // If the cell is a bulb, set the button style to amp-btn
                    case AMPOULE:
                        this.addAndRemoveClasses(button, new String[]{"lumiere-btn", "vide-btn"}, new String[]{"amp-btn"});
                        break;
                }

                // Set the action of the button to the setBulbButtonAction
                button.setOnAction(event -> setBulbButtonAction(finalButton, finalI, finalJ));

                // Add the button to the grid if the grid is not being refreshed
                if (!refresh) {

                    // Set the button dimensions to fill the grid cell
                    button.setPrefWidth(Integer.MAX_VALUE);
                    button.setPrefHeight(Integer.MAX_VALUE);

                    // Add the button to the grid
                    this.gridPane.add(button, i, j);

                    // Add the button to the buttons list if the grid is not being refreshed
                    if (this.buttons.containsKey(i)) this.buttons.get(i).add(button);
                    // If the button list does not contain the key, create a new list and add the button
                    else {
                        ArrayList<Button> buttonList = new ArrayList<>();
                        buttonList.add(button);
                        this.buttons.put(i, buttonList);
                    }
                }
            }
        }

        // Check if the game is won
        if (this.jeuAraki.verifierVictoire()) this.winGame();
    }

    /**
     * Add and remove classes from a button
     *
     * @param button the button to add and remove classes from
     * @param classesToRemove the classes to remove
     * @param classesToAdd the classes to add
     */
    private void addAndRemoveClasses(Button button, String[] classesToRemove, String[] classesToAdd) {
        for (String classToRemove : classesToRemove) button.getStyleClass().remove(classToRemove);
        for (String classToAdd : classesToAdd) {
            if (!button.getStyleClass().contains(classToAdd)) {
                button.getStyleClass().add(classToAdd);
            }
        }
    }

    /**
     * Add and remove classes from a label
     *
     * @param label the label to add and remove classes from
     * @param classesToRemove the classes to remove
     * @param classesToAdd the classes to add
     */
    private void addAndRemoveClassesLabel(Label label, String[] classesToRemove, String[] classesToAdd) {
        for (String classToRemove : classesToRemove) label.getStyleClass().remove(classToRemove);
        for (String classToAdd : classesToAdd) {
            if (!label.getStyleClass().contains(classToAdd)) {
                label.getStyleClass().add(classToAdd);
            }
        }
    }
}
