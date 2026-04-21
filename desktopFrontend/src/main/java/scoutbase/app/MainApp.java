package scoutbase.app;

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

    /**
     * Inicia la aplicación JavaFX y muestra la ventana principal.
     *
     * @param stage escenario principal de la aplicación
     */
    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = MainApp.class.getResource("/scoutbase/login-view.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("No se encontró el recurso FXML: /scoutbase/login-view.fxml");
            }

            URL cssUrl = MainApp.class.getResource("/scoutbase/dark_theme.css");
            if (cssUrl == null) {
                throw new RuntimeException("No se encontró el recurso CSS: /scoutbase/dark_theme.css");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("ScoutBase - Login");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(500);
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar la aplicación ScoutBase", e);
        }
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}