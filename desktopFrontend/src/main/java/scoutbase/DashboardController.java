package scoutbase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userInfoLabel;

    @FXML
    public void initialize() {

        String username = SessionManager.getUsername();
        String role = SessionManager.getRole();

        welcomeLabel.setText("Bienvenido, " + username);

        String cleanRole = role.replace("ROLE_", "");

        userInfoLabel.setText(
                username + " (" + cleanRole + ")"
        );
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {

        SessionManager.clear();

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("login-view.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Scoutbase - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}