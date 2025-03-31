package com.E_Handel.BetalningarMicro;

public class PaymentResponse {

    private Payment payment;
    private User user;
    private Order order;
    private double totalAmount;

    public PaymentResponse(Payment payment, Order order, User user) {
        this.payment = payment;
        this.order = order;
        this.user = user;
        this.totalAmount = calculateTotalAmount();  // Calculate the total amount per payment
    }

    public PaymentResponse(Payment payment, User user) {
        this.payment = payment;
        this.user = user;
    }

    public PaymentResponse(Payment payment, double totalAmount, Order order, User user) {
        this.payment = payment;
        this.totalAmount = totalAmount;
        this.order = order;
        this.user = user;
    }

    public PaymentResponse(Payment payment, Order order) {
        this.payment = payment;
        this.order = order;
    }


    // Method to calculate total amount
    private double calculateTotalAmount() {
        return order.getQuantity() * payment.getAmount();  // Quantity * Price per item
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentResponse(Payment payment, Order order, User user, double totalAmount) {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}