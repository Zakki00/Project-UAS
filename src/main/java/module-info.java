module com.mycompany.projectuas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    requires java.prefs;

    opens com.mycompany.projectuas to javafx.fxml;

    exports com.mycompany.projectuas;
}
