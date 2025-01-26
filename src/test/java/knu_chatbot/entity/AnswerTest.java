package knu_chatbot.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("text, references에는 null이나 공백이 허용되지 않아야 한다.")
    void notBlankTest() {
        // given
        Answer answer = Answer.builder()
                .id(1L)
                .text(null)
                .references(" ")
                .build();

        // when
        Set<ConstraintViolation<Answer>> violations = validator.validate(answer);

        // then
        assertThat(violations.size()).isEqualTo(2);
    }
}