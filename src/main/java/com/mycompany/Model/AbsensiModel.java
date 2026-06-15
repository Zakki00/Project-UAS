package com.mycompany.Model;

import javafx.beans.property.*;

public class AbsensiModel {

    private final IntegerProperty idAbsensi;
    private final StringProperty idKaryawan;
    private final StringProperty namaKaryawan;
    private final StringProperty jabatan;
    private final StringProperty tanggal;
    private final StringProperty shiftMasuk;
    private final StringProperty statusKehadiran;

    public AbsensiModel(int idAbsensi, String idKaryawan, String namaKaryawan,
            String jabatan, String tanggal, String shiftMasuk,
            String statusKehadiran) {
        this.idAbsensi = new SimpleIntegerProperty(idAbsensi);
        this.idKaryawan = new SimpleStringProperty(idKaryawan);
        this.namaKaryawan = new SimpleStringProperty(namaKaryawan);
        this.jabatan = new SimpleStringProperty(jabatan);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.shiftMasuk = new SimpleStringProperty(shiftMasuk);
        this.statusKehadiran = new SimpleStringProperty(statusKehadiran);
    }

    // ── idAbsensi ─────────────────────────────────────────
    public IntegerProperty idAbsensiProperty() {
        return idAbsensi;
    }

    public int getIdAbsensi() {
        return idAbsensi.get();
    }

    public void setIdAbsensi(int v) {
        idAbsensi.set(v);
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

    // ── tanggal ───────────────────────────────────────────
    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public void setTanggal(String v) {
        tanggal.set(v);
    }

    // ── shiftMasuk ────────────────────────────────────────
    public StringProperty shiftMasukProperty() {
        return shiftMasuk;
    }

    public String getShiftMasuk() {
        return shiftMasuk.get();
    }

    public void setShiftMasuk(String v) {
        shiftMasuk.set(v);
    }

    // ── statusKehadiran ───────────────────────────────────
    public StringProperty statusKehadiranProperty() {
        return statusKehadiran;
    }

    public String getStatusKehadiran() {
        return statusKehadiran.get();
    }

    public void setStatusKehadiran(String v) {
        statusKehadiran.set(v);
    }
}