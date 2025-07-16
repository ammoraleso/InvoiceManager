package co.com.arrive.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private BigDecimal amountPaid;
}

