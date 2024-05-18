package iut.prj2024;


import iut.prj2024.view.GameController;
import iut.prj2024.view.HomePageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AkariApp extends Application {
    private BorderPane root;
    private Stage primaryStage;

    private String dimension;
    private String difficulty;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.root = new BorderPane();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(AkariApp.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Akari");
        primaryStage.setScene(scene);

        loadHomePage();
        //showSaisieMembre(); // affichage temporaire pour validation

        primaryStage.show();
    }

    public void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AkariApp.class.getResource("view/HomePage.fxml"));

            BorderPane homePage = loader.load();
            HomePageController controller = loader.getController();
            controller.setStage(this.primaryStage);
            controller.setAkariApp(this);

            this.root.setCenter(homePage);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadGame(int width, int height, String difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AkariApp.class.getResource("view/Game.fxml"));

            BorderPane game = loader.load();

            Scene scene = new Scene(game);
            scene.getStylesheets().add(AkariApp.class.getResource("style.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Play an Akari game");

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(this.primaryStage);
            dialogStage.setScene(scene);

            System.out.println("Width: " + width + " Height: " + height + " Difficulty: " + difficulty);
            GameController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setAkariApp(this);
            controller.setJeuAraki(width, height);
            controller.setDimension(width, height);
            controller.setDifficulty(difficulty);
            controller.initGame();

            // dialogStage.show();
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setDimension(String dimension) {
        this.dimension = dimension;
    }

    private void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDimension() {
        return dimension;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public static void main2(String[] args) {
        Application.launch(args);
    }

}