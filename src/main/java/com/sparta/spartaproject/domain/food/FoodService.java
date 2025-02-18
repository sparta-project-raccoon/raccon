package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.common.utils.FileUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodInfoDto;
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

    private final FoodRepository foodRepository;
    private final CircularService circularService;
    private final FoodMapper foodMapper;

    // 음식 등록 및 음식 이미지 저장
    @Transactional
    public FoodInfoDto createFoodWithImage(CreateFoodRequestDto request, MultipartFile image) {
        // 음식점 사장 확인
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        // 음식 사진 등록
        String foodImagePath = FileUtils.saveFile(image,"food"); // FileSystem에 파일 저장
        log.info("음식 사진 등록 : {}", foodImagePath);

        // 음식 등록
        Food food = foodRepository.save(
                Food.builder()
                .store(store)
                .name(request.name())
                .price(request.price())
                .description(request.description())
                .imagePath(foodImagePath)
                .status(request.status())
                .isDeleted(false)
                .build()
        );
        log.info("음식 등록 완료 : {}", food.getId());

        return foodMapper.toFoodInfoDto(food);
    }

    // 음식 수정
    @Transactional
    public FoodInfoDto updateFoodWithImage(UUID id, UpdateFoodRequestDto update, MultipartFile image) {
        // 음식 확인
        Food food = getFoodById(id);

        // 음식점 사장 확인
        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        // 음식 정보 업데이트
        food.update(update);

        // 음식 이미지 변경 확인
        if(image!=null && !isSameImage(food.getImagePath(), image)){
            // 기존 이미지 삭제
            FileUtils.deleteFile(food.getImagePath());

            // 새로운 이미지 저장
            String newImagePath = FileUtils.saveFile(image,"food");
            food.updateImagePath(newImagePath); // 새로운 이미지 경로로 업데이트
        }

        log.info("음식 수정 완료 : {}, image:{}", food.getName(), food.getImagePath());
        return foodMapper.toFoodInfoDto(food);
    }

    // 음식 상태 수정
    @Transactional
    public FoodInfoDto updateFoodStatus(UUID id, Status newStatus) {
        // 음식 확인
        Food food = getFoodById(id);

        // 음식점 사장 확인
        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        // 음식 상태 변경
        food.updateStatus(newStatus);

        return foodMapper.toFoodInfoDto(food);
    }

    // 음식 표시 상태 변경
    public boolean updateFoodDisplay(UUID id) {
        Food food = getFoodById(id);

        // 음식점 사장 확인
        User user = circularService.getUserService().loginUser();
        Store store = food.getStore();
        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("현재 로그인한 사용자와 업주가 일치하지 않습니다.");
        }

        // 음식 표시 상태 변경
        return food.toggleIsDisplayed(); // 변경된 상태 반환
    }

    // 음식 삭제
    @Transactional
    public void deleteFoodWithImage(UUID id) {
        // 음식 확인
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
