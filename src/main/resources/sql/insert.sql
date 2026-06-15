-- TRANSAKSI PAGI

INSERT INTO
    tb_transaksi (
        id_user,
        total_pembayaran,
        uang_pembayaran,
        kembalian,
        kekurangan,
        status_pembayaran,
        tanggal_transaksi,
        pelanggan
    )
VALUES (
        2,
        25000,
        30000,
        5000,
        0,
        'Lunas',
        '2026-06-13 07:15:00',
        'Andi'
    ),
    (
        2,
        40000,
        50000,
        10000,
        0,
        'Lunas',
        '2026-06-13 08:30:00',
        'Budi'
    ),
    (
        2,
        35000,
        50000,
        15000,
        0,
        'Lunas',
        '2026-06-13 10:45:00',
        'Citra'
    );
    INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES (1, 1, NULL, 2, 10000),
    (1, 2, NULL, 1, 5000),
    (2, 1, NULL, 3, 10000),
    (2, 3, NULL, 2, 5000),
    (3, 2, NULL, 1, 5000);
    INSERT INTO
    tb_paket_ps (id_transaksi, durasi, harga)
VALUES (2, 2, 20000),
    (3, 3, 30000);
    INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES (2, NULL, 1, 1, 20000),
    (3, NULL, 2, 1, 30000);


SELECT * FROM tb_paket_ps;



SELECT ps.id_paket_ps, ps.id_transaksi, ps.durasi, ps.harga, t.tanggal_transaksi
FROM
    tb_paket_ps ps
    JOIN tb_transaksi t ON ps.id_transaksi = t.id_transaksi
WHERE
    DATE(t.tanggal_transaksi) = DATE('now', 'localtime')
    AND TIME(t.tanggal_transaksi) BETWEEN '06:00:00' AND '12:00:00';

SELECT COUNT(*) AS total_paket_ps
FROM
    tb_paket_ps ps
    JOIN tb_transaksi t ON ps.id_transaksi = t.id_transaksi
WHERE
    DATE(t.tanggal_transaksi) = DATE('now', 'localtime')
    AND TIME(t.tanggal_transaksi) BETWEEN '06:00:00' AND '12:00:00';

SELECT
    id_transaksi,
    tanggal_transaksi
FROM tb_transaksi
ORDER BY id_transaksi DESC;




SELECT t.id_transaksi, t.tanggal_transaksi, dt.id_detail, ps.id_paket_ps
FROM
    tb_transaksi t
    LEFT JOIN tb_detail_transaksi dt ON t.id_transaksi = dt.id_transaksi
    LEFT JOIN tb_paket_ps ps ON t.id_transaksi = ps.id_transaksi
WHERE
    DATE(t.tanggal_transaksi) = DATE('now', 'localtime')
    AND TIME(t.tanggal_transaksi) BETWEEN '06:00:00' AND '12:00:00';



SELECT * FROM tb_paket_ps WHERE id_transaksi IN (70, 71, 72);

INSERT INTO
    tb_paket_ps (id_transaksi, durasi, harga)
VALUES (71, 2, 30000);

SELECT * FROM tb_paket_ps WHERE id_transaksi = 71;

    INSERT INTO
    tb_transaksi (
        id_user,
        total_pembayaran,
        uang_pembayaran,
        kembalian,
        kekurangan,
        status_pembayaran,
        tanggal_transaksi,
        pelanggan
    )
VALUES (
        2,
        15000,
        20000,
        5000,
        0,
        'Lunas',
        '2026-06-13 08:15:00',
        'Rizky'
    ),
    (
        2,
        30000,
        50000,
        20000,
        0,
        'Lunas',
        '2026-06-13 09:45:00',
        'Fajar'
    );


    INSERT INTO
    tb_paket_ps (id_transaksi, durasi, harga)
VALUES (4, 1, 15000),
    (5, 2, 30000);

    INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES (4, NULL, 1, 1, 15000),
    (5, NULL, 2, 1, 30000);

    INSERT INTO
    tb_detail_transaksi (
        id_transaksi,
        id_barang,
        id_paket_ps,
        jumlah,
        harga
    )
VALUES (4, NULL, 1, 1, 15000),
    (5, NULL, 2, 1, 30000);