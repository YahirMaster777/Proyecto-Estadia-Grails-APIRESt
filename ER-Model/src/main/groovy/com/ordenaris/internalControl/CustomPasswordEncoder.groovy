// package com.ordenaris.internalControl

// import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder
// import org.springframework.security.authentication.encoding.PasswordEncoderUtils
// import org.springframework.security.crypto.codec.Hex
// import org.springframework.util.Assert

// import java.security.MessageDigest

// import grails.plugin.springsecurity.SpringSecurityService
// import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
// import org.grails.datastore.mapping.engine.event.PreInsertEvent
// import org.grails.datastore.mapping.engine.event.PreUpdateEvent
// import org.grails.datastore.mapping.engine.event.PreLoadEvent
// import org.springframework.beans.factory.annotation.Autowired
// import grails.events.annotation.gorm.Listener
// import groovy.transform.CompileStatic

// /**
//  * Custom Encryption Overrides Default Encryption
//  * The spring-security version of the project was 3.1.0, and the BaseDigestPasswordEncoder class could have been restarted.
//  * But I see that the BaseDigestPasswordEncoder class is marked as deleted, so it is implemented by rewriting the MessageDigestPasswordEncoder class method.
//  */
// class CustomPasswordEncoder extends MessageDigestPasswordEncoder {

//     // Default to MD5
//     private String algorithm = "MD5";

//     // Encryption Number (Enhanced Security)
//     private int iterations = 1;

//     CustomPasswordEncoder() {
//         // The default constructor of the current class, because the parent class has no empty constructor, so we must call the parent class parametric construct, where the incoming parameters must be the encryption rules of the parent class, otherwise the error will be reported.
//         super("SHA-256")
//     }

//     CustomPasswordEncoder(String algorithm) {
//         super(algorithm, false);
//         this.algorithm = algorithm
//     }

//     CustomPasswordEncoder(String algorithm, boolean encodeHashAsBase64) throws IllegalArgumentException {
//         super()
//         setEncodeHashAsBase64(encodeHashAsBase64);
//         this.algorithm = algorithm;
//         getMessageDigest();
//     }

    
//     String encodePassword(String rawPass, Object salt) {
//         String saltedPass = this.mergePasswordAndSalt(rawPass, salt, false)
//         MessageDigest messageDigest = this.getMessageDigest()
//         byte[] digest = messageDigest.digest(saltedPass.getBytes("UTF-8"))
//         for (int i = 1; i < iterations; i++) {
//             digest = messageDigest.digest(digest);
//         }
//         // First determine whether Base64 is enabled
//         if (this.getEncodeHashAsBase64()) {
//             return new String(Base64.encodeAsBase64(digest))
//         // Determine whether it is a custom SHA-256-1 (Framework Customized Encryption, Non-spring Security Framework, here refers to grails'own encryption)
//         } else if ("SHA-256-1".equalsIgnoreCase(algorithm)) {
//             return rawPass.encodeAsSHA256()
//         } else {
//             // Other encryption methods using user configurations
//             return new String(Hex.encode(digest))
//         }
//     }


//     String getAlgorithm() {
//         return algorithm;
//     }

//     void setIterations(int iterations) {
//         Assert.isTrue(iterations > 0, "Iterations value must be greater than zero");
//         this.iterations = iterations;
//     }


//     @Autowired
//     SpringSecurityService springSecurityService

//     @Listener(User)
//     void onPreInsertEvent(PreInsertEvent event) {
//         encodePasswordForEvent(event)
//     }

//     @Listener(User)
//     void onPreUpdateEvent(PreUpdateEvent event) {
//         encodePasswordForEvent(event)
//     }


// }