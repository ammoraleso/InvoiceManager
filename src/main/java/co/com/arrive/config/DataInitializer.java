package co.com.arrive.config;

import co.com.arrive.domain.Item;
import co.com.arrive.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    public DataInitializer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) {
        if (itemRepository.count() == 0) {
            try {
                Item item1 = new Item();
                item1.setDescription("Widget A");
                item1.setBasePrice(new BigDecimal("10.00"));

                Item item2 = new Item();
                item2.setDescription("Widget B");
                item2.setBasePrice(new BigDecimal("15.50"));

                Item item3 = new Item();
                item3.setDescription("Gadget C");
                item3.setBasePrice(new BigDecimal("7.25"));

                Item item4 = new Item();
                item4.setDescription("Tool D");
                item4.setBasePrice(new BigDecimal("20.00"));

                Item item5 = new Item();
                item5.setDescription("Accessory E");
                item5.setBasePrice(new BigDecimal("5.75"));

                itemRepository.saveAll(List.of(item1, item2, item3, item4, item5));

                log.info("5 Items initialized in DB!");
            } catch (Exception e) {
                log.error("Error creating items. " + e.getMessage());
            }
        } else {
            log.info("Items already exist in DB, skipping initialization.");
        }
    }
}