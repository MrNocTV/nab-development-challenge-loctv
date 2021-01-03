package com.example.prepaiddata.security.jwt;

import com.example.prepaiddata.dto.ViewTokenDTO;
import com.example.prepaiddata.exception.SecuredEndPointUnreachableException;
import com.example.prepaiddata.exception.TokenParsingException;
import com.example.prepaiddata.model.GetTokenRequestModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;


@Component
public class JwtTokenManager {

    public static final String BEARER_KEY = "Bearer ";
    private final SecureRandom secureRandom = new SecureRandom();
    private Key secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public ViewTokenDTO createToken(GetTokenRequestModel getTokenRequestModel, int timeout) {
        String phoneNumber = getTokenRequestModel.getPhoneNumber();
        String otp = getTokenRequestModel.getOtp();

        Date now = new Date();
        String jwtId = UUID.randomUUID().toString();
        Claims claims = Jwts.claims();

        claims.setSubject("VouchersViewToken");
        claims.setIssuedAt(now);
        claims.setExpiration(new Date(now.getTime() + timeout * 100000));
        claims.setId(jwtId);
        claims.setIssuer("LocTruong");
        claims.put("phoneNumber", phoneNumber);
        claims.put("otp", otp);

        String jwtToken = Jwts.builder().setClaims(claims).signWith(secretKey).compact();
        ViewTokenDTO viewTokenDTO = ViewTokenDTO.builder().jwtToken(jwtToken).build();

        return viewTokenDTO;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER_KEY)) {
            return bearerToken.substring(BEARER_KEY.length());
        }
        return null;
    }

    public void authenticate(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new SecuredEndPointUnreachableException("Token has expired");
        } catch (JwtException | IllegalArgumentException e1) {
            throw new TokenParsingException("Cannot parse token");
        }
    }

}