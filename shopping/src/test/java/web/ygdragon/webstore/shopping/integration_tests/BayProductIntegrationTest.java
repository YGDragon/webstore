package web.ygdragon.webstore.shopping.integration_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import web.ygdragon.webstore.shopping.models.Order;
import web.ygdragon.webstore.shopping.models.Transaction;
import web.ygdragon.webstore.shopping.cofig.ConfigIntegrationTests;
import web.ygdragon.webstore.shopping.external_api.BillingApi;
import web.ygdragon.webstore.shopping.external_api.StorageApi;
import web.ygdragon.webstore.shopping.services.ShoppingService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(ConfigIntegrationTests.class)
public class BayProductIntegrationTest {

    @MockBean
    private BillingApi billingApi;

    @MockBean
    private StorageApi storageApi;

    @Autowired
    private ShoppingService shoppingService;

    /**
     * Checking product payment success
     */
    @Test
    public void testBuyProductSuccess() {
        Long productId = 1L;
        int quantity = 1;
        BigDecimal totalPrice = new BigDecimal(100);
        Long numberCredit = 1L;

        Mockito.when(storageApi.reservationProduct(anyLong(), any(Order.class)))
                .thenReturn(
                        ResponseEntity.ok().build());

        Mockito.when(billingApi.paymentOrder(any(Transaction.class)))
                .thenReturn(
                        ResponseEntity.ok().build());

        Mockito.when(storageApi.reduceProductQuantity(anyLong(), any(Order.class)))
                .thenReturn(
                        ResponseEntity.ok().build());

        shoppingService.paymentOrder(productId, quantity, totalPrice, numberCredit);


        verify(storageApi).reservationProduct(anyLong(), any(Order.class));
        verify(billingApi).paymentOrder(any(Transaction.class));
        verify(storageApi).reduceProductQuantity(anyLong(), any(Order.class));

        verify(storageApi, never()).reservationProductRollback(anyLong(), any(Order.class));
        verify(billingApi, never()).rollbackPaymentOrder(any(Transaction.class));
    }
}