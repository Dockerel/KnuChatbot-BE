package knu_chatbot.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chat")
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getChats().remove(this);
        }
        this.member = member;
        this.member.getChats().add(this);
    }
}
