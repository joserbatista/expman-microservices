package com.joserbatista.service.transaction.core.api.query;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Word;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

@Value
@Builder
public class GetTransactionQuery {

    @Username
    String username;

    @PastOrPresent
    OffsetDateTime startDate;

    OffsetDateTime endDate;

    Set<@NotBlank String> accountNameList;
    Set<@Word String> tagValueList;

}
