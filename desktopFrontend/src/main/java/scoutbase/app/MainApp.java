package scoutbase.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Clase principal de arranque de la aplicación ScoutBase.
 *
 * <p>Se encarga de inicializar el entorno JavaFX, cargar la vista
 * de inicio de sesión desde un archivo FXML y aplicar la hoja de estilos
 * correspondiente. También configura las propiedades básicas de la ventana principal.</p>
 *
 * <p>Esta clase actúa como punto de entrada de la aplicación de escritorio.</p>
 */
public class MainApp extends Application {

    /**
     * Método de inicio de JavaFX.
     *
     * <p>Carga los recursos necesarios (FXML y CSS), construye la escena principal
     * y configura el {@link Stage} con sus propiedades básicas como tamaño mínimo,
     * título y comportamiento.</p>
     *
     * @param stage escenario principal proporcionado por JavaFX
     * @throws RuntimeException si no se encuentran los recursos necesarios o ocurre
     *                          un error durante la carga de la interfaz
     */
    @Override
    public void start(Stage stage) {
        try {
            // Carga del archivo FXML de login
            URL fxmlUrl = MainApp.class.getResource("/scoutbase/login-view.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("No se encontró el recurso FXML: /scoutbase/login-view.fxml");
            }

            // Carga de la hoja de estilos
            URL cssUrl = MainApp.class.getResource("/scoutbase/dark_theme.css");
            if (cssUrl == null) {
                throw new RuntimeException("No se encontró el recurso CSS: /scoutbase/dark_theme.css");
            }

            // Inicialización de la escena
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            // Configuración del escenario principal
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
     * Método principal que lanza la aplicación.
     *
     * <p>Invoca el método {@link Application#launch(String...)} de JavaFX,
     * que inicia el ciclo de vida de la aplicación y ejecuta {@link #start(Stage)}.</p>
     *
     * @param args argumentos de línea de comandos (no utilizados directamente)
     */
    public static void main(String[] args) {
        launch(args);
    }
}