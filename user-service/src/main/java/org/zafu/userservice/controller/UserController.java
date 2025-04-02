package org.zafu.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zafu.userservice.dto.ApiResponse;
import org.zafu.userservice.dto.request.ChangePasswordRequest;
import org.zafu.userservice.dto.request.RegisterRequest;
import org.zafu.userservice.dto.request.ResetPasswordRequest;
import org.zafu.userservice.dto.request.UserRequest;
import org.zafu.userservice.dto.response.UserInfoResponse;
import org.zafu.userservice.dto.response.UserResponse;
import org.zafu.userservice.mapper.UserMapper;
import org.zafu.userservice.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get all users")
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/current")
    public ApiResponse<UserResponse> currentUser(){
        return ApiResponse.<UserResponse>builder()
                .message("Current user")
                .result(userService.currentUser())
                .build();
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.result.username == authentication.name")
    public ApiResponse<UserResponse> getUserById(@PathVariable Integer id){
        return ApiResponse.<UserResponse>builder()
                .message("Get user successfully")
                .result(userService.getUserById(id))
                .build();
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> createUser(
            @RequestBody @Valid UserRequest request
            ){
        return ApiResponse.<UserResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(request))
                .build();
    }




    @GetMapping("/username/{username}")
    public ApiResponse<UserInfoResponse> getUserByUsername(@PathVariable String username){
        return ApiResponse.<UserInfoResponse>builder()
                .message("Found user with username: " + username)
                .result(userService.getUserInfoByUsername(username))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody RegisterRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Register user successfully!")
                .result(userService.registerUser(request))
                .build();
    }

    @PostMapping("/password/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest request){
        return ApiResponse.<String>builder()
                .message("Sending request successfully")
                .result(userService.requestChangePassword(request.getEmail()))
                .build();
    }

    @PostMapping("/password/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token,
            @RequestBody ResetPasswordRequest request){
        return ApiResponse.<String >builder()
                .message("Reset successfully")
                .result(userService.resetPassword(token, request))
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<String> verifyUser(@RequestParam String token){
        return ApiResponse.<String>builder()
                .message("Verify user token")
                .result(userService.verifyUserToken(token))
                .build();
    }

    @GetMapping("/secured_ad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloA (){
        return ResponseEntity.ok().body("WOW YOU REACHED SECURED ENDPOINT!");
    }

    @GetMapping("/secured")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> helloU (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body("WOW YOU REACHED SECURED ENDPOINT!, you are" + authentication.getName()+ " with role: "+ authentication.getAuthorities());
    }
}
