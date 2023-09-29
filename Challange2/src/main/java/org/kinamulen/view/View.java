package org.kinamulen.view;

import static org.kinamulen.utils.Constant.LINE_BOLD;
public class View {
    public static void welcome(){
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_BOLD).append("\n    Selamat datang di BinarFud ^w^      \n").append(LINE_BOLD);
        System.out.println(sb);
    }
    public static void inputField(){
        System.out.println("INPUT: ");
    }
    public static void inputQuantity(){
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_BOLD).append("\n       Masukkan quantitiy dari item     \n").append(LINE_BOLD);
        System.out.println(sb);
    }
}
