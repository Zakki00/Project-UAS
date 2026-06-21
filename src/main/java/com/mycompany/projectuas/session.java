package com.mycompany.projectuas;
import com.mycompany.Model.GoogleUser;
import javafx.scene.image.Image;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class session {
    
     
    public static GoogleUser googleUser = null; // tambah ini
    public static Object id;
    public static String username;
    public static String nama;
    public static String role;
    public static String email;

    public static void setSession(Object id, String username, String nama, String role) {
        session.id = id;
        session.username = username;
        session.nama = nama;
        session.role = role;
    }


    public static void applyFotoProfile(Label labelInisial1, Label labelInisial2,
            ImageView imageView1, ImageView imageView2) {

        imageView1.setClip(new Circle(15, 15, 15));
        imageView2.setClip(new Circle(15, 15, 15));

        if ("Admin".equalsIgnoreCase(session.role) && session.googleUser != null) {
            String photoUrl = session.googleUser.getProfilePictureUrl();

            if (photoUrl != null && !photoUrl.isEmpty()) {
                Image img1 = new Image(photoUrl, true);
                Image img2 = new Image(photoUrl, true);

                // Kalau gambar gagal load (tidak ada internet)
                img1.errorProperty().addListener((obs, old, isError) -> {
                    if (isError) {
                        javafx.application.Platform.runLater(() -> tampilInisial(
                                labelInisial1, labelInisial2,
                                imageView1, imageView2));
                    }
                });

                imageView1.setImage(img1);
                imageView2.setImage(img2);

                imageView1.setVisible(true);
                imageView1.setManaged(true);
                imageView2.setVisible(true);
                imageView2.setManaged(true);

                labelInisial1.setVisible(false);
                labelInisial1.setManaged(false);
                labelInisial2.setVisible(false);
                labelInisial2.setManaged(false);

            } else {
                tampilInisial(labelInisial1, labelInisial2, imageView1, imageView2);
            }

        } else {
            tampilInisial(labelInisial1, labelInisial2, imageView1, imageView2);
        }
    }

    // Helper — tampilkan inisial, sembunyikan imageview
    private static void tampilInisial(Label labelInisial1, Label labelInisial2,
            ImageView imageView1, ImageView imageView2) {

        imageView1.setVisible(false);
        imageView1.setManaged(false);
        imageView2.setVisible(false);
        imageView2.setManaged(false);

        labelInisial1.setVisible(true);
        labelInisial1.setManaged(true);
        labelInisial2.setVisible(true);
        labelInisial2.setManaged(true);

        String inisial = (session.nama != null && !session.nama.isBlank())
                ? String.valueOf(session.nama.charAt(0)).toUpperCase()
                : "A";

        labelInisial1.setText(inisial);
        labelInisial2.setText(inisial);
    }
   
    
}
