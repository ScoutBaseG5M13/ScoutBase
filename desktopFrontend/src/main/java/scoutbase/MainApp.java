package scoutbase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Punto de entrada principal de la aplicación ScoutBase.
 *
 * <p>Inicializa la interfaz cargando la pantalla de login
 * y configura la ventana principal.</p>
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        URL fxmlUrl = MainApp.class.getResource("/scoutbase/login-view.fxml");

        if (fxmlUrl == null) {
            throw new RuntimeException("No se encontró /scoutbase/login-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        Scene scene = new Scene(loader.load(), 900, 600);

        // 🔥 Cargar tema global (recomendado)
        scene.getStylesheets().add(
                MainApp.class
                        .getResource("/scoutbase/dark_theme.css")
                        .toExternalForm()
        );

        stage.setTitle("Scoutbase - Login");
        stage.setScene(scene);

        // ✔ Login en tamaño cómodo
        stage.setMinWidth(800);
        stage.setMinHeight(500);

        // ✔ Permitir redimensionar
        stage.setResizable(true);

        // ✔ Asegurar que no arranque maximizado
        stage.setMaximized(false);

        // ✔ Centrar en pantalla
        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}