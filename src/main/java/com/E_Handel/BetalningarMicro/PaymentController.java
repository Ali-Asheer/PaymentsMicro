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

    private final WebClient webclient;
    private final PaymentRepository paymentRepository;


    public PaymentController(WebClient.Builder webclientBuilder, PaymentRepository paymentRepository) {
        this.webclient = webclientBuilder.baseUrl("http://localhost:8081").build();
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment){
        return paymentRepository.save(payment);

    }


    @GetMapping("/{id}")
    public Mono<PaymentResponse> getPaymentById(@PathVariable Long id){

        return paymentRepository.findById(id).map( payment ->
                        webclient.get()
                                .uri("/users/" + payment.getUserId())
                                .retrieve().bodyToMono(User.class)
                                .map(user -> new PaymentResponse(payment, user)))
                .orElse(Mono.empty());
    }


    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String PAYMENT_SERVICE_URL = "http://localhost:8082";  // Change accordingly
    private static final String USER_SERVICE_URL = "http://localhost:8081";  // Change accordingly

    @GetMapping("/payment-and-users")
    public Mono<List<PaymentResponse>> getAllPaymentAndUsers() {

        WebClient webClient = webClientBuilder.baseUrl(PAYMENT_SERVICE_URL).build();

        return webClient.get()
                .uri("/payments")  // Assuming you have an endpoint that returns all payments
                .retrieve()
                .bodyToFlux(Payment.class)
                .flatMap(payment -> {
                    // For each payment, we make another call to the User Microservice to get user details
                    return webClientBuilder.baseUrl(USER_SERVICE_URL)
                            .build()
                            .get()
                            .uri("/users/" + payment.getUserId())  // Assuming you fetch user by their userId
                            .retrieve()
                            .bodyToMono(User.class)
                            .map(user -> new PaymentResponse(payment, user)); // Combine the payment and user info
                })
                .collectList(); // Collect all responses into a list
    }


//        @GetMapping("/test")
//        public String testEndpoint() {
//            return "GET request is working!";
//        }


        @GetMapping
    public ResponseEntity<List<Payment>>getAllPayments(){
        return ResponseEntity.ok(paymentRepository.findAll());
    }


    // DELETE: Delete payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (!payment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // If payment exists, delete it
        paymentRepository.delete(payment.get());

        // Optionally: If you need to perform other operations (e.g., cancel the associated order), you can call other microservices here

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // PUT: Update payment details by ID
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Optional<Payment> existingPayment = paymentRepository.findById(id);

        if (!existingPayment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 if the payment is not found
        }

        // Update the payment details with the new data
        Payment paymentToUpdate = existingPayment.get();

        paymentToUpdate.setUserId(paymentDetails.getUserId());
        paymentToUpdate.setAmount(paymentDetails.getAmount());
        paymentToUpdate.setPaymentStatus(paymentDetails.getPaymentStatus());
        paymentToUpdate.setPaymentDate(paymentDetails.getPaymentDate());  // Update other fields as needed

        // Save the updated payment
        Payment updatedPayment = paymentRepository.save(paymentToUpdate);

        // Optionally: Notify the Order Microservice (if needed)
        // Example: After updating payment, you might need to update the order status

        return ResponseEntity.status(HttpStatus.OK).body(updatedPayment);  // Return the updated payment
    }

}
