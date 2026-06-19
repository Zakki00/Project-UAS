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

        imageView1.setClip(new Circle(15, 15, 15)); // topbar → 44x44
        imageView2.setClip(new Circle(15, 15, 15)); // sidebar → 44x44
        if ("Admin".equalsIgnoreCase(session.role)) {
            String photoUrl = session.googleUser.getProfilePictureUrl();

            if (photoUrl != null && !photoUrl.isEmpty()) {
                imageView1.setImage(new javafx.scene.image.Image(photoUrl, true));
                imageView2.setImage(new javafx.scene.image.Image(photoUrl, true));

                imageView1.setVisible(true);
                imageView1.setManaged(true);
                imageView2.setVisible(true);
                imageView2.setManaged(true);

                labelInisial1.setVisible(false);
                labelInisial1.setManaged(false);
                labelInisial2.setVisible(false);
                labelInisial2.setManaged(false);

            } else {
                imageView1.setVisible(false);
                imageView1.setManaged(false);
                imageView2.setVisible(false);
                imageView2.setManaged(false);

                labelInisial1.setVisible(true);
                labelInisial1.setManaged(true);
                labelInisial2.setVisible(true);
                labelInisial2.setManaged(true);

                if (session.nama != null && !session.nama.isBlank()) {
                    labelInisial1.setText(
                            String.valueOf(session.nama.charAt(0)).toUpperCase());
                    labelInisial2.setText(
                            String.valueOf(session.nama.charAt(0)).toUpperCase());
                } else {
                    labelInisial1.setText("A");
                    labelInisial2.setText("A");
                }

               
            }
        }else{
            imageView1.setVisible(false);
            imageView1.setManaged(false);
            imageView2.setVisible(false);
            imageView2.setManaged(false);

            labelInisial1.setVisible(true);
            labelInisial1.setManaged(true);
            labelInisial2.setVisible(true);
            labelInisial2.setManaged(true);

            if (session.nama != null && !session.nama.isBlank()) {
                labelInisial1.setText(
                        String.valueOf(session.nama.charAt(0)).toUpperCase());
                labelInisial2.setText(
                        String.valueOf(session.nama.charAt(0)).toUpperCase());
            } else {
                labelInisial1.setText("A");
                labelInisial2.setText("A");
            }
        }
    }
   
    
}
