package com.mycompany.Model;

import java.util.ArrayList;
import java.util.List;

public class DetailTransaksiModel {
    // Model item transaksi
    public static class ItemTransaksi {
        public static final List<ItemTransaksi> listItem = new ArrayList<>();

        public static String id, nama, kategori;
        public static long harga;
        public static int qty;

        ItemTransaksi(String id, String nama, String kategori, long harga, int qty) {
            this.id = id;
            this.nama = nama;
            this.kategori = kategori;
            this.harga = harga;
            this.qty = qty;
        }

        public static long subtotal() {
            return harga * qty;
        }
    }

}