package lt.vu.security.config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "BV4p0TeAySnnJYzE9GPn94WXnzbMs8mxOkR84uicsX74IlYDs2Jd1dQ9GrCtnbTT4g6CbaI0F2zleTVxsYLzWoODoSW71iRtQDbLxtbbR4+ySxKVAhAP5XopIuq/h+qTl7EvGPn0XGiKqbZSPKPvDDe/CTpZb0dIIzljQG+pvKfs7i7c9RtosuE3K7cDh1xeRE4yZyGmq7z+fBvwDMIk+SQ78sTeG3G17DzsmSxEfC63xX3HURzL3C8X5CwIpO5HJplY67RerKSsqQa2nwnoSvyt+rala69Ydntmnsasjyq9nYovgSsupIbFOcUaFQshfHhtQ015xjA6RLqAjOnvLMrdFrakZf0exYWl7Es3U6Y=" ; //generated online

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

}