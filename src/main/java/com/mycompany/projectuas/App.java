package com.mycompany.projectuas;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.mycompany.services.AutoBackupService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static File logFile;

    // ── Tulis log ke file ──────────────────────────────────────────────

    public static void writeLog(String pesan) {

        try {

            if (logFile == null) {

                String appData = System.getenv("APPDATA");

                logFile = new File(appData + "\\EnjoyCafe\\app.log");

                logFile.getParentFile().mkdirs();

            }

            String waktu = LocalDateTime.now()

                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try (FileWriter fw = new FileWriter(logFile, true)) {

                fw.write("[" + waktu + "] " + pesan + "\n");

            }

        } catch (Exception ignored) {
        }

    }

    @Override
    public void start(Stage stage) throws IOException {
        writeLog("=== Aplikasi dimulai ===");

        try {

            initDatabase();
            writeLog("initDatabase() selesai");

            scene = new Scene(loadFXML("login"), 1080, 900);
            writeLog("loadFXML login selesai");

            stage.setScene(scene);
            stage.show();
            writeLog("stage.show() selesai");

            koneksi.koneksi();
            writeLog("koneksi.koneksi() selesai");

            AutoBackupService.start();
            writeLog("AutoBackupService.start() selesai");

        } catch (Exception e) {

            writeLog("ERROR di start(): " + e.getMessage());

            for (StackTraceElement el : e.getStackTrace()) {

                writeLog("  at " + el.toString());

            }

            throw e; // tetap lempar supaya JavaFX tahu ada error

        }

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {

            launch();
        } catch (Exception e) {

            writeLog("ERROR di main/launch(): " + e.getMessage());

            for (StackTraceElement el : e.getStackTrace()) {

                writeLog("  at " + el.toString());

            }

        }

    }

    private void initDatabase() {
        try {
            String appData = System.getenv("APPDATA");
            File dbFolder = new File(appData + "\\EnjoyCafe\\db");
            if (!dbFolder.exists())
                dbFolder.mkdirs();

            File dbFile = new File(dbFolder, "db_enjoy_cafe.db");
            writeLog("Path DB: " + dbFile.getAbsolutePath());

            writeLog("DB sudah ada: " + dbFile.exists());

            if (!dbFile.exists()) {
                try (InputStream in = getClass().getResourceAsStream(
                        "/db/db_enjoy_cafe.db");
                        OutputStream out = new FileOutputStream(dbFile)) {
                    if (in == null) {

                        writeLog("ERROR: db_enjoy_cafe.db tidak ditemukan di dalam JAR!");

                        return;

                    }

                    in.transferTo(out);
                    writeLog("DB berhasil disalin ke AppData");

                }
            }
        } catch (Exception e) {
            writeLog("ERROR di initDatabase(): " + e.getMessage());

            e.printStackTrace();
        }
    }
}