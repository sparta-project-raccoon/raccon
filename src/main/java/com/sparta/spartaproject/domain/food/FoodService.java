package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.common.FileUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodMapper foodMapper;
    private final FoodRepository foodRepository;
    private final CircularService circularService;

    @Transactional
    public void createFood(CreateFoodRequestDto request, MultipartFile images) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        String imagePathForFood = null;

        if (images != null && !images.isEmpty()) {
            imagePathForFood = FileUtils.saveFile(images, "food");
            log.info("음식 사진 등록 : {}", imagePathForFood);
        }

        String descriptionForGemini = circularService.getGeminiService().requestGemini(
            store.getId(), request.description()
        );

        Food newFood = foodMapper.toFood(request, store, descriptionForGemini, imagePathForFood);
        foodRepository.save(newFood);

        log.info("가게: {}, 음식 생성 완료", store.getId());
    }

    @Transactional
    public void updateFood(UUID id, UpdateFoodRequestDto update, MultipartFile images) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.update(update);

        if (images != null && !isSameImage(food.getImagePath(), images)) {
            FileUtils.deleteFile(food.getImagePath());

            String newImagePath = FileUtils.saveFile(images, "food");
            food.updateImagePath(newImagePath);
        }

        log.info("음식 수정 완료 : {}, image:{}", food.getName(), food.getImagePath());
    }

    @Transactional
    public void updateFoodStatus(UUID id, UpdateFoodStatusRequestDto update) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.updateStatus(update);
        log.info("음식: {},  상태 정보 수정 완료", food.getId());
    }

    @Transactional
    public void toggleDisplay(UUID id) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        if (food.getIsDisplayed().equals(Boolean.TRUE)) {
            log.info("음식: {}, 숨김 처리", food.getId());
        } else {
            log.info("음식: {}, 숨김 처리 해제", food.getId());
        }

        food.toggleIsDisplayed();
    }

    // 음식 삭제
    @Transactional
    public void deleteFoodWithImage(UUID id) {
        Food food = getFoodById(id);

        // 음식점 사장 확인
        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        // 음식 삭제 여부 및 삭제 일시 업데이트
        food.delete();

        // 음식 사진 삭제
        FileUtils.deleteFile(food.getImagePath());

        // TODO: 음식 아예 삭제 할 것인지? 안한다면 음식 이미지는 파일시스템에서 삭제하고 , imagePath도 삭제할 것인지?
//        foodRepository.delete(food);
    }


    // 기존 이미지와 새로운 이미지 동일 여부 확인
    private boolean isSameImage(String existingImagePath, MultipartFile newImage) {
        if (existingImagePath == null || newImage == null) {
            return false;
        }

        File existingFile = new File(existingImagePath);

        if (!existingFile.exists()) {
            return false;
        }

        try {
            // TODO: 기존 파일 크기와 새로운 파일 크기 비교
            long existingFileSize = Files.size(existingFile.toPath());
            long newFileSize = newImage.getSize();

            log.info("기존 파일 크기 : {}", existingFileSize);
            log.info("새 파일 크기 : {}", newFileSize);

            return existingFileSize == newFileSize;
        } catch (IOException e) {
            log.error("이미지 비교 중 오류", e);
            return false;
        }
    }

    public Food getFoodById(UUID id) {
        return foodRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
    }
}