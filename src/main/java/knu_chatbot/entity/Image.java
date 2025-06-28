package knu_chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    private String encodedImage;

    public static Image of(String encodedImage) {
        return Image.builder()
                .encodedImage(encodedImage)
                .build();
    }
}
