package co.com.arrive.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long id;
    private String description;
    private BigDecimal basePrice;
}
