package knu_chatbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Question extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    @NotBlank
    private String text;

    @OneToOne
    @JoinColumn(name = "ANSWER_ID", nullable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "CHAT_ID", nullable = false)
    private Chat chat;

    public void setAnswer(Answer answer) {
        if (this.answer != null) {
            this.answer.setQuestion(null);
        }
        this.answer = answer;
        this.answer.setQuestion(this);
    }

    public void setChat(Chat chat) {
        if (this.chat != null) {
            this.chat.getQuestions().remove(this);
        }
        this.chat = chat;
        this.chat.getQuestions().add(this);
    }
}
