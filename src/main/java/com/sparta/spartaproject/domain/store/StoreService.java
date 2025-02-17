package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.category.CategoryRepository;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreSummaryDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final StoreMapper storeMapper;

    @CacheEvict(value = "stores", allEntries = true) // 전체 캐시 삭제
    @Transactional
    public void createStore(CreateStoreRequestDto request) {
        // 인증 정보 & 유저 정보 가져오기
        User user = userService.loginUser();

        // 권한 검증
        validateUserRole(user);

        // 카테고리 엔티티 가져오기
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        // 가게 등록
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

    @Cacheable(value = "store", key = "#storeId")
    @Transactional(readOnly = true)
    public StoreDetailDto getStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다.") );

        return storeMapper.toStoreDetailDto(store);
    }

    @Cacheable(value = "stores", key = "'all'")
    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> getAllStores(Pageable pageable) {
        return  storeRepository.findAll(pageable)
                .map(storeMapper::toStoreSummaryDto);
    }

    @Cacheable(value = "stores")
    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> getAllStoresByCategoryId(UUID categoryId, Pageable pageable) {
        // 카테고리 가져오기
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        return storeRepository.findByCategory(category, pageable)
                .map(storeMapper::toStoreSummaryDto);
    }

    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> getMyStores(Pageable pageable) {
        User user = userService.loginUser();

        validateUserRole(user);

        return storeRepository.findByOwner(user, pageable)
                .map(storeMapper::toStoreSummaryDto);
    }

    @CacheEvict(value = {"store", "stores"}, key = "#storeId") // 개별 가게 및 전체 가게 리스트 캐시 삭제
    @Transactional
    public void updateStore(UUID storeId, UpdateStoreRequestDto update) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

        Category category = categoryRepository.findById(update.categoryId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        // 음식점 수정
        store.update(update, category);
    }

    @CacheEvict(value = {"store", "stores"}, key = "#storeId")
    @Transactional
    public void updateStoreStatus(UUID storeId, Status status) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

        // 음식점 상태 수정
        store.updateStatus(status);
    }

    @CacheEvict(value = {"store", "stores"}, key = "#storeId")
    @Transactional
    public void deleteStore(UUID storeId) {
        User user = userService.loginUser();

        validateUserRole(user);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

        // 음식점 삭제
        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
    public Page<StoreSummaryDto> searchStores(String searchWord, Pageable pageable) {
        return storeRepository.findByNameContainingOrDescriptionContaining(searchWord, searchWord, pageable)
                .map(storeMapper::toStoreSummaryDto) ;
    }

    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

    }

    // TODO:  @PreAuthorize 으로 인증 처리 대체 예정
    private void validateUserRole(User user){
        if (!(user.getRole() == Role.MANAGER || user.getRole() == Role.OWNER)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

}