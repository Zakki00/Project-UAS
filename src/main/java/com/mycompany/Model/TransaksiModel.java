package com.mycompany.Model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransaksiModel {
    public static final Map<Integer, CartItem> keranjang = new LinkedHashMap<>();
    public static final List<Produk> semuaProduk = new ArrayList<>();
    public static Long subtotal;
    public static Long total;
    public static String metodeBayar = "TUNAI";
    public static int noTrx = 1;

    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));

    // ── Model ─────────────────────────────────────────────
    public static class Produk {
        public String nama, kategori, description, imageUrl;
        public int id, stok, harga;

        public Produk(int id, String nama, int harga, String kategori, int stok, String description, String imageUrl) {
            this.id = id;
            this.nama = nama;
            this.harga = harga;
            this.kategori = kategori;
            this.stok = stok;
            this.description = description;
            this.imageUrl = imageUrl;
        }
    }

    public static class CartItem {

        public Produk produk;
        public int qty;

        public CartItem(Produk p) {
            this.produk = p;
            this.qty = 1;
        }

        public long subtotal() {
            return (long) produk.harga * qty;
        }
    }
}