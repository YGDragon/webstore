package web.ygdragon.webstore.billing.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.ygdragon.webstore.billing.exceptions.ExcessBillBalanceException;
import web.ygdragon.webstore.billing.models.Bill;
import web.ygdragon.webstore.billing.models.Transaction;
import web.ygdragon.webstore.billing.repositories.BillingRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final BillingRepository billingRepository;

    /**
     * Getting all bills from repository
     *
     * @return List of bills
     */
    public List<Bill> getAllBills() {
        return billingRepository.findAll();
    }

    /**
     * Transaction operation
     *
     * @param transaction Transaction object
     */
    @Transactional
    public void makeTransaction(Transaction transaction) {
        Bill billCredit = billingRepository.findByNumber(
                transaction.getNumberCredit());
        Bill billDebit = billingRepository.findByNumber(
                transaction.getNumberDebit());
        BigDecimal totalPriceTransaction = transaction.getTotalPrice();

        if (billCredit.getBalance().compareTo(totalPriceTransaction) < 0) {
            throw new ExcessBillBalanceException("INFO >> Zero bill balance!");
        }

        billingRepository.save(
                reduceBillBalance(billCredit, totalPriceTransaction));
        billingRepository.save(
                increaseBillBalance(billDebit, totalPriceTransaction));
    }

    /**
     * Rollback transaction operation
     *
     * @param transaction Transaction object
     */
    @Transactional
    public void rollbackTransaction(Transaction transaction) {
        Bill billCredit = billingRepository.findByNumber(
                transaction.getNumberCredit());
        Bill billDebit = billingRepository.findByNumber(
                transaction.getNumberDebit());
        BigDecimal totalPriceTransaction = transaction.getTotalPrice();

        billingRepository.save(
                reduceBillBalance(billDebit, totalPriceTransaction));
        billingRepository.save(
                increaseBillBalance(billCredit, totalPriceTransaction));
    }

    /**
     * Reduce bill balance
     *
     * @param bill       Bill object
     * @param totalPrice Transaction total price
     * @return Bill object
     */
    public Bill reduceBillBalance(Bill bill, BigDecimal totalPrice) {
        bill.setBalance(
                bill.getBalance()
                        .subtract(totalPrice)
        );
        return bill;
    }

    /**
     * Increase bill balance
     *
     * @param bill       Bill object
     * @param totalPrice Transaction total price
     * @return Bill object
     */
    public Bill increaseBillBalance(Bill bill, BigDecimal totalPrice) {
        bill.setBalance(
                bill.getBalance()
                        .add(totalPrice)
        );
        return bill;
    }
}