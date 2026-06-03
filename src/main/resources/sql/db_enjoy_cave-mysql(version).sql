DROP DATABASE db_enjoy_cave;
CREATE DATABASE IF NOT EXISTS db_enjoy_cave;


USE db_enjoy_cave;

CREATE TABLE tb_user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL
);




CREATE TABLE tb_barang(
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga INT(100) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    stok INT NOT NULL,
    deskripsi TEXT,
    image_path VARCHAR(255)
);

CREATE TABLE tb_paket_ps (
    id_paket INT AUTO_INCREMENT PRIMARY KEY,
    nama_paket VARCHAR(100) NOT NULL,
    durasi_jam INT NOT NULL,
    harga DECIMAL(12, 2) NOT NULL,
    deskripsi TEXT,
    status ENUM('aktif', 'nonaktif') DEFAULT 'aktif',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tb_transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    total_pembayaran DECIMAL(10, 2) NOT NULL,
    uang_pembayaran INT(100) NOT NULL,
    kembalian INT(100),
    kekurangan INT(100),
    status_pembayaran VARCHAR(50) NOT NULL,
    tanggal_transaksi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pelanggan VARCHAR(100),
    FOREIGN KEY (id_user) REFERENCES tb_user (id_user)
);



CREATE Table tb_detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,
    id_paket INT NOT NULL,
    jumlah INT NOT NULL,
    harga DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi (id_transaksi),
    FOREIGN KEY (id_barang) REFERENCES tb_barang (id_barang),
    FOREIGN KEY (id_paket) REFERENCES tb_paket_ps (id_paket)
);

INSERT INTO tb_user (username, password,nama_lengkap) VALUES
('admin123', 'admin123', 'endri'),
('user_123', 'user123', 'Caven');



INSERT INTO tb_barang (nama_barang, harga, kategori, stok, deskripsi) VALUES
('Kopi Arabica', 50000, 'Minuman', 100, 'Kopi Arabica berkualitas tinggi dengan cita rasa yang kaya dan aroma yang harum.'),
('Kopi Robusta', 30000, 'Minuman', 150, 'Kopi Robusta dengan rasa yang kuat dan kandungan kafein yang lebih tinggi.'),
('Cappuccino', 45000, 'Minuman', 80, 'Minuman kopi berbasis espresso dengan susu panas dan busa susu yang lembut.'),
('Latte', 40000, 'Minuman', 90, 'Minuman kopi berbasis espresso dengan susu panas dan sedikit busa susu.'),
('Espresso', 35000, 'Minuman', 120, 'Minuman kopi pekat yang disajikan dalam porsi kecil, dengan rasa yang kuat dan konsentrasi tinggi.'),
('Americano', 30000, 'Minuman', 110, 'Minuman kopi yang dibuat dengan menambahkan air panas ke espresso, menghasilkan rasa yang lebih ringan dan lebih banyak cairan.'),
('Mocha', 55000, 'Minuman', 70, 'Minuman kopi berbasis espresso dengan cokelat dan susu panas, memberikan kombinasi rasa kopi dan cokelat yang lezat.'),
('Teh Tarik', 25000, 'Minuman', 100, 'Minuman teh yang disajikan dengan cara ditarik, menghasilkan busa yang lembut dan rasa yang kaya.'),
('Matcha Latte', 60000, 'Minuman', 60, 'Minuman berbasis matcha dengan susu panas, memberikan rasa teh hijau yang khas dan aroma yang menyegarkan.'),
('Kopi Susu', 35000, 'Minuman', 90, 'Minuman kopi yang disajikan dengan susu panas, memberikan kombinasi rasa kopi dan susu yang lezat.'),
('Kopi Gula Aren', 40000, 'Minuman', 80, 'Minuman kopi dengan tambahan gula aren, memberikan rasa manis alami dan aroma yang khas.'),
('Kopi Es Krim', 55000, 'Minuman', 50, 'Minuman kopi dingin dengan tambahan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Vietnam', 45000, 'Minuman', 70, 'Minuman kopi khas Vietnam yang disajikan dengan susu kental manis, memberikan rasa manis dan kaya yang khas.'),
('Kopi Kopi', 30000, 'Minuman', 100, 'Minuman kopi yang disajikan dengan cara tradisional, memberikan rasa kopi yang autentik dan kuat.'),
('Kopi Susu Gula Aren', 40000, 'Minuman', 80, 'Minuman kopi dengan tambahan susu panas dan gula aren, memberikan kombinasi rasa kopi, susu, dan manis alami yang lezat.'),
('Kopi Es Kopi', 55000, 'Minuman', 50, 'Minuman kopi dingin dengan tambahan es batu, memberikan sensasi segar dan rasa kopi yang kuat dalam setiap tegukan.'),
('Kopi Vietnam Es', 45000, 'Minuman', 70, 'Minuman kopi khas Vietnam yang disajikan dengan es batu, memberikan sensasi segar dan rasa kopi yang kaya dalam setiap tegukan.'),
('Kopi Kopi Es', 30000, 'Minuman', 100, 'Minuman kopi dingin yang disajikan dengan es batu, memberikan sensasi segar dan rasa kopi yang kuat dalam setiap tegukan.'),
('Kopi Susu Gula Aren Es', 40000, 'Minuman', 80, 'Minuman kopi dingin dengan tambahan susu panas, gula aren, dan es batu, memberikan kombinasi rasa kopi, susu, manis alami, dan sensasi segar yang lezat.'),
('Kopi Es Kopi Es', 55000, 'Minuman', 50, 'Minuman kopi dingin dengan tambahan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Vietnam Es Kopi', 45000, 'Minuman', 70, 'Minuman kopi khas Vietnam yang disajikan dengan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Kopi Es Kopi', 30000, 'Minuman', 100, 'Minuman kopi dingin dengan tambahan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Susu Gula Aren Es Kopi', 40000, 'Minuman', 80, 'Minuman kopi dingin dengan tambahan susu panas, gula aren, es batu, dan es krim, memberikan kombinasi rasa kopi, susu, manis alami, dan sensasi segar yang lezat.'),
('Kopi Es Kopi Es Kopi', 55000, 'Minuman', 50, 'Minuman kopi dingin dengan tambahan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Vietnam Es Kopi Es', 45000, 'Minuman', 70, 'Minuman kopi khas Vietnam yang disajikan dengan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Kopi Es Kopi Es', 30000, 'Minuman', 100, 'Minuman kopi dingin dengan tambahan es batu dan es krim, memberikan sensasi segar dan manis dalam setiap tegukan.'),
('Kopi Susu Gula Aren Es Kopi Es', 40000, 'Minuman', 80, 'Minuman kopi dingin dengan tambahan susu panas, gula aren, es batu, dan es krim, memberikan kombinasi rasa kopi, susu, manis alami, dan sensasi segar yang lezat.'),
('Kopi Es Kopi Es Kopi Es', 55000, 'Minuman', 50, 'Minuman kopi dingin dengan tambahan es batu dan es krim, memberikan');


