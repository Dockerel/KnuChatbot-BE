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
@NoArgsConstructor
@AllArgsConstructor
public class Member extends DateTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<History> histories = new ArrayList<>();

    public void addHistory(History history) {
        histories.add(history);
    }
}
