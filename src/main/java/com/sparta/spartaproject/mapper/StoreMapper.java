package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {

//    @Mapping(target = "categoryName", source = "category.name")
    StoreDetailDto toStoreDetailDto(Store store);

    @Mapping(target = "storeId", source = "id")
//    @Mapping(target = "categoryName", source = "category.name")
    StoreSummaryDto toStoreSummaryDto(Store store);

}
