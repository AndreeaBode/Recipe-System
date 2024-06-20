package sd.services;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sd.entities.User;
import sd.repositories.UserRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    public PaymentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleStripeWebhook(String payload, String sigHeader) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, stripeWebhookSecret
            );
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Webhook signature verification failed", e);
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed(event);
                break;
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
            default:
                logger.log(Level.WARNING, "Unhandled event type: {0}", event.getType());
                break;
        }
    }

    private void handlePaymentIntentSucceeded(Event event) {
        try {
            logger.log(Level.INFO, "Raw event data: {0}", event.getData().toString());

            Optional<StripeObject> optionalObject = event.getDataObjectDeserializer().getObject();
            logger.log(Level.INFO, "Deserialization successful: {0}", optionalObject.isPresent());

            if (optionalObject.isPresent()) {
                PaymentIntent paymentIntent = (PaymentIntent) optionalObject.get();
                logger.log(Level.INFO, "PaymentIntent data: {0}", paymentIntent.toString());

                String userId = paymentIntent.getMetadata().get("user_id");
                if (userId != null) {
                    upgradeUserToPremium(Integer.parseInt(userId));
                } else {
                    logger.log(Level.SEVERE, "User ID not found in payment intent metadata");
                }
            } else {
                logger.log(Level.SEVERE, "PaymentIntent not found in event data");
            }
        } catch (JsonSyntaxException e) {
            logger.log(Level.SEVERE, "JsonSyntaxException while deserializing event data", e);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error handling payment_intent.succeeded event", e);
        }
    }




    private void handlePaymentIntentFailed(Event event) {
        try {
            Optional<StripeObject> optionalObject = event.getDataObjectDeserializer().getObject();
            if (optionalObject.isPresent()) {
                PaymentIntent paymentIntent = (PaymentIntent) optionalObject.get();
                String userId = paymentIntent.getMetadata().get("user_id");
                logger.log(Level.WARNING, "Payment failed for user ID: " + userId);
            } else {
                logger.log(Level.SEVERE, "PaymentIntent not found in event data");
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error handling payment_intent.payment_failed event", e);
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        try {
            logger.log(Level.INFO, "Raw event data: {0}", event.getData().toString());

            Optional<StripeObject> optionalObject = event.getDataObjectDeserializer().getObject();
            logger.log(Level.INFO, "Deserialization successful: {0}", optionalObject.isPresent());

            if (optionalObject.isPresent()) {
                Session session = (Session) optionalObject.get();
                logger.log(Level.INFO, "Session data: {0}", session.toString());

                String userId = session.getMetadata().get("user_id");
                if (userId != null) {
                    upgradeUserToPremium(Integer.parseInt(userId));
                } else {
                    logger.log(Level.SEVERE, "User ID not found in session metadata");
                }
            } else {
                logger.log(Level.SEVERE, "Session not found in event data");
            }
        } catch (JsonSyntaxException e) {
            logger.log(Level.SEVERE, "JsonSyntaxException while deserializing event data", e);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error handling checkout.session.completed event", e);
        }
    }


    public boolean upgradeUserToPremium(int userId) {
        System.out.println("PREMIUM");
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setRole("premium");
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String createCheckoutSession(int userId) throws StripeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/home-page")
                .setCancelUrl("http://localhost:4200/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(2000L) // Amount in cents (e.g., $20.00)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Premium Subscription")
                                        .build())
                                .build())
                        .build())
                .putMetadata("user_id", String.valueOf(userId))  // Add user_id to metadata
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }


    public String checkPaymentStatus(String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Session session = Session.retrieve(sessionId);
        return session.getPaymentStatus();
    }

    public boolean verifyPayment(String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Session session = Session.retrieve(sessionId);
        return "paid".equals(session.getPaymentStatus());
    }

    public boolean canUserMakeRequest(User user) {
        return user.getWeeklyRequestCount() < 3;
    }
}
