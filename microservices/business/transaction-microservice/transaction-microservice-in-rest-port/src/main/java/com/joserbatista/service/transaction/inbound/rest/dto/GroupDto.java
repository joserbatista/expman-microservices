package com.joserbatista.service.transaction.inbound.rest.dto;

import com.joserbatista.service.transaction.core.domain.Group;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class GroupDto {

    Group.By by;
    OffsetDateTime startDate;
    OffsetDateTime endDate;

    List<ElementDto> elementList;


    @Value
    @Builder
    public static class ElementDto {

        String value;
        int transactionCount;
        BigDecimal totalAmount;
        List<String> idList;

    }
}