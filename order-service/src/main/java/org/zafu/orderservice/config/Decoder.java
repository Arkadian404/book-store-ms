package org.zafu.orderservice.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Decoder implements JwtDecoder {

    @Value("${security.internal.secret-key}")
    private String secretKey;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            boolean verified = jwt.verify(verifier);
            if (!verified) {
                throw new JwtException("Invalid signature");
            }
            Instant issueTime = jwt.getJWTClaimsSet().getIssueTime().toInstant();
            Instant expiredTime = jwt.getJWTClaimsSet().getExpirationTime().toInstant();
            if (expiredTime.isBefore(Instant.now())) {
                throw new JwtException("Token has expired");
            }
            Map<String, Object> headers = jwt.getHeader().toJSONObject();
            Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();
            return new Jwt(token, issueTime, expiredTime, headers, claims);
        } catch (ParseException | JOSEException e) {
            throw new JwtException("Invalid Token");
        }
    }
}
