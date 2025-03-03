package com.ordenaris.internalControl;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserPassOrgAuthToken extends AbstractAuthenticationToken {

    private final Object credentials;
    private final Object principal;


    public UserPassOrgAuthToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public UserPassOrgAuthToken(Object principal, Object credentials, GrantedAuthority[] authorities) {
        this(principal, credentials, Arrays.asList(authorities));
    }

    public UserPassOrgAuthToken(Object principal, Object credentials, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }
}
