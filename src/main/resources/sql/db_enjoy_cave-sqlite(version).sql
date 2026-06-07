-- Active: 1780404766479@@127.0.0.1@3306

SELECT * FROM tb_user;

DROP TABLE IF EXISTS tb_detail_transaksi;

DROP TABLE IF EXISTS tb_transaksi;

DROP TABLE IF EXISTS tb_barang;
DROP TABLE IF EXISTS tb_paket_ps;

DROP TABLE IF EXISTS tb_user;

CREATE TABLE
    tb_user (
        id_user INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL,
        nama_lengkap TEXT NOT NULL,
        role TEXT NOT NULL,
        email TEXT
    );

-- BARANG
CREATE TABLE
    tb_barang (
        id_barang INTEGER PRIMARY KEY AUTOINCREMENT,
        nama_barang TEXT NOT NULL,
        harga INTEGER NOT NULL,
        kategori TEXT NOT NULL,
        stok INTEGER NOT NULL,
        deskripsi TEXT,
        image_path TEXT
    );

-- PAKET PS
CREATE TABLE
    tb_paket_ps (
        id_paket INTEGER PRIMARY KEY AUTOINCREMENT,
        nama_paket TEXT NOT NULL,
        durasi_jam INTEGER NOT NULL,
        harga REAL NOT NULL,
        deskripsi TEXT,
        status TEXT DEFAULT 'aktif' CHECK (status IN ('aktif', 'nonaktif')),
        created_at TEXT DEFAULT (datetime ('now')),
        updated_at TEXT DEFAULT (datetime ('now'))
    );

-- TRANSAKSI
CREATE TABLE
    tb_transaksi (
        id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT,
        id_user INTEGER NOT NULL,
        total_pembayaran REAL NOT NULL,
        uang_pembayaran INTEGER NOT NULL,
        kembalian INTEGER,
        kekurangan INTEGER,
        status_pembayaran TEXT NOT NULL,
        tanggal_transaksi TEXT DEFAULT (datetime ('now')),
        pelanggan TEXT,
        FOREIGN KEY (id_user) REFERENCES tb_user (id_user)
    );

-- DETAIL TRANSAKSI
CREATE TABLE
    tb_detail_transaksi (
        id_detail INTEGER PRIMARY KEY AUTOINCREMENT,
        id_transaksi INTEGER NOT NULL,
        id_barang INTEGER,
        id_paket INTEGER,
        jumlah INTEGER NOT NULL,
        harga REAL NOT NULL,
        FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi),
        FOREIGN KEY (id_barang) REFERENCES tb_barang (id_barang),
        FOREIGN KEY (id_paket) REFERENCES tb_paket_ps (id_paket)
    )