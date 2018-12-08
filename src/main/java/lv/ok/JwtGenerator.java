package lv.ok;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lv.ok.models.User;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

public class JwtGenerator {

    private String secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateToken(User user) {

        String jws = Jwts.builder()
                .setIssuer("PlanIt")
                .setSubject("msilverman")
                .claim("username", user.getUsername())
                .claim("company", user.getCompany())
                .claim("scope", "admins")
                // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
                .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
                .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(
                        SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode(secret)
                )
                .compact();
        return jws;
    }

    public boolean validateToken(String jwt, User user) {

        Jws<Claims> claims;
        try {

            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
        }
        catch (UnsupportedEncodingException uee) {return false;}

        String company = (String) claims.getBody().get("company");
        if(user.getCompany().equals(company)) {
            return true;
        }
        return false;
    }


    public static void main(String[] args){

        String an = TextCodec.BASE64.decodeToString("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=");


    }
}
