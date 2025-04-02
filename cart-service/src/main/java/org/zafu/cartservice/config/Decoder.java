package org.zafu.cartservice.config;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
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


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            Instant issueTime = jwt.getJWTClaimsSet().getIssueTime().toInstant();
            Instant expiredTime = jwt.getJWTClaimsSet().getExpirationTime().toInstant();
            if (expiredTime.isBefore(Instant.now())) {
                throw new JwtException("Token has expired");
            }
            Map<String, Object> headers = jwt.getHeader().toJSONObject();
            Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();
            return new Jwt(token, issueTime, expiredTime, headers, claims);
        } catch (ParseException e) {
            throw new JwtException("Invalid Token");
        }
    }
}
