package com.sparta.spartaproject.storeTest;

import com.sparta.spartaproject.domain.store.*;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private StoreService storeService;

    private User owner;
    private CreateStoreRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // 사장님 계정 설정
        owner = User.builder()
                .id(1L)
                .username("storeOwner")
                .role(Role.OWNER)
                .build();

        // 음식점 요청 DTO 설정
        requestDto = new CreateStoreRequestDto(
                List.of(UUID.fromString("deb88fa7-ebdf-4a01-8e3b-ae3d475faf45")),
                "평양 면옥",
                "서울 강남구",
                Status.OPEN,
                "010-1234-5678",
                "넘넘 맛있습니다",
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                ClosedDays.SUNDAY
        );
    }

    @Test
    void 음식점_등록_성공() {
        // Given
        when(userService.loginUser()).thenReturn(owner);

        // When
        storeService.createStore(requestDto);

        // Then
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void 음식점_조회_성공() {
        // Given
        UUID storeId = UUID.randomUUID();
        Store store = Store.builder()
                .id(storeId)
                .owner(owner)
                .name("맛집")
                .address("서울 강남구")
                .status(Status.OPEN)
                .tel("010-1234-5678")
                .description("베스트 맛집")
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(22, 0))
                .closedDays(ClosedDays.SUNDAY)
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // When
        StoreDetailDto result = storeService.getStore(storeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("맛집");
        assertThat(result.address()).isEqualTo("서울 강남구");
    }
}