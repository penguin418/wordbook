package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Optional<Account> findByEmail(String email);
    public Optional<Account> findByNickname(String nickname);
}
