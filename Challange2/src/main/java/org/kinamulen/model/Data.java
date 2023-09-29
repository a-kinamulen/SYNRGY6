package org.kinamulen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    //  kutipan dari kak ilyas  (latihan sebelumnya):
    //employees sengaja dibuat class variable untuk menyimpan data pegawai.
    //Sebenarnya dengan menjadikan employees static, employees dapat diakses secara langsung dari manapun.
    //Namun untuk pembelajaran, employees ini hanya diakses oleh service. Seolah-olah service dapat dari DAO.
public class Data {
    //catatan: di SonarLint, penggunaan static seharusnya tidak ter-mutasi.
    //          Tapi dalam kasus ini menggunakan list (dimana akan kita mutasi)
    //          Jadi issue ini diabaikan
    public static final List<Order> orders = new ArrayList<>();
    public static final Map<Integer,Menu> menus = new HashMap<>();
    public static final Map<Integer,Variant> varSpicy = new HashMap<>();
    public static final Map<Integer,Variant> varLauk = new HashMap<>();

    private Data(){}
}
