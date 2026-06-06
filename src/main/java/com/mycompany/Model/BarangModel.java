package com.mycompany.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BarangModel {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nama;
    private final SimpleStringProperty kategori;
    private final SimpleIntegerProperty harga;
    private final SimpleIntegerProperty stok;
    private final SimpleStringProperty deskripsi;
    private final SimpleStringProperty gambar;

    public BarangModel(int id, String nama, String kategori, int harga, int stok, String deskripsi, String gambar) {
        this.id = new SimpleIntegerProperty(id);
        this.nama = new SimpleStringProperty(nama);
        this.kategori = new SimpleStringProperty(kategori);
        this.harga = new SimpleIntegerProperty(harga);
        this.stok = new SimpleIntegerProperty(stok);
        this.deskripsi = new SimpleStringProperty(deskripsi);
        this.gambar = new SimpleStringProperty(gambar);
    }

    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }

    public String getNama() { return nama.get(); }
    public void setNama(String v) { this.nama.set(v); }
    public SimpleStringProperty namaProperty() { return nama; }

    public String getKategori() { return kategori.get(); }
    public void setKategori(String v) { this.kategori.set(v); }
    public SimpleStringProperty kategoriProperty() { return kategori; }

    public int getHarga() { return harga.get(); }
    public void setHarga(int v) { this.harga.set(v); }
    public SimpleIntegerProperty hargaProperty() { return harga; }

    public int getStok() { return stok.get(); }
    public void setStok(int v) { this.stok.set(v); }
    public SimpleIntegerProperty stokProperty() { return stok; }

    public String getDeskripsi() { return deskripsi.get(); }
    public void setDeskripsi(String v) { this.deskripsi.set(v); }
    public SimpleStringProperty deskripsiProperty() { return deskripsi; }

    public String getGambar() { return gambar.get(); }
    public void setGambar(String v) { this.gambar.set(v); }
    public SimpleStringProperty gambarProperty() { return gambar; }
}
