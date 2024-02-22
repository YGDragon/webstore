package web.ygdragon.webstore.storage.exceptions;

public class ExcessBalanceException extends RuntimeException {
    public ExcessBalanceException(String message) {
        super(message);
    }
}