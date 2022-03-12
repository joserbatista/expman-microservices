package com.joserbatista.service.account.inbound.rest.mapper;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = AccountRestMapperImpl.class)
class AccountRestMapperTest {

    @Autowired
    private AccountRestMapper mapper;

    @Test
    void toAccountTypeEnumDtoList_FromAccountTypeEnumList_IsProperlyMapped() {
        List<AccountTypeEnumDto> actual = this.mapper.toAccountTypeEnumDtoList(List.of(AccountType.CASH));

        Assertions.assertThat(this.mapper.toAccountTypeEnumDtoList(null)).isNull();
        Assertions.assertThat(actual).isEqualTo(List.of(AccountTypeEnumDto.CASH));
    }

    @Test
    void toAccountDtoList_FromAccountList_IsProperlyMapped() {
        List<Account> accountList = this.buildAccountList();

        List<AccountDto> actual = this.mapper.toAccountDtoList(accountList);
        List<AccountDto> expected = this.buildAccountDtoList();

        Assertions.assertThat(this.mapper.toAccountDtoList(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toSaveAccountCommand_FromAccountDto_IsProperlyMapped() {
        AccountDto accountDto = AccountDto.builder().name("BCP").type(AccountTypeEnumDto.BANK)
                                          .active(true).amount(BigDecimal.valueOf(256L)).notes("Millenium BCP").build();

        SaveAccountCommand actual = this.mapper.toSaveAccountCommand("eather9hmz", "2c7b2acc-8d3f-45d2-aaeb-d389f8b29b27", accountDto);
        SaveAccountCommand expected = SaveAccountCommand.builder().username("eather9hmz").id("2c7b2acc-8d3f-45d2-aaeb-d389f8b29b27")
                                                        .name("BCP").type(AccountType.BANK).notes("Millenium BCP").build();
        Assertions.assertThat(this.mapper.toSaveAccountCommand(null, null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toAccount_FromAccountDtoAndNullId_IsProperlyMapped() {
        AccountDto accountDto = AccountDto.builder()
                                          .name("BCP").type(AccountTypeEnumDto.BANK).active(true).amount(BigDecimal.valueOf(256L))
                                          .notes("Millenium BCP")
                                          .build();

        CreateAccountCommand actual = this.mapper.toCreateAccountCommand("eather9hmz", accountDto);
        CreateAccountCommand expected = CreateAccountCommand.builder().username("eather9hmz")
                                                            .name("BCP").type(AccountType.BANK).notes("Millenium BCP").build();
        Assertions.assertThat(this.mapper.toSaveAccountCommand(null, null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    //<editor-fold desc="builders">
    private List<Account> buildAccountList() {
        return List.of(
            Account.builder().id("2c7b2acc-8d3f-45d2-aaeb-d389f8b29b27").name("BCP")
                   .type(AccountType.BANK).active(true).amount(BigDecimal.valueOf(256L)).notes("Millenium BCP").build(),
            Account.builder()
                   .id("5305dc56-a521-4f2a-bb4b-9589dc3e17d8").name("PayPal")
                   .type(AccountType.PAYPAL).active(true).amount(BigDecimal.ZERO).notes("Elon Musk helped me open this account").build()
        );
    }

    private List<AccountDto> buildAccountDtoList() {
        return List.of(
            AccountDto.builder()
                      .id("2c7b2acc-8d3f-45d2-aaeb-d389f8b29b27").name("BCP")
                      .type(AccountTypeEnumDto.BANK).active(true).amount(BigDecimal.valueOf(256L)).notes("Millenium BCP").build(),
            AccountDto.builder()
                      .id("5305dc56-a521-4f2a-bb4b-9589dc3e17d8").name("PayPal")
                      .type(AccountTypeEnumDto.PAYPAL).active(true).amount(BigDecimal.ZERO).notes("Elon Musk helped me open this account").build()
        );
    }
    //</editor-fold>

}