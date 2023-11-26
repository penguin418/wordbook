package penguin.wordbook.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import penguin.auth.model.entity.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserDetail extends User{
    private Long accountId;
    private String nickname;

    public UserDetail(Account account){
        super(account.getEmail(), account.getPassword(),authorities());
        this.accountId = account.getAccountId();
        this.nickname = account.getNickname();
    }

    private static Collection<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
