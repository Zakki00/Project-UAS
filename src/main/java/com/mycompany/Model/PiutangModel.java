package com.mycompany.Model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PiutangModel {
     private final ObservableList<DataHutang> dataHutang = FXCollections.observableArrayList();
    private final static List<DataBarang> dataBarang = new ArrayList<>();

    static class DataHutang {

        int no;
        String idTransaksi;
        String namaPelanggan;
        Long total_pembayaran;
        Long uang_pembayaran;
        long kekurangan;
        String status_pembayaran;
        String tanggal_transaksi;

        DataHutang(int no, String idTransaksi, String namaPelanggan, Long total_pembayaran, Long uang_pembayaran,
                long kekurangan, String status_pembayaran, String tanggal_transaksi) {
            this.no = no;
            this.idTransaksi = idTransaksi;
            this.namaPelanggan = namaPelanggan;
            this.total_pembayaran = total_pembayaran;
            this.uang_pembayaran = uang_pembayaran;
            this.kekurangan = kekurangan;
            this.status_pembayaran = status_pembayaran;
            this.tanggal_transaksi = tanggal_transaksi;

        }

    }
    
    static class DataBarang {

        String nama_barang;
        long harga_barang;
        int qty;

        DataBarang(String nama_barang, long harga_barang, int qty) {
            this.nama_barang = nama_barang;
            this.harga_barang = harga_barang;
            this.qty = qty;
        }
    }
}
