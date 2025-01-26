package knu_chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String hashValue; // base64 인코딩된 문자열 해시화한 값

    @NotBlank
    private String url;
}
