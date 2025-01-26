package knu_chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    @OneToMany
    private List<Chat> chats;

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
}
