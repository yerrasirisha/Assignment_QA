import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

// Imports for the "Smart Wait"
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UiTests {

    WebDriver driver;
    WebDriverWait wait; // Our "Smart Wait" helper

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // Set the wait time to 10 seconds.
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @Order(1)
    @DisplayName("UI Flow 1: Create Employee and Validate Backend")
    public void testCreateEmployeeFlow() {
        // 1. Go to the MAIN page.
        driver.get("http://localhost:3000/employees");

        // 2. Wait for the "Add Employee" button to be clickable, then click it.
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=' Add Employee']"))
        ).click();

        // 3. NOW we are on the "add-employee" page.
        //    Wait for the 'firstName' field to be visible.
        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("firstName"))
        );

        // 4. Now we can safely find the elements and send keys
        String firstName = "Selenium";
        String lastName = "User";
        String email = "selenium." + System.currentTimeMillis() + "@test.com";

        firstNameField.sendKeys(firstName);
        driver.findElement(By.name("lastName")).sendKeys(lastName);
        driver.findElement(By.name("emailId")).sendKeys(email);

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        // 5. VALIDATION (UI): Wait for the URL to change back to the main list
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/employees"));

        // 6. VALIDATION (Backend)
        when()
                .get("http://localhost:8080/api/v1/employees")
                .then()
                .statusCode(200)
                .body("emailId", hasItem(email));
    }

    @Test
    @Order(2)
    @DisplayName("UI Flow 2: Update Employee Flow")
    public void testUpdateEmployeeFlow() {
        driver.get("http://localhost:3000/employees");

        // 1. Wait for the "Update" button to be clickable
        //    FIX: Added a trailing space to 'Update '
        WebElement updateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("(//button[text()='Update '])[1]"))
        );
        updateButton.click();

        // 2. Wait for the 'firstName' field to appear on the new page
        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("firstName"))
        );

        String updatedName = "UpdatedName" + System.currentTimeMillis();
        firstNameField.clear();
        firstNameField.sendKeys(updatedName);

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        // 3. VALIDATION: Wait for the page to contain the new name
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/employees"));
        assertTrue(driver.getPageSource().contains(updatedName));
    }

    @Test
    @Order(3)
    @DisplayName("UI Flow 3: Delete Employee Flow")
    public void testDeleteEmployeeFlow() {
        driver.get("http://localhost:3000/employees");

        // 1. Wait for the "Delete" button to be clickable
        //    FIX: Added a trailing space to 'Delete '
        WebElement deleteButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("(//button[text()='Delete '])[1]"))
        );
        deleteButton.click();

        // Add a small pause just to see the action
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}