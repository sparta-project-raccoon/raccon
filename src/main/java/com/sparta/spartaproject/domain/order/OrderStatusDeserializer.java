package com.sparta.spartaproject.domain.order;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sparta.spartaproject.exception.BusinessException;

import java.io.IOException;

import static com.sparta.spartaproject.exception.ErrorCode.INVALID_ORDER_STATUS;

public class OrderStatusDeserializer extends JsonDeserializer<OrderStatus> {

    @Override
    public OrderStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException, BusinessException {
        String value = p.getValueAsString();
        try {
            return OrderStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(INVALID_ORDER_STATUS);
        }
    }
}
