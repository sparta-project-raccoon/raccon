package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.ConfirmStoreRequestDto;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreStatusRequestDto;
import com.sparta.spartaproject.dto.response.StoreByCategoryDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.CategoryMapper;
import com.sparta.spartaproject.mapper.StoreCategoryMapper;
import com.sparta.spartaproject.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<StoreDetailDto> getStores(Pageable customPageable, String name) {
        Page<Store> storeList = storeRepository.findAllStoreList(customPageable, name);

        List<StoreDetailDto> storeDetailDtoList = storeList.stream().map(
            store -> storeMapper.toStoreDetailDto(
                store.getStoreCategories().stream().map(
                    storeCategory -> categoryMapper.toCategoryDto(
                        storeCategory.getCategory()
                    )
                ).toList(),
                imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
                store
            )
        ).toList();

        return new PageImpl<>(storeDetailDtoList, customPageable, storeList.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "store", key = "#id")
    public StoreDetailDto getStore(UUID id) {
        Store store = storeRepository.getStoreById(id);

        return storeMapper.toStoreDetailDto(
            store.getStoreCategories().stream().map(
                storeCategory -> categoryMapper.toCategoryDto(
                    storeCategory.getCategory()
                )
            ).toList(),
            imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
            store
        );
    }

    @Transactional(readOnly = true)
    public Page<StoreByCategoryDto> getStoresByCategory(Pageable customPageable, UUID categoryId) {
        Category category = categoryService.getCategoryById(categoryId);

        Page<Store> storeListByCategoryId = storeRepository.getStoreListByCategoryId(customPageable, category.getId());

        List<StoreByCategoryDto> storeByCategoryDtoList = storeListByCategoryId.stream().map(
            store -> storeMapper.toStoreByCategoryDto(
                categoryMapper.toCategoryDto(category),
                imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
                store
            )
        ).toList();

        return new PageImpl<>(storeByCategoryDtoList, customPageable, storeListByCategoryId.getTotalElements());
    }

    @Transactional(readOnly = true)
    public StoreDetailDto getMyStores() {
        User user = userService.loginUser();

        Store store = storeRepository.findStoreListByOwner(user.getId());

        return storeMapper.toStoreDetailDto(
            store.getStoreCategories().stream().map(
                storeCategory -> categoryMapper.toCategoryDto(storeCategory.getCategory())
            ).toList(),
            imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
            store
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

    @Transactional
    public Page<StoreDetailDto> getUnconfirmedStores(Pageable customPageable) {
        Page<Store> unconfirmedStoreList = storeRepository.findAllByUnConfirmed(customPageable);

        List<StoreDetailDto> storeDetailDtoList = unconfirmedStoreList.stream().map(
            store -> storeMapper.toStoreDetailDto(
                store.getStoreCategories().stream().map(
                    storeCategory -> categoryMapper.toCategoryDto(
                        storeCategory.getCategory()
                    )
                ).toList(),
                imageService.getImageUrlByEntity(store.getId(), EntityType.STORE),
                store
            )
        ).toList();

        return new PageImpl<>(storeDetailDtoList, customPageable, unconfirmedStoreList.getTotalElements());
    }

    @Transactional
    public void confirmedStore(ConfirmStoreRequestDto update) {
        Store store = getStoreById(update.storeId());

        if (store.getIsConfirmed() == Boolean.TRUE) {
            throw new BusinessException(ErrorCode.ALREADY_IS_TRUE);
        }

        store.confirm();

        log.info("가계: {}, 승인 완료", update.storeId());
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