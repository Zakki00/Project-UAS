package com.mycompany.projectuas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_enjoy_cave";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void koneksi() {
        try (Connection connection = getConnection()) {
            System.out.println("Koneksi berhasil! Database: " + connection.getCatalog());
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }

    public static void eksekusiQuery(String query) {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(query);
            System.out.println("Query berhasil dieksekusi!");

        } catch (SQLException e) {
            System.err.println("Gagal mengeksekusi query: " + e.getMessage());

        }

    }
    
    public static List<Object[]> ambilData(String query) {
        List<Object[]> dataList = new ArrayList<>();

        try (Connection connection = getConnection(); Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];

                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }

                dataList.add(rowData);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data: " + e.getMessage());
        }

        return dataList;
    }

}
