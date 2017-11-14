/*
 * Bignon
 */
package com.bootcamp.rest.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date; 
public class JavaJsonWebToken {
    
    String key = "bootkey";
    private String tk="initie";
    
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    
//Methode pour creer un JWT
public String createJWT(String issuer, String subject, long ttlMillis) {
    

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    //
    JwtBuilder builder = Jwts.builder()
                                .setIssuedAt(now)
                                .setIssuer(issuer)
                                .setSubject(subject)
                                .signWith(signatureAlgorithm, signingKey);    

    //Date d'expiration control
    if (ttlMillis >= 0) {
    long expMillis = nowMillis + ttlMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
    }
        
    return builder.compact();
}

 //Methode Pour verifier un JWT 
 public void   parseJWT ( String token )   { 
     
     //cette ligne retournera une erreur si le token n'est pas valide
     
     Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
     
      } 
 
 public void setToken(String token){
     this.tk = token;
 }
  
 public String getToken(){
     return tk;
 }
  
}
