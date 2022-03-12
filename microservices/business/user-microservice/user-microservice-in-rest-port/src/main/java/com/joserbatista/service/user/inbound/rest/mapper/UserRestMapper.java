package com.joserbatista.service.user.inbound.rest.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Mapper(config = BaseMapperConfig.class)
public abstract class UserRestMapper {

    private final PasswordEncoder passwordEncoder;

    protected UserRestMapper() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Mapping(target = "password", source = "userDto.password", qualifiedByName = "toEncryptedPassword")
    public abstract CreateUserCommand toCreateUserCommand(UserDto userDto);

    @Mapping(target = "password", source = "userDto.password", qualifiedByName = "toEncryptedPassword")
    public abstract SaveUserCommand toSaveUserCommand(String id, UserDto userDto);

    public abstract UserDto toUserDto(User saved);

    @Named("toEncryptedPassword")
    @Nullable
    String toEncryptedPassword(String password) {
        return StringUtils.hasText(password) ? this.passwordEncoder.encode(password) : null;
    }
}
