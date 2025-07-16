package co.com.arrive.service;

import co.com.arrive.domain.Item;
import co.com.arrive.dto.ItemDTO;
import co.com.arrive.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> pageResult = itemRepository.findAll(pageable);
        List<Item> courses = pageResult.getContent();
        return courses.stream().map(course -> modelMapper.map(course, ItemDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        Item item = modelMapper.map(itemDTO, Item.class);
        Item savedItem = itemRepository.save(item);
        return modelMapper.map(savedItem, ItemDTO.class);
    }
}