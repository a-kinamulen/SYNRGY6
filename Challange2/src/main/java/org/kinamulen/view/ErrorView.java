package org.kinamulen.view;

import static org.kinamulen.utils.Constant.LINE_BOLD;

public class ErrorView {
    public static void invalidInput() {
        String sb = LINE_BOLD + "\nPilihan tdk valid, silahkan yg sesuai ya\n" + LINE_BOLD;
        System.out.println(sb);
    }

    public static void emptyOrder() {
        String sb = LINE_BOLD + "\nPesanan masih kosong nih, yuk pilih menu!\n" + LINE_BOLD;
        System.out.println(sb);
    }
}
