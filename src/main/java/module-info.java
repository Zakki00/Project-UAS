module com.mycompany.projectuas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    requires java.prefs;
    requires mysql.connector.j;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.mycompany.projectuas to javafx.fxml;

    exports com.mycompany.projectuas;
}
