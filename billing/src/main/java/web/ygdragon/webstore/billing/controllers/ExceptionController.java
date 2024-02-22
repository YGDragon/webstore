package web.ygdragon.webstore.billing.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.ygdragon.webstore.billing.exceptions.BillNotFoundException;
import web.ygdragon.webstore.billing.exceptions.ExcessBillBalanceException;
import web.ygdragon.webstore.billing.exceptions.ResponseBodyException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {
    /**
     * Exception if the total price of order exceeds the bill balance
     *
     * @param exception Exception object
     * @return Exception response body
     */
    @ExceptionHandler(ExcessBillBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBodyException excessBillBalance(ExcessBillBalanceException exception) {
        return new ResponseBodyException(exception.getMessage(), LocalDateTime.now());
    }

    /**
     * Исключение при отсутствии заданного счета.
     *
     * @param exception объект исключения.
     * @return обертку с собственным исключением.
     */
    @ExceptionHandler(BillNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBodyException billNotFound(BillNotFoundException exception) {
        return new ResponseBodyException(exception.getMessage(), LocalDateTime.now());
    }
}