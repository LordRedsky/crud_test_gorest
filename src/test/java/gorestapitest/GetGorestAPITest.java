package gorestapitest;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class GetGorestAPITest {
    @Test
    public void GetAllData() {
        RestAssured.baseURI = "https://gorest.co.in/";
        File JSONSchemaGET = new File("src/test/resources/jsonschema/SchemaGetAllDataUsers.json");
        given()
                .when().get("public/v2/users")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(JSONSchemaGET));
    }
}
