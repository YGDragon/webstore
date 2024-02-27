package web.ygdragon.webstore.shopping.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import web.ygdragon.webstore.shopping.models.Product;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ShoppingService {

    List<Product> getAllProducts();

    void paymentOrder(Long idProduct, int quantityOrder, BigDecimal totalPrice, Long numberCredit);

    ResponseEntity<?> reservation(Long idProduct, int quantityProduct) throws HttpClientErrorException;

    void rollbackReserve(Long idProduct, int quantityOrder) throws HttpClientErrorException;

    void reduceQuantity(Long idProduct, int quantityOrder) throws HttpClientErrorException;

    ResponseEntity<?> payment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException;

    void rollbackPayment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException;
}
