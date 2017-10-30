package me.seaOf.service;

import me.seaOf.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements  ItemService {
    @Autowired
    private ItemMapper itemMapper;


    @Override
    public int findCount() {
        return itemMapper.findCount();
    }
}
