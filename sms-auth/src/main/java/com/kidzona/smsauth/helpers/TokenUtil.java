package com.kidzona.smsauth.helpers;

import com.kidzona.smsauth.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtil {

    private static final long VALID_PERIOD = 864000L;

    @Value("${auth.secret}")
    private String TOKEN_SECRET;

    public String generateJWT(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + VALID_PERIOD * 1000);
    }

}
