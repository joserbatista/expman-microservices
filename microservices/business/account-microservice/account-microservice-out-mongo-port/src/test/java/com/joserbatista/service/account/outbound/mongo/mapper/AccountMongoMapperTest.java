package com.joserbatista.service.account.outbound.mongo.mapper;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = AccountMongoMapperImpl.class)
class AccountMongoMapperTest {

    @Autowired
    private AccountMongoMapper mapper;

    @Test
    void toAccountList_FromAccountDocumentList_IsProperlyMapped() {
        List<AccountDocument> accountDocumentList = this.buildAccountDocumentList();

        List<Account> actual = this.mapper.toAccountList(accountDocumentList);
        List<Account> expected = this.buildAccountList();

        Assertions.assertThat(this.mapper.toAccountList(null)).isNull();
        Assertions.assertThat(this.mapper.toAccountList(Collections.singletonList(null))).isEqualTo(Collections.singletonList(null));
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toAccountDocument_FromSaveAccountCommand_IsProperlyMapped() {
        SaveAccountCommand command = SaveAccountCommand.builder().username("carinaxfpv").name("BCP").type(AccountType.BANK).notes("Millenium BCP")
                                                       .build();

        AccountDocument actual = this.mapper.toAccountDocument(command, "cl0122z810001ec13qrz692pp");
        AccountDocument expected = AccountDocument.builder().documentId("cl0122z810001ec13qrz692pp").name("BCP").type(AccountType.BANK)
                                                  .notes("Millenium BCP").username("carinaxfpv").build();

        Assertions.assertThat(this.mapper.toAccountDocument((SaveAccountCommand) null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    void toAccountDocumentList_FromCreateAccountCommandList_IsProperlyMapped() {
        List<CreateAccountCommand> commandList = List.of(
            CreateAccountCommand.builder().username("carinaxfpv").name("BCP").type(AccountType.BANK).notes("Millenium BCP").build(),
            CreateAccountCommand.builder().username("carinaxfpv").name("PayPal").type(AccountType.PAYPAL)
                                .notes("Elon Musk helped me open this account").build()
        );

        List<AccountDocument> actual = this.mapper.toAccountDocumentList(commandList, "5b17c78e-bf9a-4293-be5a-e16da9ba8341");
        List<AccountDocument> expected = List.of(
            AccountDocument.builder().name("BCP").type(AccountType.BANK).active(true)
                           .notes("Millenium BCP").username("carinaxfpv").build(),
            AccountDocument.builder().name("PayPal").type(AccountType.PAYPAL).active(true)
                           .notes("Elon Musk helped me open this account").username("carinaxfpv").build()
        );
        Assertions.assertThat(this.mapper.toAccountDocumentList(null, null)).isEmpty();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("id").isEqualTo(expected);
    }

    //<editor-fold desc="builders">
    private List<Account> buildAccountList() {
        return List.of(
            Account.builder()
                   .id("ad5f5f2e-bab6-4f96-afea-b17090d30bde").name("BCP").type(AccountType.BANK)
                   .active(true).amount(BigDecimal.valueOf(256L)).notes("Millenium BCP")
                   .build(),
            Account.builder()
                   .id("3cf60827-bf1c-417d-abc8-781beb4425bd").name("PayPal").type(AccountType.PAYPAL)
                   .active(true).amount(BigDecimal.ZERO).notes("Elon Musk helped me open this account")
                   .build()
        );
    }

    private List<AccountDocument> buildAccountDocumentList() {
        return List.of(
            AccountDocument.builder()
                           .id("ad5f5f2e-bab6-4f96-afea-b17090d30bde")
                           .name("BCP").type(AccountType.BANK).active(true).amount(BigDecimal.valueOf(256L))
                           .notes("Millenium BCP").username("carinaxfpv").build(),
            AccountDocument.builder()
                           .id("3cf60827-bf1c-417d-abc8-781beb4425bd")
                           .name("PayPal").type(AccountType.PAYPAL).active(true).amount(BigDecimal.ZERO)
                           .notes("Elon Musk helped me open this account").username("carinaxfpv").build()
        );
    }
    //</editor-fold>

}