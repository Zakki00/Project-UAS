package com.mycompany.Model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LaporanModel {

    public static final  ObservableList<LaporanTransaksiItem> dataLaporanTransaksi = FXCollections.observableArrayList();
    public static class LaporanTransaksiItem {

        int no;
        int idTransaksi;

        String username;
        String namaLengkap;
        String pelanggan;

        long totalPembayaran;
        long uangPembayaran;
        long kembalian;
        long kekurangan;

        String statusPembayaran;
        LocalDate tanggalTransaksi;

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
}