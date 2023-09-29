package org.kinamulen;

import java.io.*;
import java.util.ArrayList;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Locale;

import java.util.Scanner;

import java.sql.Timestamp;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Menu> menus = new ArrayList<>();

        /*Daftar makanan dan minuman kita buat dalam Class 'Menu', dan harus ada
          atribut "name" dan "price"*/
        menus.add(new Menu("Nasi Goreng",15000));
        menus.add(new Menu("Mie Goreng",13000));
        menus.add(new Menu("Nasi + Ayam",18000));
        menus.add(new Menu("Es Teh Manis",3000));
        menus.add(new Menu("Es Jeruk",5000));

        //'lebar' kertas 40 karakter, alasan estetika aja ini
        System.out.println("========================================");
        System.out.println("    Selamat datang di BinarFud ^w^      ");

        int choice = 100; //set diluar case lain
        while (!(choice == 0)) {
            printPriceList(menus);
            System.out.print("\nPilihan => ");
            choice = input.nextInt();

            if (choice == 0) { //KELUAR APLIKASI
                System.out.println("\n\n\n\n========================================");
                System.out.println("             Bye, Binarian!             ");
                System.out.println("========================================");
            } else if (choice <= menus.size()) { //PILIH MAKANAN
                System.out.println("\n\n\n\n........................................");
                System.out.println("        Ingin pesan berapa porsi?       ");
                System.out.println("........................................\n");

                //Karena indeks di ArrayList<Menu> mulai dari 0, untuk aksesnya kita -1 dulu
                int currentChoice = choice-1;
                System.out.println(padRight(menus.get(currentChoice).getName(),20) + " | " + padLeft(formatRupiah(menus.get(currentChoice).getPrice()),13));

                //Setelah itu, user akan mengisi qty menu terpilih
                System.out.println("(Pilih 0 untuk kembali)\n");
                System.out.print("Qty (porsi) => ");
                int currentItemQuantity = input.nextInt();
                if (currentItemQuantity == 0) { //KEMBALI
                } else if (currentItemQuantity>0) { //MENAMBAH PESANAN
                    quantityConfirmation(menus.get(currentChoice),currentItemQuantity);
//                } else { //currentItemQuantity bisa bernilai negatif -> bisa jadi fitur menghapus menu :D
//                    System.out.println("yang bener dong.. masa jual makanan :(");
                }
            } else if (choice == 99) { //KONFIRMASI
                System.out.println("\n\n\n\n........................................");
                System.out.println("   Silahkan cek kembali pesanan Anda!   ");
                System.out.println("........................................");
                orderConfirmation(menus);
                System.out.println("\n1. Bayar dan pesan\n2. Kembali ke menu utama\n0. Keluar aplikasi");
                System.out.print("\nPilihan => ");
                choice = input.nextInt();

                if (choice==1) { //BAYAR DAN PRINT STRUK
                    System.out.println("\n\n\n\n");
                    saveReceiptFile(menus);
                    choice = 0;
                } else if (choice==2) { //KEMBALI KE MENU UTAMA
                    choice = 100;
                } else if (choice==0) { //KELUAR APP
                    choice = 0;
                }
            } else {
                System.out.println("Eits, inputnya kurang tepat nih :(\n");
            }
        }
    }

    //mengatur format output dengan padding dan format angka
    public static String padRight(String s, int n){
        return String.format("%-" + n + "s", s);
    }
    public static String padLeft(String s, int n){
        return String.format("%" + n + "s", s);
    }
    public static String formatRupiah(int number) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat customFormat = (DecimalFormat)nf;
        String rupiah = customFormat.format(number);
        return rupiah;
    }

    //method untuk print sebuah item dan harganya
    public static void printPriceList(ArrayList<Menu> item){
        System.out.println("\n\n\n\n........................................");
        System.out.println("         D A F T A R    M E N U         ");
        System.out.println("........................................");
        System.out.println("\nSilahkan pilih makanan :");
        int k = 0;
        for (Menu i: item) {
            System.out.println(padLeft(Integer.toString(k+1),2) + ". " + padRight(i.getName(),20) + " | " + padLeft(formatRupiah(i.getPrice()),13));
            k = k+1;
        }
        System.out.println("99. Pesan dan Bayar\n 0. Keluar aplikasi");
    }

    public static void quantityConfirmation (Menu item, int quantity){
        System.out.println("Berhasil menambahkan " + item.getName() + " sebanyak " + quantity + " porsi.");
        item.setQuantity(item.getQuantity()+quantity);
    }

    public static String orderConfirmation (ArrayList<Menu> item) {
        int totalQuantity = 0;
        int totalPrice = 0;

        String str0 = "\n" + padRight("MENU",13) + " " + padLeft("QTY",3) + " " + padLeft("SATUAN",9) + " " + padLeft("TOTAL",12);
        String orderConfirmationString = str0;
        System.out.println(str0);

        for (Menu i: item) {
            if (i.getQuantity()>0) {
                String str = padRight(i.getName(),13) + " " + padLeft(Integer.toString(i.getQuantity()),3) + " " + padLeft(formatRupiah(i.getPrice()),9) + " " + padLeft(formatRupiah(i.getTotalPricePerMenu()),12);
                orderConfirmationString = orderConfirmationString + "\n" + str;
                System.out.println(str);
            }
            totalQuantity = totalQuantity + i.getQuantity();
            totalPrice = totalPrice + i.getTotalPricePerMenu();
        }
        String str2 = "---------------------------------------- +\n" + "          " + padLeft(Integer.toString(totalQuantity),7) + "         " + padLeft(formatRupiah(totalPrice),14);
        orderConfirmationString = orderConfirmationString + "\n" + str2;
        System.out.println(str2);

        return orderConfirmationString;
    }

    public static String printReceipt(ArrayList<Menu> item) {
        String str1 = "========================================\n" +
                "~BINARFUD~BINARFUD~BINARFUD~BINARFUD~BIN\n" +
                "========================================\n" +
                "   Selamat menikmati hidangan Anda! ^_^ ";
        System.out.println(str1);

        //Menggunakan method print menu yang sudah dipesan sebelumnya
        String receiptContent = orderConfirmation(item);

        //Sedikit eksperimen dengan timestamp
        Date date = new Date();
        String str2 = "\nWaktu     : " + new Timestamp(date.getTime());
        System.out.println(str2);

        String str3 = "Kasir     : Aol's bot\n" +
                "Pembayaran: BinarCash\n" +
                "\n" +
                "========================================\n" +
                "NARFUD~BINARFUD~BINARFUD~BINARFUD~BINARF\n" +
                "========================================";
        System.out.println(str3);
        return str1 + "\n" + receiptContent + "\n" + str2 + "\n" + str3;
    }

    public static void saveReceiptFile(ArrayList<Menu> item) {
        //Save Struk menjadi file .txt
        Date date = new Date();
        String fileName = "ReceiptBinarFud_2023" + ".txt";

        try {
            File file = new File(fileName);
            FileWriter fileWriterReceipt = new FileWriter(file);
            PrintWriter printWriterReceipt = new PrintWriter(fileWriterReceipt);

            //Isi dari struk menggunakan String yang merupakan keluaran dari method printReceipt
            String str = printReceipt(item);
            printWriterReceipt.write(str);
            printWriterReceipt.close();

            System.out.println("\n\nStruk tersimpan dalam file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}