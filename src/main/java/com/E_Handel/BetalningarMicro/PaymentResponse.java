package com.E_Handel.BetalningarMicro;

public class PaymentResponse {

    private Payment payment;
    private User user;
    private Order order;

    public PaymentResponse(Payment payment, Order order, User user) {
        this.payment = payment;
        this.order = order;
        this.user = user;
    }

    public PaymentResponse(Payment payment, User user) {
        this.payment = payment;
        this.user = user;
    }

    public PaymentResponse(Payment payment, Order order) {
        this.payment = payment;
        this.order = order;
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