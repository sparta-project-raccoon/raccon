package com.sparta.spartaproject.domain.verify;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.mail.MailService;
import com.sparta.spartaproject.domain.user.Status;
import com.sparta.spartaproject.dto.request.ConfirmCodeRequestDto;
import com.sparta.spartaproject.dto.request.SendCodeRequestDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyService {
    private static final Integer ISSUED_SECONDS = 180;
    private final Map<String, Code> codeStorage = new HashMap<>();
    private final MailService mailService;
    private final CircularService circularService;

    public void sendCode(SendCodeRequestDto sendCodeRequestDto) {
        if (codeStorage.containsKey(sendCodeRequestDto.email())) {
            Code codeInfo = codeStorage.get(sendCodeRequestDto.email());

            Duration duration = Duration.between(codeInfo.getIssuedAt(), LocalDateTime.now());
            // 3. 3분이 넘지 않았으면, 3분 이내 재인증 요청 에러
            if (duration.getSeconds() < ISSUED_SECONDS) {
                log.warn("이메일 : {}, {}초 이내 재인증 요청", sendCodeRequestDto.email(), ISSUED_SECONDS);
                throw new BusinessException(ErrorCode.MIN_RE_AUTHENTICATION_TIME_NOT_PASSED);
            }
            codeStorage.remove(sendCodeRequestDto.email());
        }
        String newCode = issueCode();

        mailService.sendMimeMessage(sendCodeRequestDto.email(), newCode);
        codeStorage.put(sendCodeRequestDto.email(), new Code(newCode, LocalDateTime.now()));

        log.info("인증코드 전송 성공");
    }

    public String issueCode() {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
    }

    public void confirmCode(@Validated ConfirmCodeRequestDto confirmCodeRequestDto) {
        if (!codeStorage.containsKey(confirmCodeRequestDto.email())) {
            log.error("이메일 : {}, 인증 이메일 목록에 존재하지 않습니다.", confirmCodeRequestDto.email());
            throw new BusinessException(ErrorCode.EMAIL_NOT_FOUND_IN_VERIFICATION_LIST);
        }

        Code codeInfo = codeStorage.get(confirmCodeRequestDto.email());
        Duration duration = Duration.between(codeInfo.getIssuedAt(), LocalDateTime.now());
        if (duration.getSeconds() > ISSUED_SECONDS) {
            log.warn("이메일 : {}, 인증 요청 3분 초과", confirmCodeRequestDto.email());
            codeStorage.remove(confirmCodeRequestDto.email());
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_REQUEST_TIMEOUT);
        }

        if (!Objects.equals(codeInfo.getCodeNumber(), confirmCodeRequestDto.code())) {
            log.error("이메일 : {}, 인증 실패, 인증 코드 미일치", confirmCodeRequestDto.email());

            if (codeInfo.getErrorCount() >= 5) {
                codeStorage.remove(confirmCodeRequestDto.email());
                throw new BusinessException(ErrorCode.EXCEEDED_MAX_VERIFICATION_ATTEMPTS);
            } else {
                codeInfo.incrementCount();
            }
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_MISMATCH);
        }

        log.info("이메일 : {}, 인증 성공", confirmCodeRequestDto.email());
        codeStorage.remove(confirmCodeRequestDto.email());

        circularService.getUserService().updateStatusByEmail(confirmCodeRequestDto.email(), Status.COMPLETE);
    }
}