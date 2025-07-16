package co.com.arrive.service;

import co.com.arrive.dto.InvoiceDTO;
import co.com.arrive.dto.ItemDTO;

import java.util.List;

public interface InvoiceService {

    List<InvoiceDTO> getAll(int page, int size);

    InvoiceDTO createInvoice(InvoiceDTO request);
}
