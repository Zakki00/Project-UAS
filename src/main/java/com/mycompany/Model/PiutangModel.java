package com.mycompany.Model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PiutangModel {
    public final static ObservableList<DataHutang> dataHutang = FXCollections.observableArrayList();
    public final static List<DataBarangHutang> dataBarang = new ArrayList<>();

    public static class DataHutang {

        public int no;
        public String idTransaksi;
        public String namaPelanggan;
        public Long total_pembayaran;
        public Long uang_pembayaran;
        public long kekurangan;
        public String status_pembayaran;
        public String tanggal_transaksi;

        public DataHutang(int no, String idTransaksi, String namaPelanggan, Long total_pembayaran, Long uang_pembayaran,
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

    public static class DataBarangHutang {
        public String nama_barang;
        public long harga_barang;
        public int qty;

       public  DataBarangHutang(String nama_barang, long harga_barang, int qty) {
            this.nama_barang = nama_barang;
            this.harga_barang = harga_barang;
            this.qty = qty;
        }
    }
}
