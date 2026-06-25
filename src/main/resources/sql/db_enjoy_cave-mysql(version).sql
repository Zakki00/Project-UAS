-- Active: 1782312655417@@127.0.0.1@3306@db_enjoy_cave
-- ============================================
-- HAPUS DATABASE LAMA (Opsional)
-- ============================================
-- ============================================
-- HAPUS DATABASE JIKA SUDAH ADA
-- ============================================
DROP DATABASE IF EXISTS db_enjoy_cave;

-- ============================================
-- BUAT DATABASE
-- ============================================
CREATE DATABASE db_enjoy_cave CHARACTER
SET
    utf8mb4 COLLATE utf8mb4_unicode_ci;

USE db_enjoy_cave;
USE db_enjoy_cave;

-- ============================================
-- TABEL USER
-- ============================================
CREATE TABLE tb_user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL,
    foto_profil VARCHAR(255) NOT NULL
) ENGINE = InnoDB;

-- ============================================
-- TABEL KARYAWAN
-- ============================================
CREATE TABLE tb_karyawan (
    id_karyawan VARCHAR(20) PRIMARY KEY,
    id_user INT NOT NULL UNIQUE,
    nama_lengkap VARCHAR(100) NOT NULL,
    jenis_kelamin ENUM('Laki-laki', 'Perempuan') NOT NULL,
    no_hp VARCHAR(20) NOT NULL,
    tanggal_masuk DATE NOT NULL,
    status_kerja ENUM('Aktif', 'Non Aktif') NOT NULL,
    alamat TEXT,
    role VARCHAR(30) NOT NULL,
    CONSTRAINT fk_karyawan_user FOREIGN KEY (id_user) REFERENCES tb_user (id_user)
) ENGINE = InnoDB;

-- ============================================
-- TABEL BARANG
-- ============================================
CREATE TABLE tb_barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga INT NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    stok INT NOT NULL,
    deskripsi TEXT,
    image_path VARCHAR(255)
) ENGINE = InnoDB;

-- ============================================
-- TABEL TRANSAKSI
-- ============================================
CREATE TABLE tb_transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan VARCHAR(20) NOT NULL,
    total_pembayaran DECIMAL(12, 2) NOT NULL,
    uang_pembayaran INT NOT NULL,
    kembalian INT DEFAULT NULL,
    kekurangan INT DEFAULT NULL,
    status_pembayaran VARCHAR(30) NOT NULL,
    tanggal_transaksi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pelanggan VARCHAR(100),
    CONSTRAINT fk_transaksi_karyawan FOREIGN KEY (id_karyawan) REFERENCES tb_karyawan (id_karyawan)
) ENGINE = InnoDB;

-- ============================================
-- TABEL PAKET PS
-- ============================================
CREATE TABLE tb_paket_ps (
    id_paket_ps INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    durasi INT NOT NULL,
    harga DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_paket_transaksi FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi) ON DELETE CASCADE
) ENGINE = InnoDB;

-- ============================================
-- TABEL DETAIL TRANSAKSI
-- ============================================
CREATE TABLE tb_detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT DEFAULT NULL,
    id_paket_ps INT DEFAULT NULL,
    jumlah INT NOT NULL,
    harga DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_detail_transaksi FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi) ON DELETE CASCADE,
    CONSTRAINT fk_detail_barang FOREIGN KEY (id_barang) REFERENCES tb_barang (id_barang) ON DELETE SET NULL,
    CONSTRAINT fk_detail_paket FOREIGN KEY (id_paket_ps) REFERENCES tb_paket_ps (id_paket_ps) ON DELETE SET NULL
) ENGINE = InnoDB;

-- ============================================
-- TABEL ABSENSI
-- ============================================
CREATE TABLE tb_absensi (
    id_absensi INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan VARCHAR(20) NOT NULL,
    tanggal DATE NOT NULL,
    jam_masuk TIME NOT NULL,
    status_kehadiran VARCHAR(30) NOT NULL,
    CONSTRAINT fk_absensi_karyawan FOREIGN KEY (id_karyawan) REFERENCES tb_karyawan (id_karyawan) ON DELETE CASCADE
) ENGINE = InnoDB;