package com.ordenaris.internalControl;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

<<<<<<< HEAD
public class UserPassOrgAuthToken extends AbstractAuthenticationToken {

    private final Object credentials;
    private final Object principal;
=======

public class UserPassOrgAuthToken extends AbstractAuthenticationToken {
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9

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

<<<<<<< HEAD
    public Object getPrincipal() {
        return this.principal;
=======
    public Object getCredentials() {
        return this.credentials;
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
    }

    public Object getPrincipal() {
        return this.principal;
    }

<<<<<<< HEAD
//     public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//         super.setAuthenticated(false);
//     }
}
=======
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }
}
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
