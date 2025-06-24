package site.stellarburgers.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiCore {

    private final static String SITE_URL = "https://stellarburgers.nomoreparties.site/";

    protected static RequestSpecification requestConfig() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(SITE_URL)
                .build();
    }

    protected static RequestSpecification requestConfig(String bearerPlusToken) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("authorization", bearerPlusToken)
                .setBaseUri(SITE_URL)
                .build();
    }
}