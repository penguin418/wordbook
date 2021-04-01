package penguin.wordbook.util;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import penguin.wordbook.config.UserDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
@PropertySource("classpath:application.properties")
public class JwtTokenUtil {
    public final static String ACCESS_TOKEN_NAME = "ACCESS_TOKEN";
    public final static String REFRESH_TOKEN_NAME = "REFRESH_TOKEN";
    public final static Long ACCESS_EXPIRATION_MS = 864000L;
    public final static Long REFRESH_EXPIRATION_MS = 86400000L;

    private final static String SECRET_KEY = "secret";
    private final static String SUBJECT_NAME = "penguin.wordbook.v1";
    private final static String CLAIM_ACCOUNT_ID = "id";
    private final static String CLAIM_NICKNAME = "nickname";
    private final static String CLAIM_EMAIL = "email";

    public String parseTokenString(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String issueAccessToken(UserDetail userDetails) {
        return issue(userDetails, ACCESS_EXPIRATION_MS);
    }

    public String issueRefreshToken(UserDetail userDetails) {
        return issue(userDetails, REFRESH_EXPIRATION_MS);
    }

    // 자세한 정보는 https://jwt.io
    public String issue(UserDetail userDetails, Long expiration) {
        // => 헤더, 페이로드, 키 (https://velopert.com/2389) (https://tools.ietf.org/html/rfc7519#section-4)
        return Jwts.builder()
                // 헤더 - 타입(typ)
                .setHeaderParam("typ", "JWT")
                // 페이로드 - 서브젝트(sub)
                .setSubject(SUBJECT_NAME)
                // 페이로드 - 유니큐한 식별자
                // https://stackoverflow.com/questions/47908754/setting-two-subject-in-jwt-token-generation-in-spring-boot-microservice
                .claim(CLAIM_ACCOUNT_ID, userDetails.getAccountId())
                .claim(CLAIM_NICKNAME, userDetails.getNickname())
                .claim(CLAIM_EMAIL, userDetails.getUsername())
//                .claim("account_authorities", userDetails.getAuthorities())
                // 키 - alg, 키
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 만료시간 (exp)
                // 2020-02-27 penguin 만료시간 하루로 늘림
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료시간
                .compact();
    }
    /**
     * 아이디을 반환
     *
     * @param token {String} 토큰
     * @return accountId {Long}
     */
    public Long getAccountId(String token) {
        Claims claim = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claim.get(CLAIM_ACCOUNT_ID, Long.class);
    }

    /**
     * 닉네임을 반환
     *
     * @param token {String} 토큰
     * @return nickname {String}
     */
    public String getNickname(String token) {
        Claims claim = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claim.get(CLAIM_NICKNAME, String.class);
    }

    /**
     * 이메일을 반환
     *
     * @param token {String} 토큰
     * @return email {String}
     */
    public String getEmailFromToken(String token) {
        Claims claim = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claim.get(CLAIM_EMAIL, String.class);
    }

    /**
     *
     * @param token
     * @return
     */
    public void validateToken(String token) {
        System.out.println(token);
        if (!token.equals("")) {
            try {
                assert Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token).
                        getBody().
                        getSubject().
                        equals(SUBJECT_NAME);
            } catch (SignatureException e) {
                throw new SignatureException("사인이 유효하지 않음");
            } catch (MalformedJwtException e) {
                throw new MalformedJwtException("jwt 토큰이 유효하지 않음", e);
            } catch (ExpiredJwtException e) {
                System.out.println("jwt 토큰이 만료됨: " + e);
                throw new ExpiredJwtException(e.getHeader(),e.getClaims(), e.getMessage());
            } catch (UnsupportedJwtException e) {
                throw new UnsupportedJwtException("jwt 토큰 파싱을 안했거나 실패함", e);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("jwt claim 이 비어있음", e);
            } catch (AssertionError e){
              throw new UnsupportedClassVersionError("jwt 토큰 버전이 다름( 옛날 subject name 이 적혀있음)");
            }
        }
    }
}
