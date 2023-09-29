package org.kinamulen.controller;

import org.kinamulen.model.Menu;
import org.kinamulen.model.Order;
import org.kinamulen.model.Variant;
import org.kinamulen.service.AppService;
import org.kinamulen.service.MenuService;
import org.kinamulen.service.OrderService;
import org.kinamulen.service.VariantService;
import org.kinamulen.view.ErrorView;
import org.kinamulen.view.MenuView;
import org.kinamulen.view.View;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderController {
    public void home() {
        View.welcome();
    }
    public void selectMenu() {
        MenuView menuView = new MenuView();
        MenuService menuService = new MenuService();
        Map<Integer,Menu> menus = menuService.getMenus();

        menuView.priceList(menus);
        try {
            int choice = inputChoice(); //critical section
            switch (choice){
                case 0 -> AppService.setExit(true);
                case 99 -> checkOut();
                case 1,2,3,4,5 -> customizeOrder(choice);
                default -> ErrorView.invalidInput();
            }
        } catch (InputMismatchException e) {
            ErrorView.invalidInput();
        }
    }

    public int inputChoice() {
        View.inputField();
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    public void customizeOrder(Integer keyMenu) {
        MenuView menuView = new MenuView();
        MenuService menuService = new MenuService();
        VariantService variantService = new VariantService();

        Map<Integer, Variant> var1 = menuService.getMenus(keyMenu).getVariant1();
        Map<Integer, Variant> var2 = menuService.getMenus(keyMenu).getVariant2();

        Integer keyVar1 = null;
        Integer keyVar2 = null;

        if (var1 != null) {
            variantService.getVariant1();
            menuView.viewVariant(var1);
            keyVar1 = inputChoice();
        }
        if (var2 != null) {
            variantService.getVariant2();
            menuView.viewVariant(var2);
            keyVar2 = inputChoice();
        }
        try {
            View.inputQuantity();
            Integer quantity = inputChoice();
            addOrder(keyMenu, keyVar1, keyVar2, quantity);
        } catch (InputMismatchException e) {
            ErrorView.invalidInput();
        }
    }

    private void addOrder(Integer keyMenu, Integer keyVar1, Integer keyVar2, Integer quantity) {
        OrderService orderService = new OrderService();
        orderService.saveOrder(keyMenu,keyVar1,keyVar2,quantity);
    }

    private void checkOut(){
        MenuView menuView = new MenuView();
        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getOrder();

        if (!orderService.isOrderEmpty()) {
            menuView.orderSummary(orders);
            try {
                int confirm = inputChoice();
                switch (confirm){
                    case 1 -> printAndMakeOrder();
                    case 2 -> selectMenu();
                    case 0 -> AppService.setExit(true);
                    default -> ErrorView.invalidInput();
                }
            } catch (InputMismatchException e) {
                ErrorView.invalidInput();
            }
        } else {
            ErrorView.emptyOrder();
        }
    }
    public static void printAndMakeOrder(){
        MenuView menuView = new MenuView();
        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getOrder();

        StringBuilder context = menuView.printReceipt(orders); //print terminal
        OrderService.printReceiptToTxt(context); //save txt
        AppService.setExit(true);//exit
    }
}
