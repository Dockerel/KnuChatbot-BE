package knu_chatbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @Length(max = 300)
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

    public void setAnswer(Answer answer) {
        if (this.answer == null) {
            this.answer = answer;
        }
    }
}
