package com.mycompany.Model;

import javafx.beans.property.*;

public class KaryawanModel {

    private final StringProperty idKaryawan;
    private final StringProperty namaKaryawan;
    private final StringProperty jenisKelamin;
    private final StringProperty jabatan;

    private final StringProperty noHp;
    private final StringProperty tanggalMasuk;
    private final StringProperty statusKerja;
    private final StringProperty alamat;

    public KaryawanModel(String idKaryawan, String namaKaryawan, String jenisKelamin,
            String jabatan, String noHp, String tanggalMasuk,
            String statusKerja, String alamat) {
        this.idKaryawan = new SimpleStringProperty(idKaryawan);
        this.namaKaryawan = new SimpleStringProperty(namaKaryawan);
        this.jenisKelamin = new SimpleStringProperty(jenisKelamin);
        this.jabatan = new SimpleStringProperty(jabatan);
        this.noHp = new SimpleStringProperty(noHp);
        this.tanggalMasuk = new SimpleStringProperty(tanggalMasuk);
        this.statusKerja = new SimpleStringProperty(statusKerja);
        this.alamat = new SimpleStringProperty(alamat);
    }

    // ── idKaryawan ────────────────────────────────────────
    public StringProperty idKaryawanProperty() {
        return idKaryawan;
    }

    public String getIdKaryawan() {
        return idKaryawan.get();
    }

    public void setIdKaryawan(String v) {
        idKaryawan.set(v);
    }

    // ── namaKaryawan ──────────────────────────────────────
    public StringProperty namaKaryawanProperty() {
        return namaKaryawan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan.get();
    }

    public void setNamaKaryawan(String v) {
        namaKaryawan.set(v);
    }

    // ── jenisKelamin ──────────────────────────────────────
    public StringProperty jenisKelaminProperty() {
        return jenisKelamin;
    }

    public String getJenisKelamin() {
        return jenisKelamin.get();
    }

    public void setJenisKelamin(String v) {
        jenisKelamin.set(v);
    }

    // ── jabatan ───────────────────────────────────────────
    public StringProperty jabatanProperty() {
        return jabatan;
    }

    public String getJabatan() {
        return jabatan.get();
    }

    public void setJabatan(String v) {
        jabatan.set(v);
    }

    // ── noHp ─────────────────────────────────────────────
    public StringProperty noHpProperty() {
        return noHp;
    }

    public String getNoHp() {
        return noHp.get();
    }

    public void setNoHp(String v) {
        noHp.set(v);
    }

    // ── tanggalMasuk ──────────────────────────────────────
    public StringProperty tanggalMasukProperty() {
        return tanggalMasuk;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk.get();
    }

    public void setTanggalMasuk(String v) {
        tanggalMasuk.set(v);
    }

    // ── statusKerja ───────────────────────────────────────
    public StringProperty statusKerjaProperty() {
        return statusKerja;
    }

    public String getStatusKerja() {
        return statusKerja.get();
    }

    public void setStatusKerja(String v) {
        statusKerja.set(v);
    }

    // ── alamat ────────────────────────────────────────────
    public StringProperty alamatProperty() {
        return alamat;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String v) {
        alamat.set(v);
    }
}