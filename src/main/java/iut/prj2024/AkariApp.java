package iut.prj2024; 


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AkariApp extends Application {

	public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Projet IHM 2024\nAkari App"));
        
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Vous savez comment changer le titre ?");
        primaryStage.show();
        
    }

    public static void main2(String[] args) {
        Application.launch(args);
    }

}