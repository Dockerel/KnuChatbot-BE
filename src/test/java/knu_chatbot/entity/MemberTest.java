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

class MemberTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이메일 필드에 이메일 형식에 맞지 않는 인풋이 들어오면 에러")
    void emailInputTest_X() {
        // given
        Member member = new Member().builder()
                .email("test")
                .password("test password")
                .nickname("test nickname")
                .build();

        // when
        Set<ConstraintViolation<Member>> validate = validator.validate(member);

        // then
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일 필드에 이메일 형식에 맞는 인풋이 들어오면 정상 작동")
    void emailInputTest_O() {
        // given
        Member member = new Member().builder()
                .email("test@email.com")
                .password("test password")
                .nickname("test nickname")
                .build();

        // when
        Set<ConstraintViolation<Member>> validate = validator.validate(member);

        // then
        assertThat(validate).isEmpty();
    }
}