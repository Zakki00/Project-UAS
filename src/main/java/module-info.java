module com.mycompany.projectuas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;

    opens com.mycompany.projectuas to javafx.fxml;

    exports com.mycompany.projectuas;
}
