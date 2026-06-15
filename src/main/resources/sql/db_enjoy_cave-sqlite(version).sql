-- Active: 1781220730953@@127.0.0.1@3306

SELECT * FROM tb_user;

DROP TABLE IF EXISTS tb_detail_transaksi;

DROP TABLE IF EXISTS tb_transaksi;

DROP TABLE IF EXISTS tb_barang;
DROP TABLE IF EXISTS tb_paket_ps;

DROP TABLE IF EXISTS tb_user;
DROP Table if EXISTS tb_karyawan;
DROP TABLE if EXISTS tb_absensi;

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
CREATE TABLE tb_paket_ps (
    id_paket_ps INTEGER PRIMARY KEY AUTOINCREMENT,
    id_transaksi INTEGER NOT NULL,
    durasi INTEGER NOT NULL,
    harga REAL NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi)
);

-- DETAIL TRANSAKSI
CREATE TABLE
    tb_detail_transaksi (
        id_detail INTEGER PRIMARY KEY AUTOINCREMENT,
        id_transaksi INTEGER NOT NULL,
        id_barang INTEGER,
        id_paket_ps INTEGER,
        jumlah INTEGER NOT NULL,
        harga REAL NOT NULL,
        FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi),
        FOREIGN KEY (id_barang) REFERENCES tb_barang (id_barang),
        FOREIGN KEY (id_paket_ps) REFERENCES tb_paket_ps (id_paket_ps)
    )

 CREATE TABLE tb_karyawan (
    id_karyawan TEXT PRIMARY KEY,
    nama_karyawan TEXT NOT NULL,
    jenis_kelamin TEXT NOT NULL CHECK (
        jenis_kelamin IN ('Laki-laki', 'Perempuan')
    ),
    jabatan TEXT NOT NULL,
    no_hp TEXT NOT NULL,
    tanggal_masuk TEXT NOT NULL,
    status_kerja TEXT NOT NULL CHECK (
        status_kerja IN ('Aktif', 'Non Aktif')
    ),
    alamat TEXT
);

CREATE TABLE tb_absensi (
    id_absensi INTEGER PRIMARY KEY AUTOINCREMENT,
    id_karyawan TEXT NOT NULL,
    tanggal TEXT NOT NULL,
    jam_masuk TEXT NOT NULL,
    status_kehadiran TEXT NOT NULL,
    FOREIGN KEY (id_karyawan) REFERENCES tb_karyawan (id_karyawan) ON DELETE CASCADE
);
