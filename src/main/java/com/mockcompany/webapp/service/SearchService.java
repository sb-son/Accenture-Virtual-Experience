package com.mockcompany.webapp.service;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    // After reading code/tests, we can capture the important terms in an array!
    private static final String[] importantTerms = new String[] {
            "Cool",
            "Amazing",
            "Perfect",
            "Kids"
    };

    public SearchReportResponse searchReport() {
        Map<String, Integer> hits = new HashMap<>();
        SearchReportResponse response = new SearchReportResponse();
        response.setSearchTermHits(hits);

        // For each important term, query on it and add size of results to our Map
        for (String term : importantTerms) {
            hits.put(term, countTerm(term));
        }
        response.setProductCount(productItemRepository.countAll());

        return response;
    }

    private int countTerm(String term) {
        return productItemRepository.countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(term, term);
    }
}
