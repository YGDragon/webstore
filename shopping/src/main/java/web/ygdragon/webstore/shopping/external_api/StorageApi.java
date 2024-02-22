package web.ygdragon.webstore.shopping.external_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.ygdragon.webstore.shopping.models.Order;
import web.ygdragon.webstore.shopping.models.Product;

import java.util.List;

@FeignClient(name = "storage")
public interface StorageApi {
    /**
     * Get all products from stock
     *
     * @return List of all products
     */
    @GetMapping
    List<Product> getProducts();

    /**
     * Reduce quantity of product from stock by product ID
     *
     * @param id Product ID
     * @return Response from storage API
     */
    @PostMapping("/{id}")
    ResponseEntity<?> reduceProductQuantity(@PathVariable Long id, @RequestBody Order order);

    /**
     * Reserving product in stock by ID
     *
     * @param id Product ID
     * @return Response from storage API
     */
    @PostMapping("/{id}/reserve")
    ResponseEntity<?> reservationProduct(@PathVariable Long id, @RequestBody Order order);

    /**
     * Rollback a product reservation in a stock by ID
     *
     * @param id Product ID
     */
    @PostMapping("/{id}/reserve/rollback")
    void reservationProductRollback(@PathVariable Long id, @RequestBody Order order);

}
