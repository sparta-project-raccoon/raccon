package com.sparta.spartaproject.domain.user;

import com.sparta.spartaproject.common.security.JwtUtils;
import com.sparta.spartaproject.dto.request.FindUsernameRequestDto;
import com.sparta.spartaproject.dto.request.LoginRequestDto;
import com.sparta.spartaproject.dto.request.SignUpRequestDto;
import com.sparta.spartaproject.dto.request.UpdatePasswordRequestDto;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.TokenDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.sparta.spartaproject.exception.ErrorCode.LOGIN_INPUT_INVALID;
import static com.sparta.spartaproject.exception.ErrorCode.NOT_EXIST_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final CacheManager cacheManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        // 먼저 캐시에서 유저 정보 조회
        Cache cache = cacheManager.getCache("loginUser");
        User cachedUser = cache.get(userId, User.class); // 캐시에서 유저 정보 찾기

        if (cachedUser != null) {
            log.info("캐시된 유저 반환 성공");
            return cachedUser;
        }

        // 캐시된 유저가 없으면 DB 에서 조회 후 캐시 저장
        User user = userRepository.findById(userId)
                .orElseThrow();

        cache.put(userId, user); // 캐시 저장
        log.info("{} - {}, 로그인 유저 캐시 저장", cache.getName(), userId);

        return user;
    }

    @Transactional
    public void signUp(SignUpRequestDto request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            log.error("이미 사용중인 아이디 또는 이메일 입니다.");
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "이미 사용중인 아이디 또는 이메일 입니다.");
        }

        User newUser = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .name(request.name())
                .phone(request.phone())
                .address(request.address())
                .role(Role.CUSTOMER)
                .status(Status.WAITING)
                .isDeleted(false)
                .loginFailCount(0)
                .build();

        userRepository.save(newUser);
        log.info("ID: {}, 회원가입 완료", newUser.getUsername());
    }

    @Transactional
    public TokenDto login(LoginRequestDto request) {
        // TODO: Validation 에러 메시지 적용하기

        // TODO: 유저가 없을 경우 에러 메시지 적용하기
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(NOT_EXIST_USER));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.error("사용자 로그인 오류");
            user.failLogin();

            if (user.getLoginFailCount() > 5) {
                log.error("실패 횟수 5회 초과 사용자");
                user.updateStatusStopped();
            }

            throw new BusinessException(LOGIN_INPUT_INVALID);
        }

//         TODO: 이메일 인증 단계 추가 후, 활성화
//        if (user.getStatus() != Status.COMPLETE) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 완료되지 않은 사용자이거나 정지된 사용자입니다.");
//        }

        user.successLogin();

        Cache cache = cacheManager.getCache("loginUser");
        cache.put(user.getId(), user); // 캐시 저장

        log.info("사용자:{}, 로그인 성공", user.getId());
        return jwtUtils.generateToken(user);
    }

    @Transactional(readOnly = true)
    public FindUsernameDto findUsername(FindUsernameRequestDto request) {
        // TODO: 유저가 없을 경우 에러 메시지 적용하기
        User user = userRepository.findByEmailAndName(request.email(), request.name())
                .orElseThrow();

        return userMapper.toFindUsernameDto(user);
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo() {
        User user = this.loginUser();
        return userMapper.toUserInfoDto(user);
    }

    @Transactional
    public void changePassword(UpdatePasswordRequestDto update) {
        User user = this.loginUser();

        if (passwordEncoder.matches(update.newPassword(), user.getPassword())) {
            log.error("사용자: {}, 같은 비밀번호로 변경 시도", user.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 비밀번호로는 변경이 불가능합니다.");
        }

        String newPassword = passwordEncoder.encode(update.newPassword());

        Cache cache = cacheManager.getCache("loginUser");

        if (cache != null) {
            cache.evict(user.getId());
            log.info("비밀번호 변경 유저: {}, 캐시 삭제 완료", user.getId());
        }

        user.updatePassword(newPassword);
        log.info("사용자: {}, 비밀번호 변경 완료", user.getId());
    }
}