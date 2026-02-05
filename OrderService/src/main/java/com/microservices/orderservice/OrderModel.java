package com.microservices.orderservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Orders_db")
public class OrderModel {

    @Id
    private String id;

    private String customerName;
    private String productName;
    private int qty;
    private String status;

    public OrderModel() {}

    public OrderModel(String customerName, String productName, int qty, String status) {
        this.customerName = customerName;
        this.productName = productName;
        this.qty = qty;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
