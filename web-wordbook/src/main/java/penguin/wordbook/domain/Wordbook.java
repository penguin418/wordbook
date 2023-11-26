package penguin.wordbook.domain;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "wordbook")
@Getter
@Setter
@Builder(builderClassName = "WordbookBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Wordbook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordbookId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "wordbook")
    private Set<QA> qaList = new HashSet<>();

    public void setQaList(Set<QA> qaList){
        this.qaList.clear();
        for (QA qa: qaList){
            qa.setWordbook(this);
        }
        this.qaList.addAll(qaList);
    }
}
