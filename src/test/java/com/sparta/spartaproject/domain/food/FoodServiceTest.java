//package com.sparta.spartaproject.domain.food;
//
//import com.sparta.spartaproject.common.SortUtils;
//import com.sparta.spartaproject.domain.CircularService;
//import com.sparta.spartaproject.domain.image.EntityType;
//import com.sparta.spartaproject.domain.store.Store;
//import com.sparta.spartaproject.domain.store.StoreRepository;
//import com.sparta.spartaproject.domain.store.StoreService;
//import com.sparta.spartaproject.domain.user.Role;
//import com.sparta.spartaproject.domain.user.User;
//import com.sparta.spartaproject.domain.user.UserRepository;
//import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
//import com.sparta.spartaproject.dto.response.FoodDto;
//import com.sparta.spartaproject.exception.BusinessException;
//import com.sparta.spartaproject.exception.ErrorCode;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Transactional
//@SpringBootTest
//@AutoConfigureMockMvc
//class FoodServiceTest {
//
//    private static final Logger log = LoggerFactory.getLogger(FoodServiceTest.class);
//
//    @Autowired
//    private StoreService storeService;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private FoodRepository foodRepository;
//
//    @Autowired
//    private FoodService foodService;
//
//    @Autowired
//    private CircularService circularService;
//
//    private final Integer size = 10;
//    private final Integer page = 1;
//    private UUID storeId;
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @Test
//    void getAllFoods() {
//
//        // 실제 유저 데이터 조회
//        User user = userRepository.findById(13L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String sortDirection = "asc";
//        Sort sort = SortUtils.getSort(sortDirection);
//        Pageable pageable = PageRequest.of(page - 1, size, sort);
//
//        // when
//        FoodDto findFoods = foodService.getAllFoods(page, sortDirection);
//
//        // then
//        assertThat(findFoods.foods().size()).isEqualTo(6);
//
//    }
//
//
//    @Test
//    void getAllFoodsForStore() {
//        Store store = circularService.getStoreService().getStoreById(UUID.fromString("24dfbe9f-49ec-4a17-b58e-1a5569a6d9a1"));
//        String sortDirection = "asc";
//        Sort sort = SortUtils.getSort(sortDirection);
//        Pageable pageable = PageRequest.of(page - 1, size, sort);
//
//        List<Food> foodList = foodRepository.findByStoreAndIsDisplayedIsTrueAndIsDeletedIsFalse(store, pageable);
//        assertThat(foodList.get(0).getStore().getName()).isEqualTo("왕가네 칼국수");
//        assertThat(foodList.size()).isEqualTo(0);
//    }
//
//    @Test
//    void getFood() {
//        Food food = foodRepository.findByIdAndIsDeletedFalse(UUID.fromString("75f82f42-42a9-4cea-95b3-a7261d43da85"))
//                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
//
//        assertThat(food.getName()).isEqualTo("김치찌개");
//    }
//
//    @Test
//    void createFood() {
//
//        User user = userRepository.findById(1L).get();
//        CreateFoodRequestDto request = new CreateFoodRequestDto(UUID.fromString("b5714f86-eea5-451d-ba09-7e106f24010b"),"돈까스",10000,"돈까스 좋아해요?",Status.SALE);
//        Store store = circularService.getStoreService().getStoreById(request.storeId());
//
//        if (user.getRole() == Role.OWNER) {
//            if (!user.getId().equals(store.getOwner().getId())) {
//                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
//            }
//        }
//
//        String imagePathForFood = null;
//
//        String descriptionForGemini = circularService.getGeminiService().requestGemini(
//                store.getId(), request.description()
//        );
//
//        Food newFood = foodMapper.toFood(request, store, descriptionForGemini, imagePathForFood);
//        foodRepository.save(newFood);
//
//        if (image!=null) {
//            imagePathForFood = imageService.uploadImage(newFood.getId(), EntityType.FOOD, image);
//            log.info("음식 이미지 등록 완료 : {}", imagePathForFood);
//        }
//
//        log.info("가게: {}, 음식 생성 완료", store.getId());
//    }
//
//    @Test
//    void updateFood() {
//    }
//
//    @Test
//    void updateFoodStatus() {
//    }
//
//    @Test
//    void toggleDisplay() {
//    }
//
//    @Test
//    void deleteFood() {
//    }
//
//    @Test
//    void getFoodById() {
//    }
//}