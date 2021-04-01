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

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "wordbook")
    private List<QA> qaList = new ArrayList<>();

    public void addQA(QA qa){
        qaList.add(qa);
        qa.setWordbook(this);
    }

    public void removeQA(QA qa){
        qaList.remove(qa);
        qa.setWordbook(null);
    }
}
