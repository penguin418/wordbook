package penguin.wordbook.util;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 쿠키 추가
     * @param res {HttpServletResponse} 리스폰스
     * @param name {String} 쿠키 이름
     * @param value {String} 쿠키 값
     * @param expireAt {Long} 만료
     */
    public static void addCookie(HttpServletResponse res, String name, String value, Long expireAt){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(Math.toIntExact(expireAt))
                .build();
        res.addHeader("Set-Cookie", cookie.toString());
    }
    /**
     * 쿠키 삭제
     * @param res {HttpServletResponse} 리스폰스
     * @param name {String} 쿠키 이름
     */
    public static void deleteCookie(HttpServletResponse res, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        res.addCookie(cookie);
    }
    /**
     * 쿠키 조회
     * FIXME: optional 로 바꾸는 게 맞는 거 같은데
     *
     * @param req {HttpServletRequest} 리퀘스트
     * @param name {String} 쿠키 이름
     * @return Cookie 성공 시 쿠키, 실패 시 null
     */
    public static Cookie getCookie(HttpServletRequest req, String name){
        final Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(name))
                    return cookie;
            }
        return null;
    }
}
