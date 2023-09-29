package org.kinamulen;

public class Menu {
    private final String name;
    private final int price;
    private int quantity;
    /*  Quantity setiap object di Class Menu awalnya kita set=0. Dalam Main.java nanti kita bisa mengubah
        quatity setiap menu. Class ini digunakan untuk memunculkan menu di halaman utama, konfirmasi pesanan,
        dan juga struk pembayaran.
    */

    public Menu(String name, int price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() { return this.quantity;}

    public int getTotalPricePerMenu() {
        //Agar mudah memanggil total harga per menu di halaman konfirmasi dan struk
        return price*quantity;
    }
}
