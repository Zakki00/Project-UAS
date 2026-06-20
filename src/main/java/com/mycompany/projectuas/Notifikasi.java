package com.mycompany.projectuas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class Notifikasi {

    // ═══════════════════════════════════════════════════════
    // MODEL
    // ═══════════════════════════════════════════════════════
    private record Item(String icon, String judul, String isi, String warna) {
    }

    // ═══════════════════════════════════════════════════════
    // PUBLIC API
    // ═══════════════════════════════════════════════════════

    /** Tampilkan panel notifikasi di bawah tombol lonceng */
    public static void show(Stage owner) {
        List<Item> items = load();
        showPanel(owner, items);
    }

    /** Update badge angka di label lonceng */
    public static void updateBadge(Label badge) {
        if (badge == null)
            return;
        List<Item> items = load();
        if (items.isEmpty()) {
            badge.setText("");
            badge.setVisible(false);
        } else {
            badge.setText(String.valueOf(items.size()));
            badge.setVisible(true);
        }
    }

    // ═══════════════════════════════════════════════════════
    // LOAD DATA
    // ═══════════════════════════════════════════════════════
    private static List<Item> load() {
        List<Item> list = new ArrayList<>();

        // 1. Stok Habis (stok = 0) — kritis
        List<Object[]> habis = koneksi.ambilData(
                "SELECT nama_barang FROM tb_barang WHERE stok = 0 ORDER BY nama_barang");
        if (!habis.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(habis.size(), 3); i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(habis.get(i)[0]);
            }
            if (habis.size() > 3)
                sb.append(" +").append(habis.size() - 3).append(" lainnya");
            list.add(new Item("🔴", "Stok Habis",
                    habis.size() + " produk kehabisan stok: " + sb, "#FF5C7C"));
        }

        // 2. Stok Menipis (stok 1–5)
        List<Object[]> menipis = koneksi.ambilData(
                "SELECT nama_barang, stok FROM tb_barang WHERE stok > 0 AND stok <= 5 ORDER BY stok ASC");
        if (!menipis.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(menipis.size(), 3); i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(menipis.get(i)[0]).append(" (").append(menipis.get(i)[1]).append(")");
            }
            if (menipis.size() > 3)
                sb.append(" +").append(menipis.size() - 3).append(" lainnya");
            list.add(new Item("🟡", "Stok Menipis",
                    menipis.size() + " produk perlu restock: " + sb, "#FFD166"));
        }

        // 3. Piutang akumulatif belum lunas
        List<Object[]> piutang = koneksi.ambilData("""
                    SELECT COUNT(*), COALESCE(SUM(kekurangan), 0)
                    FROM tb_transaksi
                    WHERE status_pembayaran != 'Lunas'
                    AND kekurangan > 0
                """);
        if (!piutang.isEmpty() && piutang.get(0)[0] != null) {
            int jumlah = ((Number) piutang.get(0)[0]).intValue();
            double nominal = ((Number) piutang.get(0)[1]).doubleValue();
            if (jumlah > 0) {
                list.add(new Item("💰", "Piutang Belum Lunas",
                        jumlah + " transaksi · Total Rp " + String.format("%,.0f", nominal),
                        "#FF9F43"));
            }
        }

        // 4. Pergantian Shift
        int jam = java.time.LocalTime.now().getHour();
        if (jam == 6) {
            list.add(new Item("🌤", "Shift Pagi Dimulai",
                    "Selamat bekerja! Shift pagi 06:00 — 12:00", "#00D4FF"));
        } else if (jam == 12) {
            list.add(new Item("🌙", "Shift Siang-Malam Dimulai",
                    "Pergantian shift. Shift malam 12:00 — 21:00", "#6C63FF"));
        }

        return list;
    }

    // ═══════════════════════════════════════════════════════
    // TAMPILAN PANEL
    // ═══════════════════════════════════════════════════════
    private static void showPanel(Stage owner, List<Item> items) {
        Stage panel = new Stage();
        panel.initOwner(owner);
        panel.initStyle(StageStyle.TRANSPARENT);

        // Wrapper luar — beri padding agar shadow tidak terpotong
        VBox wrapper = new VBox();
        wrapper.setPadding(new Insets(6));
        wrapper.setStyle("-fx-background-color: transparent;");

        // Card utama
        VBox root = new VBox(0);
        root.setPrefWidth(300);
        root.setStyle(
                "-fx-background-color: #1A1D2E;" +
                        "-fx-border-color: #2E3250;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 20, 0, 0, 6);");

        // Header
        Label lblHeader = new Label("🔔  Notifikasi");
        lblHeader.setMaxWidth(Double.MAX_VALUE);
        lblHeader.setStyle(
                "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 14 16 14 16;" +
                        "-fx-border-color: #2E3250;" +
                        "-fx-border-width: 0 0 1 0;");
        root.getChildren().add(lblHeader);

        // Isi notifikasi
        if (items.isEmpty()) {
            Label empty = new Label("✅  Semua aman, tidak ada notifikasi.");
            empty.setStyle(
                    "-fx-text-fill: #8B8FA8;" +
                            "-fx-font-size: 11px;" +
                            "-fx-padding: 20 16 20 16;");
            root.getChildren().add(empty);
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);

                Label icon = new Label(item.icon());
                icon.setStyle("-fx-font-size: 18px; -fx-min-width: 28;");

                Label judul = new Label(item.judul());
                judul.setStyle(
                        "-fx-text-fill: " + item.warna() + ";" +
                                "-fx-font-size: 11px;" +
                                "-fx-font-weight: bold;");

                Label isi = new Label(item.isi());
                isi.setStyle("-fx-text-fill: #8B8FA8; -fx-font-size: 10px;");
                isi.setWrapText(true);
                isi.setMaxWidth(230);

                VBox teks = new VBox(3, judul, isi);
                HBox row = new HBox(10, icon, teks);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(12, 16, 12, 16));

                if (i < items.size() - 1) {
                    row.setStyle(
                            "-fx-border-color: #2E3250;" +
                                    "-fx-border-width: 0 0 1 0;");
                }

                root.getChildren().add(row);
            }
        }

        // Footer tutup
        Label tutup = new Label("Tutup");
        tutup.setMaxWidth(Double.MAX_VALUE);
        tutup.setStyle(
                "-fx-text-fill: #6C63FF;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 16 12 16;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #2E3250;" +
                        "-fx-border-width: 1 0 0 0;");
        tutup.setOnMouseClicked(e -> panel.close());
        root.getChildren().add(tutup);

        wrapper.getChildren().add(root);

        Scene scene = new Scene(wrapper);
        scene.setFill(Color.TRANSPARENT);
        panel.setScene(scene);

        // Posisi di bawah tombol lonceng (pojok kanan atas)
        panel.show();
        panel.setX(owner.getX() + owner.getWidth() - 326);
        panel.setY(owner.getY() + 60);

        // Tutup otomatis saat panel kehilangan fokus
        panel.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused)
                panel.close();
        });
    }
}