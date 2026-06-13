package com.mycompany.services;

import java.util.prefs.Preferences;
import com.mycompany.projectuas.session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutoBackupService {

    private static Timeline timeline;
    private static final Preferences prefs = Preferences.userNodeForPackage(session.class);

    // ── Mulai service ──────────────────────
    public static void start() {
        if (timeline != null)
            timeline.stop();

        // Cek tiap 30 menit
        timeline = new Timeline(
                new KeyFrame(Duration.minutes(30), e -> cekDanBackup()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        System.out.println("AutoBackupService berjalan");
    }

    // ── Stop service ───────────────────────
    public static void stop() {
        if (timeline != null) {
            timeline.stop();
            System.out.println("AutoBackupService dihentikan");
        }
    }

    // ── Cek dan jalankan backup ────────────
    private static void cekDanBackup() {
        boolean aktif = prefs.getBoolean("backup_otomatis", false);
        if (!aktif) {
            System.out.println("Backup otomatis nonaktif, skip.");
            return;
        }

        Thread t = new Thread(() -> {
            try {
                GoogleDriveService driveService = new GoogleDriveService();
                if (driveService.isBackupExpired(getIntervalJam())) {
                    System.out.println("AutoBackup: menjalankan backup...");
                    boolean berhasil = driveService.uploadBackup();
                    System.out.println("AutoBackup: " + (berhasil ? "berhasil" : "gagal"));
                } else {
                    System.out.println("AutoBackup: belum waktunya backup");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "auto-backup-thread");
        t.setDaemon(true);
        t.start();
    }

    // ── Konversi interval string ke jam ───
    private static long getIntervalJam() {
        String interval = prefs.get("backup_interval", "24 Jam");
        switch (interval) {
            case "3 Hari":
                return 72;
            case "7 Hari":
                return 168;
            default:
                return 24; // "24 Jam"
        }
    }
    //=================================
    // test backup
    //==============================
    // private static long getIntervalJam() {
    //     return 0; // sementara buat test
    // }


    
}