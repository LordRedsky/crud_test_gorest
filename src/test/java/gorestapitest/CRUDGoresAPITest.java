package gorestapitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class CRUDGoresAPITest {
    int newUserID;

    @Test
    public void postNewUser() {
        RestAssured.baseURI = "https://gorest.co.in/";
        String access_token = "664932660bd1dba46072c20c2e9c86523733683ab08f2e47b5fcc746a39c869f";
        String auth = "Bearer " + access_token;

        String name_value = "Redsky24";
        String email_value = "lord_redsky11@mail.com";
        String gender_value = "male";
        String status_value = "active";

        JSONObject userData = new JSONObject();
        userData.put("name", name_value);
        userData.put("email", email_value);
        userData.put("gender", gender_value);
        userData.put("status", status_value);

        String payload = userData.toString();

        RequestSpecification requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", auth)
                .contentType("application/json")
                .body(payload);

        Response response = requestSpecification.post("public/v2/users");
        int statusCode = response.getStatusCode();
        newUserID = response.then().contentType(ContentType.JSON).extract().path("id");
        System.out.println(newUserID);
        Assert.assertEquals(statusCode, 201);

    }

    // VALIDATE NEW USER BY ID
    @Test
    public void verificationNewRegisteredUserByID() {
        RestAssured.baseURI = "https://gorest.co.in/";
        String access_token = "664932660bd1dba46072c20c2e9c86523733683ab08f2e47b5fcc746a39c869f";
        String auth = "Bearer " + access_token;

        given()
                .header("Authorization", auth)
                .contentType("application/json")
                .when().get("public/v2/users/" + newUserID)
                .then()
                .assertThat().statusCode(200);

    }

    //PUT update new name
    @Test
    public void updateDataUserByID() {
        RestAssured.baseURI = "https://gorest.co.in/";
        String access_token = "664932660bd1dba46072c20c2e9c86523733683ab08f2e47b5fcc746a39c869f";
        String auth = "Bearer " + access_token;

        String newName = "Lord Redsky";

        String email = given().when().get("public/v2/users/" + newUserID).getBody().jsonPath().get("email");
        String gender = given().when().get("public/v2/users/" + newUserID).getBody().jsonPath().get("gender");
        String status = given().when().get("public/v2/users/" + newUserID).getBody().jsonPath().get("gender");

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", newUserID);
        bodyMap.put("name", newName);
        bodyMap.put("email", email);
        bodyMap.put("gender", gender);
        bodyMap.put("status", status);
        JSONObject jsonObject = new JSONObject(bodyMap);
        String payload = jsonObject.toString();

        given()
                .header("Authorization", auth)
                .contentType("application/json")
                .body(payload)
                .put("public/v2/users/" + newUserID)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("name", Matchers.equalTo(newName));
    }

    //PATCH update status
    @Test
    public void updateStatusUser() {
        RestAssured.baseURI = "https://gorest.co.in/";
        String access_token = "664932660bd1dba46072c20c2e9c86523733683ab08f2e47b5fcc746a39c869f";
        String auth = "Bearer " + access_token;

        String updateStatus = "inactive";

        String status = given().when().get("public/v2/users/" + newUserID).getBody().jsonPath().get("status");
        System.out.println("old status: " + status);

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("status", updateStatus);
        JSONObject jsonObject = new JSONObject(bodyMap);
        String payload = jsonObject.toString();

        given()
                .header("Authorization", auth)
                .contentType("application/json")
                .body(payload)
                .patch("public/v2/users/" + newUserID)
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("status", Matchers.equalTo(updateStatus));
    }

    //DELETE delete data by ID
    @Test
    public void z_deleteDataByID() {
        RestAssured.baseURI = "https://gorest.co.in/";
        String access_token = "664932660bd1dba46072c20c2e9c86523733683ab08f2e47b5fcc746a39c869f";
        String auth = "Bearer " + access_token;

        given()
                .header("Authorization", auth)
                .contentType("application/json")
                .delete("public/v2/users/" + newUserID)
                .then()
                .assertThat().statusCode(204);
    }
}
