package co.com.arrive.service;

import co.com.arrive.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAll(int page, int size);
}
