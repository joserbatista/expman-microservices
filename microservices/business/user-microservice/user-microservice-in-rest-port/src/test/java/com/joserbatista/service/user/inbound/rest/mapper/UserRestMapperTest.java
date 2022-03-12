package com.joserbatista.service.user.inbound.rest.mapper;

import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = {UserRestMapperImpl.class, BCryptPasswordEncoder.class})
class UserRestMapperTest {

    @Autowired
    private UserRestMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void toCreateUserCommand_FromUserDto_IsProperlyMapped() {
        UserDto userDto = UserDto.builder().username("shawneedi").password("ckzr7n1w50001qo136egr7pjm").email("rya_mandellyuf@closure.fi")
                                 .fullName("Danyella Montano").build();
        CreateUserCommand actual = this.mapper.toCreateUserCommand(userDto);
        CreateUserCommand expected = CreateUserCommand.builder().username("shawneedi")
                                                      .password("$2a$10$wvorFJmddjqpWl7DGbKvJuCCEetSmqLpYRjs45SUlrXLNSLdOBBV.")
                                                      .email("rya_mandellyuf@closure.fi").fullName("Danyella Montano").build();

        Assertions.assertThat(this.mapper.toCreateUserCommand(null)).isNull();
        Assertions.assertThat(actual)
                  .usingRecursiveComparison()
                  .withEqualsForFields((s1, s2) -> this.passwordEncoder.matches(userDto.getPassword(), (String) s2), "password")
                  .isEqualTo(expected);
    }

    @Test
    void toSaveUserCommand_FromUserDto_IsProperlyMapped() {
        UserDto userDto = UserDto.builder().username("shawneedi").password("ckzr7n1w50001qo136egr7pjm").email("rya_mandellyuf@closure.fi")
                                 .fullName("Danyella Montano").build();
        SaveUserCommand actual = this.mapper.toSaveUserCommand("d8a3dd8c-2f02-4d0a-8143-f6dde6ffb9b0", userDto);
        SaveUserCommand expected = SaveUserCommand.builder().username("shawneedi")
                                                  .password("$2a$10$6JEKOxeK0leEYZr8BqHKNu9TK7C6HSuLx0RN0ezjZLGNlwNvarsBG")
                                                  .email("rya_mandellyuf@closure.fi").fullName("Danyella Montano").build();

        Assertions.assertThat(this.mapper.toSaveUserCommand(null, null)).isNull();
        Assertions.assertThat(actual)
                  .usingRecursiveComparison()
                  .withEqualsForFields((s1, s2) -> this.passwordEncoder.matches(userDto.getPassword(), (String) s2), "password")
                  .isEqualTo(expected);
    }

    @Test
    void toUserDto_FromUser_IsProperlyMapped() {
        User user = User.builder().username("shawneedi").password("ckzr7n1w50001qo136egr7pjm").email("rya_mandellyuf@closure.fi")
                        .fullName("Danyella Montano").build();
        UserDto actual = this.mapper.toUserDto(user);
        UserDto expected = UserDto.builder().username("shawneedi").password("ckzr7n1w50001qo136egr7pjm").email("rya_mandellyuf@closure.fi")
                                  .fullName("Danyella Montano").build();

        Assertions.assertThat(this.mapper.toUserDto(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}