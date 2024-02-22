package web.ygdragon.webstore.billing.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transaction {

    private Long numberCredit;
    private Long numberDebit;
    private BigDecimal totalPrice;

}