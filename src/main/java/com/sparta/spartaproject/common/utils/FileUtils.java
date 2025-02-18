package com.sparta.spartaproject.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Slf4j
public class FileUtils {

    private static final String BASE_PATH = "images"; // 이미지 루트 디렉토리 명

    /**
     * 이미지를 파일 시스템에 저장하는 메서드
     * @param file 저장할 이미지
     * @param folderName 저장할 폴더 명 (ex: "store", "review")
     * @return 파일 전체 경로 (path)
     */
    public static String saveFile(MultipartFile file, String folderName) {
        try {
            // 폴더 경로
            String folderPath = BASE_PATH+"/"+folderName;

            // 폴더가 존재하지 않으면 생성
            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdirs();
                log.info("files 폴더 생성 완료");
            }

            log.info(folder.getAbsolutePath()); // BASE path 부분은 yaml 뺏기에 테스트 과정에서 확인하는 로그입니다.

            if (file == null) {
                log.info("빈 파일");
                throw new RuntimeException("저장할 파일이 존재하지 않습니다");
            }

            // 랜덤 UUID를 사용하여 파일명 설정
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            File saveFile = new File(folder, fileName);

            // 파일 저장
            file.transferTo(saveFile);

            // 저장된 이미지 경로 반환
            return folderPath + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    /**
     * 파일 삭제 메서드
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("파일 삭제 성공: {}", filePath);
            } else {
                log.error("파일 삭제 실패: {}", filePath);
                throw new RuntimeException("파일 삭제 실패: " + filePath);
            }
        } else {
            log.warn("삭제하려는 파일이 존재하지 않음: {}", filePath);
        }
    }
}
