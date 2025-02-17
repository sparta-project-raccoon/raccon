package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.common.utils.FileUtils;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import com.sparta.spartaproject.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class StoreImageService {

    private final StoreImageRepository storeImageRepository;
    private final StoreService storeService;
    private final UserService userService;
    private final ImageMapper imageMapper;

    @Value("${server.port}")
    private String port; // 포트번호

    // 음식점 이미지 저장
    @Transactional
    public List<ImageInfoDto> saveStoreImages(UUID storeId, List<MultipartFile> images) {
        // 음식점 사장 확인
        User user = userService.loginUser();
        Store store = storeService.getStoreById(storeId);

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("음식점 사장님만 이미지를 등록할 수 있습니다.");
        }

        // 폴더 생성 & 파일 저장
        List<String> savedImagePaths = images.stream().map(
                image -> FileUtils.saveFile(image, "store"))
                .toList();

        // 저장된 이미지 경로를 사용하여 DB에 저장
        return savedImagePaths.stream().map(
                imagePath -> {
                    StoreImage storeImage = saveImageEntity(store, imagePath); // DB 저장
                    return imageMapper.toImageInfoDtoFromStore(storeImage);
                }).collect(Collectors.toList());
    }

    // 음식점 이미지 조회
    @Transactional(readOnly = true)
    public List<ImageInfoDto> getStoreImages(UUID storeId) {
        Store store = storeService.getStoreById(storeId);
        return storeImageRepository.findByStoreAndIsDeletedIsFalseOrderByCreatedAtAsc(store).stream().map(
                        imageMapper::toImageInfoDtoFromStore
                ).collect((Collectors.toList()));
    }

    // 이미지 삭제
    @Transactional
    public void deleteImage(UUID imageId) {
        // DB에서 음식점 이미지 조회
        StoreImage storeImage = getStoreImageById(imageId);

        // 음식점 사장 확인
        User user = userService.loginUser();
        Store store = storeService.getStoreById(storeImage.getStore().getId());

        if (!user.getId().equals(store.getOwner().getId())) {
            throw new AccessDeniedException("음식점 사장님만 이미지를 등록할 수 있습니다.");
        }

        // TODO: 삭제 처리 방식 고민 (DB와 파일 시스템에서 영구 삭제 할지)
        storeImage.delete(); // 삭제 여부 변경, 삭제 일시 추가
        log.info("삭제 여부 변경 및 삭제 일시 생성 : {},{}", storeImage.getIsDeleted(), storeImage.getDeleteAt());

        storeImageRepository.delete(storeImage); // DB에서 삭제

        FileUtils.deleteFile(storeImage.getPath()); // 파일 시스템에서 삭제
    }

    // StoreImage 테이블에 저장
    private StoreImage saveImageEntity(Store store, String imagePath) {
        return storeImageRepository.save(
                StoreImage.builder()
                        .store(store)
                        .path(imagePath)
                        .isDeleted(false)
                        .build()
        );
    }

    // 음식점 이미지 DB에서 조회
    public StoreImage getStoreImageById(UUID imageId) {
        return storeImageRepository.findById(imageId)
                .orElseThrow(()->new IllegalArgumentException("해당 음식점 이미지가 존재하지 않습니다."));
    }


}
