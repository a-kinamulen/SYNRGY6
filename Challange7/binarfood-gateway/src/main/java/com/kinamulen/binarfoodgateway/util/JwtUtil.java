package com.kinamulen.binarfoodgateway.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, Map<String, String>> decodeJwt(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        Map<String, String> headerMap = new HashMap<>(convertStringToMap(header));

        String payload = new String(decoder.decode(chunks[1]));
        Map<String, String> payloadMap = new HashMap<>(convertStringToMap(payload));

        Map<String, Map<String, String>> decodedJwt = new HashMap<>();
        decodedJwt.put("header", headerMap);
        decodedJwt.put("payload", payloadMap);

        return decodedJwt;
    }

    private Map<String, String> convertStringToMap(String s){
        Map<String, String> map = new HashMap<String, String>();
        for(final String entry : s.substring(1,s.length()-1).replace("\"","").split(",")) {
            final String[] parts = entry.split(":");
            assert(parts.length == 2) : "Invalid entry: " + entry;
            map.put(parts[0], parts[1]);
        }
        return map;
    }
}
