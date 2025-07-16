package co.com.arrive.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceLineItemDTO {
    private Long id;
    private ItemDTO item;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}


