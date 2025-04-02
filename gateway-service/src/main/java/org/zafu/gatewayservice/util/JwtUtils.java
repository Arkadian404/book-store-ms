package org.zafu.gatewayservice.util;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtils {
    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${app.jwt.expiration-time}")
    private long EXPIRATION_TIME;

//    private <T> T extractClaim(String token, Function<Claims, T> resolver){
//        final Claims claims = getClaims(token);
//        return resolver.apply(claims);
//    }

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
//
//
//    private SecretKey getSigningKey(){
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
//    }
}
