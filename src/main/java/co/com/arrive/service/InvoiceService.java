package co.com.arrive.service;

import co.com.arrive.dto.InvoiceDTO;
import co.com.arrive.dto.InvoiceLineItemDTO;
import co.com.arrive.dto.ItemDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface InvoiceService {

    List<InvoiceDTO> getAll(int page, int size);

    InvoiceDTO createInvoice(InvoiceDTO request);

    InvoiceDTO addItemsToInvoice(Long invoiceId, List<InvoiceLineItemDTO> items);

    InvoiceDTO getById(Long id);

    InvoiceDTO markAsPaid(Long invoiceId, BigDecimal amountPaid);

    InvoiceDTO payInvoice(Long invoiceId, BigDecimal amountPaid);
}
