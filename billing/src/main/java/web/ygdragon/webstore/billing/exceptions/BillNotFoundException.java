package web.ygdragon.webstore.billing.exceptions;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(String message) {
        super(message);
    }
}