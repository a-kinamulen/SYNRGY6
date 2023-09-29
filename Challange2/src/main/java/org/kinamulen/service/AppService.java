package org.kinamulen.service;

import org.kinamulen.controller.OrderController;

public class AppService { //Anggap sebagai jembatan pembeda MVC pakai terminal vs. webService
    private static boolean exit = false;

    public static void setExit(boolean exit) {
        AppService.exit = exit;
    }

    public void initiateData() {
        MenuService ds = new MenuService();
        ds.initiateData();
    }
    public void run(){
        //Home
        OrderController orderController =  new OrderController();
        orderController.home();
        while(!exit){
            orderController.selectMenu();
        }
    }
}
