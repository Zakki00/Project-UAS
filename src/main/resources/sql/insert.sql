-- ============================================
-- DATA USER
-- ============================================
INSERT INTO
    tb_user (
        username,
        password,
        nama_lengkap,
        role,
        email,
        foto_profil
    )
VALUES
    (
        'admin01',
        'admin123',
        'Budi Santoso',
        'Admin',
        'admin@enjoycafe.com',
        'default.png'
    ),
    (
        'kasir01',
        'kasir123',
        'Andi Pratama',
        'Kasir',
        'andi@enjoycafe.com',
        'default.png'
    ),
    (
        'kasir02',
        'kasir123',
        'Siti Rahma',
        'Kasir',
        'siti@enjoycafe.com',
        'default.png'
    );

-- ============================================
-- DATA KARYAWAN
-- ============================================
INSERT INTO
    tb_karyawan (
        id_karyawan,
        id_user,
        username,
        password,
        nama_lengkap,
        jenis_kelamin,
        no_hp,
        tanggal_masuk,
        status_kerja,
        alamat,
        role
    )
VALUES
    (
        'KRY001',
        1,
        'admin01',
        'admin123',
        'Budi Santoso',
        'Laki-laki',
        '081234567890',
        '2025-01-10',
        'Aktif',
        'Surabaya',
        'Admin'
    ),
    (
        'KRY002',
        2,
        'kasir01',
        'kasir123',
        'Andi Pratama',
        'Laki-laki',
        '081298765432',
        '2025-02-15',
        'Aktif',
        'Sidoarjo',
        'Kasir'
    ),
    (
        'KRY003',
        3,
        'kasir02',
        'kasir123',
        'Siti Rahma',
        'Perempuan',
        '082112223333',
        '2025-03-01',
        'Aktif',
        'Gresik',
        'Kasir'
    );

-- ============================================
-- DATA BARANG
-- ============================================
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
        'Mie Goreng',
        12000,
        'Makanan',
        50,
        'Mie goreng spesial',
        'mie.png'
    ),
    (
        'Es Teh',
        5000,
        'Minuman',
        100,
        'Es teh manis',
        'esteh.png'
    ),
    (
        'Kopi Hitam',
        10000,
        'Minuman',
        40,
        'Kopi hitam panas',
        'kopi.png'
    ),
    (
        'Roti Bakar',
        15000,
        'Makanan',
        25,
        'Roti bakar coklat',
        'roti.png'
    ),
    (
        'Air Mineral',
        4000,
        'Minuman',
        80,
        'Botol 600ml',
        'air.png'
    );

-- ============================================
-- DATA TRANSAKSI
-- ============================================
INSERT INTO
    tb_transaksi (
        id_karyawan,
        total_pembayaran,
        uang_pembayaran,
        kembalian,
        kekurangan,
        status_pembayaran,
        tanggal_transaksi,
        pelanggan
    )
VALUES
    (
        'KRY002',
        32000,
        50000,
        18000,
        0,
        'Lunas',
        '2026-06-25 10:15:00',
        'Rizky'
    ),
    (
        'KRY003',
        25000,
        30000,
        5000,
        0,
        'Lunas',
        '2026-06-25 12:30:00',
        'Dimas'
    ),
    (
        'KRY002',
        45000,
        20000,
        0,
        25000,
        'Belum Lunas',
        '2026-06-25 14:00:00',
        'Fajar'
    );

-- ============================================
-- DATA PAKET PS
-- ============================================
INSERT INTO
    tb_paket_ps (id_transaksi, durasi, harga)
VALUES
    (1, 2, 15000),
    (3, 4, 30000);

-- ============================================
-- DETAIL TRANSAKSI
-- ============================================
-- Transaksi 1
INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES
    (1, 1, NULL, 1, 12000),
    (1, 2, NULL, 1, 5000),
    (1, NULL, 1, 1, 15000);

-- Transaksi 2
INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES
    (2, 4, NULL, 1, 15000),
    (2, 3, NULL, 1, 10000);

-- Transaksi 3
INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES
    (3, 1, NULL, 1, 12000),
    (3, 2, NULL, 1, 3000),
    (3, NULL, 2, 1, 30000);

-- ============================================
-- DATA ABSENSI
-- ============================================
INSERT INTO
    tb_absensi (id_karyawan, tanggal, jam_masuk, status_kehadiran)
VALUES
    ('KRY001', '2026-06-25', '08:00:00', 'Hadir'),
    ('KRY002', '2026-06-25', '08:05:00', 'Hadir'),
    ('KRY003', '2026-06-25', '08:10:00', 'Hadir');