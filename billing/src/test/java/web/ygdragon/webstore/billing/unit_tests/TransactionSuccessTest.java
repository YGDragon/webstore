package web.ygdragon.webstore.billing.unit_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.ygdragon.webstore.billing.models.Bill;
import web.ygdragon.webstore.billing.models.Transaction;
import web.ygdragon.webstore.billing.repositories.BillingRepository;
import web.ygdragon.webstore.billing.services.BillingService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TransactionSuccessTest {

    @Mock
    private BillingRepository billingRepository;

    @InjectMocks
    private BillingService billingService;

    @Test
    public void testPaymentSuccess() {
        /* specifying settings for testing */
        Long numberCredit = 1L;
        Long numberDebit = 2L;
        BigDecimal totalPrice = new BigDecimal(100);
        Transaction transaction = new Transaction(
                numberCredit,
                numberDebit,
                totalPrice);

        BigDecimal creditBalance = new BigDecimal(1000);
        Bill billCredit = new Bill();
        billCredit.setBalance(creditBalance);

        BigDecimal debitBalance = new BigDecimal(0);
        Bill billDebit = new Bill();
        billDebit.setBalance(debitBalance);

        /* specifying Mock object behavior */
        given(billingRepository.findByNumber(numberCredit)).willReturn(billCredit);
        given(billingRepository.findByNumber(numberDebit)).willReturn(billDebit);
        billingService.makeTransaction(transaction);

        /* checking called methods */
        verify(billingRepository).findByNumber(numberCredit);
        verify(billingRepository).save(billCredit);
        verify(billingRepository).findByNumber(numberDebit);
        verify(billingRepository).save(billDebit);

        /* comparison of received data of called methods */
        assertEquals(
                creditBalance.subtract(totalPrice), billCredit.getBalance());
        assertEquals(
                debitBalance.add(totalPrice), billDebit.getBalance());
    }
}
