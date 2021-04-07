package penguin.wordbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "qa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id", foreignKey = @ForeignKey(name = "fk_qa_workbook"))
    private Wordbook wordbook;

    private String question;

    private String answer;
}
