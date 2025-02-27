// package com.ordenaris.internalControl;

// import java.util.Arrays;
// import java.util.Collection;

// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.core.GrantedAuthority;


// public class UserPassOrgAuthToken extends AbstractAuthenticationToken {

//     private final Object credentials;
//     private final Object principal;
//     private final Object distribuidor;

//     public UserPassOrgAuthToken(Object principal, Object credentials) {
//         super(null);
//         this.principal = principal;
//         this.credentials = credentials;
//         this.distribuidor = null;
//         setAuthenticated(false);
//     }

//     public UserPassOrgAuthToken(Object principal, Object credentials, GrantedAuthority[] authorities, Object distribuidor) {
//         this(principal, credentials, Arrays.asList(authorities), distribuidor);
//     }

//     public UserPassOrgAuthToken(Object principal, Object credentials, Collection<GrantedAuthority> authorities, Object distribuidor) {
//         super(authorities);
//         this.principal = principal;
//         this.credentials = credentials;
//         this.distribuidor = distribuidor;
//         super.setAuthenticated(true);
//     }

//     public Object getCredentials() {
//         return this.credentials;
//     }

//     public Object getPrincipal() {
//         return this.principal;
//     }
//     public Object getDistribuidor() {
//         return this.distribuidor;
//     }

//     public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//         super.setAuthenticated(false);
//     }
// }