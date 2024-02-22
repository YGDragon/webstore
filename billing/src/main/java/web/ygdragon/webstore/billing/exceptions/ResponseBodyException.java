package web.ygdragon.webstore.billing.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseBodyException {
    private String message;
    private LocalDateTime dateTime;

    public ResponseBodyException(String message, LocalDateTime dateTime) {
        this.message = message;
        this.dateTime = dateTime;
    }
}