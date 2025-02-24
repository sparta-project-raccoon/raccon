package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Status;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import com.sparta.spartaproject.dto.response.OnlyStoreDto;
import com.sparta.spartaproject.dto.response.StoreByCategoryDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreDto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-24T14:45:38+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class StoreMapperImpl implements StoreMapper {

    @Override
    public StoreDetailDto toStoreDetailDto(List<CategoryDto> categories, List<String> imageUrls, Store store) {
        if ( categories == null && imageUrls == null && store == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String address = null;
        Status status = null;
        String tel = null;
        String description = null;
        LocalTime openTime = null;
        LocalTime closeTime = null;
        ClosedDays closedDays = null;
        LocalDateTime createdAt = null;
        Long createdBy = null;
        LocalDateTime updatedAt = null;
        Long updatedBy = null;
        if ( store != null ) {
            id = store.getId();
            name = store.getName();
            address = store.getAddress();
            status = store.getStatus();
            tel = store.getTel();
            description = store.getDescription();
            openTime = store.getOpenTime();
            closeTime = store.getCloseTime();
            closedDays = store.getClosedDays();
            createdAt = store.getCreatedAt();
            createdBy = store.getCreatedBy();
            updatedAt = store.getUpdatedAt();
            updatedBy = store.getUpdatedBy();
        }
        List<CategoryDto> categories1 = null;
        List<CategoryDto> list = categories;
        if ( list != null ) {
            categories1 = new ArrayList<CategoryDto>( list );
        }
        List<String> imageUrls1 = null;
        List<String> list1 = imageUrls;
        if ( list1 != null ) {
            imageUrls1 = new ArrayList<String>( list1 );
        }

        StoreDetailDto storeDetailDto = new StoreDetailDto( categories1, imageUrls1, id, name, address, status, tel, description, openTime, closeTime, closedDays, createdAt, createdBy, updatedAt, updatedBy );

        return storeDetailDto;
    }

    @Override
    public OnlyStoreDto toOnlyStoreDto(Store store, List<String> imageUrls) {
        if ( store == null && imageUrls == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String address = null;
        Status status = null;
        String tel = null;
        String description = null;
        LocalTime openTime = null;
        LocalTime closeTime = null;
        ClosedDays closedDays = null;
        LocalDateTime createdAt = null;
        Long createdBy = null;
        LocalDateTime updatedAt = null;
        Long updatedBy = null;
        if ( store != null ) {
            id = store.getId();
            name = store.getName();
            address = store.getAddress();
            status = store.getStatus();
            tel = store.getTel();
            description = store.getDescription();
            openTime = store.getOpenTime();
            closeTime = store.getCloseTime();
            closedDays = store.getClosedDays();
            createdAt = store.getCreatedAt();
            createdBy = store.getCreatedBy();
            updatedAt = store.getUpdatedAt();
            updatedBy = store.getUpdatedBy();
        }
        List<String> imageUrls1 = null;
        List<String> list = imageUrls;
        if ( list != null ) {
            imageUrls1 = new ArrayList<String>( list );
        }

        OnlyStoreDto onlyStoreDto = new OnlyStoreDto( imageUrls1, id, name, address, status, tel, description, openTime, closeTime, closedDays, createdAt, createdBy, updatedAt, updatedBy );

        return onlyStoreDto;
    }

    @Override
    public StoreDto toStoreDto(List<StoreDetailDto> source, Integer currentPage, Integer totalPages, Integer totalElements) {
        if ( source == null && currentPage == null && totalPages == null && totalElements == null ) {
            return null;
        }

        List<StoreDetailDto> stores = null;
        List<StoreDetailDto> list = source;
        if ( list != null ) {
            stores = new ArrayList<StoreDetailDto>( list );
        }
        Integer currentPage1 = null;
        currentPage1 = currentPage;
        Integer totalPages1 = null;
        totalPages1 = totalPages;
        Integer totalElements1 = null;
        totalElements1 = totalElements;

        StoreDto storeDto = new StoreDto( stores, currentPage1, totalPages1, totalElements1 );

        return storeDto;
    }

    @Override
    public StoreByCategoryDto toStoreByCategoryDto(List<OnlyStoreDto> stores, Integer currentPage, Integer totalPages, Integer totalElements) {
        if ( stores == null && currentPage == null && totalPages == null && totalElements == null ) {
            return null;
        }

        List<OnlyStoreDto> stores1 = null;
        List<OnlyStoreDto> list = stores;
        if ( list != null ) {
            stores1 = new ArrayList<OnlyStoreDto>( list );
        }
        Integer currentPage1 = null;
        currentPage1 = currentPage;
        Integer totalPages1 = null;
        totalPages1 = totalPages;
        Integer totalElements1 = null;
        totalElements1 = totalElements;

        StoreByCategoryDto storeByCategoryDto = new StoreByCategoryDto( stores1, currentPage1, totalPages1, totalElements1 );

        return storeByCategoryDto;
    }

    @Override
    public Store toStore(CreateStoreRequestDto source, User user, LocalTime openTime, LocalTime closeTime) {
        if ( source == null && user == null && openTime == null && closeTime == null ) {
            return null;
        }

        Store.StoreBuilder<?, ?> store = Store.builder();

        if ( source != null ) {
            store.name( source.name() );
            store.address( source.address() );
            store.status( source.status() );
            store.tel( source.tel() );
            store.description( source.description() );
            store.closedDays( source.closedDays() );
        }
        if ( user != null ) {
            store.owner( user );
            store.isDeleted( user.getIsDeleted() );
            store.deletedAt( user.getDeletedAt() );
        }
        store.openTime( openTime );
        store.closeTime( closeTime );

        return store.build();
    }
}
