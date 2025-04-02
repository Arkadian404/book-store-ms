package org.zafu.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.userservice.dto.request.UserRequest;
import org.zafu.userservice.dto.request.UserUpdateRequest;
import org.zafu.userservice.dto.response.UserInfoResponse;
import org.zafu.userservice.dto.response.UserResponse;
import org.zafu.userservice.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
    UserInfoResponse toRegisterResponse(User user);
}
