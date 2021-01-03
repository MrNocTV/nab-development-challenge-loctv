package com.example.prepaiddata.security;

import com.example.prepaiddata.exception.SecuredEndPointUnreachableException;
import com.example.prepaiddata.security.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class SecureEndPointAspect {

    private final JwtTokenManager jwtTokenManager;

    @Before("@annotation(securedEndPoint)")
    public void doBefore(JoinPoint pjp, SecuredEndPoint securedEndPoint) {
        Object[] args = pjp.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String jwtToken = jwtTokenManager.resolveToken(request);

        if (jwtToken == null) {
            throw new SecuredEndPointUnreachableException("Request does not include JWT token");
        }

        jwtTokenManager.authenticate(jwtToken);
    }
}
