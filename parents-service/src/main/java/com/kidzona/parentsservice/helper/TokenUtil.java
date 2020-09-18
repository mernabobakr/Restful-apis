package com.kidzona.parentsservice.helper;

import com.kidzona.parentsservice.exception.TokenAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    @Value("${auth.secret}")
    private String TOKEN_SECRET;


    public int getUserIdFromJWT(String token) {
        Claims claims;
        try{
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            throw new TokenAuthenticationException("Invalid Token");
        }
        return Integer.valueOf(claims.getSubject());
    }
}
