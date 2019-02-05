package lv.ok.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lv.ok.models.User;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

public class JwtGenerator {

    private String secret = "Yn2kjibdd8WXmohJMCvigQggaEypa5E=";

    public String generateToken(User user) {

        String jws = null;
        try {
            jws = Jwts.builder()
                    .setIssuer("PlanIt")
                    .setSubject("msilverman")
                    .claim(Constants.USERNAME, user.getUsername())
                  //  .claim("company", user.getCompany())
                    .claim("scope", "admins")
                    // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
                    .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                    // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
                    .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                    .signWith(
                            SignatureAlgorithm.HS256,
                            secret.getBytes("UTF-8")
                    )
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jws;
    }


    public boolean validateToken(String jwt) {

        Jws<Claims> claims;
        try {

            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
        }
        catch (UnsupportedEncodingException uee) {return false;}

        String issuer = (String) claims.getBody().get("iss");
        String username = (String) claims.getBody().get(Constants.USERNAME);

        if("PlanIt".equals(issuer) && username != null) {
            return true;
        }
        return false;
    }

//    private void parseJWT(String jwt) {
//
//        //This line will throw an exception if it is not a signed JWS (as expected)
//        Claims claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
//                .parseClaimsJws(jwt).getBody();
//        System.out.println("ID: " + claims.getId());
//        System.out.println("Subject: " + claims.getSubject());
//        System.out.println("Issuer: " + claims.getIssuer());
//        System.out.println("Expiration: " + claims.getExpiration());
//    }


    public static void main(String[] args) throws UnsupportedEncodingException {


    }
}
