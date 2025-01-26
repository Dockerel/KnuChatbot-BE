package knu_chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends DateTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
}
