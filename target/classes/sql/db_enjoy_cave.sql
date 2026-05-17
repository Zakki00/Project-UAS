CREATE DATABASE IF NOT EXISTS db_enjoy_cave;
USE db_enjoy_cave;

CREATE TABLE tb_user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


CREATE TABLE tb_transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    tanggal_transaksi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES tb_user(id_user)
);
CREATE Table tb_detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,
    jumlah INT NOT NULL,
    harga DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi),
    FOREIGN KEY (id_barang) REFERENCES barang(id_barang)
);

CREATE TABLE tb_barang(
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga DECIMAL(10, 2) NOT NULL,
    stok INT NOT NULL
);

CREATE TABLE tb_menu(
    id_menu INT AUTO_INCREMENT PRIMARY KEY,
    nama_menu VARCHAR(100) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    harga DECIMAL(10, 2) NOT NULL,
    stok INT NOT NULL,
    deskripsi TEXT
);
