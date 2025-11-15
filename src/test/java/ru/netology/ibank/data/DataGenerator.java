package ru.netology.ibank.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final Faker FAKER = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")        // ВАЖНО: localhost, без заглавных
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }

    
    public static RegistrationDto getUser(String status) {
        return new RegistrationDto(
                getRandomLogin(),
                getRandomPassword(),
                status
        );
    }


    public static RegistrationDto getRegisteredUser(String status) {
        RegistrationDto user = getUser(status);
        sendRequest(user);
        return user;
    }
}