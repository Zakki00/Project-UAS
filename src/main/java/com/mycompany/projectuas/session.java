package com.mycompany.projectuas;
import com.mycompany.Model.GoogleUser;

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


   
    
}
