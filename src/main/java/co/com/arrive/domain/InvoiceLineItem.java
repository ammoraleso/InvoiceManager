package co.com.arrive.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class InvoiceLineItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Invoice invoice;

    @ManyToOne
    private Item item;

    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
