package com.mycompany.Model;

import javafx.beans.property.*;

public class KaryawanModel {

    private final StringProperty idKaryawan;
    private final StringProperty idUser;
    private final StringProperty username;
    private final StringProperty password;

    private final StringProperty namaLengkap;
    private final StringProperty jenisKelamin;

    private final StringProperty noHp;
    private final StringProperty tanggalMasuk;
    private final StringProperty statusKerja;
    private final StringProperty alamat;
    private final StringProperty role;

    public KaryawanModel(
            String idKaryawan,
            String idUser,
            String username,
            String password,
            String namaLengkap,
            String jenisKelamin,
            String noHp,
            String tanggalMasuk,
            String statusKerja,
            String alamat,
            String role) {
        this.idKaryawan = new SimpleStringProperty(idKaryawan);
        this.idUser = new SimpleStringProperty(idUser);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.jenisKelamin = new SimpleStringProperty(jenisKelamin);
        this.noHp = new SimpleStringProperty(noHp);
        this.tanggalMasuk = new SimpleStringProperty(tanggalMasuk);
        this.statusKerja = new SimpleStringProperty(statusKerja);
        this.alamat = new SimpleStringProperty(alamat);
        this.role = new SimpleStringProperty(role);
    }

    public KaryawanModel(
            String idKaryawan,
            String username,
            String password,
            String namaLengkap,
            String jenisKelamin,
            String noHp,
            String tanggalMasuk,
            String statusKerja,
            String alamat,
            String role) {
        this(idKaryawan, "", username, password, namaLengkap, jenisKelamin, noHp, tanggalMasuk, statusKerja, alamat,
                role);
    }

    // ── ID ─────────────────────────────
    public StringProperty idKaryawanProperty() {
        return idKaryawan;
    }

    public String getIdKaryawan() {
        return idKaryawan.get();
    }

    public void setIdKaryawan(String v) {
        idKaryawan.set(v);
    }

    // ── ID USER ─────────────────────────
    public StringProperty idUserProperty() {
        return idUser;
    }

    public String getIdUser() {
        return idUser.get();
    }

    public void setIdUser(String v) {
        idUser.set(v);
    }

    // ── USERNAME ───────────────────────
    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String v) {
        username.set(v);
    }

    // ── PASSWORD ───────────────────────
    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String v) {
        password.set(v);
    }

    // ── NAMA ───────────────────────────
    public StringProperty namaLengkapProperty() {
        return namaLengkap;
    }

    public String getNamaLengkap() {
        return namaLengkap.get();
    }

    public void setNamaLengkap(String v) {
        namaLengkap.set(v);
    }

    // ── JENIS KELAMIN ──────────────────
    public StringProperty jenisKelaminProperty() {
        return jenisKelamin;
    }

    public String getJenisKelamin() {
        return jenisKelamin.get();
    }

    public void setJenisKelamin(String v) {
        jenisKelamin.set(v);
    }

    // ── NO HP ──────────────────────────
    public StringProperty noHpProperty() {
        return noHp;
    }

    public String getNoHp() {
        return noHp.get();
    }

    public void setNoHp(String v) {
        noHp.set(v);
    }

    // ── TANGGAL MASUK ──────────────────
    public StringProperty tanggalMasukProperty() {
        return tanggalMasuk;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk.get();
    }

    public void setTanggalMasuk(String v) {
        tanggalMasuk.set(v);
    }

    // ── STATUS ─────────────────────────
    public StringProperty statusKerjaProperty() {
        return statusKerja;
    }

    public String getStatusKerja() {
        return statusKerja.get();
    }

    public void setStatusKerja(String v) {
        statusKerja.set(v);
    }

    // ── ALAMAT ─────────────────────────
    public StringProperty alamatProperty() {
        return alamat;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String v) {
        alamat.set(v);
    }

    // ── ROLE ───────────────────────────
    public StringProperty roleProperty() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String v) {
        role.set(v);
    }
}