module gazelleFX {
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.controls;

    exports gazelle.ui;

    opens gazelle.ui to javafx.fxml;
}