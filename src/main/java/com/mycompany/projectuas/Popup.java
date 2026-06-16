package com.mycompany.projectuas;

import com.mycompany.Model.GoogleUser;
import com.mycompany.services.GoogleAuthService;
import javafx.scene.layout.Background;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.image.*;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
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

    public void showModernPopup(String title, String message, PopupType type, Stage ownerStage) {
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.setAlwaysOnTop(true);

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
        lblMessage.setMaxWidth(210);

        VBox textBox = new VBox(3, lblTitle, lblMessage);
        textBox.setAlignment(Pos.CENTER_LEFT);

        // ── Progress bar ──
        ProgressBar progressBar = new ProgressBar(1.0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(4);
        progressBar.setMaxHeight(4);
        progressBar.setMinHeight(4);
        progressBar.setStyle(
                "-fx-accent: " + borderColor + ";"
                        + "-fx-background-color: #2a2a3e;"
                        + "-fx-background-radius: 0 0 12 12;"
                        + "-fx-padding: 0;"
                        + "-fx-background-insets: 0;");

        // ── HBox content ──
        HBox content = new HBox(12, icon, textBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(14, 16, 10, 16));
        VBox.setVgrow(content, Priority.ALWAYS);
        VBox.setVgrow(progressBar, Priority.NEVER);

        // ── VBox card ──
        VBox card = new VBox(0, content, progressBar);
        card.setMinWidth(330);
        card.setMaxWidth(330);
        card.setPrefWidth(330);
        card.setStyle(
                "-fx-background-color: " + bgColor + ";"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: " + borderColor + ";"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 12;"
                        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 20, 0, 0, 6);");

        // ── StackPane root ──
        StackPane root = new StackPane(card);
        root.setBackground(Background.EMPTY);
        root.setPadding(new Insets(6));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.sizeToScene();

        // ── Posisi popup ──
        popupStage.show();

        double targetX, targetY;
        if (ownerStage != null && ownerStage.isShowing()) {
            targetX = ownerStage.getX() + ownerStage.getWidth() - 350;
            targetY = ownerStage.getY() + ownerStage.getHeight() - 120;
        } else {
            targetX = javafx.stage.Screen.getPrimary().getVisualBounds().getMaxX() - 350;
            targetY = javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY() - 110;
        }
        popupStage.setX(targetX);
        popupStage.setY(targetY);

        // ── Ikuti window saat digeser ──
        if (ownerStage != null) {
            ChangeListener<Number> moveListener = (obs, oldVal, newVal) -> {
                popupStage.setX(ownerStage.getX() + ownerStage.getWidth() - 350);
                popupStage.setY(ownerStage.getY() + ownerStage.getHeight() - 120);
            };
            ownerStage.xProperty().addListener(moveListener);
            ownerStage.yProperty().addListener(moveListener);
            popupStage.setOnHidden(e -> {
                ownerStage.xProperty().removeListener(moveListener);
                ownerStage.yProperty().removeListener(moveListener);
            });
        }

        // ── Animasi masuk ──
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), root);
        slideIn.setFromX(40);
        slideIn.setToX(0);
        new ParallelTransition(fadeIn, slideIn).play();

        // ── Animasi keluar (reusable) ──
        Runnable closeAnim = () -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), root);
            slideOut.setFromX(0);
            slideOut.setToX(40);
            ParallelTransition exitAnim = new ParallelTransition(fadeOut, slideOut);
            exitAnim.setOnFinished(ev -> popupStage.close());
            exitAnim.play();
        };

        // ── Countdown progress bar ──
        // PENTING: dibuat setelah scene sudah tampil
        Timeline countdown = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(progressBar.progressProperty(), 0.0)));
        countdown.setOnFinished(e -> closeAnim.run());

        // Setelah scene dibuat, tambahkan ini:
        scene.setFill(Color.TRANSPARENT);

        // Tambahkan inline stylesheet untuk hapus scrollbar
        scene.getStylesheets().add("data:text/css," +
                ".root { -fx-background-color: transparent; }" +
                ".scroll-bar { visibility: hidden; -fx-opacity: 0; }" +
                ".scroll-pane { -fx-background-color: transparent; }" +
                ".progress-bar > .track { -fx-background-color: #2a2a3e; -fx-background-insets: 0; -fx-background-radius: 0 0 12 12; }"
                +
                ".progress-bar > .bar { -fx-background-insets: 0; -fx-background-radius: 0 0 12 12; -fx-padding: 0; }");

        // ── Animasi warna: hijau → kuning → merah ──
        // Ganti Timeline colorAnim menjadi:
        Timeline colorAnim = new Timeline(
                new KeyFrame(Duration.ZERO, e -> progressBar.lookup(".bar").setStyle(
                        "-fx-background-color: #00d084;"
                                + "-fx-background-insets: 0;"
                                + "-fx-background-radius: 0 0 12 12;")),
                new KeyFrame(Duration.seconds(1), e -> progressBar.lookup(".bar").setStyle(
                        "-fx-background-color: #ffd60a;"
                                + "-fx-background-insets: 0;"
                                + "-fx-background-radius: 0 0 12 12;")),
                new KeyFrame(Duration.seconds(2), e -> progressBar.lookup(".bar").setStyle(
                        "-fx-background-color: #ff4d6d;"
                                + "-fx-background-insets: 0;"
                                + "-fx-background-radius: 0 0 12 12;")));

        countdown.setOnFinished(e -> closeAnim.run());

        // Jalankan setelah scene benar-benar rendered
        Platform.runLater(() -> {
            countdown.play();
            colorAnim.play();
        });

        // ── Klik untuk tutup manual ──
        root.setOnMouseClicked(e -> {
            countdown.stop();
            colorAnim.stop();
            closeAnim.run();
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

    public void showGoogleSuccessPopup(String title, String message, GoogleUser user) {
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);

        // === FOTO PROFIL ===
        ImageView avatar = new ImageView();
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
        avatar.setPreserveRatio(true);

        // Clip bulat
        Circle clip = new Circle(40, 40, 40);
        avatar.setClip(clip);

        // Border foto
        Circle avatarBorder = new Circle(42);
        avatarBorder.setFill(Color.TRANSPARENT);
        avatarBorder.setStroke(Color.web("#00d084"));
        avatarBorder.setStrokeWidth(2);

        // Load foto dari URL Google
        try {
            Image img = new Image(user.getProfilePictureUrl(), true); // true = background load
            avatar.setImage(img);
        } catch (Exception e) {
            // Fallback: inisial nama
        }

        // === CENTANG DI POJOK KANAN BAWAH (seperti Chrome) ===
        Circle badgeBg = new Circle(14);
        badgeBg.setFill(Color.web("#00d084"));
        badgeBg.setStroke(Color.web("#1a1a2e"));
        badgeBg.setStrokeWidth(2.5);

        SVGPath checkPath = new SVGPath();
        checkPath.setContent("M6,10 L10,14 L18,6");
        checkPath.setStroke(Color.web("#0a2e1e"));
        checkPath.setStrokeWidth(2.2);
        checkPath.setFill(Color.TRANSPARENT);
        checkPath.setStrokeLineCap(StrokeLineCap.ROUND);
        checkPath.setStrokeLineJoin(StrokeLineJoin.ROUND);

        // Animasi draw centang
        checkPath.getStrokeDashArray().add(40.0);
        checkPath.setStrokeDashOffset(40.0);
        KeyValue kvCheck = new KeyValue(checkPath.strokeDashOffsetProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kfCheck = new KeyFrame(Duration.millis(400), kvCheck);
        Timeline checkTimeline = new Timeline(kfCheck);
        checkTimeline.setDelay(Duration.millis(400));
        checkTimeline.play();

        StackPane badge = new StackPane(badgeBg, checkPath);
        badge.setPrefSize(28, 28);
        badge.setMaxSize(28, 28);

        // === GABUNG FOTO + BADGE ===
        StackPane avatarStack = new StackPane();
        avatarStack.setPrefSize(90, 90);
        avatarStack.setMaxSize(90, 90);

        StackPane.setAlignment(avatar, Pos.CENTER);
        StackPane.setAlignment(avatarBorder, Pos.CENTER);
        StackPane.setAlignment(badge, Pos.BOTTOM_RIGHT);
        badge.setTranslateX(4);
        badge.setTranslateY(4);

        avatarStack.getChildren().addAll(avatarBorder, avatar, badge);

        // Animasi pop badge masuk
        badge.setScaleX(0);
        badge.setScaleY(0);
        ScaleTransition badgePop = new ScaleTransition(Duration.millis(300), badge);
        badgePop.setFromX(0);
        badgePop.setToX(1.0);
        badgePop.setFromY(0);
        badgePop.setToY(1.0);
        badgePop.setInterpolator(Interpolator.SPLINE(0.34, 0.0, 0.64, 1.0));
        badgePop.setDelay(Duration.millis(300));
        badgePop.play();

        // Pulse animation border
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), avatarBorder);
        pulse.setFromX(1.0);
        pulse.setToX(1.06);
        pulse.setFromY(1.0);
        pulse.setToY(1.06);
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
        msgLabel.setAlignment(Pos.CENTER);
        msgLabel.setMaxWidth(240);

        // Email label
        Label emailLabel = new Label(user.getEmail());
        emailLabel.setStyle(
                "-fx-text-fill: #00d084;"
                        + "-fx-font-size: 12px;");
        emailLabel.setTextAlignment(TextAlignment.CENTER);
        emailLabel.setAlignment(Pos.CENTER);

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
        btnOk.setOnMouseEntered(e -> btnOk.setStyle(btnOk.getStyle().replace("#00d084", "#00b874")));
        btnOk.setOnMouseExited(e -> btnOk.setStyle(btnOk.getStyle().replace("#00b874", "#00d084")));

        // === LAYOUT ===
        VBox card = new VBox(8, avatarStack, titleLabel, emailLabel, msgLabel);
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
        scaleIn.setInterpolator(Interpolator.SPLINE(0.34, 0.0, 0.64, 1.0));

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), card);
        slideIn.setFromY(16);
        slideIn.setToY(0);

        ParallelTransition enter = new ParallelTransition(fadeIn, scaleIn, slideIn);
        enter.play();

        popup.show();
    }

    public class LoginProgressDialog {

        private static final int TIMEOUT_SECONDS = 120; // 2 menit
        private static final double DIALOG_WIDTH = 320;
        private static final double DIALOG_HEIGHT = 300;

        private static final String COLOR_BG = "#FFFFFF";
        private static final String COLOR_BLUE = "#4285F4";
        private static final String COLOR_RED = "#EA4335";
        private static final String COLOR_YELLOW = "#FBBC05";
        private static final String COLOR_GREEN = "#34A853";
        private static final String COLOR_TEXT_MAIN = "#1a1a1a";
        private static final String COLOR_TEXT_SUB = "#666666";
        private static final String COLOR_TEXT_HINT = "#BBBBBB";
        private static final String COLOR_TRACK = "#EEEEEE";

        private final Stage ownerStage;
        private Stage dialogStage;
        private Timeline countdownTimeline;
        private int secondsLeft = TIMEOUT_SECONDS;

        private Label lblCountdown;
        private Label lblStatus;
        private Arc arcProgress;

        private java.util.function.Consumer<GoogleUser> onSuccess;
        private Runnable onCancelled;
        private Runnable onTimeout;
        private volatile boolean finished = false;

        public LoginProgressDialog(Stage ownerStage) {
            this.ownerStage = ownerStage;
        }

        public void show(
                java.util.function.Consumer<GoogleUser> onSuccess,
                Runnable onCancelled,
                Runnable onTimeout) {

            this.onSuccess = onSuccess;
            this.onCancelled = onCancelled;
            this.onTimeout = onTimeout;

            buildDialog();
            startCountdown();
            startLogin();

            dialogStage.show();
        }

        public void close() {
            stopCountdown();
            if (dialogStage != null && dialogStage.isShowing())
                dialogStage.close();
        }

        private void buildDialog() {
            dialogStage = new Stage();
            dialogStage.initOwner(ownerStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setResizable(false);
            dialogStage.setOnCloseRequest(e -> e.consume());

            // ── Card ────────────────────────────────────────────────────────────
            VBox card = new VBox(0);
            card.setAlignment(Pos.TOP_CENTER);
            card.setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT);
            card.setMaxSize(DIALOG_WIDTH, DIALOG_HEIGHT);
            card.setStyle(
                    "-fx-background-color: " + COLOR_BG + ";" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-radius: 20;");
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.color(0, 0, 0, 0.22));
            shadow.setRadius(28);
            shadow.setOffsetY(8);
            card.setEffect(shadow);
            card.setPadding(new Insets(28, 28, 24, 28));

            // ── Google dots ──────────────────────────────────────────────────────
            HBox logoRow = buildGoogleLogo();
            VBox.setMargin(logoRow, new Insets(0, 0, 18, 0));

            // ── Arc countdown ─────────────────────────────────────────────────────
            StackPane arcStack = buildCountdownArc();
            VBox.setMargin(arcStack, new Insets(0, 0, 18, 0));

            // ── Status label ──────────────────────────────────────────────────────
            lblStatus = new Label("Menunggu login di browser...");
            lblStatus.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
            lblStatus.setTextFill(Color.web(COLOR_TEXT_MAIN));
            lblStatus.setTextAlignment(TextAlignment.CENTER);
            lblStatus.setAlignment(Pos.CENTER); // ← tambahkan ini
            lblStatus.setWrapText(true);
            lblStatus.setMaxWidth(DIALOG_WIDTH - 56);
            VBox.setMargin(lblStatus, new Insets(0, 0, 6, 0));

            // ── Subtitle ──────────────────────────────────────────────────────────
            Label lblSub = new Label("Selesaikan login di Chrome,\nlalu kembali ke aplikasi.");
            lblSub.setFont(Font.font("System", 12));
            lblSub.setTextFill(Color.web(COLOR_TEXT_SUB));
            lblSub.setTextAlignment(TextAlignment.CENTER);
            lblSub.setAlignment(Pos.CENTER); // ← tambahkan ini
            lblSub.setWrapText(true);
            lblSub.setMaxWidth(DIALOG_WIDTH - 56);
            VBox.setMargin(lblSub, new Insets(0, 0, 12, 0));

            // ── Hint ─────────────────────────────────────────────────────────────
            Label lblHint = new Label("Sesi otomatis berakhir jika browser ditutup");
            lblHint.setFont(Font.font("System", 11));
            lblHint.setTextFill(Color.web(COLOR_TEXT_HINT));
            lblHint.setTextAlignment(TextAlignment.CENTER);
            lblHint.setAlignment(Pos.CENTER); // ← tambahkan ini
            lblHint.setWrapText(true);
            lblHint.setMaxWidth(DIALOG_WIDTH - 56);

            card.getChildren().addAll(logoRow, arcStack, lblStatus, lblSub, lblHint);

            // ── Root transparent (hanya card, tanpa overlay gelap) ───────────────
            // Overlay gelap tidak dipakai karena WINDOW_MODAL sudah memblokir input
            StackPane root = new StackPane(card);
            root.setStyle("-fx-background-color: transparent;");
            root.setPadding(new Insets(10)); // ruang untuk shadow

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialogStage.setScene(scene);

            // ── Posisi: tepat di tengah ownerStage ───────────────────────────────
            // Harus dipanggil setelah scene di-set agar getWidth/getHeight valid
            dialogStage.setOnShown(e -> {
                dialogStage.setX(ownerStage.getX() + (ownerStage.getWidth() - dialogStage.getWidth()) / 2);
                dialogStage.setY(ownerStage.getY() + (ownerStage.getHeight() - dialogStage.getHeight()) / 2);
            });
        }

        private HBox buildGoogleLogo() {
            HBox row = new HBox(6);
            row.setAlignment(Pos.CENTER);
            String[] colors = { COLOR_BLUE, COLOR_RED, COLOR_YELLOW, COLOR_GREEN };
            for (String c : colors) {
                Circle dot = new Circle(4.5);
                dot.setFill(Color.web(c));
                row.getChildren().add(dot);
            }
            return row;
        }

        private StackPane buildCountdownArc() {
            double radius = 36; // radius lingkaran
            double stroke = 5; // tebal garis

            // Ukuran canvas = diameter + stroke kiri kanan + sedikit padding
            double size = (radius + stroke) * 2 + 4;

            // ── Track (lingkaran abu penuh) ───────────────────────────────────────
            // Pakai Arc bukan Circle agar center-nya sama persis dengan arcProgress
            Arc track = new Arc();
            track.setCenterX(size / 2);
            track.setCenterY(size / 2);
            track.setRadiusX(radius);
            track.setRadiusY(radius);
            track.setStartAngle(0);
            track.setLength(360);
            track.setType(ArcType.OPEN);
            track.setFill(Color.TRANSPARENT);
            track.setStroke(Color.web(COLOR_TRACK));
            track.setStrokeWidth(stroke);

            // ── Progress arc (berkurang seiring countdown) ────────────────────────
            // Center HARUS sama dengan track
            arcProgress = new Arc();
            arcProgress.setCenterX(size / 2);
            arcProgress.setCenterY(size / 2);
            arcProgress.setRadiusX(radius);
            arcProgress.setRadiusY(radius);
            arcProgress.setStartAngle(90); // mulai dari atas (12 o'clock)
            arcProgress.setLength(-360); // penuh, berkurang saat tick()
            arcProgress.setType(ArcType.OPEN);
            arcProgress.setFill(Color.TRANSPARENT);
            arcProgress.setStroke(Color.web(COLOR_BLUE));
            arcProgress.setStrokeWidth(stroke);
            arcProgress.setStrokeLineCap(StrokeLineCap.ROUND);

            // ── Angka di tengah ───────────────────────────────────────────────────
            lblCountdown = new Label(String.valueOf(TIMEOUT_SECONDS));
            lblCountdown.setFont(Font.font("System", FontWeight.BOLD, 24));
            lblCountdown.setTextFill(Color.web(COLOR_TEXT_MAIN));

            // Gunakan Pane (bukan StackPane) agar posisi arc absolute & tidak di-layout
            // ulang
            javafx.scene.layout.Pane arcPane = new javafx.scene.layout.Pane();
            arcPane.setPrefSize(size, size);
            arcPane.setMaxSize(size, size);
            arcPane.getChildren().addAll(track, arcProgress);

            // Label tetap di StackPane agar mudah di-center
            StackPane stack = new StackPane(arcPane, lblCountdown);
            stack.setPrefSize(size, size);
            stack.setMaxSize(size, size);
            stack.setAlignment(Pos.CENTER);
            return stack;
        }

        // =========================================================================
        // Countdown
        // =========================================================================

        private void startCountdown() {
            secondsLeft = TIMEOUT_SECONDS;
            countdownTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> tick()));
            countdownTimeline.setCycleCount(TIMEOUT_SECONDS);
            countdownTimeline.play();
        }

        private void tick() {
            secondsLeft--;
            lblCountdown.setText(String.valueOf(secondsLeft));

            double progress = (double) secondsLeft / TIMEOUT_SECONDS;
            arcProgress.setLength(-360 * progress);

            if (progress > 0.5) {
                arcProgress.setStroke(Color.web(COLOR_BLUE));
                lblCountdown.setTextFill(Color.web(COLOR_TEXT_MAIN));
            } else if (progress > 0.2) {
                arcProgress.setStroke(Color.web(COLOR_YELLOW));
                lblCountdown.setTextFill(Color.web(COLOR_YELLOW));
            } else {
                arcProgress.setStroke(Color.web(COLOR_RED));
                lblCountdown.setTextFill(Color.web(COLOR_RED));
            }
        }

        private void stopCountdown() {
            if (countdownTimeline != null) {
                countdownTimeline.stop();
                countdownTimeline = null;
            }
        }

        // =========================================================================
        // Login
        // =========================================================================

        private void startLogin() {
            GoogleAuthService authService = new GoogleAuthService();

            authService.setOnSuccess(user -> {
                if (finished)
                    return;
                finished = true;
                stopCountdown();

                lblStatus.setText("Login berhasil!");
                lblStatus.setTextFill(Color.web(COLOR_GREEN));
                arcProgress.setStroke(Color.web(COLOR_GREEN));
                arcProgress.setLength(-360);
                lblCountdown.setText("✓");
                lblCountdown.setTextFill(Color.web(COLOR_GREEN));

                Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                    close();
                    if (onSuccess != null)
                        onSuccess.accept(user);
                }));
                delay.play();
            });

            authService.setOnCancelled(() -> {
                if (finished)
                    return;
                finished = true;
                stopCountdown();

                lblStatus.setText("Login dibatalkan");
                lblStatus.setTextFill(Color.web(COLOR_RED));

                Timeline delay = new Timeline(new KeyFrame(Duration.millis(800), ev -> {
                    close();
                    if (onCancelled != null)
                        onCancelled.run();
                }));
                delay.play();
            });

            authService.setOnTimeout(() -> {
                if (finished)
                    return;
                finished = true;
                stopCountdown();

                lblStatus.setText("Waktu habis");
                lblStatus.setTextFill(Color.web(COLOR_RED));

                Timeline delay = new Timeline(new KeyFrame(Duration.millis(800), ev -> {
                    close();
                    if (onTimeout != null)
                        onTimeout.run();
                }));
                delay.play();
            });

            authService.loginAsync();
        }
    }
}
