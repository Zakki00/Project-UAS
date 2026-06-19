package com.mycompany.projectuas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class koneksi {
    private static final String URL = "jdbc:sqlite:" + getDbPath();
    // private static final String URL =
    // "jdbc:mysql://localhost:3306/db_enjoy_cave";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static String getDbPath() {
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isEmpty()) {
            File appDataDb = new File(appData + "\\ProjectUAS\\db\\db_enjoy_cafe.db");
            if (appDataDb.exists()) {
                return appDataDb.getAbsolutePath();
            }
        }
        // Fallback: development di VSCode
        return "src/main/resources/db/db_enjoy_cafe.db";
    }
    
    public static void koneksi() {
        
        try (Connection connection = getConnection()) {
            System.out.println("Koneksi berhasil! Database: " + connection.getCatalog());
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }

    public static void eksekusiQuery(String query) {
        eksekusiQuery(query, new Object[] {});

    }

    public static void eksekusiQuery(String query, Object... params) {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            System.out.println("Query berhasil dieksekusi!");

        } catch (SQLException e) {
            System.err.println("Gagal mengeksekusi query: " + e.getMessage());
        }

    }

    public static List<Object[]> ambilData(String query) {
        return ambilData(query, new Object[] {});
    }

    public static List<Object[]> ambilData(String query, Object... params) {
        List<Object[]> dataList = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];

                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }

                    dataList.add(rowData);
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data: " + e.getMessage());
        }

        return dataList;
    }

}
