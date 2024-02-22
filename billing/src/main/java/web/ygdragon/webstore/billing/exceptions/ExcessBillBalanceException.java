package web.ygdragon.webstore.billing.exceptions;

public class ExcessBillBalanceException extends RuntimeException {
    public ExcessBillBalanceException(String message) {
        super(message);
    }
}
