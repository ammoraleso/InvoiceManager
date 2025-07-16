package co.com.arrive.web;

import co.com.arrive.dto.InvoiceDTO;
import co.com.arrive.dto.ItemDTO;
import co.com.arrive.service.InvoiceService;
import co.com.arrive.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return invoiceService.getAll(page, size);
    }

    @PostMapping
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO request) {
        return invoiceService.createInvoice(request);
    }
}
