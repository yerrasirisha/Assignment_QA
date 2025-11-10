import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTests {

    private static String createdEmployeeId;

    @BeforeAll
    public static void setup() {
        // This MUST point to your backend API
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/v1";
    }

    @Test
    @Order(1)
    @DisplayName("API Test 1: POST - Create Employee (Positive)")
    public void testCreateEmployee_Positive() {
        HashMap<String, String> body = new HashMap<>();
        body.put("firstName", "APITest");
        body.put("lastName", "User");
        body.put("emailId", "apitest.user@gmail.com");

        createdEmployeeId = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/employees")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("APITest"))
                .extract()
                .path("id").toString();
    }

    @Test
    @Order(2)
    @DisplayName("API Test 2: GET - Get All Employees")
    public void testGetAllEmployees() {
        when()
                .get("/employees")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItem(Integer.parseInt(createdEmployeeId)));
    }

    @Test
    @Order(3)
    @DisplayName("API Test 3: DELETE - Delete Employee (Positive)")
    public void testDeleteEmployee_Positive() {
        when()
                .delete("/employees/" + createdEmployeeId)
                .then()
                .statusCode(200)
                .body("deleted", is(true));
    }

    @Test
    @DisplayName("API Test 4: POST - Create Employee (Negative - Invalid Payload)")
    public void testCreateEmployee_Negative() {
        HashMap<String, String> body = new HashMap<>();
        body.put("firstName", "Fail"); // Missing lastName and email

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/employees")
                .then()
                .statusCode(400); // 400 Bad Request
    }
}
