package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.common.pageable.SortUtils;
import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private final ImageService imageService;

    private final Integer size = 10;

    @Transactional
    public void createStore(CreateStoreRequestDto request, List<MultipartFile> imageList) {
        User user = userService.loginUser();

        LocalTime openTime = LocalTime.parse(request.openTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closeTime = LocalTime.parse(request.closeTime(), DateTimeFormatter.ofPattern("HH:mm"));

        Store newStore = storeMapper.toStore(request, user, openTime, closeTime);
        storeRepository.save(newStore);

        ArrayList<StoreCategory> newStoreCategoryList = new ArrayList<>();

        for (UUID categoryId : request.categoriesId()) {
            Category category = categoryService.getCategoryById(categoryId);

            StoreCategory newStoreCategory = storeCategoryMapper.toStoreCategory(newStore, category);
            newStoreCategoryList.add(newStoreCategory);
        }

        storeCategoryRepository.saveAll(newStoreCategoryList);
        log.info("카테고리 : {} 개 - 가게 생성 완료", newStoreCategoryList.size());

        if (imageList != null && !imageList.isEmpty()) {
            List<String> imageUrls = imageList.stream()
                .map(
                    image -> imageService.uploadImage(
                        newStore.getId(), EntityType.STORE, image
                    )
                ).toList();

            log.info("가게 이미지 {}개 저장 완료", imageUrls.size());
        }
    }

    @Transactional(readOnly = true)
    public StoreDto getStores(int page, String sortDirection, String name) {
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Store> storeList = storeRepository.findAllStoreList(pageable, name);

        int totalStoreCount = storeList.size();

        return storeMapper.toStoreDto(
            storeList.stream().map(
                store -> storeMapper.toStoreDetailDto(
                    storeCategoryService.getCategoriesByStore(store).stream().map(
                        categoryMapper::toCategoryDto
                    ).toList(),
                    imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
                    store
                )
            ).toList(),
            page,
            (int) Math.ceil((double) totalStoreCount / size),
            totalStoreCount
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "store", key = "#p0")
    public StoreDetailDto getStore(UUID id) {
        Store store = getStoreById(id);

        return storeMapper.toStoreDetailDto(
            storeCategoryService.getCategoriesByStore(store).stream().map(
                categoryMapper::toCategoryDto
            ).toList(),
            imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
            store
        );
    }

    @Transactional(readOnly = true)
    public StoreByCategoryDto getStoresByCategory(int page, String sortDirection, UUID categoryId) {
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Category category = categoryService.getCategoryById(categoryId);

        List<OnlyStoreDto> storeList = storeCategoryService.getStoresByCategory(pageable, category).stream().map(
            store -> {
                return storeMapper.toOnlyStoreDto(store,
                    imageService.getImageUrlByEntity(store.getId(), EntityType.STORE));
            }
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
    public StoreDto getMyStores(int page, String sortDirection) {
        User user = userService.loginUser();
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Store> storeList = storeRepository.findAllStoreListByOwner(pageable, user.getId());

        int totalStoreCount = storeList.size();

        return storeMapper.toStoreDto(
            storeList.stream().map(
                store -> storeMapper.toStoreDetailDto(
                    storeCategoryService.getCategoriesByStore(store).stream().map(
                        categoryMapper::toCategoryDto
                    ).toList(),
                    imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
                    store
                )
            ).toList(),
            page,
            (int) Math.ceil((double) totalStoreCount / size),
            totalStoreCount
        );
    }

    @Transactional
    @CacheEvict(value = "store", key = "#p0")
    public void updateStore(UUID id, UpdateStoreRequestDto update, List<MultipartFile> imageList) {
        User user = userService.loginUser();
        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        store.update(update);

        imageService.deleteAllImagesByEntity(store.getId(), EntityType.STORE);

        if (imageList != null && !imageList.isEmpty()) {
            imageList.forEach(image -> {
                String imageUrl = imageService.uploadImage(store.getId(), EntityType.STORE, image);
                log.info("업로드된 음식점 이미지 URL: {}", imageUrl);
            });
        }

        // TODO: 카테고리 변경하는 로직 생각하기

        List<Category> categoryList = storeCategoryService.getCategoriesByStore(store);

        log.info("음식점: {}, 수정 완료", id);
    }

    @Transactional
    @CacheEvict(value = "store", key = "#p0")
    public void updateStoreStatus(UUID id, UpdateStoreStatusRequestDto update) {
        User user = userService.loginUser();
        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        store.updateStatus(update);
        log.info("가게: {}, 상태, {} - 변경 완료", id, update.status());
    }

    @CacheEvict(value = "store", key = "#p0")
    @Transactional
    public void deleteStore(UUID id) {
        User user = userService.loginUser();

        Store store = getStoreById(id);

        if (user.getRole() == Role.OWNER && !user.getId().equals(store.getOwner().getId())) {
            throw new BusinessException(ErrorCode.STORE_UNAUTHORIZED);
        }

        // TODO: 완료되지 않은 주문이 있을 경우, 가게 삭제 X

        store.delete();

        imageService.deleteAllImagesByEntity(id, EntityType.STORE);

        log.info("가게: {}, 삭제 완료", id);
    }


    public Store getStoreById(UUID id) {
        return storeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    public Store getStoreByIdAndIsDeletedIsFalse(UUID id) {
        return storeRepository.findByIdAndIsDeletedIsFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    public Store getStoreByOwnerId(Long ownerId) {
        return storeRepository.findByOwnerIdAndIsDeletedIsFalse(ownerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }
}