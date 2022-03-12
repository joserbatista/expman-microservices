package com.joserbatista.service.importer.core.mapper;

import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.AccountType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@SpringBootTest(classes = TransactionCoreMapperImpl.class)
class TransactionCoreMapperTest {

    @Autowired
    private TransactionCoreMapper mapper;

    @Test
    void toTransactionList_FromTransactionListAndIdMap_IsProperlyMapped() {
        List<Account> accountList = List.of(
            Account.builder().name("Debit Card").id("dc432619-e15c-4c90-bc42-988798c666dc").build(),
            Account.builder().name("Credit Card").id("6c13f8c9-fd7d-417d-8c70-508c4c1725ad").build()
        );

        List<CreateTransactionCommand> commandList = List.of(CreateTransactionCommand.builder()
                                                                                     .amount(BigDecimal.valueOf(2.92))
                                                                                     .account(Account.builder().name("Credit Card").build())
                                                                                     .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                                     .note("Locked footage hood duties antarctica bald justin, note.")
                                                                                     .tagList(List.of("Syria", "SolarRoad", "Annada")).build());
        List<CreateTransactionCommand> actual = this.mapper.toCreateTransactionCommandList(commandList, accountList);
        List<CreateTransactionCommand> expected = List.of(CreateTransactionCommand.builder()
                                                                                  .amount(BigDecimal.valueOf(2.92))
                                                                                  .account(Account.builder()
                                                                                                  .id("6c13f8c9-fd7d-417d-8c70-508c4c1725ad")
                                                                                                  .name("Credit Card")
                                                                                                  .build())
                                                                                  .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                                  .note("Locked footage hood duties antarctica bald justin, note.")
                                                                                  .tagList(List.of("Syria", "SolarRoad", "Annada")).build());

        Assertions.assertThat(this.mapper.toCreateTransactionCommandList(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void toCreateTransactionCommand_FromLine_IsProperlyMapped() {
        String[] stringList = { //date;account;tags;amount;note
            "2022-12-03T10:15:30Z", "Credit Card", "Syria|SolarRoad|Annada", "2.92", "Locked footage hood duties antarctica bald justin, note."
        };

        CreateTransactionCommand actual = this.mapper.toCreateTransactionCommand("lorimarp", "\\|", stringList);
        CreateTransactionCommand expected = CreateTransactionCommand.builder().amount(BigDecimal.valueOf(2.92))
                                                                    .account(Account.builder().name("Credit Card").build())
                                                                    .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                    .username("lorimarp")
                                                                    .note("Locked footage hood duties antarctica bald justin, note.")
                                                                    .tagList(List.of("Syria", "SolarRoad", "Annada")).build();

        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toCreateAccountCommandList_FromNameString_IsProperlyMapped() {
        List<CreateAccountCommand> actual = this.mapper.toCreateAccountCommandList("lorimarp", List.of("Credit Card", "PayPal"));
        List<CreateAccountCommand> expected = List.of(
            CreateAccountCommand.builder().username("lorimarp").name("Credit Card").type(AccountType.OTHER).build(),
            CreateAccountCommand.builder().username("lorimarp").name("PayPal").type(AccountType.OTHER).build()
        );

        Assertions.assertThat(this.mapper.toCreateAccountCommandList(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}