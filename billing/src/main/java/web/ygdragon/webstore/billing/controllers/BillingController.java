package web.ygdragon.webstore.billing.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.ygdragon.webstore.billing.models.Transaction;
import web.ygdragon.webstore.billing.models.Bill;
import web.ygdragon.webstore.billing.services.BillingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @GetMapping()
    public ResponseEntity<List<Bill>> getBills() {
        return ResponseEntity.ok().body(billingService.getAllBills());
    }

    /**
     * Transaction operation
     *
     * @param transaction Transaction object
     * @return Operation success response
     */
    @PostMapping()
    public ResponseEntity<Void> paymentOrder(@RequestBody Transaction transaction) {
        billingService.makeTransaction(transaction);
        return ResponseEntity.ok().body(null);
    }

    /**
     * Rollback transaction operation
     *
     * @param transaction Transaction object
     * @return Operation success response
     */
    @PostMapping("/rollback")
    public ResponseEntity<Void> rollbackPaymentOrder(@RequestBody Transaction transaction) {
        billingService.rollbackTransaction(transaction);
        return ResponseEntity.ok().body(null);
    }
}