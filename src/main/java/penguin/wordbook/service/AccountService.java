package penguin.wordbook.service;

import static penguin.wordbook.controller.dto.AccountDto.*;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.domain.Account;
import penguin.wordbook.config.UserDetail;
import penguin.wordbook.mapper.AccountMapper;
import penguin.wordbook.repository.AccountRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    /**
     * 회원 가입
     *
     * @param accountCreateDto {AccountCreateDto} 가입 요청
     * @return AccountResponseDto 가입 정보
     */
    public AccountInfoDto create(AccountCreateDto accountCreateDto) throws EntityExistsException {

        // 검증
        validateDuplicateAccountNickname(accountCreateDto.getNickname());
        validateDuplicateAccountEmail(accountCreateDto.getEmail());

        // 가입
        Account account = accountMapper.map(accountCreateDto);
        accountRepository.save(account);

        return accountMapper.map(account);
    }

    /**
     * 닉네임 중복 확인
     * - 가입 시 최종 확인
     * TODO: 가입 중 확인, 미구현시 private 으로 다시 바꿀 것
     *
     * @param nickname {String} 닉네임
     * @throws EntityExistsException 중복된 닉네임
     */
    public void validateDuplicateAccountNickname(final String nickname) {
        accountRepository.findByNickname(nickname).ifPresent(account -> {
            throw new EntityExistsException("존재하는 닉네임");
        });
    }

    /**
     * 이메일이 중복 확인
     * - 가입 시 최종 확인
     * TODO: 가입 중 확인, 미구현시 private 으로 다시 바꿀 것
     *
     * @param email {String} 이메일
     * @throws EntityExistsException 중복된 이메일
     */
    public void validateDuplicateAccountEmail(final String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new EntityExistsException("존재하는 이메일");
        });
    }

    /**
     * 검색
     * @param id {Long} 아이디
     * @return AccountInfoDto 가입 정보
     */
    public AccountInfoDto findOne(final Long id){
        Account account = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return accountMapper.map(account);
    }

    /**
     * 갱신
     * @param accountUpdateDto {AccountUpdateDto} 수정 내역
     * @return AccountInfoDto 가입 정보
     * @throws EntityExistsException 이미 존재하는 이메일, 닉네임으로 설정하려 할때 발생
     */
    public AccountInfoDto update(final AccountUpdateDto accountUpdateDto) throws EntityExistsException{

        validateDuplicateAccountNickname(accountUpdateDto.getNickname());
        validateDuplicateAccountEmail(accountUpdateDto.getEmail());

        Account account = accountMapper.map(accountUpdateDto);
        accountRepository.save(account);
        return accountMapper.map(account);
    }

    /**
     * 유저 디테일을 가져옴
     * - UserDetailsService 상속 하면서 만듦
     * - 로그인 시 사용됨
     *
     * @param email {String} 만약 변경 시, unique 한 account 식별자 가능 (nickname 도 가능하나 로그인 시 email 써서 email 씀)
     * @return UserDetail 유저디테일 정보
     * @throws UsernameNotFoundException 없는 유저일 경우
     */
    @Override
    public UserDetail loadUserByUsername(final String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        if (account == null) throw new UsernameNotFoundException("일치하는 아이디를 찾을 수 없습니다");
        return new UserDetail(account);
    }
}
