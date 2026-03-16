module scoutbase {
    requires javafx.controls;
    requires javafx.fxml;

    opens scoutbase to javafx.fxml;
    exports scoutbase;
}