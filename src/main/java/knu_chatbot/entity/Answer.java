package knu_chatbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Answer {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String text;
    @NotBlank
    private String references;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnswerImage> answerImages = new ArrayList<>();

    public void addAnswerImage(AnswerImage answerImage) {
        this.answerImages.add(answerImage);
    }
}
