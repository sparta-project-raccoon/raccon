package com.sparta.spartaproject.domain.user;

import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailCount(User user) {
        user.failLogin();
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStopped(User user) {
        user.updateStatus(Status.STOPPED);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));

        return new CustomUserDetails(userData);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache("authenticatedUser");
        User cachedUser = cache.get(id, User.class); // 캐시에서 유저 정보 찾기

        if (cachedUser != null) {
            log.info("로그인 검증 유저 캐시 사용");
            return new CustomUserDetails(cachedUser);
        }

        User userData = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));

        cache.put(id, userData); // 캐시 저장
        log.info("{} - {}, 로그인 유저 캐시 저장", cache.getName(), id);

        return new CustomUserDetails(userData);
    }
}