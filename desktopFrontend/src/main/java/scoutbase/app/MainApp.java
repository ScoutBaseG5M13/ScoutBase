package scoutbase.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Clase principal de arranque de la aplicación ScoutBase Desktop.
 *
 * <p>Actúa como punto de entrada de la aplicación JavaFX y se encarga
 * de inicializar la interfaz gráfica principal.</p>
 *
 * <p>Sus responsabilidades principales son:</p>
 * <ul>
 *     <li>Cargar la vista inicial de login desde FXML.</li>
 *     <li>Aplicar la hoja de estilos global de la aplicación.</li>
 *     <li>Configurar la ventana principal de JavaFX.</li>
 *     <li>Iniciar el ciclo de vida gráfico de la aplicación.</li>
 * </ul>
 */
public class MainApp extends Application {

    /**
     * Ruta del archivo FXML correspondiente a la pantalla de login.
     */
    private static final String LOGIN_VIEW = "/scoutbase/login-view.fxml";

    /**
     * Ruta de la hoja de estilos principal de la aplicación.
     */
    private static final String DEFAULT_THEME = "/scoutbase/dark_theme.css";

    /**
     * Anchura inicial de la ventana principal.
     */
    private static final double INITIAL_WIDTH = 900;

    /**
     * Altura inicial de la ventana principal.
     */
    private static final double INITIAL_HEIGHT = 600;

    /**
     * Anchura mínima permitida para la ventana.
     */
    private static final double MIN_WIDTH = 800;

    /**
     * Altura mínima permitida para la ventana.
     */
    private static final double MIN_HEIGHT = 500;

    /**
     * Método principal de inicialización de JavaFX.
     *
     * <p>Este método es invocado automáticamente por el framework JavaFX
     * tras ejecutar {@link #main(String[])}.</p>
     *
     * <p>Se encarga de:</p>
     * <ul>
     *     <li>Validar y cargar los recursos FXML y CSS.</li>
     *     <li>Construir la escena principal.</li>
     *     <li>Configurar el {@link Stage} principal.</li>
     *     <li>Mostrar la ventana de login.</li>
     * </ul>
     *
     * @param stage escenario principal proporcionado por JavaFX
     * @throws RuntimeException si no se pueden cargar los recursos necesarios
     */
    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = resolveResource(LOGIN_VIEW);
            URL cssUrl = resolveResource(DEFAULT_THEME);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);

            Scene scene = new Scene(
                    loader.load(),
                    INITIAL_WIDTH,
                    INITIAL_HEIGHT
            );

            scene.getStylesheets().add(cssUrl.toExternalForm());

            configureStage(stage, scene);

            stage.show();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al iniciar la aplicación ScoutBase Desktop",
                    e
            );
        }
    }

    /**
     * Configura las propiedades principales de la ventana de la aplicación.
     *
     * @param stage escenario principal
     * @param scene escena principal ya inicializada
     */
    private void configureStage(Stage stage, Scene scene) {
        stage.setTitle("ScoutBase - Login");
        stage.setScene(scene);

        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        stage.setResizable(true);
        stage.setMaximized(false);

        stage.centerOnScreen();
    }

    /**
     * Resuelve y valida la existencia de un recurso dentro del classpath.
     *
     * <p>Este método se utiliza para cargar recursos como archivos FXML,
     * hojas de estilo CSS o cualquier otro recurso empaquetado dentro
     * de {@code src/main/resources}.</p>
     *
     * @param resourcePath ruta absoluta del recurso
     * @return URL del recurso encontrado
     * @throws RuntimeException si el recurso no existe
     */
    private URL resolveResource(String resourcePath) {
        URL resourceUrl = MainApp.class.getResource(resourcePath);

        if (resourceUrl == null) {
            throw new RuntimeException(
                    "No se encontró el recurso: " + resourcePath
            );
        }

        return resourceUrl;
    }

    /**
     * Método principal de arranque de la aplicación.
     *
     * <p>Delegado estándar de JavaFX que inicia el ciclo de vida gráfico
     * ejecutando internamente el método {@link #start(Stage)}.</p>
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}