package scoutbase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = MainApp.class.getResource("/scoutbase/login-view.fxml");

        if (fxmlUrl == null) {
            throw new RuntimeException("No se encontró /scoutbase/login-view.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Scene scene = new Scene(fxmlLoader.load(), 420, 320);
        stage.setTitle("Scoutbase - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}