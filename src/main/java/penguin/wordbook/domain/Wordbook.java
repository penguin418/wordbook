package penguin.wordbook.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity(name = "wordbook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wordbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordbookId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "wordbook")
    private List<QA> qaList = new ArrayList<>();

    public void setQaList(List<QA> qaList){
        for (QA qa: qaList){
            qa.setWordbook(this);
        }
        this.qaList = qaList;
    }

    public void addQA(QA qa){
        qaList.add(qa);
        qa.setWordbook(this);
    }

    public void removeQA(QA qa){
        qaList.remove(qa);
        qa.setWordbook(null);
    }
}
