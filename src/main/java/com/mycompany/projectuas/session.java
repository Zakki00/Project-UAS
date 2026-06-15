package com.mycompany.projectuas;

import java.util.List;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
