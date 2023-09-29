package org.kinamulen.service;

import org.kinamulen.model.Data;
import org.kinamulen.model.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class OrderService {
    public static void printReceiptToTxt(StringBuilder context) {
        try {
            String basePath = "Challange2/src/main/java/org/kinamulen/output/";
            String fileName = basePath + "ReceiptBinarFood.txt";

            File file = new File(fileName); //critical point
            FileWriter fileWriterRecepit = new FileWriter(file);
            PrintWriter printWriterReceipt = new PrintWriter(fileWriterRecepit);

            printWriterReceipt.write(context.toString());
            printWriterReceipt.close();

            System.out.println("\n\nStruk tersimpan dalam file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void saveOrder(Integer keyMenu, Integer varKey1, Integer varKey2, Integer quantity) {
        //basically copy si menu details ke order, tapi ada qty nya
        Order o = new Order();
        o.setName(Data.menus.get(keyMenu).getName());
        o.setVariant1(Objects.nonNull(Data.varLauk.get(varKey1))?Data.varLauk.get(varKey1).getName(): null);
        o.setVariant2(Objects.nonNull(Data.varSpicy.get(varKey2))?Data.varSpicy.get(varKey2).getName(): null);
        o.setQuantity(quantity);

        int addPrice1 = Objects.nonNull(Data.varLauk.get(varKey1))?Data.varLauk.get(varKey1).getAdditionalPrice(): 0;
        int addPrice2 = Objects.nonNull(Data.varSpicy.get(varKey2))?Data.varSpicy.get(varKey2).getAdditionalPrice(): 0;

        o.setPrice((Data.menus.get(keyMenu).getPrice()
                +addPrice1
                +addPrice2
                )*quantity);
        Data.orders.add(o);
    }
    public List<Order> getOrder() {
        return Data.orders;
    }
    public boolean isOrderEmpty() {
        return Data.orders.isEmpty();
    }
    public static void checkOut() {
        //save txt???
    }
}
