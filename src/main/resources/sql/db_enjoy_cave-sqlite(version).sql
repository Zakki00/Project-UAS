-- Active: 1780404766479@@127.0.0.1@3306

SELECT * FROM tb_detail_transaksi JOIN tb_transaksi ON tb_detail_transaksi.id_transaksi = tb_transaksi.id_transaksi ;

DROP DATABASEs IF EXISTS db_enjoy_cafe;
CREATE DATABASE db_enjoy_cave;

-- USER
CREATE TABLE
    tb_user (
        id_user INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL,
        nama_lengkap TEXT NOT NULL
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
    );


    -- =====================
    -- input data awal

-- =====================
-- USER
-- =====================
INSERT INTO
    tb_user (username, password, nama_lengkap)
VALUES
    ('admin', 'admin123', 'Administrator Utama'),
    ('rizky', '12345', 'Rizky Pratama');

-- =====================
-- BARANG
-- =====================
INSERT INTO
    tb_barang (
        nama_barang,
        harga,
        kategori,
        stok,
        deskripsi,
        image_path
    )
VALUES
    (
        'Snack Chitato',
        12000,
        'Makanan',
        50,
        'Keripik kentang rasa sapi panggang',
        NULL
    ),
    (
        'Aqua 600ml',
        5000,
        'Minuman',
        100,
        'Air mineral botol',
        NULL
    );

-- =====================
-- PAKET PS
-- =====================
INSERT INTO
    tb_paket_ps (nama_paket, durasi_jam, harga, deskripsi, status)
VALUES
    (
        'Paket Hemat PS2 Jam',
        2,
        10000,
        'Main PS selama 2 jam',
        'aktif'
    ),
    (
        'Paket Malam PS5 Jam',
        5,
        22000,
        'Cocok untuk main lama',
        'aktif'
    );

-- =====================
-- TRANSAKSI
-- =====================
INSERT INTO
    tb_transaksi (
        id_user,
        total_pembayaran,
        uang_pembayaran,
        kembalian,
        kekurangan,
        status_pembayaran,
        pelanggan
    )
VALUES
    (1, 22000, 25000, 3000, 0, 'lunas', 'Andi'),
    (2, 15000, 10000, 0, 5000, 'Belum Lunas', 'Budi');

-- =====================
-- DETAIL TRANSAKSI
-- =====================
-- Transaksi 1 (Andi)
INSERT INTO
    tb_detail_transaksi (id_transaksi, id_barang, id_paket, jumlah, harga)
VALUES
    (1, NULL, 2, 1, 22000);

-- Transaksi 2 (Budi)
INSERT INTO
    tb_detail_transaksi (id_transaksi, id_barang, id_paket, jumlah, harga)
VALUES
    (2, 1, NULL, 1, 12000);