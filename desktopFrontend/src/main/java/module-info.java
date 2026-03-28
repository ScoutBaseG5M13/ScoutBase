/**
 * Módulo del cliente de escritorio ScoutBase.
 * Define las dependencias necesarias para:
 * - Interfaz gráfica JavaFX
 * - Comunicación HTTP con el backend
 * - Procesamiento de JSON mediante Jackson
 */
module scoutbase {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens scoutbase to javafx.fxml, com.fasterxml.jackson.databind;

    exports scoutbase;
}