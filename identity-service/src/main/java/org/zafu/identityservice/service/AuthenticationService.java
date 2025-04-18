package org.zafu.identityservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zafu.identityservice.client.UserClient;
import org.zafu.identityservice.dto.request.AuthenticationRequest;
import org.zafu.identityservice.dto.request.IntrospectRequest;
import org.zafu.identityservice.dto.request.LogoutRequest;
import org.zafu.identityservice.dto.request.RefreshRequest;
import org.zafu.identityservice.dto.response.AuthenticationResponse;
import org.zafu.identityservice.dto.response.BasicUserInfo;
import org.zafu.identityservice.dto.response.IntrospectResponse;
import org.zafu.identityservice.dto.response.UserInfo;
import org.zafu.identityservice.exception.AppException;
import org.zafu.identityservice.exception.ErrorCode;
import org.zafu.identityservice.util.JwtUtils;
import org.zafu.identityservice.util.TokenType;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "IDENTITY-SERVICE")
public class AuthenticationService {
    private final JwtUtils jwtUtils;
    private final UserClient client;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public AuthenticationResponse login(AuthenticationRequest request){
        try{
            var response = client
                    .findUserClientByUsername(request.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            UserInfo userInfo = response.getResult();
            log.info("User info {}", userInfo.toString());
            if(!passwordEncoder.matches(request.getPassword(), userInfo.getPassword())){
                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
            }
            if(!userInfo.isActivated()){
                throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
            }
            BasicUserInfo basic = BasicUserInfo.builder()
                    .userId(userInfo.getId())
                    .username(userInfo.getUsername())
                    .role(userInfo.getRole())
                    .build();
            String accessToken = jwtUtils.generateToken(basic, TokenType.AUTHENTICATE);
            String refreshToken = jwtUtils.generateToken(basic, TokenType.REFRESH);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .authenticated(true)
                    .build();
        }catch (FeignException ex){
            log.warn("Error: {}", ex.getMessage());
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request){
        IntrospectResponse response = IntrospectResponse.builder().build();
        try{
            if(jwtUtils.isValidToken(request.getToken())) response.setValid(true);
        }catch (JOSEException | ParseException ex){
            log.warn("Error: {}", ex.getMessage());
            response.setValid(false);
        }
        return response;
    }

    public AuthenticationResponse refreshToken(RefreshRequest request){
        try{
            SignedJWT jwt = jwtUtils.verifyToken(request.getRefreshToken());
            String username = jwt.getJWTClaimsSet().getSubject();
            var response = client.findUserClientByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            BasicUserInfo userInfo = BasicUserInfo.builder()
                    .username(response.getResult().getUsername())
                    .role(response.getResult().getRole())
                    .build();
            String accessToken = jwtUtils.generateToken(userInfo, TokenType.AUTHENTICATE);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(request.getRefreshToken())
                    .authenticated(true)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.UNABLE_TO_REFRESH_TOKEN);
        }
    }

    public void logout(LogoutRequest request){
        try{
            String token = request.getToken();
            SignedJWT jwt = verify(token);
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            String jti = jwt.getJWTClaimsSet().getJWTID();
            long duration = expirationTime.getTime() - System.currentTimeMillis();
            log.info("Token {} will be blacklisted for {} milliseconds", token, duration);
            redisService.save(jti, token, duration, TimeUnit.MILLISECONDS);

        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNABLE_TO_LOGOUT);
        }
    }

    public String genEmailToken(BasicUserInfo userInfo){
        return jwtUtils.generateToken(userInfo, TokenType.EMAIL_VERIFICATION);
    }

    public SignedJWT verify(String token){
        try{
            SignedJWT jwt =  jwtUtils.verifyToken(token);
            String jti = jwt.getJWTClaimsSet().getJWTID();
            if(redisService.hasKey(jti)){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return jwt;
        } catch (JOSEException | ParseException e) {
            log.info("Error when verify user token");
            throw new AppException(ErrorCode.UNABLE_TO_VERIFY_TOKEN);
        }
    }


}
