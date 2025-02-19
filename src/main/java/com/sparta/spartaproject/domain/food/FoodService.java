package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.common.FileUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
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
    private final ImageService imageService;
    private final FoodRepository foodRepository;
    private final CircularService circularService;

    @Transactional
    public void createFood(CreateFoodRequestDto request, MultipartFile image) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        String imagePathForFood = null;

        String descriptionForGemini = circularService.getGeminiService().requestGemini(
            store.getId(), request.description()
        );

        Food newFood = foodMapper.toFood(request, store, descriptionForGemini, imagePathForFood);
        foodRepository.save(newFood);


        if (image!=null) {
            imagePathForFood = imageService.uploadImage(newFood.getId(), EntityType.FOOD, image);
            newFood.updateImagePath(imagePathForFood); // Food 내 imagePath 갱신
            foodRepository.saveAndFlush(newFood);
            log.info("음식 이미지 등록 완료 : {}", imagePathForFood);
        }

        log.info("가게: {}, 음식 생성 완료", store.getId());
    }

    @Transactional
    public void updateFood(UUID id, UpdateFoodRequestDto update, MultipartFile image) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }
        String newImagePath = null;
        if (image!=null){
            newImagePath = imageService.uploadImage(id,EntityType.FOOD, image);
        }
        food.update(update);
        food.updateImagePath(newImagePath); // Food의 imagePath 갱신
        foodRepository.saveAndFlush(food);
        imageService.deleteAllImagesByEntity(id, EntityType.FOOD);

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
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.delete();
        foodRepository.saveAndFlush(food);
        imageService.deleteAllImagesByEntity(food.getId(), EntityType.FOOD);
    }


    public Food getFoodById(UUID id) {
        return foodRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
    }


}
