package co.com.arrive.repository;

import co.com.arrive.domain.InvoiceLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, Long> {

}