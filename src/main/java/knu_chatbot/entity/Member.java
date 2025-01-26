package knu_chatbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@NoArgsConstructor
@AllArgsConstructor
public class Member extends DateTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<History> histories = new ArrayList<>();

    public void addHistory(History history) {
        this.histories.add(history);
    }
}
