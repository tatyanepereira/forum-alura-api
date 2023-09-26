package com.forumalura.services.impl;

import com.forumalura.services.AuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getEmail() {
        return SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getName();
    }
}
