package ru.netology.ibank.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.DataGenerator;
import ru.netology.ibank.data.RegistrationDto;

import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Успешный вход: активный зарегистрированный пользователь")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();

        $("h2").shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Ошибка, если пользователь не зарегистрирован")
    void shouldGetErrorIfNotRegisteredUser() {
        RegistrationDto notRegisteredUser = DataGenerator.getUser("active");

        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка, если пользователь заблокирован")
    void shouldGetErrorIfBlockedUser() {
        RegistrationDto blockedUser = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }
}