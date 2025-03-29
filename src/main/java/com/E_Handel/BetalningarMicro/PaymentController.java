package com.E_Handel.BetalningarMicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private WebClient.Builder webClientBuilder;
    private final WebClient webclient;
    private final PaymentRepository paymentRepository;
    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private static final String ORDER_SERVICE_URL = "http://localhost:8083";



    public PaymentController(WebClient.Builder webclientBuilder, PaymentRepository paymentRepository) {
        this.webclient = webclientBuilder.baseUrl("http://localhost:8082").build();
        this.paymentRepository = paymentRepository;
    }


    // Create a payment

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }


    // Get a payment and users and orders by id

    @GetMapping("/{id}")
    public Mono<PaymentResponse> getPaymentById(@PathVariable Long id) {
        // Fetch the Payment from the Payment Service using paymentRepository
        return paymentRepository.findById(id)
                .map(payment -> {
                    // Fetch the Order related to the Payment using Order Microservice
                    return webClientBuilder.baseUrl("http://localhost:8083")  // Order Service URL
                            .build()
                            .get()
                            .uri("/orders/{orderId}", payment.getOrderId())  // Fetch Order by Order ID
                            .retrieve()
                            .bodyToMono(Order.class)  // Convert response to Mono<Order>
                            .flatMap(order -> {
                                // Step 3: Fetch the User related to the Order using User Microservice
                                return webClientBuilder.baseUrl("http://localhost:8081")  // User Service URL
                                        .build()
                                        .get()
                                        .uri("/users/{userId}", order.getUserId())  // Fetch User by User ID
                                        .retrieve()
                                        .bodyToMono(User.class)  // Convert response to Mono<User>
                                        .map(user -> new PaymentResponse(payment, order, user));  // Combine Payment, Order, and User
                            });
                })
                .orElse(Mono.empty()); // Handle case when payment is not found
    }



    // Get all payment and users and orders

    @GetMapping("/payments_details")
    public Mono < List < PaymentResponse >> getAllPaymentAndUsers() {

        return webclient.get()
                .uri("/payments")
                .retrieve()
                .bodyToFlux(Payment.class)
                .flatMap(payment -> {

                    Mono < Order > orderMono = webClientBuilder.baseUrl(ORDER_SERVICE_URL).build().get()
                            .uri("/orders/{orderId}", payment.getOrderId())
                            .retrieve()
                            .bodyToMono(Order.class);

                    return orderMono.flatMap(order -> {

                        return webClientBuilder.baseUrl(USER_SERVICE_URL).build().get()
                                .uri("/users/{userId}", order.getUserId())
                                .retrieve()
                                .bodyToMono(User.class)
                                .map(user -> new PaymentResponse(payment, order, user));
                    });

                })
                .collectList();

    }


    // Get a list of payments

    @GetMapping
    public ResponseEntity < List < Payment >> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }


    // Delete payment by ID

    @DeleteMapping("/{id}")
    public ResponseEntity < Void > deletePayment(@PathVariable Long id) {
        Optional < Payment > payment = paymentRepository.findById(id);
        if (!payment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        paymentRepository.delete(payment.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // Update payment details by ID

    @PutMapping("/{id}")
    public ResponseEntity < Payment > updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Optional < Payment > existingPayment = paymentRepository.findById(id);
        if (!existingPayment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if the payment is not found
        }
        Payment paymentToUpdate = existingPayment.get();
        paymentToUpdate.setUserId(paymentDetails.getUserId());
        paymentToUpdate.setAmount(paymentDetails.getAmount());
        paymentToUpdate.setPaymentStatus(paymentDetails.getPaymentStatus());
        paymentToUpdate.setPaymentDate(paymentDetails.getPaymentDate());
        Payment updatedPayment = paymentRepository.save(paymentToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPayment);
    }

}