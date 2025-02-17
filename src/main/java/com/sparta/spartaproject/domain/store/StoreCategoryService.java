package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreCategoryService {
    private final StoreCategoryRepository storeCategoryRepository;

    public List<Category> getCategoriesByStore(Store store) {
        return storeCategoryRepository.findAllCategoryListByStore(store);
    }

    public List<Store> getStoresByCategory(Pageable pageable, Category category) {
        return storeCategoryRepository.findAllStoreListByCategory(pageable, category);
    }
}