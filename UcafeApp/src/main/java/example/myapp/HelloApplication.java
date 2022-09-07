package example.myapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("I count and calculate your KaSHA");
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
