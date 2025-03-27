package com.E_Handel.BetalningarMicro;

public class PaymentResponse {

    private Payment payment;
    private User user;

    public PaymentResponse(Payment payment, User user) {
        this.payment = payment;
        this.user = user;
    }

    // Getters and setters
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
