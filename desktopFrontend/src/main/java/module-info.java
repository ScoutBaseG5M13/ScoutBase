/**
 * Módulo del cliente de escritorio ScoutBase.
 *
 * <p>Define las dependencias necesarias para:
 * <ul>
 *   <li>Interfaz gráfica JavaFX</li>
 *   <li>Comunicación HTTP con el backend</li>
 *   <li>Procesamiento de JSON mediante Jackson</li>
 * </ul>
 */
module scoutbase {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    exports scoutbase.app;
    exports scoutbase.auth;
    exports scoutbase.common;
    exports scoutbase.dashboard;
    exports scoutbase.club;
    exports scoutbase.team;
    exports scoutbase.player;
    exports scoutbase.scout;
    exports scoutbase.user;

    opens scoutbase.app to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.auth to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.common to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.dashboard to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.club to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.team to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.player to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.scout to javafx.fxml, com.fasterxml.jackson.databind;
    opens scoutbase.user to javafx.fxml, com.fasterxml.jackson.databind;
}