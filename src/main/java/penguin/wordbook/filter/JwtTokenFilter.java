package penguin.wordbook.filter;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import penguin.wordbook.config.UserDetail;
import penguin.wordbook.service.AccountService;
import penguin.wordbook.util.CookieUtil;
import penguin.wordbook.util.JwtTokenUtil;
import penguin.wordbook.util.RedisUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 토큰을 검사하고 로그인 되었을 경우 auth 정보 추가
 *  - OncePerRequestFilter 상속: request 시만 동작
 */
@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final AccountService accountApiLogicService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;

    /**
     * 세션에 토큰이 있는 지 검사하고 데이터를 전달
     * @param req {HttpServletRequest}
     * @param res {HttpServletResponse}
     * @param filterChain {FilterChain}
     * @throws ServletException 서블릿 예외
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {

        final Cookie accessTokenCookie = CookieUtil.getCookie(req, JwtTokenUtil.ACCESS_TOKEN_NAME);
        String email = null;
        if(accessTokenCookie != null) {
            try {
                final String accessToken = accessTokenCookie.getValue();
                jwtTokenUtil.validateToken(accessToken);
                email = jwtTokenUtil.getEmailFromToken(accessToken);
            } catch (ExpiredJwtException e) {
                // jwt 만료 시 갱신
                final String newAccessToken = getNewAccessTokenIfPossible(req, res);
                if (newAccessToken != null)
                    email = jwtTokenUtil.getEmailFromToken(newAccessToken);
            }catch (UnsupportedClassVersionError e){
                // 이전 버전의 토큰이 발견됨, 삭제함
                CookieUtil.deleteCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME);
                CookieUtil.deleteCookie(res, JwtTokenUtil.REFRESH_TOKEN_NAME);
            }
            catch (Exception ignored) {
                // 로그인 정보 없음
            }
        }

        if (email != null){
            // 유저 정보를 auth 에 삽입
            final UserDetail userDetails = accountApiLogicService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(req, res);
    }

    /**
     * redis 에서 리프레시 토큰 확인 후 발급
     * @param req {HttpServletRequest}
     * @param res {HttpServletResponse}
     * @return
     */
    private String getNewAccessTokenIfPossible(HttpServletRequest req, HttpServletResponse res){
        final Cookie refreshTokenCookie = CookieUtil.getCookie(req, JwtTokenUtil.REFRESH_TOKEN_NAME);
        String accessToken = null;

        if (refreshTokenCookie != null){
            // 레디스에서 리프레시 토큰 확인
            final String refreshToken = refreshTokenCookie.getValue();
            final String emailFromToken = jwtTokenUtil.getEmailFromToken(refreshToken);
            String emailFromRedis = redisUtil.getData(refreshToken);

            // 리프레시 토큰이 유효하면 재발급
            if (emailFromToken != null && emailFromToken.equals(emailFromRedis)){
                final UserDetail userDetails = accountApiLogicService.loadUserByUsername(emailFromToken);
                accessToken = jwtTokenUtil.issueAccessToken(userDetails);
                CookieUtil.addCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME, accessToken, JwtTokenUtil.ACCESS_EXPIRATION_MS);
            }
        }
        return accessToken;
    }
}
