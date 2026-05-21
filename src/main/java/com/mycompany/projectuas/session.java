package com.mycompany.projectuas;

public class session {
    public static int id_user;
    public static String username;
    public static String nama;
    public static String role;
    public static void setSession(int id_user, String username, String nama, String role) {
        session.id_user = id_user;
        session.username = username;
        session.nama = nama;
        session.role = role;
    }
}
