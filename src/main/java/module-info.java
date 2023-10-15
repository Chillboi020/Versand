module de.edwin.versand {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.edwin.versand to javafx.fxml;
    exports de.edwin.versand;
}