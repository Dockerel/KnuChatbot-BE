package knu_chatbot.entity;

import jakarta.persistence.*;

@Entity
public class AnswerImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ANSWER_ID", nullable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "IMAGE_ID", nullable = true)
    private Image image;

    public void setAnswer(Answer answer) {
        if (this.answer != null) {
            this.answer.getAnswerImages().remove(this);
        }
        this.answer = answer;
        this.answer.getAnswerImages().add(this);
    }

    public void setImage(Image image) {
        if (this.image != null) {
            this.image.getAnswerImages().remove(this);
        }
        this.image = image;
        this.image.getAnswerImages().add(this);
    }
}
