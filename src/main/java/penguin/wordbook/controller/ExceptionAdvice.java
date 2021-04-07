package penguin.wordbook.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    /*
     * 400 Bad Request 요청이 유효하지 않음
     * - 더 진행 시 서버에러 발생 예측되는 경우
     * - 에러 이유 밝힐 것
     *
     * 401 Unauthorized 권한이 없음
     * - 인증이 되지 않는 경우
     *
     * 403 Forbidden 금지됨
     * - 자격이 없는 경우
     *
     * 404 NotFound 자원이 없는 경우
     * - 자원이 없음
     *
     * 405 Method Not Allowed 허용되지 않음
     * - 지원하는 메소드(GET,POST,PUT,DELETE)가 아님
     *
     * 409 Conflict 충돌
     * - 비지니스 로직 상 허용하지 않는 경우
     */

    /**
     * 1001, "이미 사용중인 닉네임입니다"
     * 1002, "이미 사용중인 이메일입니다"
     * @param e DuplicateKeyException
     * @return ErrorType.EmailExists | ErrorType.NicknameExists
     */
    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorType> duplicateKeyException(Exception e) {
        if (e.getMessage().equals("nickname")) {
            return new ResponseEntity<ErrorType>(ErrorType.NicknameExists, HttpStatus.CONFLICT);
        } else if (e.getMessage().equals("email")) {
            return new ResponseEntity<ErrorType>(ErrorType.EmailExists, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<ErrorType>(ErrorType.EmailExists, HttpStatus.CONFLICT);
    }

    /**
     * 로그인 문제 - 비밀번호가 틀립니다,
     * 회원정보 수정 문제 - 비밀번호가 틀립니다
     *
     * @param e BadCredentialsException
     * @return ErrorType.PasswordDoesNotMatch
     */
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorType> badCredentialsException(Exception e) {
        return new ResponseEntity<ErrorType>(ErrorType.PasswordDoesNotMatch, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 로그인 문제 - 이메일이 존재하지 않습니다
     * authenticate 에서 발생
     * @param e BadCredentialsException
     * @return ErrorType.PasswordDoesNotMatch
     */
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorType> internalAuthenticationServiceException(Exception e) {
        return new ResponseEntity<ErrorType>(ErrorType.UsernameNotFound, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 로그인 문제
     * - 회원정보를 찾을 수 없습니다
     * - 사용자가 로그인되어있는 상태에서 유저가 삭제된 상황
     * @param e UsernameNotFoundException
     * @return ErrorType.UsernameNotFound
     */
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorType> usernameNotFoundException(Exception e) {
        return new ResponseEntity<ErrorType>(ErrorType.UsernameNotFound, HttpStatus.UNAUTHORIZED);
    }
}
