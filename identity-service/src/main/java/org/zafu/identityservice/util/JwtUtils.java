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

//    private <T> T extractClaim(String token, Function<Claims, T> resolver){
//        final Claims claims = getClaims(token);
//        return resolver.apply(claims);
//    }
//
//
//    public String generateToken(UserInfo userInfo){
//        return Jwts.builder()
//                .subject(userInfo.getUsername())
//                .claim("role", userInfo.getRole())
//                .issuer("ZafuOG")
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(getSigningKey(), Jwts.SIG.HS256)
//                .compact();
//    }
//
//    private Claims getClaims(String token){
//        return Jwts.parser()
//                .verifyWith(getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    public Date getExpirationDate(String token){
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public boolean isExpired(String token){
//        return getExpirationDate(token).before(new Date());
//    }
//
//    public boolean isValidToken(String token){
//        if(isExpired(token)){
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//        return true;
//    }
//
//
//    private SecretKey getSigningKey(){
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
//    }



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
