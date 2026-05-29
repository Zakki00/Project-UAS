package com.mycompany.Model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LaporanModel {

    public static final ObservableList<LaporanTransaksiItem> dataLaporanTransaksi = FXCollections.observableArrayList();
    private ObservableList<LaporanBarangItem> dataLaporanBarang = FXCollections.observableArrayList();

    public static class LaporanTransaksiItem {

        public int no;
        public int idTransaksi;

        public String username;
        public String namaLengkap;
        public String pelanggan;

        public long totalPembayaran;
        public long uangPembayaran;
        public long kembalian;
        public long kekurangan;

        public String statusPembayaran;
        public LocalDate tanggalTransaksi;

        public LaporanTransaksiItem(
                int no,
                int idTransaksi,
                String username,
                String namaLengkap,
                String pelanggan,
                long totalPembayaran,
                long uangPembayaran,
                long kembalian,
                long kekurangan,
                String statusPembayaran,
                LocalDate tanggalTransaksi) {
            this.no = no;
            this.idTransaksi = idTransaksi;
            this.username = username;
            this.namaLengkap = namaLengkap;
            this.pelanggan = pelanggan;
            this.totalPembayaran = totalPembayaran;
            this.uangPembayaran = uangPembayaran;
            this.kembalian = kembalian;
            this.kekurangan = kekurangan;
            this.statusPembayaran = statusPembayaran;
            this.tanggalTransaksi = tanggalTransaksi;
        }
    }

    public static class LaporanBarangItem {

        public int no;
        public int idBarang;

        public String namaBarang;
        public String kategori;

        public int stok;
        public long harga;

        public LaporanBarangItem(
                int no,
                int idBarang,
                String namaBarang,
                String kategori,
                int stok,
                long harga) {
            this.no = no;
            this.idBarang = idBarang;
            this.namaBarang = namaBarang;
            this.kategori = kategori;
            this.stok = stok;
            this.harga = harga;
        }
    }
    public static class LaporanPiutangItem {

        public int no;
        public String idTransaksi;
        public String namaPelanggan;
        public Long total_pembayaran;
        public Long uang_pembayaran;
        public long kekurangan;
        public String status_pembayaran;
        public String tanggal_transaksi;

        public LaporanPiutangItem(int no, String idTransaksi, String namaPelanggan, Long total_pembayaran, Long uang_pembayaran,
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

}