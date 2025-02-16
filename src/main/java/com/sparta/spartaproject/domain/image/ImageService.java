package com.sparta.spartaproject.domain.image;

import com.sparta.spartaproject.domain.image.store.StoreImage;
import com.sparta.spartaproject.domain.image.store.StoreImageRepository;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.SaveImageRequestDto;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import com.sparta.spartaproject.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final StoreImageRepository storeImageRepository;
    private final StoreRepository storeRepository;
    private final ImageMapper imageMapper;
    private final UserService userService;

    private static final String BASE_PATH = "images"; // 이미지 루트 디렉토리 명


    // 이미지 저장 (다중)
    @Transactional
    public List<ImageInfoDto> saveImages(SaveImageRequestDto request) {
        // 권한 검증
        User user = userService.loginUser();
        validateUserRole(user);

        // TODO: 해당 음식점의 업주인지 검증

        // 저장할 폴더 경로 설정
        String folderPath = BASE_PATH + "/" + request.type().toString().toLowerCase();
        createDirectoryIfNotExists(folderPath); // 폴더가 없으면 생성

        // 업로드할 엔티티 가져오기
        Object entity = getEntityByType(request.id(), request.type());

        // 파일 저장 후 DB에 저장
        return request.images().stream()
                .map(image -> {
                    String imagePath = saveImageFile(image, folderPath); // 파일 시스템에 저장
                    return saveImageEntity(entity, imagePath); // DB에 저장
                })
                .collect(Collectors.toList());
    }

    // 이미지 조회
    @Transactional(readOnly = true)
    public List<ImageInfoDto> getAllImages(Type type, UUID id) {
        // 음식점 이미지 조회
        if(Type.STORE.equals(type)){
            Store store = storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
            return storeImageRepository.findByStoreOrderByCreatedAtAsc(store)
                    .stream().map(imageMapper::toImageInfoDto).collect(Collectors.toList());
        }

        // TODO: 리뷰 이미지 조회 추가

        throw new IllegalArgumentException("지원되지 않는 이미지 타입입니다.");
    }

    // 이미지 삭제
    @Transactional
    public void deleteImage(UUID imageId) {
        // 권한 검증
        User user = userService.loginUser();
        validateUserRole(user);

        // TODO: 해당 음식점의 업주인지 검증

        // 이미지 삭제
        StoreImage storeImage = storeImageRepository.findById(imageId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 이미지 입니다."));
        storeImageRepository.delete(storeImage);
    }

    // 이미지 정보를 DB에 저장
    private ImageInfoDto saveImageEntity(Object entity, String imagePath) {
        // 음식점 이미지 저장
        if(entity instanceof Store store){
            StoreImage storeImage = storeImageRepository.save(
                    StoreImage.builder()
                            .store(store)
                            .path(imagePath)
                            .build()
            );
            return imageMapper.toImageInfoDto(storeImage);
        }
        // TODO: 리뷰 이미지 저장 로직 추가
        throw new RuntimeException("지원되지 않는 이미지 저장 타입입니다.");
    }

    // 파일 시스템에 파일 저장
    private String saveImageFile(MultipartFile file, String folderPath) {
        try {
            // 랜덤 UUID를 사용하여 파일명 설정
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            File saveFile = new File(folderPath + "/" + fileName);
            file.transferTo(saveFile);
            return  folderPath+"/"+fileName;
        }catch (IOException e){
            throw new RuntimeException("파일 시스템에 저장 중 오류 발생");
        }
    }

    // 엔티티 가져오기
    private Object getEntityByType(UUID id, Type type) {
        // 음식점 엔티티 조회
        if(type == Type.STORE){
            return storeRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 음식점입니다."));
        }
        // TODO: 리뷰 엔티티 조회 추가
        throw new RuntimeException("지원되지 않는 이미지 저장 타입입니다.");
    }

    // 폴더 생성 메서드
    private void createDirectoryIfNotExists(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                log.info("폴더 생성 완료 : {} ", path);
            } else {
                log.error("폴더 생성 실패 : {}", path);
            }
        }
    }

    // TODO:  @PreAuthorize 으로 인증 처리 대체 예정
    private void validateUserRole(User user){
        if (!(user.getRole() == Role.MANAGER || user.getRole() == Role.OWNER)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

}
