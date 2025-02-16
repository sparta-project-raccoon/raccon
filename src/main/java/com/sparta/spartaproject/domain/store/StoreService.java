package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreSummaryDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final UserService userService;
    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;
    private final CategoryService categoryService;

    @Transactional
    @CacheEvict(value = "stores", allEntries = true) // 전체 캐시 삭제
    public void createStore(CreateStoreRequestDto request) {
        User user = userService.loginUser();

        validateUserRole(user);

        Category category = categoryService.getCategoryById(request.categoryId());

        Store store = Store.builder()
            .owner(user)
            .category(category)
            .name(request.name())
            .address(request.address())
            .status(request.status())
            .tel(request.tel())
            .description(request.description())
            .openTime(request.openTime())
            .closeTime(request.closeTime())
            .closedDays(request.closedDays())
            .build();
        storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "store", key = "#storeId")
    public StoreDetailDto getStore(UUID storeId) {
        Store store = getStoreById(storeId);

        return storeMapper.toStoreDetailDto(store);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "stores", key = "'all'")
    public Page<StoreSummaryDto> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).map(
            storeMapper::toStoreSummaryDto
        );
    }

    @Cacheable(value = "stores")
    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> getAllStoresByCategoryId(UUID categoryId, Pageable pageable) {
        Category category = categoryService.getCategoryById(categoryId);

        return storeRepository.findByCategory(category, pageable).map(
            storeMapper::toStoreSummaryDto
        );
    }

    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> getMyStores(Pageable pageable) {
        User user = userService.loginUser();

        validateUserRole(user);

        return storeRepository.findByOwner(user, pageable).map(
            storeMapper::toStoreSummaryDto
        );
    }

    @Transactional
    @CacheEvict(value = {"store", "stores"}, key = "#storeId") // 개별 가게 및 전체 가게 리스트 캐시 삭제
    public void updateStore(UUID storeId, UpdateStoreRequestDto update) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = getStoreById(storeId);

        Category category = categoryService.getCategoryById(update.categoryId());

        store.update(update, category);
        log.info("음식점: {}, 수정 완료", storeId);
    }

    @CacheEvict(value = {"store", "stores"}, key = "#storeId")
    @Transactional
    public void updateStoreStatus(UUID storeId, Status status) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = getStoreById(storeId);

        store.updateStatus(status);
    }

    @CacheEvict(value = {"store", "stores"}, key = "#storeId")
    @Transactional
    public void deleteStore(UUID storeId) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = getStoreById(storeId);

        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> searchStores(String searchWord, Pageable pageable) {
        return storeRepository.findByNameContaining(pageable, searchWord).map(
            storeMapper::toStoreSummaryDto
        );
    }


    public Store getStoreById(UUID id) {
        return storeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    // TODO:  @PreAuthorize 으로 인증 처리 대체 예정
    private void validateUserRole(User user) {
        if (!(user.getRole() == Role.MANAGER || user.getRole() == Role.OWNER)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}