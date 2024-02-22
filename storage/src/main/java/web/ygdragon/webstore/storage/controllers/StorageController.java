package web.ygdragon.webstore.storage.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.ygdragon.webstore.storage.models.Order;
import web.ygdragon.webstore.storage.models.Product;
import web.ygdragon.webstore.storage.services.StorageService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StorageController {
    private final StorageService storageService;

    /**
     * Getting all products from stock
     *
     * @return Response to client
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok().body(storageService.getAllProduct());
    }

    /**
     * Getting product from stock by ID
     *
     * @param id Product ID
     * @return Response to client
     */
    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(storageService.getProductById(id));
    }

    /**
     * Reduce quantity of product from stock by product ID
     *
     * @param idProduct Product ID
     * @param order     Order object
     * @return Operation success response
     */
    @PostMapping("{id}")
    public ResponseEntity<Void> reduce(@PathVariable("id") Long idProduct, @RequestBody Order order) {
        storageService.reduceProductQuantity(idProduct, order.getQuantity());
        return ResponseEntity.ok().body(null);
    }

    /**
     * Reservation for product in stock by product ID
     *
     * @param idProduct Product ID
     * @param order     Order object
     * @return Operation success response
     */
    @PostMapping("{id}/reserve")
    public ResponseEntity<Void> reserve(@PathVariable("id") Long idProduct, @RequestBody Order order) {
        storageService.reserveProduct(idProduct, order.getQuantity());
        return ResponseEntity.ok().body(null);
    }

    /**
     * Rollback reservation of product in stock by product ID
     *
     * @param idProduct Product ID
     * @param order     Order object
     * @return Operation success response
     */
    @PostMapping("{id}/reserve/rollback")
    public ResponseEntity<Void> rollbackReserve(@PathVariable("id") Long idProduct, @RequestBody Order order) {
        storageService.rollbackReserveProduct(idProduct, order.getQuantity());
        return ResponseEntity.ok().body(null);
    }

}
