package com.mycompany.projectuas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_enjoy_cave";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void koneksi() {
        try (Connection connection = getConnection()) {
            System.out.println("Koneksi berhasil!");
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }

    public static void eksekusiQuery(String query) {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(query);
            System.out.println("Query berhasil dieksekusi!");
            JOptionPane.showMessageDialog(null, "Query berhasil dieksekusi!");

        } catch (SQLException e) {
            System.err.println("Gagal mengeksekusi query: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal Mengesekusi Query" + e.getMessage());

        }

    }

    public static DefaultTableModel tampilkanData(String query) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data: " + e.getMessage());
        }

        return model;

    }

    public static List<Object[]> ambilData(String query) {
        List<Object[]> dataList = new ArrayList<>();

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
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
