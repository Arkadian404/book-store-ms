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
import org.zafu.userservice.dto.request.*;
import org.zafu.userservice.dto.response.UserInfoResponse;
import org.zafu.userservice.dto.response.UserResponse;
import org.zafu.userservice.dto.response.ghn.District;
import org.zafu.userservice.dto.response.ghn.Province;
import org.zafu.userservice.dto.response.ghn.Ward;
import org.zafu.userservice.mapper.UserMapper;
import org.zafu.userservice.service.GhnService;
import org.zafu.userservice.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final GhnService ghnService;
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


    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserUpdateRequest request
            ){
        return ApiResponse.<UserResponse>builder()
                .message("Update user successfully")
                .result(userService.updateUser(id, request))
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
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request){
        return ApiResponse.<String>builder()
                .message("Sending request successfully! Check you email")
                .result(userService.requestChangePassword(request.getEmail()))
                .build();
    }

    @PostMapping("/password/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token,
            @RequestBody @Valid ResetPasswordRequest request){
        return ApiResponse.<String >builder()
                .message("Reset successfully")
                .result(userService.resetPassword(token, request))
                .build();
    }

    @PutMapping("/current/password")
    public ApiResponse<String> updatePassword(
            @RequestBody @Valid UserUpdatePasswordRequest request){
        return ApiResponse.<String>builder()
                .message("Update password successfully")
                .result(userService.passwordChange(request))
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<String> verifyUser(@RequestParam String token){
        return ApiResponse.<String>builder()
                .message("Verify user complete, you can login now!")
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


    @GetMapping("/location/provinces")
    public ApiResponse<List<Province>> getProvinces(){
        return ApiResponse.<List<Province>>builder()
                .message("Get all provinces")
                .result(ghnService.getProvinces())
                .build();
    }

    @GetMapping("/location/districts")
    public ApiResponse<List<District>> getDistricts(
            @RequestParam Integer provinceId
    ){
        return ApiResponse.<List<District>>builder()
                .message("Get all provinces")
                .result(ghnService.getDistricts(provinceId))
                .build();
    }

    @GetMapping("/location/wards")
    public ApiResponse<List<Ward>> getWards(
            @RequestParam Integer districtId
    ){
        return ApiResponse.<List<Ward>>builder()
                .message("Get all provinces")
                .result(ghnService.getWards(districtId))
                .build();
    }
}
