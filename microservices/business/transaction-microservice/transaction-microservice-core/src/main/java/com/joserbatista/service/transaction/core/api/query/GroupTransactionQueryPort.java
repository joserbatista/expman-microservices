package com.joserbatista.service.transaction.core.api.query;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.domain.Group;

public interface GroupTransactionQueryPort {

    Group byFilter(GetTransactionQuery getTransactionQuery, Group.By groupBy) throws BaseException;
}
