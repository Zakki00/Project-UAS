package com.mycompany.projectuas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        initDatabase();
        scene = new Scene(loadFXML("pengaturan"), 1080, 900);
        stage.setScene(scene);
        stage.show();
        koneksi.koneksi();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    private void initDatabase() {
    try {
        String appData = System.getenv("APPDATA");
        File dbFolder = new File(appData + "\\ProjectUAS\\db");
        if (!dbFolder.exists()) dbFolder.mkdirs();

        File dbFile = new File(dbFolder, "db_enjoy_cafe.db");
        if (!dbFile.exists()) {
            // Copy dari dalam JAR ke AppData
            try (InputStream in = getClass().getResourceAsStream("/db/db_enjoy_cafe.db");
                 OutputStream out = new java.io.FileOutputStream(dbFile)) {
                in.transferTo(out);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}