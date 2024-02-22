package web.ygdragon.webstore.shopping.models;

import java.math.BigDecimal;

public record Transaction (Long numberCredit, Long numberDebit, BigDecimal totalPrice) {
}
