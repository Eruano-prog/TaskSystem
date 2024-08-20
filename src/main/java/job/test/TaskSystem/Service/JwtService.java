package job.test.TaskSystem.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import job.test.TaskSystem.Model.User;
import job.test.TaskSystem.Model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для работы с JWT токенами.
 * Предоставляет методы для генерации, извлечения данных и проверки валидности токенов.
 */
@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT токен.
     * @return Имя пользователя.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерирует JWT токен для пользователя.
     *
     * @param userDetails Данные пользователя.
     * @return JWT токен.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Извлекает данные пользователя из токена.
     *
     * @param token JWT токен.
     * @return DTO пользователя со всеми данными из токена.
     */
    public UserDTO extractUser(String token) {
        Claims claims = extractAllClaims(token);

        return UserDTO.builder()
                .id(claims.get("id", Long.class))
                .nickName(claims.getSubject())
                .email(claims.get("email", String.class))
                .role(claims.get("role", String.class))
                .build();
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token       JWT токен.
     * @param userDetails Данные пользователя.
     * @return {@code true}, если токен валиден, иначе {@code false}.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлекает данные из токена.
     *
     * @param token           JWT токен.
     * @param claimsResolvers Функция извлечения данных.
     * @param <T>             Тип данных.
     * @return Данные.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT токен с дополнительными данными.
     *
     * @param extraClaims Дополнительные данные.
     * @param userDetails Данные пользователя.
     * @return JWT токен.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Проверяет, просрочен ли токен.
     *
     * @param token JWT токен.
     * @return {@code true}, если токен просрочен, иначе {@code false}.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения токена.
     *
     * @param token JWT токен.
     * @return Дата истечения.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все данные из токена.
     *
     * @param token JWT токен.
     * @return Данные.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    /**
     * Получает ключ для подписи токена.
     *
     * @return Ключ.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
