package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final ProductItemRepository productItemRepository;

    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    public List<ProductItem> searchItems(String query) {
        Iterable<ProductItem> allItems = productItemRepository.findAll();
        List<ProductItem> itemList = new ArrayList<>();

        for (ProductItem item : allItems) {
            // TODO: Figure out if the item should be returned based on the query parameter!
            String search = query.trim().toLowerCase();
            boolean matchesSearch = item.getName().toLowerCase().contains(search) || item.getDescription().toLowerCase().contains(search);

            if (query.startsWith("\"") && query.endsWith("\"")) {
                String exactMatch = query.substring(1, query.length() - 1).trim().toLowerCase();
                matchesSearch = (item.getName().trim().equalsIgnoreCase(exactMatch) || item.getDescription().trim().equalsIgnoreCase(exactMatch));
            }

            if (matchesSearch) {
                itemList.add(item);
            }
        }
        return itemList;
    }
}
