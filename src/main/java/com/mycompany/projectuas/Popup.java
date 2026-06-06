package com.mycompany.projectuas;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Popup {

        public enum PopupType {
                SUCCESS, ERROR, WARNING, INFO
        }

        public void showModernPopup(String title, String message, PopupType type) {
                Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                popupStage.setAlwaysOnTop(true);

                // Warna berdasarkan tipe
                String bgColor, iconText, borderColor;
                switch (type) {
                        case SUCCESS:
                                bgColor = "#1a1a2e";
                                iconText = "✓";
                                borderColor = "#00d084";
                                break;
                        case ERROR:
                                bgColor = "#1a1a2e";
                                iconText = "✕";
                                borderColor = "#ff4d6d";
                                break;
                        case WARNING:
                                bgColor = "#1a1a2e";
                                iconText = "⚠";
                                borderColor = "#ffd60a";
                                break;
                        default:
                                bgColor = "#1a1a2e";
                                iconText = "ℹ";
                                borderColor = "#4cc9f0";
                }

                // ── Icon circle ──
                Label icon = new Label(iconText);
                icon.setFont(Font.font("System", FontWeight.BOLD, 18));
                icon.setTextFill(Color.web(borderColor));
                icon.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-border-color: " + borderColor + ";"
                                                + "-fx-border-radius: 50;"
                                                + "-fx-background-radius: 50;"
                                                + "-fx-border-width: 2;"
                                                + "-fx-min-width: 36px;"
                                                + "-fx-min-height: 36px;"
                                                + "-fx-max-width: 36px;"
                                                + "-fx-max-height: 36px;"
                                                + "-fx-alignment: center;");

                // ── Teks ──
                Label lblTitle = new Label(title);
                lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
                lblTitle.setTextFill(Color.WHITE);

                Label lblMessage = new Label(message);
                lblMessage.setFont(Font.font("System", 12));
                lblMessage.setTextFill(Color.web("#a0a0b0"));
                lblMessage.setWrapText(true);
                lblMessage.setMaxWidth(220);

                VBox textBox = new VBox(3, lblTitle, lblMessage);
                textBox.setAlignment(Pos.CENTER_LEFT);

                // ── Progress bar auto-dismiss ──
                ProgressBar progressBar = new ProgressBar(1.0);
                progressBar.setPrefWidth(310);
                progressBar.setPrefHeight(3);
                progressBar.setStyle(
                                "-fx-accent: " + borderColor + ";"
                                                + "-fx-background-color: #2a2a3e;"
                                                + "-fx-background-radius: 0;"
                                                + "-fx-border-radius: 0;");

                // ── Layout utama ──
                HBox content = new HBox(14, icon, textBox);
                content.setAlignment(Pos.CENTER_LEFT);
                content.setPadding(new Insets(16, 18, 12, 18));

                VBox card = new VBox(0, content, progressBar);
                card.setStyle(
                                "-fx-background-color: " + bgColor + ";"
                                                + "-fx-background-radius: 12;"
                                                + "-fx-border-color: " + borderColor + ";"
                                                + "-fx-border-width: 1;"
                                                + "-fx-border-radius: 12;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 20, 0, 0, 6);");
                card.setMinWidth(310);
                card.setMaxWidth(310);

                StackPane root = new StackPane(card);
                root.setStyle("-fx-background-color: transparent;");
                root.setPadding(new Insets(6));

                Scene scene = new Scene(root, 322, 80);
                scene.setFill(Color.TRANSPARENT);
                popupStage.setScene(scene);

                // ── Posisi kanan bawah layar ──
                javafx.geometry.Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
                popupStage.setX(screen.getMaxX() - 340);
                popupStage.setY(screen.getMaxY() - 110);

                popupStage.show();

                // ── Animasi fade in ──
                FadeTransition fadeIn = new FadeTransition(Duration.millis(250), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), root);
                slideIn.setFromX(40);
                slideIn.setToX(0);

                ParallelTransition enterAnim = new ParallelTransition(fadeIn, slideIn);
                enterAnim.play();

                // ── Progress bar countdown (3 detik) ──
                Timeline countdown = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 1.0)),
                                new KeyFrame(Duration.seconds(3), new KeyValue(progressBar.progressProperty(), 0.0)));
                countdown.play();

                // ── Fade out lalu tutup ──
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);

                        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), root);
                        slideOut.setFromX(0);
                        slideOut.setToX(40);

                        ParallelTransition exitAnim = new ParallelTransition(fadeOut, slideOut);
                        exitAnim.setOnFinished(ev -> popupStage.close());
                        exitAnim.play();
                });
                pause.play();

                // ── Klik untuk tutup manual ──
                root.setOnMouseClicked(e -> {
                        pause.stop();
                        countdown.stop();
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), root);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(ev -> popupStage.close());
                        fadeOut.play();
                });
        }

        public void showSuccessPopup(String title, String message) {
                Stage popup = new Stage(StageStyle.TRANSPARENT);
                popup.initModality(Modality.APPLICATION_MODAL);

                // === CENTANG SVG ===
                SVGPath checkPath = new SVGPath();
                checkPath.setContent("M6,17 L13,24 L26,9");
                checkPath.setStroke(Color.web("#00d084"));
                checkPath.setStrokeWidth(2.5);
                checkPath.setFill(Color.TRANSPARENT);
                checkPath.setStrokeLineCap(StrokeLineCap.ROUND);
                checkPath.setStrokeLineJoin(StrokeLineJoin.ROUND);

                // Animasi draw centang
                checkPath.getStrokeDashArray().add(50.0);
                checkPath.setStrokeDashOffset(50.0);
                KeyValue kvCheck = new KeyValue(checkPath.strokeDashOffsetProperty(), 0, Interpolator.EASE_OUT);
                KeyFrame kfCheck = new KeyFrame(Duration.millis(450), kvCheck);
                Timeline checkTimeline = new Timeline(kfCheck);
                checkTimeline.setDelay(Duration.millis(200));
                checkTimeline.play();

                // === RING LINGKARAN ===
                Circle ring = new Circle(34);
                ring.setFill(Color.web("rgba(0,208,132,0.12)"));
                ring.setStroke(Color.web("#00d084"));
                ring.setStrokeWidth(1.5);
                ring.setOpacity(0.8);

                StackPane iconPane = new StackPane(ring, checkPath);
                iconPane.setPrefSize(68, 68);
                iconPane.setMaxSize(68, 68);

                // Pulse animation ring
                ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), ring);
                pulse.setFromX(1.0);
                pulse.setToX(1.08);
                pulse.setFromY(1.0);
                pulse.setToY(1.08);
                pulse.setAutoReverse(true);
                pulse.setCycleCount(Animation.INDEFINITE);
                pulse.play();

                // === TEXT ===
                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-text-fill: #f0f0f0;"
                                                + "-fx-font-size: 16px;"
                                                + "-fx-font-weight: bold;");

                Label msgLabel = new Label(message);
                msgLabel.setStyle(
                                "-fx-text-fill: #7878a0;"
                                                + "-fx-font-size: 13px;");
                msgLabel.setWrapText(true);
                msgLabel.setTextAlignment(TextAlignment.CENTER);
                msgLabel.setMaxWidth(240);

                // === TOMBOL ===
                Button btnOk = new Button("Oke, Mengerti");
                btnOk.setStyle(
                                "-fx-background-color: #00d084;"
                                                + "-fx-text-fill: #0a2e1e;"
                                                + "-fx-font-size: 14px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-cursor: hand;"
                                                + "-fx-padding: 10 0 10 0;");
                btnOk.setMaxWidth(Double.MAX_VALUE);
                btnOk.setOnAction(e -> popup.close());
                btnOk.setOnMouseEntered(e -> btnOk.setStyle(btnOk.getStyle()
                                .replace("#00d084", "#00b874")));
                btnOk.setOnMouseExited(e -> btnOk.setStyle(btnOk.getStyle()
                                .replace("#00b874", "#00d084")));

                // === LAYOUT ===
                VBox card = new VBox(8, iconPane, titleLabel, msgLabel);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(36, 32, 28, 32));
                VBox.setMargin(btnOk, new Insets(16, 0, 0, 0));
                card.getChildren().add(btnOk);
                card.setStyle(
                                "-fx-background-color: #1a1a2e;"
                                                + "-fx-background-radius: 20;"
                                                + "-fx-border-color: rgba(0,208,132,0.25);"
                                                + "-fx-border-radius: 20;"
                                                + "-fx-border-width: 1;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 30, 0, 0, 8);");
                card.setMaxWidth(300);

                StackPane root = new StackPane(card);
                root.setStyle("-fx-background-color: transparent;");
                root.setPadding(new Insets(20));

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                popup.setScene(scene);

                // === ANIMASI MASUK ===
                card.setOpacity(0);
                card.setScaleX(0.8);
                card.setScaleY(0.8);
                card.setTranslateY(16);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(350), card);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                ScaleTransition scaleIn = new ScaleTransition(Duration.millis(400), card);
                scaleIn.setFromX(0.8);
                scaleIn.setToX(1.0);
                scaleIn.setFromY(0.8);
                scaleIn.setToY(1.0);
                // Use valid control points in [0,1]
                scaleIn.setInterpolator(Interpolator.SPLINE(0.34, 0.0, 0.64, 1.0));

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), card);
                slideIn.setFromY(16);
                slideIn.setToY(0);

                ParallelTransition enter = new ParallelTransition(fadeIn, scaleIn, slideIn);
                enter.play();

                popup.show();
        }

        public void showConfirmPopup(String title, String message, Runnable onConfirm) {
                Stage popup = new Stage(StageStyle.TRANSPARENT);
                popup.initModality(Modality.APPLICATION_MODAL);

                // === ICON TANDA TANYA ===
                Circle ringBg = new Circle(34);
                ringBg.setFill(Color.web("#1f1a00"));
                ringBg.setStroke(Color.web("#ffd60a"));
                ringBg.setStrokeWidth(1.5);
                ringBg.setOpacity(0.7);

                Label iconLabel = new Label("?");
                iconLabel.setStyle(
                                "-fx-text-fill: #ffd60a;" +
                                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;");

                StackPane iconPane = new StackPane(ringBg, iconLabel);
                iconPane.setPrefSize(68, 68);
                iconPane.setMaxSize(68, 68);

                ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), ringBg);
                pulse.setFromX(1.0);
                pulse.setToX(1.08);
                pulse.setFromY(1.0);
                pulse.setToY(1.08);
                pulse.setAutoReverse(true);
                pulse.setCycleCount(Animation.INDEFINITE);
                pulse.play();

                // === TEXT ===
                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-text-fill: #f0f0f0;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;");

                Label msgLabel = new Label(message);
                msgLabel.setStyle(
                                "-fx-text-fill: #7878a0;" +
                                                "-fx-font-size: 13px;");
                msgLabel.setWrapText(true);
                msgLabel.setTextAlignment(TextAlignment.CENTER);
                msgLabel.setMaxWidth(240);

                // === TOMBOL BATAL ===
                Button btnBatal = new Button("Batal");
                btnBatal.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #8888aa;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: rgba(255,255,255,0.12);" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-padding: 10 0 10 0;");
                btnBatal.setMaxWidth(Double.MAX_VALUE);
                btnBatal.setOnAction(e -> popup.close());
                btnBatal.setOnMouseEntered(e -> btnBatal.setStyle(btnBatal.getStyle()
                                .replace("-fx-background-color: transparent;",
                                                "-fx-background-color: rgba(255,255,255,0.06);")));
                btnBatal.setOnMouseExited(e -> btnBatal.setStyle(btnBatal.getStyle()
                                .replace("-fx-background-color: rgba(255,255,255,0.06);",
                                                "-fx-background-color: transparent;")));

                // === TOMBOL KONFIRMASI ===
                Button btnKonfirmasi = new Button("Konfirmasi");
                btnKonfirmasi.setStyle(
                                "-fx-background-color: #ffd60a;" +
                                                "-fx-text-fill: #1a1200;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-padding: 10 0 10 0;");
                btnKonfirmasi.setMaxWidth(Double.MAX_VALUE);
                btnKonfirmasi.setOnAction(e -> {
                        popup.close();
                        if (onConfirm != null)
                                onConfirm.run();
                });
                btnKonfirmasi.setOnMouseEntered(e -> btnKonfirmasi.setStyle(btnKonfirmasi.getStyle()
                                .replace("#ffd60a", "#e6c000")));
                btnKonfirmasi.setOnMouseExited(e -> btnKonfirmasi.setStyle(btnKonfirmasi.getStyle()
                                .replace("#e6c000", "#ffd60a")));

                HBox btnRow = new HBox(10, btnBatal, btnKonfirmasi);
                HBox.setHgrow(btnBatal, Priority.ALWAYS);
                HBox.setHgrow(btnKonfirmasi, Priority.ALWAYS);
                btnRow.setMaxWidth(Double.MAX_VALUE);

                // === LAYOUT ===
                VBox card = new VBox(8, iconPane, titleLabel, msgLabel);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(36, 32, 28, 32));
                VBox.setMargin(btnRow, new Insets(16, 0, 0, 0));
                card.getChildren().add(btnRow);
                card.setStyle(
                                "-fx-background-color: #1a1a2e;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-border-color: rgba(255,214,10,0.2);" +
                                                "-fx-border-radius: 20;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 30, 0, 0, 8);");
                card.setMaxWidth(300);

                StackPane root = new StackPane(card);
                root.setStyle("-fx-background-color: transparent;");
                root.setPadding(new Insets(20));

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                popup.setScene(scene);

                // === ANIMASI MASUK ===
                card.setOpacity(0);
                card.setScaleX(0.8);
                card.setScaleY(0.8);
                card.setTranslateY(16);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(350), card);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                ScaleTransition scaleIn = new ScaleTransition(Duration.millis(400), card);
                scaleIn.setFromX(0.8);
                scaleIn.setToX(1.0);
                scaleIn.setFromY(0.8);
                scaleIn.setToY(1.0);
                scaleIn.setInterpolator(Interpolator.SPLINE(0.34, 0.0, 0.64, 1.0));

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), card);
                slideIn.setFromY(16);
                slideIn.setToY(0);

                ParallelTransition enter = new ParallelTransition(fadeIn, scaleIn, slideIn);
                enter.play();

                popup.show();
        }

}
