package co.com.arrive.service;

import co.com.arrive.domain.Invoice;
import co.com.arrive.domain.InvoiceLineItem;
import co.com.arrive.domain.Item;
import co.com.arrive.dto.InvoiceDTO;
import co.com.arrive.dto.InvoiceLineItemDTO;
import co.com.arrive.dto.ItemDTO;
import co.com.arrive.repository.InvoiceRepository;
import co.com.arrive.repository.ItemRepository;
import co.com.arrive.utils.InvoiceStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> pageResult = invoiceRepository.findAllByDeletedAtIsNull(pageable);
        List<Invoice> courses = pageResult.getContent();
        return courses.stream().map(course -> modelMapper.map(course, InvoiceDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceDTO createInvoice(InvoiceDTO request) {
        Invoice invoice = new Invoice();
        invoice.setCustomer(request.getCustomer());
        invoice.setDate(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.DRAFT);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceLineItem> lineItems = new ArrayList<>();

        for (InvoiceLineItemDTO dto : request.getLineItems()) {
            Item item = itemRepository.findById(dto.getItem().getId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + dto.getItem().getId()));

            InvoiceLineItem lineItem = new InvoiceLineItem();
            lineItem.setInvoice(invoice);
            lineItem.setItem(item);
            lineItem.setQuantity(dto.getQuantity());
            lineItem.setTotalPrice(item.getBasePrice().multiply(BigDecimal.valueOf(dto.getQuantity())));

            totalAmount = totalAmount.add(lineItem.getTotalPrice());
            lineItems.add(lineItem);
        }

        invoice.setTotalAmount(totalAmount);
        invoice.setLineItems(lineItems);

        invoiceRepository.save(invoice);

        return modelMapper.map(invoice, InvoiceDTO.class);
    }
}