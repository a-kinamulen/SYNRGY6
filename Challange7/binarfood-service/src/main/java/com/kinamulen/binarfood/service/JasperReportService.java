package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.invoice.InvoiceRow;
import com.kinamulen.binarfood.dto.invoice.ReportRow;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.entity.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    public byte[] getItemReport(Order order, List<InvoiceRow> invoiceRows, String format) {

        JasperReport jasperReport;

        try {
            jasperReport = (JasperReport)
                    JRLoader.loadObject(ResourceUtils.getFile("invoice.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:jasper/invoice.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "invoice.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceRows);
        Map<String, Object> parameters = new HashMap<>();
        String invoiceNumber = "INV/BinarFoodByAull/"+order.getId().toString();
        parameters.put("orderId", "Invoice number: "+invoiceNumber);
        parameters.put("username", "Username/buyer: "+order.getUser().getUsername());
        parameters.put("destination", "Delivery Address: "+order.getDestinationAddress());
        String orderDate = order.getCreatedAt().getDayOfWeek() +
                ", " + order.getCreatedAt().getDayOfMonth() +
                " " + order.getCreatedAt().getMonth().toString() +
                " " + order.getCreatedAt().getYear();
        parameters.put("orderTime", "Ordered at: " + orderDate);
        double totalPrice = invoiceRows.stream().mapToDouble(InvoiceRow::getTotalPrice).sum();
        parameters.put("totalPrice", "Total amount: IDR "+totalPrice);
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (format) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }
    public byte[] getMerchantReport(Merchant merchant,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    List<ReportRow> reportRows,
                                    String format) {

        JasperReport jasperReport;

        try {
            jasperReport = (JasperReport)
                    JRLoader.loadObject(ResourceUtils.getFile("report.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:jasper/report.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "report.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportRows);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("merchantName", "Merchant Name: " + merchant.getMerchantName());
        String startDateFormated = startDate.getDayOfWeek() +
                ", " + startDate.getDayOfMonth() +
                " " + startDate.getMonth().toString() +
                " " + startDate.getYear();
        parameters.put("startDate", "Start date: " + startDateFormated);
        String endDateFormated = endDate.getDayOfWeek() +
                ", " + endDate.getDayOfMonth() +
                " " + endDate.getMonth().toString() +
                " " + endDate.getYear();
        parameters.put("endDate", "Ennd Date: " + endDateFormated);
        double totalEarning = reportRows.stream().mapToDouble(ReportRow::getPrice).sum();
        parameters.put("totalEarning", "Total earning in this range: IDR " + totalEarning);
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (format) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }
}
