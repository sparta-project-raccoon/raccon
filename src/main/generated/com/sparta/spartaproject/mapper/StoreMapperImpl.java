package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreSummaryDto;
import java.time.LocalTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T22:20:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class StoreMapperImpl implements StoreMapper {

    @Override
    public StoreDetailDto toStoreDetailDto(Store store) {
        if ( store == null ) {
            return null;
        }

        String name = null;
        String address = null;
        String tel = null;
        String description = null;
        LocalTime openTime = null;
        LocalTime closeTime = null;
        ClosedDays closedDays = null;

        name = store.getName();
        address = store.getAddress();
        tel = store.getTel();
        description = store.getDescription();
        openTime = store.getOpenTime();
        closeTime = store.getCloseTime();
        closedDays = store.getClosedDays();

        String statusDesc = null;

        StoreDetailDto storeDetailDto = new StoreDetailDto( name, address, statusDesc, tel, description, openTime, closeTime, closedDays );

        return storeDetailDto;
    }

    @Override
    public StoreSummaryDto toStoreSummaryDto(Store store) {
        if ( store == null ) {
            return null;
        }

        UUID storeId = null;
        String name = null;

        storeId = store.getId();
        name = store.getName();

        String statusDesc = null;

        StoreSummaryDto storeSummaryDto = new StoreSummaryDto( storeId, name, statusDesc );

        return storeSummaryDto;
    }
}
