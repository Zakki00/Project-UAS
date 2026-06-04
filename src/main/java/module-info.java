module com.mycompany.projectuas {

    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.desktop;
    requires java.prefs;

    requires mysql.connector.j;

    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    requires com.google.api.client;
    requires com.google.oauth.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.drive;

    opens com.mycompany.projectuas to javafx.fxml;
    opens com.mycompany.Model to javafx.fxml, javafx.base;

    exports com.mycompany.projectuas;
}