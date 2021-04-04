package penguin.wordbook.controller;

import static penguin.wordbook.controller.dto.AccountDto.*;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import penguin.wordbook.config.UserDetail;
import penguin.wordbook.service.AccountService;
import penguin.wordbook.util.CookieUtil;
import penguin.wordbook.util.JwtTokenUtil;
import penguin.wordbook.util.RedisUtil;

import javax.persistence.EntityExistsException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * account 관련 rest api 지원
 * - 가입, 로그인, 로그아웃, 탈퇴
 */
@RestController
@AllArgsConstructor
public class AccountApiController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;

    /**
     * 회원가입
     * - 구체적인 생성 과정은 서비스에 위임한다
     * - 사이클 방지를 위해 password 해시는 controller 에서 수행
     * @param accountCreateDto {AccountCreateDto} 가입 요청
     * @return ResponseEntity 성공 시: AccountResponseDto, 실패 시 BadRequest
     */
    @PostMapping("/api/accounts")
    public ResponseEntity<AccountInfoDto> create(@RequestBody AccountCreateDto accountCreateDto) {
        try {
            accountCreateDto.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
            AccountInfoDto response = accountService.create(accountCreateDto);
            return ResponseEntity.ok(response);
        } catch (EntityExistsException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    /**
     * 로그인
     * -
     * @param accountLoginDto {AccountLoginDto} 로그인 요청
     * @param res {HttpServletResponse} 쿠키를 저장할 리스폰스
     * @return ResponseEntity 성공 시: AccountResponseDto, 실패 시 BadRequest
     */
    @PostMapping("/api/accounts/login")
    public ResponseEntity<AccountInfoDto> login(@RequestBody AccountLoginDto accountLoginDto,
                                                HttpServletResponse res) {
        final UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(accountLoginDto.getEmail(), accountLoginDto.getPassword());
        try {
            // 정보 확인
            authenticationManager.authenticate(token);

            // 로그인 토큰을 쿠키에 삽입
            final UserDetail userDetail = accountService.loadUserByUsername(accountLoginDto.getEmail());
            final String accessToken = jwtTokenUtil.issueAccessToken(userDetail);
            final String refreshToken = jwtTokenUtil.issueRefreshToken(userDetail);
            CookieUtil.addCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME, accessToken, JwtTokenUtil.ACCESS_EXPIRATION_MS);
            CookieUtil.addCookie(res, JwtTokenUtil.REFRESH_TOKEN_NAME, refreshToken, JwtTokenUtil.REFRESH_EXPIRATION_MS);
            redisUtil.setDataExpire(refreshToken, userDetail.getUsername(), JwtTokenUtil.REFRESH_EXPIRATION_MS);

            // 리턴
            AccountInfoDto responseDto
                    = new AccountInfoDto(userDetail.getAccountId(), userDetail.getNickname(), userDetail.getUsername());
            return ResponseEntity.ok(responseDto);
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 로그아웃
     * @param req {HttpServletRequest} 로그인 여부를 확인할 리퀘스트
     * @param res {HttpServletResponse} 쿠키를 삭제할 리스폰스
     * @return ResponseEntity noContent
     */
    @PostMapping("/api/accounts/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res){
        Cookie refreshToken = CookieUtil.getCookie(req, JwtTokenUtil.REFRESH_TOKEN_NAME);
        if (refreshToken != null){
            redisUtil.deleteData(refreshToken.getValue());
        }
        CookieUtil.deleteCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME);
        CookieUtil.deleteCookie(res, JwtTokenUtil.REFRESH_TOKEN_NAME);
        return ResponseEntity.noContent().build();
    }

    /**
     * 계정 정보
     * @param authentication {Authentication} 인증 객체
     * @param res {HttpServletResponse} 로그인 한 상태에서 계정이 삭제된 경우 토큰을 삭제할 리스폰스
     * @return ResponseEntity 성공 시: AccountResponseDto, 실패 시: notFound
     */
    @GetMapping("/api/accounts/my")
    public ResponseEntity<AccountInfoDto> getMyAccount(Authentication authentication, HttpServletResponse res){
        UserDetail userDetails = (UserDetail)authentication.getPrincipal();
        try{
            AccountInfoDto account = accountService.findOne(userDetails.getAccountId());
            return ResponseEntity.ok(account);
        }catch (EntityExistsException e){
            System.out.println("삭제된 아이디");
            CookieUtil.deleteCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME);
            CookieUtil.deleteCookie(res, JwtTokenUtil.REFRESH_TOKEN_NAME);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 업데이트
     * @param accountUpdateDto {AccountUpdateDto} 업데이트 할 내용
     * @return ResponseEntity 성공 시: AccountResponseDto, 실패 시: badRequest
     */
    @PutMapping("/api/accounts/my")
    public ResponseEntity<AccountInfoDto> update(@RequestBody AccountUpdateDto accountUpdateDto) {
        final UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(accountUpdateDto.getEmail(), accountUpdateDto.getPassword());
        try {
            // 비밀번호 검사
            authenticationManager.authenticate(token);

            // 만약 비밀번호를 업데이트하는 경우 업데이트 시켜줌
            final String newPassword = accountUpdateDto.getNewPassword();
            if (newPassword != null && !newPassword.equals("")) {
                accountUpdateDto.setPassword(passwordEncoder.encode(newPassword));
            }

            // 갱신
            AccountInfoDto responseDto = accountService.update(accountUpdateDto);
            return ResponseEntity.ok(responseDto);

        }  catch (BadCredentialsException e) {
            // 로그인된 사용자의 행동이 아님 (현재 비밀번호 모름)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityExistsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
