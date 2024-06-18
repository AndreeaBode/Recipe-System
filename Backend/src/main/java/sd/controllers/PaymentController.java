package sd.controllers;

import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd.entities.User;
import sd.repositories.UserRepository;
import sd.services.PaymentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public PaymentController(PaymentService paymentService, UserRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/payment/can-make-request")
    public ResponseEntity<Map<String, String>> canMakeRequest(@RequestParam int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, String> response = new HashMap<>();
        if (paymentService.canUserMakeRequest(user)) {
            response.put("message", "User can make a request");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Search limit exceeded. Please make a payment.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            paymentService.handleStripeWebhook(payload, sigHeader);
            return ResponseEntity.ok("Webhook handled successfully");
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to handle webhook");
        }
    }

    @PostMapping("/payment/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestParam int userId) {
        try {
            String sessionUrl = paymentService.createCheckoutSession(userId);
            if (sessionUrl != null) {
                Map<String, String> response = new HashMap<>();
                response.put("url", sessionUrl);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to create checkout session");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Exception while creating checkout session: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/payment/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestParam String sessionId) {
        System.out.println("AAAAA");
        try {
            boolean paymentStatus = paymentService.verifyPayment(sessionId);
            return ResponseEntity.ok().body(paymentStatus ? "Payment successful" : "Payment not successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment");
        }
    }


    @GetMapping("/payment/check-payment")
    public ResponseEntity<?> checkPaymentStatus(@RequestParam String sessionId) {
        System.out.println("BBBBB");
        try {
            String paymentStatus = paymentService.checkPaymentStatus(sessionId);
            return ResponseEntity.ok(paymentStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking payment status: " + e.getMessage());
        }
    }

/*    @PutMapping("/payment/users/{userId}/premium")
    public ResponseEntity<?> upgradeUserToPremium(@PathVariable int userId) {
        boolean success = paymentService.upgradeUserToPremium(userId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
}
