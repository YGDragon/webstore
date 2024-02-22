package web.ygdragon.webstore.shopping.models;

import java.math.BigDecimal;

public record Product (Long id, String title, int quantity, BigDecimal price) {
}
