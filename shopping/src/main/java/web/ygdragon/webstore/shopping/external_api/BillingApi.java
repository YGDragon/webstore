package web.ygdragon.webstore.shopping.external_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import web.ygdragon.webstore.shopping.models.Transaction;

/**
 * External API with FeignService - BillingAPI
 */
@FeignClient(name = "payment")
public interface BillingApi {

    /**
     * Payment transaction operation
     *
     * @param transaction Transaction service object
     * @return Response from billing API
     */
    @PostMapping()
    ResponseEntity<?> paymentOrder(@RequestBody Transaction transaction);

    /**
     * Rollback transaction operation
     *
     * @param transaction Transaction service object
     */
    @PostMapping("/rollback")
    void rollbackPaymentOrder(@RequestBody Transaction transaction);

}
