package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Faker FAKER = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new io.restassured.builder.RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(io.restassured.filter.log.LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendRequest(DataGenerator.RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .log().all()
                .post("/api/system/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var user = getUser(status);
            sendRequest(user);
            return user;
        }
    }

    public static class RegistrationDto {
        public String login;
        public String password;
        String status;

        public RegistrationDto(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }
    }
}
