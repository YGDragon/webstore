package web.ygdragon.webstore.shopping.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import web.ygdragon.webstore.shopping.aspects.LogLeadTime;
import web.ygdragon.webstore.shopping.external_api.BillingApi;
import web.ygdragon.webstore.shopping.external_api.StorageApi;
import web.ygdragon.webstore.shopping.models.Order;
import web.ygdragon.webstore.shopping.models.Product;
import web.ygdragon.webstore.shopping.models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementing a design pattern ADAPTER
 */
@Service
public class ShoppingAdapter implements ShoppingService {

    private static final String TITLE_FILE =
            "client_actions" + ".txt";

    private final BillingApi billingApi;
    private final StorageApi storageApi;
    private final FileGateway fileGateway;
    private final String storeBill;

    public ShoppingAdapter(
            BillingApi billingApi,
            StorageApi storageApi,
            FileGateway fileGateway,
            @Value("${shop.account.number}") String storeBill) {
        this.billingApi = billingApi;
        this.storageApi = storageApi;
        this.fileGateway = fileGateway;
        this.storeBill = storeBill;
    }

    /**
     * Getting current formatted data/time
     *
     * @return Current data - time
     */
    private String getDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");
        return dateTime.format(formatter);
    }

    private String getMessage(String textAction, Long idProduct, int quantity) {
        return getDateTime() + textAction + storageApi.getProduct(idProduct).title()
                + " - by " + quantity + " units";
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
     * Payment for the order
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

                fileGateway.writeToFile(
                        TITLE_FILE, getMessage(
                                " action >> reducing the quantity of product: ",
                                idProduct, quantityOrder)
                );

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
    public ResponseEntity<?> reservation(Long idProduct, int quantityProduct) throws HttpClientErrorException {

        fileGateway.writeToFile(
                TITLE_FILE, getMessage(
                        " action >> reserve the quantity of product: ",
                        idProduct, quantityProduct)
        );


        return storageApi.reservationProduct(idProduct, new Order(quantityProduct));
    }

    /**
     * Rollback a product reservation in a stock
     *
     * @param idProduct Product ID
     * @throws HttpClientErrorException Exception http status from client application
     */
    @LogLeadTime
    public void rollbackReserve(Long idProduct, int quantityOrder) throws HttpClientErrorException {
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
    public void reduceQuantity(Long idProduct, int quantityOrder) throws HttpClientErrorException {
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
    public ResponseEntity<?> payment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException {
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
    public void rollbackPayment(Long numberCredit, BigDecimal totalPrice) throws HttpClientErrorException {
        billingApi.rollbackPaymentOrder(
                new Transaction(
                        numberCredit,
                        Long.parseLong(storeBill),
                        totalPrice)
        );
    }
}