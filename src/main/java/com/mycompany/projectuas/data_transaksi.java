package com.mycompany.projectuas;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mycompany.projectuas.TransaksiController.CartItem;

public class data_transaksi {

    public static Long subtotal;
    public static Long total;
    public static final Map<Integer, CartItem> keranjang = new LinkedHashMap<>();
}
