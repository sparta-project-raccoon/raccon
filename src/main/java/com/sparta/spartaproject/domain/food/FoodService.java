package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.common.FileUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CircularService circularService;
    private final FoodMapper foodMapper;
    private final ImageService imageService;

    // 음식 등록 및 음식 이미지 저장
    @Transactional
    public FoodDetailDto createFoodWithImage(CreateFoodRequestDto request, MultipartFile image) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        Food food = foodRepository.save(
                Food.builder()
                .store(store)
                .name(request.name())
                .price(request.price())
                .description(request.description())
                .status(request.status())
                .isDisplayed(true)
                .isDeleted(false)
                .build()
        );
        log.info("음식 등록 완료 : {}", food.getId());

        // 음식 이미지 등록 (S3 업로드 후 URL 저장)
        if (image!=null) {
            String imagePath = imageService.uploadImage(food.getId(), EntityType.FOOD, image);
            food.updateImagePath(imagePath);
            log.info("음식 이미지 등록 완료 : {}", imagePath);
        }

        return foodMapper.toFoodDetailDto(food);
    }

    // 음식 수정
    @Transactional
    public FoodDetailDto updateFoodWithImage(UUID id, UpdateFoodRequestDto update, MultipartFile newImage) {
        Food food = getFoodById(id);

        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        food.update(update);

        if(newImage!=null){
            String newImagePath = imageService.replaceAllImages(food.getId(),EntityType.FOOD, List.of(newImage)).get(0);
            food.updateImagePath(newImagePath);
        }else {
            imageService.deleteAllImagesByEntity(food.getId(), EntityType.FOOD);
            food.updateImagePath(null);
        }

        log.info("음식 수정 완료 : {}, image:{}", food.getName(), food.getImagePath());
        return foodMapper.toFoodDetailDto(food);
    }

    // 음식 상태(준비중, 판매중, 품절) 수정
    @Transactional
    public FoodDetailDto updateFoodStatus(UUID id, Status newStatus) {
        Food food = getFoodById(id);

        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        food.updateStatus(newStatus);
        return foodMapper.toFoodDetailDto(food);
    }

    // 음식 표시 상태(숨김(false), 표시(true)) 변경
    public boolean toggleIsDisplayed(UUID id) {
        Food food = getFoodById(id);

        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        return food.toggleIsDisplayed(); // 변경된 상태 반환
    }

    // 음식 삭제
    @Transactional
    public void deleteFoodWithImage(UUID id) {
        Food food = getFoodById(id);

        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        food.delete();
        imageService.deleteAllImagesByEntity(food.getId(), EntityType.FOOD);
    }


    // 기존 이미지와 새로운 이미지 동일 여부 확인
    private boolean isSameImage(String existingImagePath, MultipartFile newImage) {
        if(existingImagePath == null || newImage==null){
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
        return foodRepository.findById(id)
                .orElseThrow(()->new RuntimeException("해당 음식점을 찾을 수 없습니다."));
    }


}
