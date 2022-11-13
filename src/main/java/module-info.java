module hu.petrik.stopperapp {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens hu.petrik.stopperapp to javafx.fxml;
    exports hu.petrik.stopperapp;
}