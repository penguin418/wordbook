package penguin.wordbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "qa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QA {

    @Id
    @Column(name = "qa_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordbook_id;

    private String question;

    private String answer;
}
