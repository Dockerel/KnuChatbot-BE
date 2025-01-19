package knu_chatbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;
    @NotBlank
    private String disclaimer;
    @NotBlank
    private String references;

    @OneToMany(mappedBy = "answer")
    private List<AnswerImage> answerImages = new ArrayList<>();

    @OneToOne(mappedBy = "answer", optional = false)
    private Question question;

    public List<AnswerImage> getAnswerImages() {
        return answerImages;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
