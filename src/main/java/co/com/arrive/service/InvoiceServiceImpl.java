package co.com.arrive.service;

import co.com.arrive.domain.Invoice;
import co.com.arrive.domain.InvoiceLineItem;
import co.com.arrive.domain.Item;
import co.com.arrive.dto.InvoiceDTO;
import co.com.arrive.dto.InvoiceLineItemDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        request.setLineItems(consolidateLineItems(request.getLineItems()));
        processLineItemsToSave(request.getLineItems(), invoice);

        invoiceRepository.save(invoice);

        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    @Override
    @Transactional
    public InvoiceDTO addItemsToInvoice(Long invoiceId, List<InvoiceLineItemDTO> newItems) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        if (invoice.getDeletedAt() != null) {
            throw new RuntimeException("Invoice is deleted and cannot be updated.");
        }

        List<InvoiceLineItem> existingLineItems = invoice.getLineItems();
        List<InvoiceLineItemDTO> existingLineItemsDTO = InvoiceLineItemDTO.mapEntity2DTO(existingLineItems, modelMapper);
        newItems.addAll(existingLineItemsDTO);
        List<InvoiceLineItemDTO> consolidatedNewItems = consolidateLineItems(newItems);

        processLineItemsToSave(consolidatedNewItems, invoice);

        invoiceRepository.save(invoice);

        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        if (invoice.getDeletedAt() != null) {
            throw new RuntimeException("Invoice with id " + id + " has been deleted.");
        }

        return modelMapper.map(invoice, InvoiceDTO.class);
    }


    private List<InvoiceLineItemDTO> consolidateLineItems(List<InvoiceLineItemDTO> invoiceLineItemDTOS) {

        if (invoiceLineItemDTOS == null || invoiceLineItemDTOS.isEmpty()) {
            throw new RuntimeException("Invoice cannot be created without items.");
        }
        Map<Long, InvoiceLineItemDTO> consolidatedMap = new HashMap<>();

        for (InvoiceLineItemDTO dto : invoiceLineItemDTOS) {
            Long itemId = dto.getItem().getId();
            if (itemId == null) {
                throw new IllegalArgumentException("Item ID cannot be null");
            }

            if (consolidatedMap.containsKey(itemId)) {
                InvoiceLineItemDTO existing = consolidatedMap.get(itemId);
                existing.setQuantity(existing.getQuantity() + dto.getQuantity());

                if (existing.getId() == null && dto.getId() != null) {
                    existing.setId(dto.getId());
                }
            } else {
                InvoiceLineItemDTO copy = new InvoiceLineItemDTO();
                copy.setId(dto.getId());
                copy.setItem(dto.getItem());
                copy.setQuantity(dto.getQuantity());
                copy.setTotalPrice(dto.getTotalPrice());
                consolidatedMap.put(itemId, copy);
            }
        }

        return new ArrayList<>(consolidatedMap.values());
    }

    private InvoiceLineItem fillLineItemObject(InvoiceLineItemDTO dto, Invoice invoice, Item item) {
        InvoiceLineItem lineItem = new InvoiceLineItem();
        lineItem.setId(dto.getId());
        lineItem.setInvoice(invoice);
        lineItem.setItem(item);
        lineItem.setQuantity(dto.getQuantity());
        lineItem.setBasePrice(item.getBasePrice());

        lineItem.setTotalPrice(item.getBasePrice().multiply(BigDecimal.valueOf(dto.getQuantity())));

        return lineItem;
    }

    private List<InvoiceLineItem> processLineItemsToSave(List<InvoiceLineItemDTO> lineItems, Invoice invoice) {
        List<InvoiceLineItem> finalLineItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InvoiceLineItemDTO dto : lineItems) {
            Item item = itemRepository.findById(dto.getItem().getId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + dto.getItem().getId()));

            InvoiceLineItem lineItem = fillLineItemObject(dto, invoice, item);
            totalAmount = totalAmount.add(lineItem.getTotalPrice());
            finalLineItems.add(lineItem);
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setLineItems(finalLineItems);

        return finalLineItems;
    }

}