package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.invoice.InvoiceRow;
import com.kinamulen.binarfood.dto.invoice.ReportRow;
import com.kinamulen.binarfood.entity.Merchant;
import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.entity.OrderDetail;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.repository.MerchantRepository;
import com.kinamulen.binarfood.repository.OrderRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class InvoiceService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    JasperReportService jasperReportService;

    public byte[] generateInvoice(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.get();

        List<InvoiceRow> invoiceRows = new ArrayList<>();
        Set<OrderDetail> orderDetails = order.getOrdersDetails();

        orderDetails.forEach(orderDetail -> {
            Product product = orderDetail.getProduct();
            Merchant merchant = product.getMerchant();
            invoiceRows.add(InvoiceRow.builder()
                    .merchantName(merchant.getMerchantName())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .totalPrice(orderDetail.getTotalPrice())
                    .build());
        });

        byte[] reportContent = jasperReportService.getItemReport(order,invoiceRows, "pdf");
        return reportContent;
    }

    public byte[] genereteReport(UUID merchantId, LocalDateTime startDate, LocalDateTime endDate) {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        //to do here: asign each row table from query into reportRow
        List<ReportRow> reportRows = merchantRepository.findReportByMerchantId(merchantId, startDate, endDate);
        byte[] reportContent = jasperReportService.getMerchantReport(merchant.get(),
                startDate, endDate, reportRows, "pdf");
        return null;
    }
}
