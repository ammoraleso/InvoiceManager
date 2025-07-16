package co.com.arrive.dto;

import co.com.arrive.domain.InvoiceLineItem;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InvoiceLineItemDTO {
    private Long id;
    private ItemDTO item;
    private Integer quantity;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;

    public static List<InvoiceLineItemDTO> mapEntity2DTO(List<InvoiceLineItem> existingLineItems, ModelMapper modelMapper){
        List<InvoiceLineItemDTO> existingDTOs = existingLineItems.stream()
                .map(entity -> modelMapper.map(entity, InvoiceLineItemDTO.class))
                .collect(Collectors.toList());
        return existingDTOs;
    }
}


