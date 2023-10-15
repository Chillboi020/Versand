module de.edwin.versand {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.edvschuleplattling.ekorn to javafx.fxml;
    exports de.edvschuleplattling.ekorn;
}