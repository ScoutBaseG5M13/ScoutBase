package scoutbase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    @FXML
    public void initialize() {
        lblWelcome.setText("Bienvenido a Scoutbase");
    }

    public void onLoginClick(ActionEvent actionEvent) {
    }
}