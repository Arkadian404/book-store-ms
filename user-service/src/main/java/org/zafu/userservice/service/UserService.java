package org.zafu.userservice.service;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zafu.userservice.client.IdentityClient;
import org.zafu.userservice.dto.ApiResponse;
import org.zafu.userservice.dto.request.*;
import org.zafu.userservice.dto.response.UserInfoResponse;
import org.zafu.userservice.dto.response.UserResponse;
import org.zafu.userservice.exception.AppException;
import org.zafu.userservice.exception.ErrorCode;
import org.zafu.userservice.mapper.UserMapper;
import org.zafu.userservice.model.Role;
import org.zafu.userservice.model.User;
import org.zafu.userservice.repository.UserRepository;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final IdentityClient identityClient;

    private final ProducerService producerService;

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    public UserResponse createUser(UserRequest request){
        User user = mapper.toUser(request);
        return mapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(int id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        mapper.updateUser(user, request);
        return mapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public UserInfoResponse getUserInfoByUsername(String username){
        return userRepository.findByUsername(username)
                .map(mapper::toRegisterResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse registerUser(RegisterRequest request){
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isActivated(false)
                .build();
        UserInfoBasic userInfo = UserInfoBasic.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
        String token = identityClient.generateVerificationToken(userInfo).getResult();
        user.setActivationToken(token);
        UserResponse response = mapper.toUserResponse(userRepository.save(user));

        EmailVerificationRequest verificationRequest = new EmailVerificationRequest(
                user.getUsername(), user.getEmail(), token
        );
        producerService.sendEmailVerificationRequest(verificationRequest);
        return response;
    }

    public String verifyUserToken(String verificationToken){
        try{
            ApiResponse<String> response = identityClient.verifyToken(verificationToken);
            String token = response.getResult();
            SignedJWT jwt = SignedJWT.parse(token);
            String username = jwt.getJWTClaimsSet().getSubject();
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            if(user.isActivated()) throw new AppException(ErrorCode.USER_IS_ACTIVATED);
            user.setActivated(true);
            user.setActivationToken(null);
            userRepository.save(user);
        }catch (ParseException e){
            log.info("Error when verifying user token");
            throw new AppException(ErrorCode.INVALID_USER_VERIFICATION_TOKEN);
        }
        return "User verification complete";
    }

    public String requestChangePassword(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserInfoBasic userInfo = UserInfoBasic.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
        String token = identityClient.generateVerificationToken(userInfo).getResult();
        user.setResetPasswordToken(token);
        userRepository.save(user);
        PasswordResetRequest request = new PasswordResetRequest(
                user.getUsername(), user.getEmail(), token);
        producerService.sendPasswordResetRequest(request);
        return "Action has been sent. Please check your email!";
    }

    public String resetPassword(String token, ResetPasswordRequest request) {
        try {
            String response = identityClient.verifyToken(token).getResult();
            SignedJWT jwt = SignedJWT.parse(response);
            String username = jwt.getJWTClaimsSet().getSubject();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            if(!token.equals(user.getResetPasswordToken())) throw new AppException(ErrorCode.INVALID_USER_RESET_TOKEN);
            if(!request.getPassword().equals(request.getConfirmPassword())) throw new AppException(ErrorCode.PASSWORD_UNMATCHED);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setResetPasswordToken(null);
            userRepository.save(user);

            PasswordChangedNotification notification = new PasswordChangedNotification(user.getUsername(), user.getEmail());
            producerService.sendPasswordChangedNotification(notification);
            return "Password changed successfully";
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_USER_RESET_TOKEN);
        }

    }

    public UserResponse getUserById(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return mapper.toUserResponse(user);
    }

    public UserResponse currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return mapper.toUserResponse(user);
    }


    public String passwordChange(UserUpdatePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!request.getNewPassword().equals(request.getConfirmPassword())) throw new AppException(ErrorCode.PASSWORD_UNMATCHED);
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) throw new AppException(ErrorCode.PASSWORD_UNMATCHED);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

}
