package knu_chatbot.repository;

import knu_chatbot.entity.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    private Long saveImageId;
    public static final String TEST_HASH_VALUE = "test hash value";
    public static final String TEST_IMAGE_URL = "test image url";

    @BeforeEach
    void setUp() {
        Image image = new Image().builder()
                .hashValue(TEST_HASH_VALUE)
                .url(TEST_IMAGE_URL)
                .build();
        Image saveImage = imageRepository.save(image);
        saveImageId = saveImage.getId();
    }

    @Test
    @DisplayName("Image가 DB에 저장되어야 함")
    void saveImage() {
        // given

        // when
        Optional<Image> findImage = imageRepository.findById(saveImageId);

        // then
        assertAll(
                () -> assertThat(findImage).isPresent(),
                () -> {
                    Image image = findImage.get();
                    assertThat(image.getHashValue()).isEqualTo(TEST_HASH_VALUE);
                    assertThat(image.getUrl()).isEqualTo(TEST_IMAGE_URL);
                }
        );
    }

    @Test
    @DisplayName("Image가 DB에서 삭제되어야 함")
    void deleteImage() {
        // given

        // when
        imageRepository.deleteById(saveImageId);
        Optional<Image> findImage = imageRepository.findById(saveImageId);

        // then
        assertThat(findImage).isEmpty();
    }
}
