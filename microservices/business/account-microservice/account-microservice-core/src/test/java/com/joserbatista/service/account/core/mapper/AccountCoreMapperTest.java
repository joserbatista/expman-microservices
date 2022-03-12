package com.joserbatista.service.account.core.mapper;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.domain.AccountType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest(classes = AccountCoreMapperImpl.class)
class AccountCoreMapperTest {

    @Autowired
    private AccountCoreMapper mapper;

    @Test
    void toCreateAccountCommandList_FromNameString_IsProperlyMapped() {
        List<CreateAccountCommand> actual = this.mapper.toCreateAccountCommandList("lorimarp", Set.of("Credit Card", "PayPal"));
        List<CreateAccountCommand> expected = List.of(
            CreateAccountCommand.builder().username("lorimarp").name("Credit Card").type(AccountType.OTHER).build(),
            CreateAccountCommand.builder().username("lorimarp").name("PayPal").type(AccountType.OTHER).build()
        );

        Assertions.assertThat(this.mapper.toCreateAccountCommandList(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}