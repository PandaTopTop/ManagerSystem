package com.dzomp.use_rmanagement_system.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey secretKey;
    private static final long EXPIRATION_TIME = 86400000;

    public JWTUtils() {
        String secretString = "SSB3YXMgdGhpcnRlZW4uIEkgaGVyZGVkIGxhbWJzDQpCZXlvbmQgdGhlIHZpbGxhZ2Ugb24gdGhlIGxlYS4NClRoZSBtYWdpYyBvZiB0aGUgc3VuLCBwZXJoYXBzLA0KT3Igd2hhdCB3YXMgaXQgYWZmZWN0ZWQgbWU/DQpJIGZlbHQgd2l0aCBqb3kgYWxsIG92ZXJjb21lLA0KQXMgdGhvdWdoIHdpdGggR29kLi4uLg==";
        byte [] keyBytes = Base64.getDecoder().decode(secretString);

        this.secretKey = new SecretKeySpec(keyBytes,"HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }


    public String generateRefreshToken(HashMap<String,Object> claims, UserDetails userDetails){

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public  boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}



