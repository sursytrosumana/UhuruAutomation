package org.example.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class loginTest {

    private WebDriver driver;

    By phoneField = By.name("phoneNumber");
    By passwordField = By.id("password");
    By continueBtn = By.xpath("//button[normalize-space()='Continue']");
    By logoImg = By.cssSelector("img[alt='Logo']");

    @BeforeEach
    void initDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://staging.uhuru.market/en/signin");
    }

    @AfterEach
    void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Login with valid creds shows dashboard logo")
    void loginShowsDashboardLogo() {
        driver.findElement(phoneField).sendKeys("68 012 3793");
        driver.findElement(passwordField).sendKeys("Nepal@123455");
        driver.findElement(continueBtn).click();

        WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(logoImg));

        assertTrue(logo.isDisplayed(),
                "Dashboard logo should be visible after login");

    }
}
