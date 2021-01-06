package penguin.wordbook.domain;

import javax.persistence.*;

@Entity(name="qa")
public class QA {
    @Id
    @Column(name = "qa_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long wordbook_id;
    private String question;
    private String answer;

    public QA(){}
    public QA(String question, String answer){this.question = question; this.answer = answer;}

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWordbook_id() {
        return wordbook_id;
    }

    public void setWordbook_id(Long wordbook_id) {
        this.wordbook_id = wordbook_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
