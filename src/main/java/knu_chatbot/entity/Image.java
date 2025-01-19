package knu_chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Image {

    @Id
    private String id;

    @NotBlank
    private String url;

    @OneToMany(mappedBy = "image")
    private List<AnswerImage> answerImages = new ArrayList<>();

    public List<AnswerImage> getAnswerImages() {
        return answerImages;
    }
}
