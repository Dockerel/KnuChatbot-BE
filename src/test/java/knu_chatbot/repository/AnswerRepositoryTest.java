package knu_chatbot.repository;

import jakarta.persistence.EntityManager;
import knu_chatbot.entity.Answer;
import knu_chatbot.entity.AnswerImage;
import knu_chatbot.entity.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerImageRepository answerImageRepository;
    @Autowired
    private EntityManager em;

    public static final String TEST_HASH_VALUE = "test hash value";
    public static final String TEST_IMAGE_URL = "test image url";

    @Test
    @DisplayName("Answer에서 AnswerImage를 통해 Image까지 영속성 전이가 이루어져야 함")
    void SaveAnswerTest() {
        // given
        Image image = new Image().builder()
                .hashValue(TEST_HASH_VALUE)
                .url(TEST_IMAGE_URL)
                .build();
        AnswerImage answerImage = new AnswerImage();
        answerImage.setImage(image);

        Answer answer = new Answer().builder()
                .text("test answer text")
                .references("test answer reference")
                .build();
        answer.addAnswerImage(answerImage);
        Answer saveAnswer = answerRepository.save(answer);

        // when
        Optional<Answer> findAnswerOptional = answerRepository.findById(saveAnswer.getId());

        // then
        assertAll(
                () -> assertThat(findAnswerOptional).isPresent(),
                () -> {
                    Answer findAnswer = findAnswerOptional.get();
                    List<AnswerImage> findAnswerImages = findAnswer.getAnswerImages();
                    assertThat(findAnswerImages.size()).isEqualTo(1);

                    Image findImage = findAnswerImages.get(0).getImage();
                    assertThat(findImage).isNotNull();
                    assertThat(findImage.getHashValue()).isEqualTo(TEST_HASH_VALUE);
                    assertThat(findImage.getUrl()).isEqualTo(TEST_IMAGE_URL);
                }
        );

    }

}