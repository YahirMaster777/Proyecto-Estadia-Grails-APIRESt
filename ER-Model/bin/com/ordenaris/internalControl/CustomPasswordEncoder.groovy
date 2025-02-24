package com.ordenaris.internalControl
package com.ordenaris.distribuidores

// import grails.plugin.springsecurity.SpringSecurityService
// import org.springframework.security.authentication.encoding.PasswordEncoder
// import org.springframework.beans.factory.annotation.Autowired
// import groovy.transform.CompileStatic

// class CustomPasswordEncoder implements PasswordEncoder {

//     SpringSecurityService springSecurityService

//     public String encodePassword(String password, salt = null) {
//         return springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
//     }

//     public boolean isPasswordValid(String encodedPassword, String rawPassword, salt = null) {
//         return (encodedPassword == encodePassword(rawPassword, salt))
//     }
// }

    CustomPasswordEncoder(String algorithm, boolean encodeHashAsBase64) throws IllegalArgumentException {
        super()
        setEncodeHashAsBase64(encodeHashAsBase64);
        this.algorithm = algorithm;
        getMessageDigest();
    }

    
    String encodePassword(String rawPass, Object salt) {
        String saltedPass = this.mergePasswordAndSalt(rawPass, salt, false)
        MessageDigest messageDigest = this.getMessageDigest()
        byte[] digest = messageDigest.digest(saltedPass.getBytes("UTF-8"))
        for (int i = 1; i < iterations; i++) {
            digest = messageDigest.digest(digest);
        }
        // First determine whether Base64 is enabled
        if (this.getEncodeHashAsBase64()) {
            return new String(Base64.encodeAsBase64(digest))
        // Determine whether it is a custom SHA-256-1 (Framework Customized Encryption, Non-spring Security Framework, here refers to grails'own encryption)
        } else if ("SHA-256-1".equalsIgnoreCase(algorithm)) {
            return rawPass.encodeAsSHA256()
        } else {
            // Other encryption methods using user configurations
            return new String(Hex.encode(digest))
        }
    }

    @Override
    boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String password1 = "" + encPass
        String password2 = encodePassword(rawPass, salt)
        return PasswordEncoderUtils.equals(password1, password2)
        password1 = ""
        password2 = ""
    }

    String getAlgorithm() {
        return algorithm;
    }

    void setIterations(int iterations) {
        Assert.isTrue(iterations > 0, "Iterations value must be greater than zero");
        this.iterations = iterations;
    }


    @Autowired
    SpringSecurityService springSecurityService

    @Listener(Users)
    void onPreInsertEvent(PreInsertEvent event) {
        encodePasswordForEvent(event)
    }

    @Listener(Users)
    void onPreUpdateEvent(PreUpdateEvent event) {
        encodePasswordForEvent(event)
    }

    private void encodePasswordForEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof Users) {
            Users u = event.entityObject as Users
            if (u.password && ((event instanceof  PreInsertEvent) || (event instanceof PreUpdateEvent && u.isDirty('password')))) {
                event.getEntityAccess().setProperty('password', encodePasswordSpring(u.password))
            }
        }
    }

    private String encodePasswordSpring(String password) {
        springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
