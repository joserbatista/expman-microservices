package com.joserbatista.service.transaction.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
@Builder
@Schema(name = "GetTransactionQuery")
public class GetTransactionQueryDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime endDate;
    Set<String> accountNameList;
    Set<String> tagList;

    public boolean hasValues() {
        if (this.startDate != null) {
            return true;
        }
        if (this.endDate != null) {
            return true;
        }
        if (!CollectionUtils.isEmpty(this.accountNameList)) {
            return true;
        }
        return !CollectionUtils.isEmpty(this.tagList);

    }
}