SELECT * FROM  tb_transaksi JOIN tb_detail_transaksi ON tb_transaksi.id_transaksi = tb_detail_transaksi.id_transaksi
JOIN tb_barang ON tb_detail_transaksi.id_barang = tb_barang.id_barang WHERE tb_transaksi.status_pembayaran = 'Belum Lunas';




-- // versi sqllite


-- USER
CREATE TABLE tb_user (
    id_user INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nama_lengkap TEXT NOT NULL
);

-- BARANG
CREATE TABLE tb_barang (
    id_barang INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_barang TEXT NOT NULL,
    harga INTEGER NOT NULL,
    kategori TEXT NOT NULL,
    stok INTEGER NOT NULL,
    deskripsi TEXT,
    image_path TEXT
);

-- PAKET PS
CREATE TABLE tb_paket_ps (
    id_paket INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_paket TEXT NOT NULL,
    durasi_jam INTEGER NOT NULL,
    harga REAL NOT NULL,
    deskripsi TEXT,
    status TEXT DEFAULT 'aktif' CHECK (
        status IN ('aktif', 'nonaktif')
    ),
    created_at TEXT DEFAULT(datetime('now','localtime')),
    updated_at TEXT DEFAULT(datetime('now','localtime'))
);

-- TRANSAKSI
CREATE TABLE tb_transaksi (
    id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT,
    id_user INTEGER NOT NULL,
    total_pembayaran REAL NOT NULL,
    uang_pembayaran INTEGER NOT NULL,
    kembalian INTEGER,
    kekurangan INTEGER,
    status_pembayaran TEXT NOT NULL,
    tanggal_transaksi TEXT DEFAULT(datetime('now','localtime')),
    pelanggan TEXT,
    FOREIGN KEY (id_user) REFERENCES tb_user (id_user)
);

-- DETAIL TRANSAKSI
CREATE TABLE tb_detail_transaksi (
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