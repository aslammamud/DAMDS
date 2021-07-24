package com.slowcoders.damds;

import java.util.Date;

public class MedicineOrderItem {
    String customer;
    String phone;
    String address;
    String prescriptionImage;
    String description;
    String orderDate;
    String orderTime;
    String orderStatus;

    public MedicineOrderItem() {
    }

    public MedicineOrderItem(String customer, String phone, String address, String prescriptionImage, String description, String orderDate, String orderTime, String orderStatus) {
        this.customer = customer;
        this.phone = phone;
        this.address = address;
        this.prescriptionImage = prescriptionImage;
        this.description = description;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}

