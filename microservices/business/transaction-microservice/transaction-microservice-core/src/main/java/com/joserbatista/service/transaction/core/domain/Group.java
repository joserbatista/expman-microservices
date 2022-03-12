package com.joserbatista.service.transaction.core.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class Group {

    By by;
    OffsetDateTime startDate;
    OffsetDateTime endDate;

    List<Element> elementList;


    @Value
    @Builder
    public static class Element {

        String value;
        int transactionCount;
        BigDecimal totalAmount;
        List<String> idList;
    }


    public enum By {
        ACCOUNT,
        TAG
    }
}
