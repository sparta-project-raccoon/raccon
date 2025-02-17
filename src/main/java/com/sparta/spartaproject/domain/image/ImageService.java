package com.sparta.spartaproject.domain.image;

import com.sparta.spartaproject.domain.store.StoreImage;
import com.sparta.spartaproject.domain.store.StoreImageRepository;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
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

    public String saveImage(MultipartFile file, String folderPath) {
        try {
            // 폴더가 존재하지 않으면 생성
            File folder = new File(folderPath);
            if (!folder.exists() && !folder.mkdirs()) {
                log.error("폴더 생성 실패 : {}", folderPath);
                throw new RuntimeException("폴더 생성 실패: " + folderPath);
            }

            // 랜덤 UUID를 사용하여 파일명 설정
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            File saveFile = new File(folder, fileName);

            // 파일 저장
            file.transferTo(saveFile);

            // 저장된 이미지 경로 반환
            return folderPath + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 시스템에 저장 중 오류 발생", e);
        }
    }

    // 파일 시스템에서 이미지 삭제
    public void deleteImageFile(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("이미지 파일 삭제 성공: {}", imagePath);
            } else {
                log.error("이미지 파일 삭제 실패: {}", imagePath);
                throw new RuntimeException("이미지 파일 삭제 실패: " + imagePath);
            }
        } else {
            log.warn("삭제하려는 파일이 존재하지 않습니다: {}", imagePath);
        }
    }



}
