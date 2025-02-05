package knu_chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class History extends DateTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "history", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(List<Question> questions) {
        this.questions.addAll(questions);
    }
}
