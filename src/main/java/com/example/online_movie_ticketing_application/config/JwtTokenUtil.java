package com.example.online_movie_ticketing_application.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;


    //================================================================
    //Secret key is must for retrieving any information from the token (Secret key will be provided via application.properties file)
    private Claims getAllClaimsFromToken(String token){
        Claims claims = Jwts
                //calling parser() method for parsing the token to get all the claims
                .parser()
                //secret key will be provided via application.properties file
                .setSigningKey(secret)
                //providing the tokens from which claims will be retrieved
                .parseClaimsJws(token)
                //Finally getting the body of the claims
                .getBody();


        System.out.println("Hi, here we are printing the claims");
        System.out.println(claims);
        for(Map.Entry<String, Object> entry : claims.entrySet()){
            System.out.println("Hi, here we are printing the claims");
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        return claims;
    }

    //This is a generic method for getting all the claims from the given token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        //Retrieved claims will be constant throughout the process hence, they will be declared final
        final Claims claims = getAllClaimsFromToken(token);
        //Calling apply() method of 'Function' interface
        return claimsResolver.apply(claims);
    }
    //================================================================



    //================================================================
    //Check if the token is expired
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //Extracting expiration date from jwt token
    private Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, claims -> claims.getExpiration());
    }
    //================================================================



    //================================================================
    //Checking validation status of the token
    public Boolean validateToken(String token, CustomUserDetails customUserDetails){
        final String userEmail = getUserEmailFromToken(token);
        String userEmailInRecords = customUserDetails.getUserEntity().getUserEmail();
        return (userEmail.equals(userEmailInRecords) && !isTokenExpired(token));
    }

    //Extracting username from the token
    public String getUserEmailFromToken(String token) {
        //In our case subject is userEmail and hence Username is also userEmail
        return getClaimFromToken(token, claims -> claims.getSubject());
    }
    //================================================================


    //================================================================
    //Generation of token for the user
    public String generateToken(CustomUserDetails customUserDetails){
        Map<String, Object> claims = new HashMap<>();
        String userEmail = customUserDetails.getUserEntity().getUserEmail();
        return doGenerateToken(claims,userEmail);
    }

    private String doGenerateToken(Map<String,Object> claims,String subject){
        return Jwts
                .builder()
                //Here we are setting the claims of the token
                .setClaims(claims)
                //Here we are setting the subject of the token (for our case it will be username)
                .setSubject(subject)
                //Here we will set the date when was the token issued
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //And here the expiration date
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                //Algorithms we are going to use for hashing is HS512
                .signWith(SignatureAlgorithm.HS512, secret)
                //This is quite important as we have to make the token compact: Compaction means converting JWT into a
                //URL-Safe String
                .compact();
    }
    //================================================================



}
