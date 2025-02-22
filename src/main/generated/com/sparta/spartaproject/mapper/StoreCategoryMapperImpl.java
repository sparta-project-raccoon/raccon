package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T22:30:15+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class StoreCategoryMapperImpl implements StoreCategoryMapper {

    @Override
    public StoreCategory toStoreCategory(Store store, Category category) {
        if ( store == null && category == null ) {
            return null;
        }

        StoreCategory.StoreCategoryBuilder<?, ?> storeCategory = StoreCategory.builder();

        if ( store != null ) {
            storeCategory.store( store );
            if ( store.getIsDeleted() != null ) {
                storeCategory.isDeleted( store.getIsDeleted() );
            }
            storeCategory.deletedAt( store.getDeletedAt() );
        }
        storeCategory.category( category );

        return storeCategory.build();
    }
}
