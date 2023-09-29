package org.kinamulen.view;

import org.kinamulen.model.Menu;
import org.kinamulen.model.Order;
import org.kinamulen.model.Variant;

import java.util.List;
import java.util.Map;

import static org.kinamulen.utils.Constant.*;
import static org.kinamulen.utils.Format.*;

public class MenuView {
    public void priceList(Map<Integer, Menu> menus) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(LINE_THIN)
                .append("\n         D A F T A R    M E N U         \n")
                .append(LINE_THIN)
                .append("\nSilahkan pilih :");
        int k = 0;
        for (Map.Entry<Integer, Menu> i:menus.entrySet()) {
            sb.append("\n")
                    .append(padLeft(Integer.toString(k + 1), 2))
                    .append(". ").append(padRight(i.getValue().getName(), 20))
                    .append(" | ").append(padLeft(formatRupiah(i.getValue().getPrice()), 13));
            k = k+1;
        }
        sb.append("\n99. Pesan dan Bayar\n 0. Keluar aplikasi\n").append(LINE_BOLD);
        System.out.println(sb);
    }

    public void viewVariant(Map<Integer, Variant> variant){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(LINE_THIN)
                .append("\nPilih varian menu \n")
                .append(LINE_THIN)
                .append("\n    Varian              Additional Price");
        int k = 0;
        for (Map.Entry<Integer, Variant> i:variant.entrySet()) {
            sb.append("\n")
                    .append(padLeft(Integer.toString(k + 1), 2))
                    .append(". ").append(padRight(i.getValue().getName(), 20))
                    .append(" | ").append(padLeft("+" + formatRupiah(i.getValue().getAdditionalPrice()), 13));
            k = k+1;
        }
        sb.append("\n98. Kembali\n 0. Keluar aplikasi\n");
        System.out.println(sb);
    }
    public void orderSummary(List<Order> orders){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(LINE_BOLD)
                .append("\n-         KONFIRMASI PESANAN           -\n").append(LINE_BOLD)
                .append("\n" + padRight("MENU",20) + " " + padLeft("QTY",5) + " " + padLeft("TOTAL",12));

        int totalQuantity = 0;
        int totalPrice = 0;

        for (Order i:orders) {
            sb.append("\n")
                    .append(padRight(i.getName(),20)).append(" ")
                    .append(padLeft(Integer.toString(i.getQuantity()),5)).append(" ")
                    .append(padLeft(formatRupiah(i.getPrice()),12)).append(" ");

            if (i.getVariant1() != null){
                sb.append("\n").append("- ").append(i.getVariant1());
            }
            if (i.getVariant2() != null){
                sb.append("\n").append("- ").append(i.getVariant2());
            }
            totalQuantity = totalQuantity + i.getQuantity();
            totalPrice = totalPrice + i.getPrice();
        }

        sb.append("\n---------------------------------------- +\n").append("                    ")
                .append(padLeft(Integer.toString(totalQuantity), 6))
                .append(padLeft(formatRupiah(totalPrice), 13))
                .append("\n1. Bayar\n2. Kembali\n3. Keluar Aplikasi");

        System.out.println(sb);
    }
    public StringBuilder printReceipt(List<Order> orders){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(LINE_BOLD)
                .append("\n~BINARFUD~BINARFUD~BINARFUD~BINARFUD~BIN\n").append(LINE_BOLD)
                .append("\n         selamat menikmati -Aol         \n").append(LINE_DOT)
                .append("\n" + padRight("MENU",20) + " " + padLeft("QTY",5) + " " + padLeft("TOTAL",12));

        int totalQuantity = 0;
        int totalPrice = 0;

        for (Order i:orders) {
            sb.append("\n")
                    .append(padRight(i.getName(),20)).append(" ")
                    .append(padLeft(Integer.toString(i.getQuantity()),5)).append(" ")
                    .append(padLeft(formatRupiah(i.getPrice()),12)).append(" ");

            if (i.getVariant1() != null){
                sb.append("\n").append("- ").append(i.getVariant1());
            }
            if (i.getVariant2() != null){
                sb.append("\n").append("- ").append(i.getVariant2());
            }
            totalQuantity = totalQuantity + i.getQuantity();
            totalPrice = totalPrice + i.getPrice();
        }

        sb.append("\n---------------------------------------- +\n").append("                    ")
                .append(padLeft(Integer.toString(totalQuantity), 6))
                .append(padLeft(formatRupiah(totalPrice), 13));

        sb.append("\n\nSimpan struk ini untuk tanda bayar").append("\n").append(LINE_BOLD)
                .append("\n~BINARFUD~BINARFUD~BINARFUD~BINARFUD~BIN\n").append(LINE_BOLD);

        System.out.println(sb);
        return sb;
    }

}
