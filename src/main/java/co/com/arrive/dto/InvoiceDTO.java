package co.com.arrive.dto;

import co.com.arrive.utils.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceDTO {
    private Long id;
    private String customer;
    private LocalDateTime date;
    private BigDecimal totalAmount;
    private BigDecimal partialAmount;
    private InvoiceStatus status;
    private List<InvoiceLineItemDTO> lineItems;
    private LocalDateTime paidAt;
}


