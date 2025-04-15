package org.zafu.identityservice.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zafu.identityservice.dto.response.BasicUserInfo;
import org.zafu.identityservice.dto.response.UserInfo;
import org.zafu.identityservice.exception.AppException;
import org.zafu.identityservice.exception.ErrorCode;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Component
@Slf4j(topic = "JWT-UTILS")
public class JwtUtils {
    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${app.jwt.expiration-time}")
    private long EXPIRATION_TIME;
    @Value("${app.jwt.refresh-expiration-time}")
    private long REFRESH_EXPIRATION_TIME;


    public String generateToken(BasicUserInfo userInfo, TokenType purpose){
        try{
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userInfo.getUsername())
                    .issuer("zafu@og.com")
                    .issueTime(new Date())
                    .expirationTime(
                            purpose == TokenType.REFRESH ?
                                    new Date(
                                            Instant.now()
                                                    .plus(REFRESH_EXPIRATION_TIME, ChronoUnit.SECONDS)
                                                    .toEpochMilli()) :
                                    new Date(
                                            Instant.now()
                                                    .plus(EXPIRATION_TIME, ChronoUnit.SECONDS)
                                                    .toEpochMilli())
                    )
                    .claim("scope", buildScope(userInfo))
                    .claim("purpose", purpose)
                    .claim("uid", userInfo.getUserId())
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());
            JWSObject object = new JWSObject(header, payload);
            object.sign(new MACSigner(SECRET_KEY.getBytes()));
            return object.serialize();
        } catch (JOSEException e) {
            log.info("Can't create token! cause {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isValidToken(String token) throws JOSEException, ParseException {
        boolean isValid = true;
        try{
            verifyToken(token);
        }catch (AppException e){
            isValid = false;
        }
        return isValid;
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT jwt = SignedJWT.parse(token);
        Date expiredTime = jwt.getJWTClaimsSet().getExpirationTime();
        boolean verified = jwt.verify(verifier);
        if(!(verified && expiredTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return jwt;
    }

    private String buildScope(BasicUserInfo userInfo) {
        StringJoiner joiner = new StringJoiner(" ");
        if(!userInfo.getRole().isEmpty()){
            joiner.add("ROLE_" + userInfo.getRole());
        }
        return joiner.toString();
    }

}
