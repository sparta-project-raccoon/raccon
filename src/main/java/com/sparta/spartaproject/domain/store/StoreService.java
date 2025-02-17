package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreStatusRequestDto;
import com.sparta.spartaproject.dto.response.OnlyStoreDto;
import com.sparta.spartaproject.dto.response.StoreByCategoryDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.CategoryMapper;
import com.sparta.spartaproject.mapper.StoreCategoryMapper;
import com.sparta.spartaproject.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreMapper storeMapper;
    private final UserService userService;
    private final CategoryMapper categoryMapper;
    private final StoreRepository storeRepository;
    private final CategoryService categoryService;
    private final StoreCategoryMapper storeCategoryMapper;
    private final StoreCategoryService storeCategoryService;
    private final StoreCategoryRepository storeCategoryRepository;

    private final Integer size = 10;

    @Transactional
    public void createStore(CreateStoreRequestDto request) {
        User user = userService.loginUser();

        Store newStore = storeMapper.toStore(request, user);
        storeRepository.save(newStore);

        ArrayList<StoreCategory> newStoreCategoryList = new ArrayList<>();

        for (UUID categoryId : request.categoriesId()) {
            Category category = categoryService.getCategoryById(categoryId);

            StoreCategory newStoreCategory = storeCategoryMapper.toStoreCategory(newStore, category);
            newStoreCategoryList.add(newStoreCategory);
        }

        storeCategoryRepository.saveAll(newStoreCategoryList);
        log.info("카테고리 : {} 개 - 가게 생성 완료", newStoreCategoryList.size());
    }

    @Transactional(readOnly = true)
    public StoreDto getStores(int page, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);

        List<Store> storeList = storeRepository.findAllStoreList(pageable, name);

        int totalStoreCount = storeList.size();

        return storeMapper.toStoreDto(
            storeList.stream().map(
                store -> storeMapper.toStoreDetailDto(
                    storeCategoryService.getCategoriesByStore(store).stream().map(
                        categoryMapper::toCategoryDto
                    ).toList(),
                    store
                )
            ).toList(),
            page,
            (int) Math.ceil((double) totalStoreCount / size),
            totalStoreCount
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "store", key = "#id")
    public StoreDetailDto getStore(UUID id) {
        Store store = getStoreById(id);

        return storeMapper.toStoreDetailDto(
            storeCategoryService.getCategoriesByStore(store).stream().map(
                categoryMapper::toCategoryDto
            ).toList(),
            store
        );
    }

    @Transactional(readOnly = true)
    public StoreByCategoryDto getStoresByCategory(int page, UUID categoryId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Category category = categoryService.getCategoryById(categoryId);

        List<OnlyStoreDto> storeList = storeCategoryService.getStoresByCategory(pageable, category).stream().map(
            storeMapper::toOnlyStoreDto
        ).toList();

        int totalStoreCount = storeList.size();

        return storeMapper.toStoreByCategoryDto(
            storeList,
            page,
            (int) Math.ceil((double) totalStoreCount / size),
            totalStoreCount
        );
    }

    @Transactional(readOnly = true)
    public List<StoreDetailDto> getMyStores() {
        User user = userService.loginUser();

        List<Store> stores = storeRepository.findAllStoreListByOwner(user);

        return stores.stream().map(
            store -> storeMapper.toStoreDetailDto(
                storeCategoryService.getCategoriesByStore(store).stream().map(
                    categoryMapper::toCategoryDto
                ).toList(),
                store
            )
        ).toList();
    }

    @Transactional
    @CacheEvict(value = "store", key = "#id")
    public void updateStore(UUID id, UpdateStoreRequestDto update) {
        User user = userService.loginUser();

        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        store.update(update);

        List<Category> category = storeCategoryService.getCategoriesByStore(store);

        // TODO: 카테고리 변경하는 로직 생각하기

        log.info("음식점: {}, 수정 완료", id);
    }

    @Transactional
    @CacheEvict(value = "store", key = "#id")
    public void updateStoreStatus(UUID id, UpdateStoreStatusRequestDto update) {
        User user = userService.loginUser();

        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        store.updateStatus(update);
        log.info("가게: {}, 상태, {} - 변경 완료", id, update.status());
    }

    @CacheEvict(value = "store", key = "#id")
    @Transactional
    public void deleteStore(UUID id) {
        User user = userService.loginUser();

        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        // TODO: 완료된 주문이 있을 경우, 가게 삭제 X

        storeRepository.delete(store);
        log.info("가게: {}, 삭제 완료", id);
    }


    public Store getStoreById(UUID id) {
        return storeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }
}