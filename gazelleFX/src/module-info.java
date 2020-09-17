module gazelleFX {
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.controls;

    exports gazelle.ui;
    exports gazelle.model;

    opens gazelle.ui to javafx.fxml;
}