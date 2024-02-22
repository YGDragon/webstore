package web.ygdragon.webstore.shopping.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import web.ygdragon.webstore.shopping.models.Order;
import web.ygdragon.webstore.shopping.models.Product;
import web.ygdragon.webstore.shopping.models.Transaction;
import web.ygdragon.webstore.shopping.aspects.LogLeadTime;
import web.ygdragon.webstore.shopping.external_api.BillingApi;
import web.ygdragon.webstore.shopping.external_api.StorageApi;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingService {
    private final BillingApi billingApi;
    private final StorageApi storageApi;
    private final String storeBill;

    public ShoppingService(
            BillingApi billingApi,
            StorageApi storageApi,
            @Value("${shop.account.number}") String storeBill) {
        this.billingApi = billingApi;
        this.storageApi = storageApi;
        this.storeBill = storeBill;
    }

    /**
     * Getting all products from the stock
     *
     * @return List of all products
     */
    @LogLeadTime
    public List<Product> getAllProducts() {
        return storageApi.getProducts();
    }

    /**
     * Payment for the order. На каждом этапе происходит проверка,
     * In case в случае получения исключения происходит откат выполненных транзакций.
     *
     * @param idProduct     Product ID
     * @param quantityOrder Quantity of products in order
     * @param totalPrice    Order price
     * @param numberCredit  Debit bill number of web store
     */
    @LogLeadTime
    public void paymentOrder(Long idProduct, int quantityOrder, BigDecimal totalPrice, Long numberCredit) {
        ResponseEntity<?> responseReservation = reservation(idProduct, quantityOrder);

        if (responseReservation.getStatusCode().is2xxSuccessful()) {
            ResponseEntity<?> responsePayment = payment(numberCredit, totalPrice);

            if (responsePayment.getStatusCode().is2xxSuccessful()) {
                reduceQuantity(idProduct, quantityOrder);
            } else {
                rollbackPayment(numberCredit, totalPrice);
                rollbackReserve(idProduct, quantityOrder);
            }

        } else {
            rollbackReserve(idProduct, quantityOrder);
        }
    }

    /**
     * Reserving products in stock
     *
     * @param idProduct Product ID
     * @return ResponseEntity object
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    private ResponseEntity<?> reservation(Long idProduct, int quantityProduct) throws HttpClientErrorException {
        return storageApi.reservationProduct(idProduct, new Order(quantityProduct));
    }

    /**
     * Rollback a product reservation in a stock
     *
     * @param idProduct Product ID
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    private void rollbackReserve(Long idProduct, int quantityOrder) throws HttpClientErrorException {
        storageApi.reservationProductRollback(
                idProduct, new Order(quantityOrder));
    }

    /**
     * Reduce quantity of product from stock
     *
     * @param idProduct Product ID
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    private void reduceQuantity(Long idProduct, int quantityOrder) throws HttpClientErrorException {
        storageApi.reduceProductQuantity(
                idProduct, new Order(quantityOrder));
    }

    /**
     * Payment of order
     *
     * @param numberCredit Credit bill number
     * @param totalPrice   Transaction total price
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    private ResponseEntity<?> payment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException {
        return billingApi.paymentOrder(
                new Transaction(
                        numberCredit,
                        Long.parseLong(storeBill),
                        totalPrice)
        );
    }

    /**
     * Payment rollback of order
     *
     * @param numberCredit Credit bill number
     * @param totalPrice   Transaction total price
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    private void rollbackPayment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException {
        billingApi.rollbackPaymentOrder(
                new Transaction(
                        numberCredit,
                        Long.parseLong(storeBill),
                        totalPrice)
        );
    }
}
