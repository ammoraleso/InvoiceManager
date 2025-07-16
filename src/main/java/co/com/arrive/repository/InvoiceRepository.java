package co.com.arrive.repository;


import co.com.arrive.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    //@Query("SELECT DISTINCT b FROM Book b WHERE b.library.id = :libraryId AND b.status = :status")
    // Page<Book> findByLibraryIdAndStatus(Long libraryId, Book.BookStatus status, Pageable pageable);
}