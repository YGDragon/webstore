package web.ygdragon.webstore.storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.ygdragon.webstore.storage.exceptions.ResponseBodyException;
import web.ygdragon.webstore.storage.exceptions.ExcessBalanceException;
import web.ygdragon.webstore.storage.exceptions.ProductNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    /**
     * Exception if the quantity of products exceeds the balance
     *
     * @param exception Exception object
     * @return Exception response body
     */
    @ExceptionHandler(ExcessBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBodyException excessBalance(ExcessBalanceException exception) {
        return new ResponseBodyException(exception.getMessage(), LocalDateTime.now());
    }

    /**
     * Exception if the bill not found in repository
     *
     * @param exception Exception object
     * @return Exception response body
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBodyException notFoundProduct(ProductNotFoundException exception) {
        return new ResponseBodyException(exception.getMessage(), LocalDateTime.now());
    }

}
