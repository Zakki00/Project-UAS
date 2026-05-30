package com.mycompany.Model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LaporanModel {

    
    public static final ObservableList<LaporanTransaksiItem> dataLaporanTransaksi = FXCollections.observableArrayList();
    // Static list masing-masing
    public static final ObservableList<HarianItem> dataHarian = FXCollections.observableArrayList();
    public static final ObservableList<BulananItem> dataBulanan = FXCollections.observableArrayList();
    public static final ObservableList<TransaksiItem> dataTransaksi = FXCollections.observableArrayList();
    public static final ObservableList<StokItem> dataStok = FXCollections.observableArrayList();
 
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
            LocalDate tanggalTransaksi
    ) {
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

// ═══════════════════════════════════════════════
// MODEL CHART 1 & 2 — Harian (Area + Bar)
// ═══════════════════════════════════════════════
// Chart 1 & 2 — pakai ini sekaligus
public static class HarianItem {
    public String hari;
    public long totalPenjualan; // → Area chart
    public int jumlahTransaksi; // → Bar chart
}

// Chart 3
public static class BulananItem {
    public String bulan;
    public int noBulan;
    public long totalOmzet;
}

// Table 4
public static class TransaksiItem {

    public String id, items, kasir, waktu;
    public long total;

    public TransaksiItem(String id, String items, String kasir, String waktu, long total) {
        this.id = id;
        this.items = items;
        this.kasir = kasir;
        this.waktu = waktu;
        this.total = total;
    }
}

// Progress bar 5
public static class StokItem {
    public String nama, status;
    public int stok, stokMax;

    public double persen() {
        return stokMax > 0 ? (double) stok / stokMax : 0;
    }
}

}