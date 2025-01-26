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

class QuestionTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("질문 text 비어 있으면 에러")
    void createQuestionWithEmptyText() {
        // given
        Question question = new Question().builder()
                .text("")
                .build();

        // when
        Set<ConstraintViolation<Question>> validate = validator.validate(question);

        // then
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문 text가 300 글자가 넘으면 에러")
    void createQuestionWithOver300Text() {
        // given
        Question question = new Question().builder()
                .text("a".repeat(301))
                .build();

        // when
        Set<ConstraintViolation<Question>> validate = validator.validate(question);

        // then
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문 생성 성공케이스")
    void createQuestionWithTextSuccess() {
        // given
        Question question = new Question().builder()
                .text("a".repeat(300))
                .build();

        // when
        Set<ConstraintViolation<Question>> validate = validator.validate(question);

        // then
        assertThat(validate).isEmpty();
    }

}