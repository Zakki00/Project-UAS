package com.mycompany.projectuas;

import java.util.List;

public class test {

    public static void main(String[] args) {

        String sql = "SELECT * FROM tb_transaksi ORDER BY id_transaksi DESC LIMIT 1";

        List<Object[]> data = koneksi.ambilData(sql);

        for (Object[] row : data) {
            int id_transaski = ((Number) row[0]).intValue();
            int id_user = ((Number) row[1]).intValue();

            int poin = ((Number) row[2]).intValue();

            System.out.println("ID: " + id_transaski);
            System.out.println("Nama Hadiah: " + id_user);
            System.out.println("Poin: " + poin);
            System.out.println("-------------------");
        }
    }

    // safe conversion from Object[] to typed fields
    // Object idObj = row[0];
    // String idTransaksi = idObj == null ? "" : idObj.toString();

    // Object namaObj = row[1];
    // String namaPelanggan = namaObj == null ? "" : namaObj.toString();

    // Number kekuranganNum = (row[2] instanceof Number) ? (Number) row[2] : null;
    // long kekurangan = kekuranganNum == null ? 0L : kekuranganNum.longValue();

    // Number totalNum = (row[3] instanceof Number) ? (Number) row[3] : null;
    // Long total_pembayaran = totalNum == null ? 0L : totalNum.longValue();

    // Object statusObj = row[4];
    // String status_pembayaran = statusObj == null ? "" : statusObj.toString();

    // Object tanggalObj = row[5];
    // String tanggal_transaksi = tanggalObj == null ? "" : tanggalObj.toString();

    // Object namaBarangObj = row[6];
    // String nama_barang = namaBarangObj == null ? "" : namaBarangObj.toString();

    // Number hargaNum = (row[7] instanceof Number) ? (Number) row[7] : null;
    // long harga_barang = hargaNum == null ? 0L : hargaNum.longValue();

    // Number qtyNum = (row[8] instanceof Number) ? (Number) row[8] : null;
    // int qty = qtyNum == null ? 0 : qtyNum.intValue();
}