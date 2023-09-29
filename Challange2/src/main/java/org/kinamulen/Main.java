package org.kinamulen;

import org.kinamulen.service.AppService;

public class Main {
    public static void main(String[] args) {
        AppService appService = new AppService();
        appService.initiateData();
        appService.run();
    }
}